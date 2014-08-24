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
package vn.cybersoft.summerms.controllers;

import java.io.Console;
import java.util.List;

import vn.cybersoft.summerms.Constants;
import vn.cybersoft.summerms.Preferences;
import vn.cybersoft.summerms.R;
import vn.cybersoft.summerms.tasks.TimeConsumeTask;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author vietdung
 *
 */
public class RunningListFragment extends Fragment {
	private PackageManager pm;
	private ActivityManager activityManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Preferences pre = Preferences.getInstance();
		pm = pre.getApplicationContext().getPackageManager();
		activityManager = (ActivityManager) pre.getBaseContext().getSystemService(
				Context.ACTIVITY_SERVICE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_runnings, null);
		final TextView textView = (TextView) view.findViewById(R.id.runnings);
		final StringBuilder stringBuilder = new StringBuilder();
		
		TimeConsumeTask task = new TimeConsumeTask(getActivity()) {
			@Override
			protected Boolean doInBackground(Void... params) {
				super.doInBackground();
				
				/*List<ActivityManager.RunningTaskInfo> taskInfos = activityManager
						.getRunningTasks(40);
				
				for (RunningTaskInfo runningTaskInfo : taskInfos) {
					stringBuilder.append("id="+runningTaskInfo.id+"\n");
					stringBuilder.append("desc="+runningTaskInfo.description+"\n");
					stringBuilder.append("numActivities="+runningTaskInfo.numActivities+"\n");
					stringBuilder.append("numRunning="+runningTaskInfo.numRunning+"\n");
					stringBuilder.append("topActivity="+runningTaskInfo.topActivity+"\n");
					stringBuilder.append("baseActivity="+runningTaskInfo.baseActivity+"\n");
					stringBuilder.append("\n\n");
				}*/
				
				List<RunningAppProcessInfo> processInfos = activityManager.
						getRunningAppProcesses();
				for (RunningAppProcessInfo info : processInfos) {
					stringBuilder.append("processName: "+info.processName+"\n");
					stringBuilder.append("pid: "+info.pid+"\n");
					stringBuilder.append("uid: "+info.uid+"\n");
					stringBuilder.append("importance: "+info.importance+"\n");
					stringBuilder.append("importanceReasonCode: "+info.importanceReasonCode+"\n");
					stringBuilder.append("importanceReasonComponent: "+info.importanceReasonComponent+"\n");
					stringBuilder.append("importanceReasonPid: "+info.importanceReasonPid+"\n");
					stringBuilder.append("lru: "+info.lru+"\n");
					stringBuilder.append("pkgList: ");
					for (String pkg : info.pkgList) {
						stringBuilder.append(pkg+" ");
					}
					stringBuilder.append("\n\n");
				}
				
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				if (result) {
					textView.setText(stringBuilder.toString());
					textView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							activityManager.killBackgroundProcesses(Constants.CURRENT_PACKAGE);
							Process.sendSignal(Process.myPid(), Process.SIGNAL_KILL);
						}
					});
				}
			}
		};
		task.execute();
		
		
		return view;
	}

}
