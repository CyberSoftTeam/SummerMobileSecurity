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

import java.util.Timer;

import vn.cybersoft.summerms.services.AppLockerService;

import android.app.ActivityManager;

public class PermissionTimer extends Timer {
	private PermissionTimerTask task;

	/**
	 * Constructor
	 * 
	 * @param context
	 * @param db
	 */
	public PermissionTimer(AppLockerService service, ActivityManager activityManager) {
		task = new PermissionTimerTask(service, activityManager);
	}

	/**
	 * Schedule the task
	 */
	public void schedule(long period) {
		super.schedule(task, 0, period);
	}

	@Override
	public void cancel() {
		super.cancel();
	}

}
