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

import java.util.HashSet;
import java.util.Set;

import vn.cybersoft.summerms.database.DatabaseContact;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;

public class Preferences extends Application {
	// Preferences Constants
	private final String PREFERENCES_NAME = "preferences";
	private final String SOMETHING = "something";
	private final String LOCKED_APPS = "applist";
	private final String PIN = "pinpin";

	private SharedPreferences preferences;
	private static Preferences pre;
	
	private DatabaseContact datacontact; 
	
	@SuppressLint("UseSparseArrays")
	@Override
	public void onCreate() {
		super.onCreate();
		pre = this;
		preferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
		datacontact = new DatabaseContact();
	}

	/**
	 * Get an instance of Preferences
	 * 
	 * @return
	 */
	public static Preferences getInstance() {
		return pre;
	}
	
	/**
	 * 
	 */
	public DatabaseContact getDatabaseContact(){
		return datacontact;
	}
	
	/**
	 * Save state of an application
	 * 
	 */
	public void saveAppState(String app, int state) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(app, state);
		editor.commit();
	}

	/**
	 * Get state of an application
	 * 
	 */
	public int getAppState(String app) {
		return preferences.getInt(app, 0);
	}

	/**
	 * Save PIN to preference
	 * 
	 * @param pin
	 */
	public void savePIN(String pin) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(PIN, pin);
		editor.commit();
	}

	/**
	 * Get PIN from preference
	 * 
	 * @return null if not exist
	 */
	public String getPIN() {
		return preferences.getString(PIN, null);
	}

	/**
	 * Save apps to preference
	 * 
	 * @param apps
	 */
	public void saveLockedApps(Set<String> apps) {
		SharedPreferences.Editor editor = preferences.edit();
		StringBuilder appset = new StringBuilder();
		for (String app : apps) {
			appset.append(app+Constants.COMMA);
		}
		editor.putString(LOCKED_APPS, appset.toString());
		editor.commit();
	}

	/**
	 * Get apps from preference
	 * 
	 * @return empty set if not exist
	 */
	public Set<String> getLockedApps() {
		Set<String> appset = new HashSet<String>();
		String data = preferences.getString(LOCKED_APPS, null);
		if (data!=null) {
			String[] apps = data.split(Constants.COMMA);
			for (String app : apps) {
				appset.add(app);
			}
		}
		return appset;
	}

	/**
	 * Save something to preference
	 * 
	 * @param something
	 */
	public void saveSomething(String something) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(SOMETHING, something);
		editor.commit();
	}

	/**
	 * Get something from preference
	 * 
	 * @return null if not exist
	 */
	public String getSomething() {
		return preferences.getString(SOMETHING, null);
	}

}
