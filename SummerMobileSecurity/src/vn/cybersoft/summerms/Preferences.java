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

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;

public class Preferences extends Application {
	// Preferences Constants
	private final String PREFERENCES_NAME = "preferences";
	private final String SOMETHING = "something";

	private SharedPreferences preferences;
	private static Preferences pre;

	@SuppressLint("UseSparseArrays")
	@Override
	public void onCreate() {
		super.onCreate();
		pre = this;
		preferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
		
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
