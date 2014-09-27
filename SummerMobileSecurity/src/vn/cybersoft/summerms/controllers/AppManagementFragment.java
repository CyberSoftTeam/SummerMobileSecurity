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


import vn.cybersoft.summerms.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AppManagementFragment extends Fragment{
	//	private static final float TEXTSIZE = 16f;

	private FragmentTabHost mTabHost;
	public AppManagementFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		View rootView = inflater.inflate(R.layout.fragment_apps, container, false);

		mTabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
		mTabHost.setup(getActivity(),getChildFragmentManager(), R.id.realtabcontent);		        

		String allApps = getString(R.string.all_apps);
		String runningApps = getString(R.string.running_apps);
		mTabHost.addTab(mTabHost.newTabSpec(allApps).setIndicator(allApps),
				AppListFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec(runningApps).setIndicator(runningApps),
				RunningListFragment.class, null);
		
		for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
			mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.summer_tab_indicator_holo);
		}
		
		return rootView;
	}
}
