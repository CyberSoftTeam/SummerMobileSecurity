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

import java.util.ArrayList;
import java.util.List;

import vn.cybersoft.summerms.Preferences;
import vn.cybersoft.summerms.R;
import vn.cybersoft.summerms.tasks.TimeConsumeTask;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * @author vietdung
 *
 */
public class RunningListFragment extends Fragment {
	private ActivityManager activityManager;
	private static List<RunningItem> items = new ArrayList<RunningListFragment.RunningItem>();
	private ListView listView;
	private RunningItemAdapter adapter;
	private PackageManager pm;

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
		View view = inflater.inflate(R.layout.running_list, null);
		listView = (ListView) view.findViewById(R.id.runninglist);
		
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
				}
				
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
				}*/
				
				List<RunningAppProcessInfo> processInfos = activityManager.
						getRunningAppProcesses();
				for (RunningAppProcessInfo info : processInfos) {
					RunningItem item = new RunningItem();
					
					try {
						ApplicationInfo appInfo = pm
								.getApplicationInfo(info.processName,
										PackageManager.GET_ACTIVITIES);
						item.label = appInfo.loadLabel(pm);
						item.icon = appInfo.loadIcon(pm);
						
					} catch (NameNotFoundException e) {
						item.label = info.processName;
						item.icon = getResources().getDrawable(R.drawable.default_app_icon);
					}
					
					switch (info.importance) {
					case RunningAppProcessInfo.IMPORTANCE_BACKGROUND:
						item.importance = getString(R.string.importance_background_desc);
						break;
					case RunningAppProcessInfo.IMPORTANCE_EMPTY:
						item.importance = getString(R.string.importance_empty_desc);
						break;
					case RunningAppProcessInfo.IMPORTANCE_FOREGROUND:
						item.importance = getString(R.string.importance_foreground_desc);
						break;
					case RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE:
						item.importance = getString(R.string.importance_perceptible_desc);
						break;
					case RunningAppProcessInfo.IMPORTANCE_SERVICE:
						item.importance = getString(R.string.importance_service_desc);
						break;
					case RunningAppProcessInfo.IMPORTANCE_VISIBLE:
						item.importance = getString(R.string.importance_visible_desc);
						break;
					}
					
					items.add(item);
				}
				
				adapter = new RunningItemAdapter(getActivity(), pm);
				adapter.setData(items);
				
				return true;
			}
			
			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				if (result) {
					listView.setAdapter(adapter);
				}
			}
		};
		task.execute();
		
		return view;
	}
	
	public static class RunningItem {
		public Drawable icon;
		public CharSequence label;
		public String importance;
	}
}
