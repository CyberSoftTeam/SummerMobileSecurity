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
package vn.cybersoft.summerms.timers;

import java.util.List;
import java.util.TimerTask;

import vn.cybersoft.summerms.Constants;
import vn.cybersoft.summerms.Preferences;
import vn.cybersoft.summerms.services.AppLockerService;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.util.Log;

public class PermissionTimerTask extends TimerTask {
	// OS packages
	private final String LAUNCHER = "com.android.launcher";
	private final String RECENTS = "com.android.systemui";

	// OS setting packages
	private final String SETTING = "com.android.settings";

	private ActivityManager activityManager;
	private AppLockerService service;

	/**
	 * Constructor
	 * 
	 * @param activityManager
	 */
	public PermissionTimerTask(AppLockerService service, ActivityManager activityManager) {
		this.activityManager = activityManager;
		this.service = service;
	}

	@Override
	public void run() {
		Log.i(Constants.TAG, "===  Service is checking  ===");
		checkPermission();
	}

	/**
	 * Check running permission
	 */
	private void checkPermission() {
		// Get the info from the currently running task
		List<ActivityManager.RunningTaskInfo> taskInfo = activityManager
				.getRunningTasks(1);

		ComponentName componentInfo = taskInfo.get(0).topActivity;
		Log.i(Constants.TAG, componentInfo.getPackageName() + " - "
				+ componentInfo.getShortClassName());

		// check application
		String packName = componentInfo.getPackageName();
		boolean exceptions = 
				packName.equalsIgnoreCase(LAUNCHER) ||
				packName.equalsIgnoreCase(RECENTS) ||
				packName.equalsIgnoreCase(SETTING);
		boolean isLocked = Preferences.getInstance()
				.getAppState(packName)==Constants.STATE_LOCKED;
		
		if (!exceptions && isLocked) {
			String PIN = Preferences.getInstance().getPIN();
			if (PIN!=null) {
				// Kill locked app
				activityManager.killBackgroundProcesses(packName);

				// checking PIN input
				service.showPINInput(packName);
			}
		} 

	}

}
