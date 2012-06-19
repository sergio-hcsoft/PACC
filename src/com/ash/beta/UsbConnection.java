package com.ash.beta;

import java.io.IOException;

import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.widget.TextView;

public class UsbConnection {

	private static final String TAG="com.ash.beta.usbwriter";
	private static final int TIMEOUT = 1000;
	
	private NameFactory factory = new NameFactory();
	
	private int inMaxPS;
		
	private UsbManager mUsbManager;
    private UsbDevice mDevice;  
    
    private UsbDeviceConnection mConnection;
    private UsbInterface mIntf;
    
    private UsbEndpoint mEndpointBulkIn;
    private UsbEndpoint mEndpointBulkOut;
    private UsbEndpoint mEndpointIntr;
	
    private Session session;
    
    TextView tv1, tv2, tv3, tv4, tv5, tv6;
    
	public UsbConnection(UsbManager manager, UsbDevice device, NameFactory fact){
		factory = fact;
		mUsbManager = manager;
		mDevice = device;
		mConnection = mUsbManager.openDevice(device);
		mIntf = findUsbInterface(device); //device.getInterface(0);
		session = new Session();
		getEndpoints();
	}	
	
	private UsbInterface findUsbInterface(UsbDevice device) {
		int count = device.getInterfaceCount();
		for (int i = 0; i < count; i++) {
			UsbInterface intf = device.getInterface(i);
			if (intf.getInterfaceClass() == 6 && intf.getInterfaceSubclass() == 1 
				&& intf.getInterfaceProtocol() == 1) 
			{
				return intf;
			}
		}
		return null;
	}
	
	public void setTV(TextView tv1, TextView tv2, TextView tv3, TextView tv4, TextView tv5, TextView tv6)
	{
		this.tv1 = tv1;
		this.tv2 = tv2;
		this.tv3 = tv3;
		this.tv4 = tv4;
		this.tv5 = tv5;
		this.tv6 = tv6;
		
	}
	
	public void getEndpoints()
	{
    	UsbEndpoint epOut = null;
        UsbEndpoint epIn = null;
        UsbEndpoint epEv = null;
        
        for (int i = 0; i < mIntf.getEndpointCount(); i++) {
            UsbEndpoint ep = mIntf.getEndpoint(i);
            if (ep.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                if (ep.getDirection() == UsbConstants.USB_DIR_OUT) {
                    epOut = ep;
                } else {
                    epIn = ep;
                }
            }
            if (ep.getType() == UsbConstants.USB_ENDPOINT_XFER_INT){
            		epEv = ep;
            }
        }
        if (epOut == null || epIn == null || epEv == null) {
            throw new IllegalArgumentException("not all endpoints found");
        }
        
        mEndpointBulkOut = epOut;
        mEndpointBulkIn = epIn;
        mEndpointIntr = epEv;
        inMaxPS = mEndpointBulkIn.getMaxPacketSize();
	}
	
	public void write(byte[] data, int length, int timeout)
	{
		Log.d(TAG,"Sending command");
		mConnection.bulkTransfer(mEndpointBulkOut, data , length , timeout);
		/*int retries=3;
		int tmp=-1;
		for(int i=0;i<retries;retries--){
			mConnection.bulkTransfer(mEndpointBulkOut, data , length , timeout);
			if(tmp<0)
				Log.e(TAG,"Sending failed, retry");
			else
				break;
		}*/
		
		
	}
	
	public byte[] read(int timeout)
	{
		Log.d(TAG,"Reading data");
		byte data[] = new byte[inMaxPS];	
		mConnection.bulkTransfer(mEndpointBulkIn, data , inMaxPS , timeout);
		//return new Response (buf, inMaxPS, factory);	
		/*
		int retries=3;
		int tmp=-1;
		for(int i=0;i<retries;retries--){
			mConnection.bulkTransfer(mEndpointBulkIn, data , inMaxPS , timeout);
			if(tmp<0)
				Log.e(TAG,"Reading failed, retry");
			else
				break;
		}
		Log.d(TAG,"Read: " + tmp);	
		*/
		return data;
		
	}
	
	public byte[] readInterrupt(int timeout)
	{

		byte buf[] = new byte[inMaxPS];	
		mConnection.bulkTransfer(mEndpointIntr, buf , inMaxPS , timeout);
		Event event = new Event(buf, factory);
		tv5.append(event.toString());
		
		return buf;
	}
	
	public void getEvent()
	{
		readInterrupt(1000);
		
	}
	
	public void updateFactory(NameFactory fact){	
		factory = fact;		
	}
	
	public void openSession()
	{
		Command command = new Command(Command.OpenSession, session, session.getNextSessionID ());
		write(command.data, command.length, TIMEOUT);
		session.open();				
		byte[] buf = read(TIMEOUT);
		tv1.setText("OpenSession:");
		Response response = new Response (buf, inMaxPS, factory);
		tv1.append(response.toString());
	}
	
