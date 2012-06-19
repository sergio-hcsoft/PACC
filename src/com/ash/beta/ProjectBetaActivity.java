package com.ash.beta;


/*	
 *  Copyright 2012 by Ashraf <android@hungerattack.com>
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
 *  
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
	
		private UsbManager mUsbManager;
	    private UsbDevice mDevice;
	    
	    
	    private UsbConnection usbconnection;
	    private NameFactory factory = new NameFactory();


		
	    Button button1, button2, button3, button4, button5, button6, button7, button8;
		TextView tv1, tv2, tv3, tv4, tv5, tv6;
		UsbDevice usbCamera;


		/** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main);

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
	        button5 = (Button) findViewById (R.id.button5);
	        button6 = (Button) findViewById (R.id.button6);
	        button7 = (Button) findViewById (R.id.button7);
	        button8 = (Button) findViewById (R.id.button8);
	        //////////////////OnResume

	        mUsbManager = (UsbManager)getSystemService(Context.USB_SERVICE);	        
	        Intent intent = getIntent();
	        String action = intent.getAction();

	        UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
	        mDevice = device;
	        
	        if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
	        	usbconnection = new UsbConnection(mUsbManager, mDevice, factory);
	        	usbconnection.setTV(tv1, tv2, tv3, tv4, tv5, tv6);
   
	        } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
	            if (mDevice != null && mDevice.equals(device)) {
	                //setDevice(null);
	            }
	        }
	        
	        ///////////////////////////
	        button1.setOnClickListener(new View.OnClickListener() 
	        {
				@Override
				public void onClick(View v) {
					clearTV();

				}
			});
	        
	        button2.setOnClickListener(new View.OnClickListener() {		
				@Override
				public void onClick(View v) {
					clearTV();
					usbconnection.openSession();
					factory = usbconnection.getDeviceInfo(tv6);
					usbconnection.closeSession();

					
				}
			});
	        
	        button3.setOnClickListener(new View.OnClickListener() 
	        {
				@Override
				public void onClick(View v) {
					clearTV();
					usbconnection.openSession();
					usbconnection.captureImage();
					usbconnection.closeSession();
	
				}
			}); 
	        
	        button4.setOnClickListener(new View.OnClickListener() 
	        {
				@Override
				public void onClick(View v) {	 
					usbconnection.openSession();
					
					
				}
			}); 
	        button5.setOnClickListener(new View.OnClickListener() 
	        {
				@Override
				public void onClick(View v) {	 
					usbconnection.closeSession();

					
				}
			});
	        button6.setOnClickListener(new View.OnClickListener() 
	        {
				@Override
				public void onClick(View v) {	 
					tv5.setText("Reading: \n");
					Response response = usbconnection.readResponse(tv5);
					
				}
			});	        
	        button7.setOnClickListener(new View.OnClickListener() 
	        {
				@Override
				public void onClick(View v) {	 
					usbconnection.getLiveView();
					
				}
			});
	        button8.setOnClickListener(new View.OnClickListener() 
	        {
				@Override
				public void onClick(View v) {	 
					usbconnection.getEvent();
					
				}
			});
	        
	}
	    

	    
	    
	    
	    @Override
	    public void onResume() {
	        super.onResume();
	     
	        
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
	    

	    



		
	}
				