/*******************************************************************************
 * Copyright 2014 IUH.CyberSoft Team (http://cyberso.wordpress.com/)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package vn.cybersoft.summerms;

import java.io.File;

import android.os.Environment;

public class Constants {
	public static final String TAG = "SummerMobileSecurity";
	public static final String TAG_SOUND_DETECT = "detect";

	public static final String NEW_LINE = "\n";
	public static final String STRING_CONNECTER = " - ";
	public static final String UNDERSCORE = "_";
	public static final String COMMA = ",";
	public static final String COLON = ": ";
	public static final String COLON2 = ":";
	public static final String DOT = ".";
	public static final String THREE_DOT = "...";
	public static final String MINUS = "-";
	public static final String PLUS = "+";
	public static final String SPACE = " ";
	public static final String EMAIL_FORMAT = "mailto:%s?subject=%s&body=%s";
	public static final String SPACE_ENC = "%20";
	public static final String SLASH = "/";
	public static final String HTTP = "http://";
	public static final String HTTP2 = "http";
	public static final String HTTPS = "https://";

	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final String EMPTY_STRING = "";
	public static final String SUCCESS = "SUCCESS";
	public static final String FAIL = "FAIL";
	public static final String UTF8 = "UTF-8";
	
	public static final String CURRENT_PACKAGE = "vn.cybersoft.summerms";
	
	// Application's states
	public static final int STATE_UNLOCKED = 0; 
	public static final int STATE_LOCKED = 1; 
	public static final int STATE_PINPASSED = 2; 
	
	// Storage paths
	public static final String APPLICATION_PATH = Environment.getExternalStorageDirectory() + File.separator
			+ "SummerMobileSecuriry";
	public static final String DATABASE_PATH = APPLICATION_PATH + File.separator + "databases"; 
}
