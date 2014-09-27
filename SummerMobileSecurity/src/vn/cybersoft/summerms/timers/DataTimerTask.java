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

import java.util.TimerTask;



import vn.cybersoft.summerms.Constants;
import vn.cybersoft.summerms.R;
import vn.cybersoft.summerms.model.TrafficSnapshot;
import vn.cybersoft.summerms.services.AppLockerService;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class DataTimerTask extends TimerTask {
	// OS packages

	// OS setting packages
	NotificationManager notificationManager;
	private ActivityManager activityManager;
	private AppLockerService service;

	/**
	 * Constructor
	 * 
	 * @param activityManager
	 */
	private Context mContext;
	public DataTimerTask(AppLockerService service, ActivityManager activityManager,Context mContext) {
		this.activityManager = activityManager;
		this.service = service;
		this.mContext=mContext;
		notificationManager = (NotificationManager) this.mContext.getSystemService(Context.NOTIFICATION_SERVICE);
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
		TrafficSnapshot latest=new TrafficSnapshot(mContext);
		long dataCurrent=latest.getDevice().getRx()+latest.getDevice().getTx();
		if(dataCurrent >getSharedPreferences()){
			NotificationCompat.Builder mbBuilder=new NotificationCompat.Builder(mContext)
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentTitle(R.string.app_name+"")
			.setContentText("You have exceeded the default data set!")
			.setAutoCancel(true);
			
			notificationManager.notify(1, mbBuilder.build());
		}
	}
	public long getSharedPreferences()
	{
		SharedPreferences pre=mContext.getSharedPreferences
				("my_data",Context.MODE_PRIVATE);

		if(pre != null){
			Log.d("share", pre.getLong("plan", 1)+"");
			return pre.getLong("plan", 1);
		}
		return 1;
	}
}
