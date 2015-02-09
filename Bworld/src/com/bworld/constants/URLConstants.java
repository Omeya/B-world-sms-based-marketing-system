package com.bworld.constants;

public interface URLConstants {

	public static String URL_HEAD									="http://test.droidquizgame.com/index.php/";
	//public static String URL_HEAD									="http://datakeeping.net/ws/";
	
	public static String URL_LOGIN									=URL_HEAD+"users/login";
	
	public static String URL_FB_LOGIN								=URL_HEAD+"users/fbAdd";
	
	public static String URL_SIGN_UP								=URL_HEAD+"users/add";
	
	public static String URL_GET_PROFILE							=URL_HEAD+"users/getUser";
	
	public static String URL_GET_FRIENDS							=URL_HEAD+"users/getUsers/";
	
	public static String URL_GET_TRUSTED_FRIENDS					=URL_HEAD+"users/getTrustedUsers/";
	
	public static String URL_ADD_TRUSTED_FRIENDS					=URL_HEAD+"user_friends/add";
	
	public static String URL_CONFIRM_TRUSTED_FRIENDS				=URL_HEAD+"user_friends/accept";
	
	public static String URL_GET_CONTACTS							=URL_HEAD+"contacts/getContacts";
	
	public static String URL_GET_RAW_CONTACTS						=URL_HEAD+"contacts/getRawContacts";
	
	public static String URL_GET_ADDS								=URL_HEAD+"advertisements/getAdds";
	
	public static String URL_ADD_RAW_CONTACTS						=URL_HEAD+"contacts/addRawContacts";
	
	public static String URL_ADD_CONTACTS							=URL_HEAD+"contacts/addContacts";
	
	public static String URL_ADD_DELIVERY_REPORT					=URL_HEAD+"user_advertisements/add";
	
	public static String URL_ADD_LOCATION							=URL_HEAD+"users/addLocation";
	
}
  