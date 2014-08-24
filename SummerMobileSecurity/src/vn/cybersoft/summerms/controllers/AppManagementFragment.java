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
		
		/*for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
			TextView tabTitle = (TextView) mTabHost.getTabWidget()
					.getChildAt(i).findViewById(android.R.id.title);
			tabTitle.setTextSize(TEXTSIZE);
			mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.rta_tab_selector);
		}
		 */

		return rootView;
	}
}
