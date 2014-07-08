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
package vn.cybersoft.summerms.services;

import java.util.List;

import vn.cybersoft.summerms.Constants;
import vn.cybersoft.summerms.Preferences;
import vn.cybersoft.summerms.R;
import vn.cybersoft.summerms.controllers.AppListFragment;
import vn.cybersoft.summerms.controllers.EnterPINActivity;
import vn.cybersoft.summerms.controllers.MainActivity;
import vn.cybersoft.summerms.receivers.ScreenServiceReceiver;
import vn.cybersoft.summerms.timers.PermissionTimer;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * @author vietdung
 *
 */
public class AppLockerService extends Service {
	private static final String t = "AppLockerService";
	public static final int FOREGROUND_NTF_ID = 8681;
	private final int PEMISSION_PERIOD = 3*1000;
	public static boolean isRunning = false;

	private ActivityManager activityManager;
	private ScreenServiceReceiver receiver;
	private PermissionTimer permissionTimer;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		activityManager = (ActivityManager) getBaseContext().getSystemService(
				Context.ACTIVITY_SERVICE);

		// Register screen on/off receiver
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		receiver = new ScreenServiceReceiver(this);

		registerReceiver(receiver, filter);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(t,"Starting background service");
		isRunning = true;
		
		Intent appIntent = new Intent(this, MainActivity.class);
		PendingIntent startAppPintent = 
				PendingIntent.getActivity(this, 0, appIntent, 0);
		RemoteViews ntfContentView = 
				new RemoteViews(getPackageName(), R.layout.notification_bar_layout);
		ntfContentView.setOnClickPendingIntent(R.id.app_icon, startAppPintent);

		Notification notification = new Notification();
		notification.icon = R.drawable.ic_launcher;
		notification.contentView = ntfContentView;
		startForeground(FOREGROUND_NTF_ID, notification);

		// Start checking timer
		startChecking();

		return START_REDELIVER_INTENT;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		isRunning = false;
		
		// Unregister screen on/off
		unregisterReceiver(receiver);

		// Stop checking permission
		stopChecking();

		Log.d(t,"Stopped background service");

	}

	/**
	 * Start service when screen on
	 */
	public void screenOn() {
		Log.i(Constants.TAG, "xxxxx  Service screen on xxxxx");
		startChecking();
	}

	/**
	 * Stop service when screen off
	 */
	public void screenOff() {
		Log.i(Constants.TAG, "xxxxx  Service screen off xxxxx");
		stopChecking();
		resetPINPassState();
	}

	/**
	 * Reset 'PIN passed' state of applications to 'Locked state' 
	 */
	private void resetPINPassState() {
		Preferences pre = Preferences.getInstance();
		List<AppListFragment.AppItem> items = AppListFragment.getApps();
		String packName;
		for (AppListFragment.AppItem appItem : items) {
			packName = appItem.info.activityInfo.packageName;
			int currentState = pre.getAppState(packName);
			if (currentState==Constants.STATE_PINPASSED) {
				pre.saveAppState(packName, Constants.STATE_LOCKED);
			}
		}
		
		// reset PINpassed state of current package
		Preferences.getInstance()
			.saveAppState(Constants.CURRENT_PACKAGE, Constants.STATE_LOCKED);
	}

	/**
	 * Start timer to checking permission
	 */
	private void startChecking() {
		// Start checking timer
		permissionTimer = new PermissionTimer(AppLockerService.this, activityManager);
		permissionTimer.schedule(PEMISSION_PERIOD);
	}

	/**
	 * Stop timer which is checking permission
	 */
	private void stopChecking() {
		if (permissionTimer != null) {
			permissionTimer.cancel();
			System.gc();
		}
	}

	public void showPINInput(final String packName){
		Intent intent = new Intent(this, EnterPINActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(EnterPINActivity.PACK_KEY, packName);
		startActivity(intent);
		stopChecking();
	}

}