	public void closeSession()
	{
		Command command = new Command(Command.CloseSession, session);
		write(command.data, command.length, TIMEOUT);	
		session.close();	
		byte[] buf = read(TIMEOUT);
		tv2.setText("CloseSession:");
		Response response = new Response (buf, inMaxPS, factory);
		tv2.append(response.toString());
		/*
		StringBuffer hexString = new StringBuffer(); 
		for (int i=0;i<buf.length;i++) { 
		    hexString.append(Integer.toHexString(0xFF & buf[i])); 
		    }
		tv2.append(hexString);
		*/
	}
	
	public void getData()
	{
		Data data = new Data(factory);
		byte buf[] = read(TIMEOUT);
        data.data = buf;
        data.length = inMaxPS;
        // get the rest of it
        int expected = data.getLength();
        /*
        if (data instanceof FileData) {
            FileData fd = (FileData) data;

            fd.write(buf, Data.HDR_LEN, len - Data.HDR_LEN);
            if (len == inMaxPS && expected != inMaxPS) {
                InputStream is = in.getInputStream();

                // at max usb data rate, 128K ~= 0.11 seconds
                // typically it's more time than that
                buf = new byte[128 * 1024];
                do {
                    len = is.read(buf);
                    fd.write(buf, 0, len);
                } while (len == buf.length);
            }

        } else if (len == inMaxPS && expected != inMaxPS) {
            buf = new byte[expected];
            System.arraycopy(data.data, 0, buf, 0, len);
            data.data = buf;
            data.length += in.getInputStream().read(buf, len, expected - len);
        }
        // if ((expected % inMaxPS) == 0)
        //	... next packet will be zero length
        data.parse();
        */
		
	}
	
	
	public NameFactory getDeviceInfo(TextView tv)
	{
		Command command = new Command(Command.GetDeviceInfo, session);
		write(command.data, command.length, TIMEOUT);				
		byte buf[] = read(TIMEOUT);
		
		DeviceInfo	info = new DeviceInfo (factory);
		info.data = buf;
		info.length = info.getLength();
		info.parse();
		if (info.vendorExtensionId != 0) {
		    factory = factory.updateFactory (info.vendorExtensionId);
		    info.factory = factory;
		}
		info.showInTextView(tv);
		
		tv3.setText("GetDevice:");
		StringBuffer hexString = new StringBuffer(); 
		for (int i=0;i<buf.length;i++) { 
		    hexString.append(Integer.toHexString(0xFF & buf[i])); 
		    }
		tv3.append(hexString);
		Response response = new Response (buf, inMaxPS, factory);
		tv3.append("\n"+response.toString());
		
		buf = read(TIMEOUT);
		
		tv3.append("\n");
		tv3.append("GetDeviceResponse: ");
		hexString = new StringBuffer(); 
		for (int i=0;i<buf.length;i++) { 
		    hexString.append(Integer.toHexString(0xFF & buf[i])); 
		    }
		tv3.append(hexString);
		response = new Response (buf, inMaxPS, factory);
		tv3.append("\n"+response.toString());
		
		
		return factory;


	}
	
	public void captureImage()
	{
		Command command = new Command(Command.EOS_OC_Capture, session);
		write(command.data, command.length, TIMEOUT);				
		byte buf[] = read(TIMEOUT);
		
		tv3.setText("CaptureImage:");
		Response response = new Response (buf, inMaxPS, factory);
		tv3.append(response.toString());
		
	}
	
	public void getLiveView()
	{
		Command command = new Command(Command.EOS_OC_GetLiveViewPicture, session);
		write(command.data, command.length, TIMEOUT);				
		byte buf[] = read(TIMEOUT);
		
		tv3.setText("GetLive:");
		Response response = new Response (buf, inMaxPS, factory);
		tv3.append(response.toString());
		
		/*
		ptp.Code=PTP_OC_CANON_EOS_GetViewFinderData;
        ptp.Nparam=1;
        ptp.Param1=0x00100000;
		*/
		
	}
	
    public Response readResponse (TextView tv) {
		byte buf[] = new byte[inMaxPS];		
		mConnection.bulkTransfer(mEndpointBulkIn, buf ,inMaxPS , 1000);
		Response response = new Response (buf, inMaxPS, factory);
		tv.append("  Type: " + response.getBlockTypeName(response.getBlockType()) +" " +response.getBlockType() +"\n");   // getU16 (4)
		tv.append("  Name: " + response.getCodeName(response.getCode())+ ", code: 0x" +Integer.toHexString(response.getCode()) +"\n"); //getU16 (6)
		tv.append("  CodeString:" + response.getCodeString()+ "\n");
		tv.append("  Length:" + response.getLength()+ "\n");  //getS32 (0)
		tv.append("  String:" + response.toString());
		return response;
    }
}
