package com.ash.beta;

//Copyright 2000 by David Brownell <dbrownell@users.sourceforge.net>
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//

import java.io.PrintStream;


/**
 * Command messages start PTP transactions, and are sent from
 * initiator to responder.  They include an operation code,
 * either conform to chapter 10 of the PTP specification or
 * are vendor-specific commands.
 *
 * <p> Create these objects in helper routines which package
 * intelligence about a given Operation.  That is, it'll know
 * the command code, how many command and response parameters
 * may be used, particularly significant response code, and
 * whether the transaction has a data phase (and its direction). 
 *
 * @version $Id: Command.java,v 1.3 2001/04/12 23:13:00 dbrownell Exp $
 * @author David Brownell
 */
public class Command extends ParamVector
{
    private Command (int nparams, int code, Session s)
    {
	super (new byte [HDR_LEN + (4 * nparams)], s.getFactory ());
	putHeader (data.length, 1 /*OperationCode*/, code, s.getNextXID ());
    }

    /**
     * This creates a zero-parameter command.
     * @param code as defined in section 10, table 18
     * @param s session this command is associated with
     */
    Command (int code, Session s)
    {
	this (0, code, s);
    }

    /**
     * This creates a one-parameter command.
     * @param code as defined in section 10, table 18
     * @param s session this command is associated with
     * @param param1 first operation parameter
     */
    Command (int code, Session s, int param1)
    {
	this (1, code, s);
	put32 (param1);
    }

    /**
     * This creates a two-parameter command.
     * @param code as defined in section 10, table 18
     * @param s session this command is associated with
     * @param param1 first operation parameter
     * @param param2 second operation parameter
     */
    Command (int code, Session s, int param1, int param2)
    {
	this (2, code, s);
	put32 (param1);
	put32 (param2);
    }

    /**
     * This creates a three-parameter command.
     * @param code as defined in section 10, table 18
     * @param s session this command is associated with
     * @param param1 first operation parameter
     * @param param2 second operation parameter
     * @param param3 third operation parameter
     */
    Command (int code, Session s, int param1, int param2, int param3)
    {
	this (3, code, s);
	put32 (param1);
	put32 (param2);
	put32 (param3);
    }

    // allegedly some commands could have up to five params


    /** OperationCode: */
    public static final int GetDeviceInfo = 0x1001;
    /** OperationCode: */
    public static final int OpenSession = 0x1002;
    /** OperationCode: */
    public static final int CloseSession = 0x1003;

    /** OperationCode: */
    public static final int GetStorageIDs = 0x1004;
    /** OperationCode: */
    public static final int GetStorageInfo = 0x1005;
    /** OperationCode: */
    public static final int GetNumObjects = 0x1006;
    /** OperationCode: */
    public static final int GetObjectHandles = 0x1007;

    /** OperationCode: */
    public static final int GetObjectInfo = 0x1008;
    /** OperationCode: */
    public static final int GetObject = 0x1009;
    /** OperationCode: */
    public static final int GetThumb = 0x100a;
    /** OperationCode: */
    public static final int DeleteObject = 0x100b;

    /** OperationCode: */
    public static final int SendObjectInfo = 0x100c;
    /** OperationCode: */
    public static final int SendObject = 0x100d;
    /** OperationCode: */
    public static final int InitiateCapture = 0x100e;
    /** OperationCode: */
    public static final int FormatStore = 0x100f;

    /** OperationCode: */
    public static final int ResetDevice = 0x1010;
    /** OperationCode: */
    public static final int SelfTest = 0x1011;
    /** OperationCode: */
    public static final int SetObjectProtection = 0x1012;
    /** OperationCode: */
    public static final int PowerDown = 0x1013;

    /** OperationCode: */
    public static final int GetDevicePropDesc = 0x1014;
    /** OperationCode: */
    public static final int GetDevicePropValue = 0x1015;
    /** OperationCode: */
    public static final int SetDevicePropValue = 0x1016;
    /** OperationCode: */
    public static final int ResetDevicePropValue = 0x1017;

    /** OperationCode: */
    public static final int TerminateOpenCapture = 0x1018;
    /** OperationCode: */
    public static final int MoveObject = 0x1019;
    /** OperationCode: */
    public static final int CopyObject = 0x101a;
    /** OperationCode: */
    public static final int GetPartialObject = 0x101b;

    /** OperationCode: */
    public static final int InitiateOpenCapture = 0x101c;
    
