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


import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import vn.cybersoft.summerms.R;
import vn.cybersoft.summerms.model.App;
import vn.cybersoft.summerms.model.DataMonitorHelper;
import vn.cybersoft.summerms.model.DateTraffic;
import vn.cybersoft.summerms.model.TrafficRecord;
import vn.cybersoft.summerms.model.TrafficSnapshot;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentDataMonitor  extends Fragment{
	public static final String TAG="FragmentMain";
	private TrafficSnapshot latest=null;
	private DataMonitorHelper dataHelper;
	public FragmentDataMonitor() {
		// TODO Auto-generated constructor stub
	}
	private FragmentTabHost mTabHost;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		dataHelper=new DataMonitorHelper(getActivity());
		getData();
		View rootView=inflater.inflate(R.layout.layout_fragment_datamonitor, container, false);
		mTabHost = (FragmentTabHost) rootView.findViewById(R.id.tabhost) ;
		mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.tabFrameLayout);
		mTabHost.addTab(mTabHost.newTabSpec("monitor").setIndicator("Monitor"),
				FragmentTotal.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("statistics").setIndicator("Statistics"),
				FragmentAllAppUserData.class, null);
		return mTabHost;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mTabHost = null;
	}
	@SuppressWarnings("deprecation")
	private void getData() {
		latest=new TrafficSnapshot(getActivity());
		DateTraffic dateTraffic=new DateTraffic();
		dateTraffic=dataHelper.getDAY((new Date().getDate()-1)+"."+(new Date().getMonth()+1));
		if((new Date().getDate()>1)&&(dateTraffic.getDate() != null)){
				long dataR=latest.getDevice().getRx()-dateTraffic.getStartdownLoad();
				long dataT=latest.getDevice().getTx()-dateTraffic.getStartupLoad();
				dataHelper.updateDAY(new DateTraffic(new Date().getDate()+"."+(new Date().getMonth()+1),dataT,dataR));
				Log.d(TAG, "Update new-"+dataR +"-"+dataT+"-"+dateTraffic.getDate());
		}else{
			Log.d(TAG, "mis");
			dataHelper.deteleAllTable();
			dataHelper=new DataMonitorHelper(getActivity());
			Calendar calendar = Calendar.getInstance();
			int year=calendar.get(Calendar.YEAR);
			calendar.set(year, new Date().getMonth(), 2);
			int dayNumber=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			for (int i = 1; i <(new Date().getDate()); i++) {
				if(dataHelper.addDAY(new DateTraffic((i)+"."+(new Date().getMonth()+1),0,0))!=-1){
					Log.d(TAG, "Add Day1-"+latest.getDevice().getRx() +"-"+latest.getDevice().getTx());
				}
			}
			if(dataHelper.addDAY(new DateTraffic(new Date().getDate()+"."+(new Date().getMonth()+1),latest.getDevice().getTx(),latest.getDevice().getRx()))!=-1){
				Log.d(TAG, "Add Day-"+latest.getDevice().getRx() +"-"+latest.getDevice().getTx());
			}
			for (int i = (new Date().getDate())+1; i <=dayNumber; i++) {
				if(dataHelper.addDAY(new DateTraffic((i)+"."+(new Date().getMonth()+1),0,0))!=-1){
					Log.d(TAG, "Add Day2-"+latest.getDevice().getRx() +"-"+latest.getDevice().getTx());
				}
			}

		}

		HashSet<Integer> intersection=new HashSet<Integer>(latest.getApps().keySet());

		for (Integer uid : intersection) {
			TrafficRecord latest_rec=latest.getApps().get(uid);
			addAppData(uid,latest_rec.getTag(), latest_rec);
		}

	}
	private void addAppData(int uid,CharSequence name, TrafficRecord latest_rec) {
		if (latest_rec.getRx()>-1 || latest_rec.getTx()>-1) {
			App app =new App();
			app.setAppid(uid);
			app.setAppName(name.toString());
			app.setLaststartdownLoad(0);
			app.setLaststartupLoad(0);
			app.setStartdownLoad(latest_rec.getRx());
			app.setStartupLoad(latest_rec.getTx());
			if(!dataHelper.isAppContain(uid)) dataHelper.addApp(app);
		}
	}

}
