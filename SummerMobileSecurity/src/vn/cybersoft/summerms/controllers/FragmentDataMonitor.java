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
/**
 * @author Phạm Văn Năm
 */
import vn.cybersoft.summerms.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentDataMonitor  extends Fragment{
	public static final String TAG="FragmentMain";
	public FragmentDataMonitor() {
		// TODO Auto-generated constructor stub
	}
	private FragmentTabHost mTabHost;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//getData();
		View rootView=inflater.inflate(R.layout.layout_fragment_datamonitor, container, false);
		mTabHost = (FragmentTabHost) rootView.findViewById(R.id.tabhost) ;
		mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.tabFrameLayout);
		mTabHost.addTab(mTabHost.newTabSpec("monitor").setIndicator("Monitor"),
				FragmentTotal.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("statistics").setIndicator("Statistics"),
				FragmentAllAppUserData.class, null);

		for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
			mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.summer_tab_indicator_holo);
		}
		
		return mTabHost;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mTabHost = null;
	}
	
}