    /**PTP Operation Codes (EOS specific)*/
	public static final int EOS_OC_GetStorageIDs =				0x9101;
	public static final int EOS_OC_GetStorageInfo =				0x9102;
	public static final int EOS_OC_GetObject =					0x9107;
	public static final int EOS_OC_GetDeviceInfoEx =			0x9108;
	public static final int EOS_OC_GetObjectIDs	=				0x9109;
	public static final int EOS_OC_Capture =					0x910f;
	public static final int EOS_OC_SetDevicePropValue =			0x9110;
	public static final int EOS_OC_SetPCConnectMode	=			0x9114;
	public static final int EOS_OC_SetExtendedEventInfo =		0x9115;
	public static final int EOS_OC_GetEvent	=					0x9116;
	public static final int EOS_OC_TransferComplete	=			0x9117;
	public static final int EOS_OC_CancelTransfer =				0x9118;
	public static final int EOS_OC_ResetTransfer =				0x9119;
	public static final int EOS_OC_GetDevicePropValue =			0x9127;
	public static final int EOS_OC_GetLiveViewPicture =			0x9153;
	public static final int EOS_OC_MoveFocus =					0x9155;

	/**PTP Device Properties*/
	public static final int EOS_DPC_CameraDescription =			0xD402;

	/**Non-PTP Device properties*/
	public static final int EOS_DPC_Aperture =					0xD101;
	public static final int EOS_DPC_ShutterSpeed =				0xD102;
	public static final int EOS_DPC_Iso	= 						0xD103;
	public static final int EOS_DPC_ExposureCompensation =		0xD104;
	public static final int EOS_DPC_ShootingMode =				0xD105;
	public static final int EOS_DPC_DriveMode =					0xD106;
	public static final int EOS_DPC_ExpMeterringMode =			0xD107;
	public static final int EOS_DPC_AFMode =					0xD108;
	public static final int EOS_DPC_WhiteBalance =				0xD109;
	public static final int EOS_DPC_PictureStyle =				0xD110;
	public static final int EOS_DPC_TransferOption =			0xD111;
	public static final int EOS_DPC_UnixTime =					0xD113;
	public static final int EOS_DPC_ImageQuality =				0xD120;
	public static final int EOS_DPC_LiveView =					0xD1B0;
	public static final int EOS_DPC_AvailableShots =  			0xD11B;
	public static final int EOS_DPC_CaptureDestination =   		0xD11C;
	public static final int EOS_DPC_BracketMode =          		0xD11D;

	/**Non-PTP Events*/
	public static final int EOS_EC_DevPropChanged =				0xC189;
	public static final int EOS_EC_ObjectCreated =				0xC181;
	public static final int EOS_EC_DevPropValuesAccepted =		0xC18A;
	public static final int EOS_EC_Capture =					0xC18B;
	public static final int EOS_EC_HalfPushReleaseButton =		0xC18E;
	
	
    public String getCodeName (int code)
    {
	return factory.getOpcodeString (code);
    }

    static String _getOpcodeString (int code)
    {
	switch (code) {
	    case GetDeviceInfo:		return "GetDeviceInfo";
	    case OpenSession:		return "OpenSession";
	    case CloseSession:		return "CloseSession";
	       
	    case GetStorageIDs:		return "GetStorageIDs";
	    case GetStorageInfo:	return "GetStorageInfo";
	    case GetNumObjects:		return "GetNumObjects";
	    case GetObjectHandles:	return "GetObjectHandles";
	       
	    case GetObjectInfo:		return "GetObjectInfo";
	    case GetObject:		return "GetObject";
	    case GetThumb:		return "GetThumb";
	    case DeleteObject:		return "DeleteObject";
	       
	    case SendObjectInfo:	return "SendObjectInfo";
	    case SendObject:		return "SendObject";
	    case InitiateCapture:	return "InitiateCapture";
	    case FormatStore:		return "FormatStore";
	       
	    case ResetDevice:		return "ResetDevice";
	    case SelfTest:		return "SelfTest";
	    case SetObjectProtection:	return "SetObjectProtection";
	    case PowerDown:		return "PowerDown";
	       
	    case GetDevicePropDesc:	return "GetDevicePropDesc";
	    case GetDevicePropValue:	return "GetDevicePropValue";
	    case SetDevicePropValue:	return "SetDevicePropValue";
	    case ResetDevicePropValue:	return "ResetDevicePropValue";
	       
	    case TerminateOpenCapture:	return "TerminateOpenCapture";
	    case MoveObject:		return "MoveObject";
	    case CopyObject:		return "CopyObject";
	    case GetPartialObject:	return "GetPartialObject";
	       
	    case InitiateOpenCapture:	return "InitiateOpenCapture";
	}
	return Container.getCodeString (code);
    }
}
