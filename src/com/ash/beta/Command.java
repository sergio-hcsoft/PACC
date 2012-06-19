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
	public static final int EOS_OC_Capture	=					0x910f;
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
	
	public static final int PTP_DPC_CANON_EOS_Aperture				= 0xD101;
	public static final int PTP_DPC_CANON_EOS_ShutterSpeed			= 0xD102;
	public static final int PTP_DPC_CANON_EOS_ISOSpeed				= 0xD103;
	public static final int PTP_DPC_CANON_EOS_ExpCompensation		= 0xD104;
	public static final int PTP_DPC_CANON_EOS_AutoExposureMode		= 0xD105;
	public static final int PTP_DPC_CANON_EOS_DriveMode				= 0xD106;
	public static final int PTP_DPC_CANON_EOS_MeteringMode			= 0xD107; 
	public static final int PTP_DPC_CANON_EOS_FocusMode				= 0xD108;
	public static final int PTP_DPC_CANON_EOS_WhiteBalance			= 0xD109;
	public static final int PTP_DPC_CANON_EOS_ColorTemperature		= 0xD10A;
	public static final int PTP_DPC_CANON_EOS_WhiteBalanceAdjustA	= 0xD10B;
	public static final int PTP_DPC_CANON_EOS_WhiteBalanceAdjustB	= 0xD10C;
	public static final int PTP_DPC_CANON_EOS_WhiteBalanceXA		= 0xD10D;
	public static final int PTP_DPC_CANON_EOS_WhiteBalanceXB		= 0xD10E;
	public static final int PTP_DPC_CANON_EOS_ColorSpace			= 0xD10F;
	public static final int PTP_DPC_CANON_EOS_PictureStyle			= 0xD110;
	public static final int PTP_DPC_CANON_EOS_BatteryPower			= 0xD111;
	public static final int PTP_DPC_CANON_EOS_BatterySelect			= 0xD112;
	public static final int PTP_DPC_CANON_EOS_CameraTime			= 0xD113;
	public static final int PTP_DPC_CANON_EOS_Owner					= 0xD115;
	public static final int PTP_DPC_CANON_EOS_ModelID				= 0xD116;
	public static final int PTP_DPC_CANON_EOS_PTPExtensionVersion	= 0xD119;
	public static final int PTP_DPC_CANON_EOS_DPOFVersion			= 0xD11A;
	public static final int PTP_DPC_CANON_EOS_AvailableShots		= 0xD11B;
	public static final int PTP_CANON_EOS_CAPTUREDEST_HD			= 4;
	public static final int PTP_DPC_CANON_EOS_CaptureDestination	= 0xD11C;
	public static final int PTP_DPC_CANON_EOS_BracketMode			= 0xD11D;
	public static final int PTP_DPC_CANON_EOS_CurrentStorage		= 0xD11E;
	public static final int PTP_DPC_CANON_EOS_CurrentFolder			= 0xD11F;
	
	public static final int PTP_DPC_CANON_EOS_ImageFormat		= 0xD120;	
	public static final int PTP_DPC_CANON_EOS_ImageFormatCF		= 0xD121;	
	public static final int PTP_DPC_CANON_EOS_ImageFormatSD		= 0xD122;	
	public static final int PTP_DPC_CANON_EOS_ImageFormatExtHD	= 0xD123;	
	public static final int PTP_DPC_CANON_EOS_CompressionS		= 0xD130;
	public static final int PTP_DPC_CANON_EOS_CompressionM1		= 0xD131;
	public static final int PTP_DPC_CANON_EOS_CompressionM2		= 0xD132;
	public static final int PTP_DPC_CANON_EOS_CompressionL		= 0xD133;
	public static final int PTP_DPC_CANON_EOS_PCWhiteBalance1	= 0xD140;
	public static final int PTP_DPC_CANON_EOS_PCWhiteBalance2	= 0xD141;
	public static final int PTP_DPC_CANON_EOS_PCWhiteBalance3	= 0xD142;
	public static final int PTP_DPC_CANON_EOS_PCWhiteBalance4	= 0xD143;
	public static final int PTP_DPC_CANON_EOS_PCWhiteBalance5	= 0xD144;
	public static final int PTP_DPC_CANON_EOS_MWhiteBalance		= 0xD145;
	public static final int PTP_DPC_CANON_EOS_PictureStyleStandard	= 0xD150;
	public static final int PTP_DPC_CANON_EOS_PictureStylePortrait	= 0xD151;
	public static final int PTP_DPC_CANON_EOS_PictureStyleLandscape	= 0xD152;
	public static final int PTP_DPC_CANON_EOS_PictureStyleNeutral	= 0xD153;
	public static final int PTP_DPC_CANON_EOS_PictureStyleFaithful	= 0xD154;
	public static final int PTP_DPC_CANON_EOS_PictureStyleBlackWhite	= 0xD155;
	public static final int PTP_DPC_CANON_EOS_PictureStyleUserSet1	= 0xD160;
	public static final int PTP_DPC_CANON_EOS_PictureStyleUserSet2	= 0xD161;
	public static final int PTP_DPC_CANON_EOS_PictureStyleUserSet3	= 0xD162;
	public static final int PTP_DPC_CANON_EOS_PictureStyleParam1	= 0xD170;
	public static final int PTP_DPC_CANON_EOS_PictureStyleParam2	= 0xD171;
	public static final int PTP_DPC_CANON_EOS_PictureStyleParam3	= 0xD172;
	public static final int PTP_DPC_CANON_EOS_FlavorLUTParams	= 0xD17f;
	public static final int PTP_DPC_CANON_EOS_CustomFunc1		= 0xD180;
	public static final int PTP_DPC_CANON_EOS_CustomFunc2		= 0xD181;
	public static final int PTP_DPC_CANON_EOS_CustomFunc3		= 0xD182;
	public static final int PTP_DPC_CANON_EOS_CustomFunc4		= 0xD183;
	public static final int PTP_DPC_CANON_EOS_CustomFunc5		= 0xD184;
	public static final int PTP_DPC_CANON_EOS_CustomFunc6		= 0xD185;
	public static final int PTP_DPC_CANON_EOS_CustomFunc7		= 0xD186;
	public static final int PTP_DPC_CANON_EOS_CustomFunc8		= 0xD187;
	public static final int PTP_DPC_CANON_EOS_CustomFunc9		= 0xD188;
	public static final int PTP_DPC_CANON_EOS_CustomFunc10		= 0xD189;
	public static final int PTP_DPC_CANON_EOS_CustomFunc11		= 0xD18a;
	public static final int PTP_DPC_CANON_EOS_CustomFunc12		= 0xD18b;
	public static final int PTP_DPC_CANON_EOS_CustomFunc13		= 0xD18c;
	public static final int PTP_DPC_CANON_EOS_CustomFunc14		= 0xD18d;
	public static final int PTP_DPC_CANON_EOS_CustomFunc15		= 0xD18e;
	public static final int PTP_DPC_CANON_EOS_CustomFunc16		= 0xD18f;
	public static final int PTP_DPC_CANON_EOS_CustomFunc17		= 0xD190;
	public static final int PTP_DPC_CANON_EOS_CustomFunc18		= 0xD191;
	public static final int PTP_DPC_CANON_EOS_CustomFunc19		= 0xD192;
	public static final int PTP_DPC_CANON_EOS_CustomFuncEx		= 0xD1a0;
	public static final int PTP_DPC_CANON_EOS_MyMenu		= 0xD1a1;
	public static final int PTP_DPC_CANON_EOS_MyMenuList		= 0xD1a2;
	public static final int PTP_DPC_CANON_EOS_WftStatus		= 0xD1a3;
	public static final int PTP_DPC_CANON_EOS_WftInputTransmission	= 0xD1a4;
	public static final int PTP_DPC_CANON_EOS_HDDirectoryStructure	= 0xD1a5;
	public static final int PTP_DPC_CANON_EOS_BatteryInfo		= 0xD1a6;
	public static final int PTP_DPC_CANON_EOS_AdapterInfo		= 0xD1a7;
	public static final int PTP_DPC_CANON_EOS_LensStatus		= 0xD1a8;
	public static final int PTP_DPC_CANON_EOS_QuickReviewTime	= 0xD1a9;
	public static final int PTP_DPC_CANON_EOS_CardExtension		= 0xD1aa;
	public static final int PTP_DPC_CANON_EOS_TempStatus		= 0xD1ab;
	public static final int PTP_DPC_CANON_EOS_ShutterCounter	= 0xD1ac;
	public static final int PTP_DPC_CANON_EOS_SpecialOption		= 0xD1ad;
	public static final int PTP_DPC_CANON_EOS_PhotoStudioMode	= 0xD1ae;
	public static final int PTP_DPC_CANON_EOS_SerialNumber		= 0xD1af;
	public static final int PTP_DPC_CANON_EOS_EVFOutputDevice	= 0xD1b0;
	public static final int PTP_DPC_CANON_EOS_EVFMode		= 0xD1b1;
	public static final int PTP_DPC_CANON_EOS_DepthOfFieldPreview	= 0xD1b2;
	public static final int PTP_DPC_CANON_EOS_EVFSharpness		= 0xD1b3;
	public static final int PTP_DPC_CANON_EOS_EVFWBMode		= 0xD1b4;
	public static final int PTP_DPC_CANON_EOS_EVFClickWBCoeffs	= 0xD1b5;
	public static final int PTP_DPC_CANON_EOS_EVFColorTemp		= 0xD1b6;
	public static final int PTP_DPC_CANON_EOS_ExposureSimMode	= 0xD1b7;
	public static final int PTP_DPC_CANON_EOS_EVFRecordStatus	= 0xD1b8;
	public static final int PTP_DPC_CANON_EOS_LvAfSystem		= 0xD1ba;
	public static final int PTP_DPC_CANON_EOS_MovSize		= 0xD1bb;
	public static final int PTP_DPC_CANON_EOS_LvViewTypeSelect	= 0xD1bc;
	public static final int PTP_DPC_CANON_EOS_Artist		= 0xD1d0;
	public static final int PTP_DPC_CANON_EOS_Copyright		= 0xD1d1;
	public static final int PTP_DPC_CANON_EOS_BracketValue		= 0xD1d2;
	public static final int PTP_DPC_CANON_EOS_FocusInfoEx		= 0xD1d3;
	public static final int PTP_DPC_CANON_EOS_DepthOfField		= 0xD1d4;
	public static final int PTP_DPC_CANON_EOS_Brightness		= 0xD1d5;
	public static final int PTP_DPC_CANON_EOS_LensAdjustParams	= 0xD1d6;
	public static final int PTP_DPC_CANON_EOS_EFComp		= 0xD1d7;
	public static final int PTP_DPC_CANON_EOS_LensName		= 0xD1d8;
	public static final int PTP_DPC_CANON_EOS_AEB			= 0xD1d9;
	public static final int PTP_DPC_CANON_EOS_StroboSetting		= 0xD1da;
	public static final int PTP_DPC_CANON_EOS_StroboWirelessSetting	= 0xD1db;
	public static final int PTP_DPC_CANON_EOS_StroboFiring		= 0xD1dc;
	public static final int PTP_DPC_CANON_EOS_LensID		= 0xD1dd;
	
	
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
