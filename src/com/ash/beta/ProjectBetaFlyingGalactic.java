package com.ash.beta;



import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ProjectBetaFlyingGalactic extends Activity {


	private static final String ACTION_USB_PERMISSION = "com.test.hello.USB_PERMISSION";
	private static final String TAG = "TestRunActivity";

	public Thread thread;
	private Toast toast;
	private Timer eventTimer;


	private UsbManager mUsbManager;
	private UsbDevice mDevice;

	private UsbDeviceConnection mConnection;
	private UsbInterface intf;
	private UsbEndpoint mEndpointBulkIn;
	private UsbEndpoint mEndpointBulkOut;
	private UsbEndpoint mEndpointIntr;
	//	    private UsbRequest mRequest;
	private Session session;

	static final int 	MSG_SHOW_TV2 	= 2;
	static final int 	MSG_SHOW_TV3 	= 3;
	static final int 	MSG_SHOW_TV4 	= 4;
	static final int 	MSG_SHOW_TV5 	= 5;
	static final int 	MSG_SHOW_TV6 	= 6;
	static final String THREAD_SHUTTER 	= "THREAD_SHUTTER";
	static final String THREAD_ISO 		= "THREAD_ISO";



	//	    private static final byte CLASS_CANCEL_REQ = (byte) 0x64;
	//	    private static final byte CLASS_GET_EVENT_DATA = (byte) 0x65;
	//	    private static final byte CLASS_DEVICE_RESET = (byte) 0x66;
	//	    private static final byte CLASS_GET_DEVICE_STATUS = (byte) 0x67;

	private NameFactory factory = new NameFactory();

	private int inMaxPS;
	//	    private Byte[] bytes;


	Button button1, button2, button3, button4;
	TextView tv1, tv2, tv3, tv4, tv5, tv6;
	//		UsbDevice usbCamera;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Log.d(TAG, "onCreate");
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

		IntentFilter filter = new IntentFilter();
		filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
		filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
		registerReceiver(mUsbReceiver, filter);


		button1.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tv6.setText("Button Works");
				try {
					openSession (tv2);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // opens device
			}
		});

		button2.setOnClickListener(new View.OnClickListener() {	// detailed Device info	
			@Override
			public void onClick(View v) {
				clearTV();
				if (session == null) {
					tv1.setText("Error: Session not open!");
					return;
				}
				tv1.setText("Starting. ");
				Command command = new Command(Command.GetDeviceInfo, session);
				mConnection.bulkTransfer(mEndpointBulkOut, command.data , command.length , 1000);
				tv1.append(" Sent: ");

				StringBuffer hexString1 = new StringBuffer(); 
				for (int i=0;i<command.data.length;i++) { 
					hexString1.append(Integer.toHexString(0xFF & command.data[i])); 
				} 
				tv1.append(hexString1);
				byte buf[] = new byte[inMaxPS];					
				mConnection.bulkTransfer(mEndpointBulkIn, buf ,inMaxPS , 1000);

				tv1.append(" Receiving:");
				StringBuffer hexString = new StringBuffer(); 
				for (int i=0;i<buf.length;i++) { 
					hexString.append(Integer.toHexString(0xFF & buf[i])); 
				}
				tv1.append(hexString);

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



		button3.setOnClickListener(new View.OnClickListener() // Release Shutter
		{
			@Override
			public void onClick(View v) {
				clearTV();
				tv1.setText("Release shutter");
				if (session == null) {
					tv1.setText("Error: Session = null!");
					return;
				}
				tv2.setText("Starting");					
				thread  = new Thread (r, THREAD_SHUTTER);
				thread.start();
			}
		}); 

		button4.setOnClickListener(new View.OnClickListener() // Select ISO  EOS_DPC_Iso
		{
			@Override
			public void onClick(View v) {
				clearTV();
				tv1.setText("Test");
				if (session == null) {
					tv1.setText("Error: Session = null!");
					return;
				}
				tv2.setText("Starting.");
				thread  = new Thread (r, THREAD_ISO);
				thread.start();
			}
		}); 


	}

	public Response getResponse () {
		byte buf[] = new byte[inMaxPS];		
		mConnection.bulkTransfer(mEndpointBulkIn, buf ,inMaxPS , 1000);
		return new Response (buf, inMaxPS, factory);	    	
	}

	public Response showResponse (Response response) {
		log ("  Type: " + response.getBlockTypeName(response.getBlockType()) +" (Code: " +response.getBlockType() +")\n");   // getU16 (4)
		log ("  Name: " + response.getCodeName(response.getCode())+ ", code: 0x" +Integer.toHexString(response.getCode()) +"\n"); //getU16 (6)
		//			log ("  CodeString:" + response.getCodeString()+ "\n");
		log ("  Length: " + response.getLength()+ " bytes\n");  //getS32 (0)
		log ("  String: " + response.toString());
		return response;
	}

	public Event getEvent (){ //TODO
//		log(MSG_SHOW_TV5, "\nSend EOS_OC_GetEvent \n");
//		Command command = new Command (Command.EOS_OC_GetEvent, session);
//		mConnection.bulkTransfer(mEndpointBulkOut, command.data , command.length , 1000);
//		showResponse (getResponse());
		byte buf[] = new byte[inMaxPS];		
		mConnection.bulkTransfer(mEndpointIntr, buf ,inMaxPS , 1000);
		Event event = new Event (buf, factory);
		if (event.getCode() != 0) {
			log(MSG_SHOW_TV5, "  Event Name: " + event.getCodeName(event.getCode())+ ", code: 0x" +Integer.toHexString(event.getCode()));
			log(MSG_SHOW_TV5, "  Length:" + event.getLength()+ "\n");
			log(MSG_SHOW_TV5, "  String:" + event.toString() +"\n");
		}
		return event;
	}


	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
		initDevice (searchDevice ());

	}

	// search connected devices, returns only protocol 0 devices
	public UsbDevice searchDevice () {
		tv1.setText("Search Device");
		UsbDevice device = null;
		for (UsbDevice lDevice :  mUsbManager.getDeviceList().values()) {
			Log.d(TAG, "Device: " +lDevice.getDeviceName() +" class " +lDevice.getDeviceClass());
			if (lDevice.getDeviceProtocol() == 0) device = lDevice;	        	
		}
		return device;
	}


	// inits device, 
	public void initDevice (UsbDevice device) {
		if (device != null){
			Log.d(TAG, "initDevice: "+device.getDeviceName());
			//	        	log ("Device: " +device.getDeviceName());
			session = new Session();
			mDevice = device;
			setDevice(device);
			setDevice2(device);
			eventTimer = new Timer ("eventTimer");
			eventTimer.schedule(new TimerTask() {
				@Override
				public void run() { 
					if (session != null && session.isActive()) {
						if (getEvent ().getCode() != 0) Log.d(TAG, "EventTimer Event received ");					
					}
				}
			}, 0, 250);  // delay, interval
		}
	}

	public void detachDevice () {
		if (mDevice != null /*&& mDevice.equals(device)*/) {
			Log.d(TAG, "detachDevice: " +mDevice.getDeviceName());
			eventTimer.cancel();
			eventTimer.purge();
			closeSession (session);
//			if (session != null) session.close();
			session = null;
			mDevice = null;
		}
	}

	// receive Broadcasts
	BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG, "mUsbReceiver  onReceive");
			String action = intent.getAction();
			UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
			if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action) /*|| (device != null)*/) {
				clearTV ();
				tv1.setText("USB_DEVICE_ATTACHED");
				initDevice (device);
			} else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
				tv1.setText("USB_DEVICE_DETACHED");
				detachDevice ();
			}	    			        
		}
	};


	@Override
	public void onStop() {
		super.onStop();
		detachDevice ();
	}




	private void setDevice(UsbDevice device) {
		tv2.setText("N: "+device.getDeviceName() + "\nPID: " +device.getProductId() + " VID: "+ device.getVendorId());
		tv3.setText("Prot: "+device.getDeviceProtocol() +", Cl: "+device.getDeviceClass()+", Subcl: "+device.getDeviceSubclass());

	}

	private void setDevice2(UsbDevice device){
		mConnection = mUsbManager.openDevice(mDevice); 
		intf = findUsbInterface (device);  // find an interface class 6
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
		//mConnection.controlTransfer(CLASS_GET_DEVICE_STATUS, CLASS_GET_DEVICE_STATUS, value, index, buffer, length, timeout)

		tv4.setText("Get: "+mDevice.getInterfaceCount()+" Other: "+mDevice.getDeviceName());
		tv4.append ("\nClass: "+intf.getInterfaceClass()+","+intf.getInterfaceSubclass()+","+intf.getInterfaceProtocol()
				+ "\nIendpoints: "+mEndpointBulkIn.getMaxPacketSize()+ " "+ mEndpointBulkIn.getType() + " "+mEndpointBulkIn.getDirection()); //512 2 USB_ENDPOINT_XFER_BULK USB_DIR_IN 
		tv4.append ("\nOendpoints: "+mEndpointBulkOut.getMaxPacketSize()+ " "+ mEndpointBulkOut.getType() + " "+mEndpointBulkOut.getDirection()); //512 2 USB_ENDPOINT_XFER_BULK USB_DIR_OUT 
		tv4.append ("\nEendpoints: "+mEndpointIntr.getMaxPacketSize()+ " "+ mEndpointIntr.getType() + " "+mEndpointIntr.getDirection()); //8,3 USB_ENDPOINT_XFER_INT USB_DIR_IN  
	}

	// searches for an interface on the given USB device, returns only class 6  // From androiddevelopers ADB-Test
	private UsbInterface findUsbInterface(UsbDevice device) {
		Log.d (TAG, "findAdbInterface " + device.getDeviceName());
		int count = device.getInterfaceCount();
		for (int i = 0; i < count; i++) {
			UsbInterface intf = device.getInterface(i);
			Log.d (TAG, "Interface " +i + " Class " +intf.getInterfaceClass() +" Prot " +intf.getInterfaceProtocol());
			if (intf.getInterfaceClass() == 6 && intf.getInterfaceSubclass() == 1 
				&& intf.getInterfaceProtocol() == 1) 
			{
				return intf;
			}
		}
		return null;
	}


	public Response openSession (TextView tv4)
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
			//		    switch (response.getCode ()) {
			if (response.getCode () == Response.OK) {
				session.open();
				tv4.append("Session Opened!");
				return response;
			} else {tv4.append(response.toString ());
			return response;
			}
			//			default:
			//			    throw new IOException (response.toString ());
			//		    }
		}
			}
	
	public Response closeSession (Session session) {
		log("\nCloseSession\n");
		Command command = new Command (Command.CloseSession, session);
		mConnection.bulkTransfer(mEndpointBulkOut, command.data , command.length , 1000);
		Response response = getResponse ();
		session.close();
		return response;
	}
	

	public void clearTV ()
	{
		tv1.setText("Clear");
		tv2.setText("Clear");
		tv3.setText("Clear");
		tv4.setText("Clear");
		tv5.setText("Clear");
		tv6.setText("Clear");	    
	}


	/** opens session, sends EOS_OC_Capture command, closes session
	 * 
	 * @param session
	 * @return
	 */
	public boolean releaseShutter (Session session){
		Log.d(TAG, "Starting releaseShutter");
		Command command;
		Response response;
		boolean result = session.isActive();
		if (!session.isActive()) {
			log (", Send Open Session\n");
			command = new Command(Command.OpenSession, session, session.getNextSessionID ());
			mConnection.bulkTransfer(mEndpointBulkOut, command.data , command.length , 1000);
			session.open();				
			response =getResponse();
			result = response.getCode() == Response.OK;
		}
		if (result){
			// optional loop inserted to catch DeviceBusy state
			int count = 1;
			do {
				log("\nSend EOS_OC_Capture  #" +count +"\n");
				command = new Command (Command.EOS_OC_Capture, session);
				mConnection.bulkTransfer(mEndpointBulkOut, command.data , command.length , 1000);
				response = getResponse();
				if (response.getCode() == Response.DeviceBusy) {
					count ++;
					try {
						log("   wait... ");
						thread.sleep(500);
					} catch (Exception e) {
						e.printStackTrace();
						log("\nWaiting Error\n");
					}
				}
			} while (count < 20 && response.getCode() == Response.DeviceBusy);
			result = response.getCode() == Response.OK;

		}
		return result;
	}  //releaseShutter



	public void setISO (Session session){
		Command command;
		Response response;
		boolean result = session.isActive();
		if (!session.isActive()) {
			log (", Send Open Session\n");
			command = new Command(Command.OpenSession, session, session.getNextSessionID ());
			mConnection.bulkTransfer(mEndpointBulkOut, command.data , command.length , 1000);
			session.open();				
			response =getResponse();
			result = response.getCode() == Response.OK;
		}
		
		getEvent (); // not working
		command = new Command (Command.EOS_OC_SetDevicePropValue, session, Command.EOS_DPC_Iso, 0x86 /*iso 1600*/);  // operation not supported (0x2005),  iso values in canon.h
	
		mConnection.bulkTransfer(mEndpointBulkOut, command.data , command.length , 1000);
		response =showResponse(getResponse());
	}


	//	 **************** run USB tasks in its own thread **********************

	final Runnable r =new Runnable() {
		public void run() {
			Log.d(TAG, "running thread " +thread.getName());
			if (thread.getName().equals(THREAD_SHUTTER)) { 
				releaseShutter (session);
			}; 		
			if (thread.getName().equals(THREAD_ISO)) { 
				setISO (session);
			}; 		
		}
	};

	// show messages
	void log (String s) {
		Message msg = new Message ();
		msg.what = MSG_SHOW_TV2;
		msg.obj = s;
		myHandler.sendMessage(msg);
	}

	void log (int msgWhat, String s) {
		Message msg = new Message ();
		msg.what = msgWhat;
		msg.obj = s;
		myHandler.sendMessage(msg);
	}

	Handler myHandler = new Handler() {  
		public void handleMessage(Message msg) {
			switch (msg.what ){
			case MSG_SHOW_TV2: tv2.append ((String) msg.obj);
			break;
			case MSG_SHOW_TV5: tv5.append ((String) msg.obj);
			break;
			}
			// do some code here, eg display on screen
		}
	};			



}