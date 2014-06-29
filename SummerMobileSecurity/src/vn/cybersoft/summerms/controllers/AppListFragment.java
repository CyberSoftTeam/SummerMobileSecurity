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
import vn.cybersoft.summerms.services.PackageService;
import vn.cybersoft.summerms.tasks.TimeConsumeTask;
import vn.cybersoft.summerms.widgets.AnimatedExpandableListView;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;

/**
 * @author vietdung
 *
 */
public class AppListFragment extends Fragment {
	private AnimatedExpandableListView listview;
	private AppItemAdapter adapter;
	private static List<AppItem> items = new ArrayList<AppListFragment.AppItem>();
	private PackageManager pm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pm = Preferences.getInstance().getApplicationContext().getPackageManager();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.app_list, null);
		listview = (AnimatedExpandableListView) view.findViewById(R.id.applist);

		TimeConsumeTask task = new TimeConsumeTask(getActivity()) {
			@Override
			protected Boolean doInBackground(Void... params) {
				super.doInBackground();

				List<ResolveInfo> apps = PackageService.getInstance().getApps(pm);
				AppItem app;

				try {
					for (ResolveInfo resolveInfo : apps) {
						app = new AppItem();
						app.info = resolveInfo;
						app.permissions = PackageService.getInstance()
								.getAppPermissions(pm, resolveInfo);
						items.add(app);
					}
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}

				adapter = new AppItemAdapter(getActivity(), pm);
				adapter.setData(items);
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				if (result) {
					listview.setAdapter(adapter);
				}
			}
		};
		task.execute();
		
		// In order to show animations, we need to use a custom click handler
		// for our ExpandableListView.
		listview.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				Log.d("tag","on group click");
				// We call collapseGroupWithAnimation(int) and
				// expandGroupWithAnimation(int) to animate group 
				// expansion/collapse.
				if (listview.isGroupExpanded(groupPosition)) {
					listview.collapseGroupWithAnimation(groupPosition);
				} else {
					listview.expandGroupWithAnimation(groupPosition);
				}
				return true;
			}

		});
		
		return view;
	}

	public static List<AppListFragment.AppItem> getApps() {
		return items;
	}
	
	public static class AppItem {
		public ResolveInfo info;
		public String[] permissions;
	}

}
