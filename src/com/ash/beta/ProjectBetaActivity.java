package com.ash.beta;


/*	
 *  Copyright 2012 by Ashraf <code@awesomeash.com>
 * 
 *  This file is part of PTP Android Camera Control (PACC).
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or 
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */



import android.app.Activity;
import android.os.Bundle;
///http://www.koders.com/info.aspx?c=ProjectInfo&pid=UCBHEX8BYMVVNMXBWVSEQ1BH8A
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.SensorManager;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;
import android.mtp.MtpDevice;
import android.mtp.MtpDeviceInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ProjectBetaActivity extends Activity {


		private static final String ACTION_USB_PERMISSION = "com.test.hello.USB_PERMISSION";
		private static final String TAG = "TestRunActivity";

		private Thread thread;
		private Toast toast;
		
		
		private UsbManager mUsbManager;
	    private UsbDevice mDevice;
	    
	    private UsbDeviceConnection mConnection;
	    private UsbInterface intf;
	    private UsbEndpoint mEndpointBulkIn;
	    private UsbEndpoint mEndpointBulkOut;
	    private UsbEndpoint mEndpointIntr;
	    private UsbRequest mRequest;
	    private Session session;
	    
	    private static final byte CLASS_CANCEL_REQ = (byte) 0x64;
	    private static final byte CLASS_GET_EVENT_DATA = (byte) 0x65;
	    private static final byte CLASS_DEVICE_RESET = (byte) 0x66;
	    private static final byte CLASS_GET_DEVICE_STATUS = (byte) 0x67;
	    
	    private NameFactory factory = new NameFactory();
	    
	    private int inMaxPS;
	    private Byte[] bytes;

		
	    Button button1, button2, button3, button4;
		TextView tv1, tv2, tv3, tv4, tv5, tv6;
		UsbDevice usbCamera;


		/** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main);
	       
	        mUsbManager = (UsbManager)getSystemService(Context.USB_SERVICE);
	      
	        tv1 = (TextView) findViewById(R.id.tv1);
	        tv2 = (TextView) findViewById(R.id.tv2);
	        tv3 = (TextView) findViewById(R.id.tv3);
	        tv4 = (TextView) findViewById(R.id.tv4);
	        tv5 = (TextView) findViewById(R.id.tv5);
	        tv6 = (TextView) findViewById(R.id.tv6);
	        button1 = (Button) findViewById (R.id.button1);
	        button2 = (Button) findViewById (R.id.button2);
	        button3 = (Button) findViewById (R.id.button3);
	        button4 = (Button) findViewById (R.id.button4);
	        
	        button1.setOnClickListener(new View.OnClickListener() 
	        {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					tv3.setText("Button Works");

				}
			});
	        
	        button2.setOnClickListener(new View.OnClickListener() {		
				@Override
				public void onClick(View v) {
				

					tv6.setText("Starting. ");
					
					Command command = new Command(Command.GetDeviceInfo, session);

					
					mConnection.bulkTransfer(mEndpointBulkOut, command.data , command.length , 1000);
					//session.open();
					tv6.append(" Sent:");
					
					StringBuffer hexString1 = new StringBuffer(); 
					for (int i=0;i<command.data.length;i++) { 
					    hexString1.append(Integer.toHexString(0xFF & command.data[i])); 
					    } 
					tv6.append(hexString1);
					
					byte buf[] = new byte[inMaxPS];
					
					mConnection.bulkTransfer(mEndpointBulkIn, buf ,inMaxPS , 1000);
					
					tv6.append(" Receiving:");
					StringBuffer hexString = new StringBuffer(); 
					for (int i=0;i<buf.length;i++) { 
					    hexString.append(Integer.toHexString(0xFF & buf[i])); 
					    }
					tv6.append(hexString);
					
					DeviceInfo	info = new DeviceInfo (factory);
					info.data = buf;
					info.length = info.getLength();
					info.parse();
					if (info.vendorExtensionId != 0) {
					    factory = factory.updateFactory (info.vendorExtensionId);
					    info.factory = factory;
					}
					info.showInTextView(tv2);
					
					
					
					
					
					
				
					
				}
			});
	        
	        button3.setOnClickListener(new View.OnClickListener() 
	        {
				@Override
				public void onClick(View v) {
					tv6.setText("Starting. ");
					
					Command command = new Command(Command.GetDeviceInfo, session);

					
					mConnection.bulkTransfer(mEndpointBulkOut, command.data , command.length , 1000);
					//session.open();
					tv6.append(" Sent:");
					
					StringBuffer hexString1 = new StringBuffer(); 
					for (int i=0;i<command.data.length;i++) { 
					    hexString1.append(Integer.toHexString(0xFF & command.data[i])); 
					    } 
					tv6.append(hexString1);
					
					byte buf[] = new byte[inMaxPS];
					
					mConnection.bulkTransfer(mEndpointBulkIn, buf ,inMaxPS , 1000);
					
					tv6.append(" Receiving:");
					StringBuffer hexString = new StringBuffer(); 
					for (int i=0;i<buf.length;i++) { 
					    hexString.append(Integer.toHexString(0xFF & buf[i])); 
					    }
					tv6.append(hexString);
					
					DeviceInfo	info = new DeviceInfo (factory);
					info.data = buf;
					info.length = info.getLength();
					info.parse();
					if (info.vendorExtensionId != 0) {
					    factory = factory.updateFactory (info.vendorExtensionId);
					    info.factory = factory;
					}
					info.showInTextView(tv2);
					
					//newStuff
					command = new Command (Command.OpenSession, session,
			    			session.getNextSessionID ());
					
					mConnection.bulkTransfer(mEndpointBulkOut, command.data , command.length , 1000);
					
					tv2.setText(" Sent:");
					tv2.append(" Sent:");
					
					hexString1 = new StringBuffer(); 
					for (int i=0;i<command.data.length;i++) { 
					    hexString1.append(Integer.toHexString(0xFF & command.data[i])); 
					    } 
					tv2.append(hexString1);
					
					
					mConnection.bulkTransfer(mEndpointBulkIn, buf ,inMaxPS , 1000);
					
					tv2.append(" Receiving:");
					hexString = new StringBuffer(); 
					for (int i=0;i<buf.length;i++) { 
					    hexString.append(Integer.toHexString(0xFF & buf[i])); 
					    }
					tv2.append(hexString);
					Response response = new Response (buf, inMaxPS, factory);
					tv1.setText("Type:" + response.getBlockType()+ "\n");//= 3
					tv1.append("Name:" + response.getCodeName(response.getCode())+ "\n");//=OK
					tv1.append("CodeString:" + response.getCodeString()+ "\n");//=OK
					tv1.append("Length:" + response.getLength()+ "\n");//=OK
					tv1.append("String:" +response.toString());
					
					
				}
			}); 
	        
	        button4.setOnClickListener(new View.OnClickListener() 
	        {
				@Override
				public void onClick(View v) {
					
					
					

			    	
					
				}
			}); 
	    

	}
	    

	    
	    
	    
	    @Override
	    public void onResume() {
	        super.onResume();
	        Intent intent = getIntent();
	        String action = intent.getAction();

	        UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
	        if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
	        	session = new Session();
	        	mDevice = device;
				setDevice(device);
	            setDevice2(device);
	            
	            
	        } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
	            if (mDevice != null && mDevice.equals(device)) {
	                //setDevice(null);
	            }
	        }
	        
	        
	     
	        
	    }
	    
	    private void setDevice(UsbDevice device) {
	    	tv2.setText("N:"+device.getDeviceName() + " P:" +device.getProductId() + " V:"+ device.getVendorId());
	        tv3.setText("Pro:"+device.getDeviceProtocol()+" Sub:"+device.getDeviceSubclass());
	        
	    }
	    
	    private void setDevice2(UsbDevice device){

	    	mConnection = mUsbManager.openDevice(mDevice); 
	    	//mConnection.claimInterface(intf, true);//cannot use claimInterface. Why ah? -Ash
	    	intf = mDevice.getInterface(0);
	    	UsbRequest intrRequest = new UsbRequest();
	    	UsbEndpoint epOut = null;
	        UsbEndpoint epIn = null;
	        UsbEndpoint epEv = null;
	        // look for our bulk endpoints
	        for (int i = 0; i < intf.getEndpointCount(); i++) {
	            UsbEndpoint ep = intf.getEndpoint(i);
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
	        inMaxPS = mEndpointBulkOut.getMaxPacketSize();
	        intrRequest.initialize(mConnection, mEndpointIntr);
	        //intrRequest
	        //mConnection.controlTransfer(CLASS_GET_DEVICE_STATUS, CLASS_GET_DEVICE_STATUS, value, index, buffer, length, timeout)

	        tv4.setText("Get:"+mDevice.getInterfaceCount()+"Other:"+mDevice.getDeviceName()+" Class:"+intf.getInterfaceClass()+","+intf.getInterfaceSubclass()+","+intf.getInterfaceProtocol()+ "Iendpoints:"+mEndpointBulkIn.getMaxPacketSize()+ " "+ mEndpointBulkIn.getType() + " "+mEndpointBulkIn.getDirection()); //512 2 USB_ENDPOINT_XFER_BULK USB_DIR_IN 
	        tv4.append(" Oendpoints:"+mEndpointBulkOut.getMaxPacketSize()+ " "+ mEndpointBulkOut.getType() + " "+mEndpointBulkOut.getDirection()); //512 2 USB_ENDPOINT_XFER_BULK USB_DIR_OUT 
	        tv4.append(" Eendpoints:"+mEndpointIntr.getMaxPacketSize()+ " "+ mEndpointIntr.getType() + " "+mEndpointIntr.getDirection()); //8,3 USB_ENDPOINT_XFER_INT USB_DIR_IN  

	        

	    }
	    
	    public void openSession (TextView tv4)
	    throws IOException
	    {
		Command		command;
		Response	response;

		synchronized (session) {
		    command = new Command (Command.OpenSession, session,
		    			session.getNextSessionID ());
		    
		    mConnection.bulkTransfer(mEndpointBulkOut, command.data , command.length , 1000);
		    byte buf[] = new byte[inMaxPS];			
			mConnection.bulkTransfer(mEndpointBulkIn, buf ,inMaxPS , 1000);
			response = new Response (buf, inMaxPS, factory);
		    switch (response.getCode ()) {
			case Response.OK:
			    session.open();
			    tv4.append("Session Opened!");
			    return;
			default:
			    throw new IOException (response.toString ());
		    }
		}
	    }	    
	    

	    



		
	}
				