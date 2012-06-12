package com.ash.beta;

public class EosExtension extends NameFactory{

	EosExtension () { }


    /*-------------------------------------------------------------*/

	// PTP Operation Codes (EOS specific)
	public static final int EOS_OC_GetStorageIDs =				0x9101;
	public static final int EOS_OC_GetStorageInfo =				0x9102;
	public static final int EOS_OC_GetObject =	 				0x9107;
	public static final int EOS_OC_GetDeviceInfoEx =			0x9108;
	public static final int EOS_OC_GetObjectIDs =				0x9109;
	public static final int EOS_OC_Capture =					0x910f;
	public static final int EOS_OC_SetDevicePropValue =			0x9110;
	public static final int EOS_OC_SetPCConnectMode =			0x9114;
	public static final int EOS_OC_SetExtendedEventInfo =		0x9115;
	public static final int EOS_OC_GetEvent =					0x9116;
	public static final int EOS_OC_TransferComplete =			0x9117;
	public static final int EOS_OC_CancelTransfer =				0x9118;
	public static final int EOS_OC_ResetTransfer =				0x9119;
	public static final int	EOS_OC_GetDevicePropValue = 		0x9127;
	public static final int	EOS_OC_GetLiveViewPicture =			0x9153;
	public static final int	EOS_OC_MoveFocus =					0x9155;

    public String getOpcodeString (int code)
    {
	switch (code) {
	    case EOS_OC_GetStorageIDs:
	    	return "EOS_OC_GetStorageIDs";
	    case EOS_OC_GetStorageInfo:
			return "EOS_OC_GetStorageInfo";	
	    case EOS_OC_GetObject:
			return "EOS_OC_GetObject";	
	    case EOS_OC_GetDeviceInfoEx:
			return "EOS_OC_GetDeviceInfoEx";	
	    case EOS_OC_GetObjectIDs:
			return "EOS_OC_GetObjectIDs";	
	    case EOS_OC_Capture:
			return "EOS_OC_Capture";	
	    case EOS_OC_SetDevicePropValue:
			return "EOS_OC_SetDevicePropValue";
	    case EOS_OC_SetPCConnectMode:
			return "EOS_OC_SetPCConnectMode";
	    case EOS_OC_SetExtendedEventInfo:
			return "EOS_OC_SetExtendedEventInfo";
	    case EOS_OC_GetEvent:
			return "EOS_OC_GetEvent";
	    case EOS_OC_TransferComplete:
			return "EOS_OC_TransferComplete";
	    case EOS_OC_CancelTransfer:
			return "EOS_OC_CancelTransfer";
	    case EOS_OC_ResetTransfer:
			return "EOS_OC_ResetTransfer";
	    case EOS_OC_GetDevicePropValue:
			return "EOS_OC_GetDevicePropValue";
	    case EOS_OC_GetLiveViewPicture:
			return "EOS_OC_GetLiveViewPicture";
	    case EOS_OC_MoveFocus:
			return "EOS_OC_MoveFocus";
	}
	return Command._getOpcodeString (code);
    }

    /*-------------------------------------------------------------*/

    /** ResponseCode: */
    public static final int FilenameRequired = 0xa001;

    /** ResponseCode: */
    public static final int FilenameConflicts = 0xa002;

    /** ResponseCode: */
    public static final int FilenameInvalid = 0xa003;

    public String getResponseString (int code)
    {
	switch (code) {
	    case FilenameRequired:
		return "Kodak_FilenameRequired";
	    case FilenameConflicts:
		return "Kodak_FilenameConflicts";
	    case FilenameInvalid:
		return "Kodak_FilenameInvalid";
	}
	return Response._getResponseString (code);
    }

    /*-------------------------------------------------------------*/

    /** ObjectFormatCode: ".fw" file for device firmware.  */
    public static final int Firmware = 0xb001;

    /** ObjectFormatCode: ".m3u" style MP3 playlist.  */
    public static final int M3U = 0xb002;


    public String getFormatString (int code)
    {
	switch (code) {
	    case Firmware:
		return "Kodak_Firmware";
	    case M3U:
		return "Kodak_M3U";
	}
	return ObjectInfo._getFormatString (code);
    }

    /*-------------------------------------------------------------*/

    public static final int EOS_DPC_CameraDescription = 			0xD402;
    /** Property code: */
    public static final int prop1 = 0xd001;

    /** Property code: */
    public static final int prop2 = 0xd002;

    /** Property code: */
    public static final int prop3 = 0xd003;

    /** Property code: */
    public static final int prop4 = 0xd004;

    /** Property code: */
    public static final int prop5 = 0xd005;

    /** Property code: */
    public static final int prop6 = 0xd006;

    public String getPropertyName (int code)
    {
	switch (code) {
		case EOS_DPC_CameraDescription:
		return "EOS_DPC_CameraDescription";
	    case prop1:
		return "Kodak_prop1";
	    case prop2:
		return "Kodak_prop2";
	    case prop3:
		return "Kodak_prop3";
	    case prop4:
		return "Kodak_prop4";
	    case prop5:
		return "Kodak_prop5";
	    case prop6:
		return "Kodak_prop6";
	}
	return DevicePropDesc._getPropertyName (code);
    }
}
