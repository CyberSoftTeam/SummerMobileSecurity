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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;

import vn.cybersoft.summerms.R;
import vn.cybersoft.summerms.model.App;
import vn.cybersoft.summerms.model.AppAdapter;
import vn.cybersoft.summerms.model.DataMonitorHelper;
import vn.cybersoft.summerms.model.TrafficRecord;
import vn.cybersoft.summerms.model.TrafficSnapshot;
import vn.cybersoft.summerms.tasks.TimeConsumeTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FragmentAllAppUserData extends Fragment {
	private TrafficSnapshot current=null;
	private DataMonitorHelper dataHelper;
	private ListView lsv;
	private List<App> dataset;
	public FragmentAllAppUserData()  {
		// TODO Auto-generated constructor stub
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView=inflater.inflate(R.layout.layout_usage_app_data, container,
				false);
		dataHelper=new DataMonitorHelper(getActivity());
		lsv=(ListView) rootView.findViewById(R.id.lsv_use_app_data);
		getData();
		return rootView;
	}
	private void getData() {
	
		TimeConsumeTask task = new TimeConsumeTask(getActivity()) {
			@Override
			protected Boolean doInBackground(Void... params) {
				super.doInBackground();
				current=new TrafficSnapshot(getActivity());
				HashSet<Integer> intersection=new HashSet<Integer>(current.getApps().keySet());
				HashMap<String, App> apps=dataHelper.getallAPP();
				dataset=new ArrayList<App>();
				for (Integer uid : intersection) {
					TrafficRecord latest_rec=current.getApps().get(uid);
					if(apps.containsKey(latest_rec.getTag())){
						long rx=Math.abs(latest_rec.getRx()-apps.get(latest_rec.getTag()).getStartdownLoad());
						long tx=Math.abs(latest_rec.getTx()-apps.get(latest_rec.getTag()).getStartupLoad());
						long rm=0,tm=0;
						//update data app
						App app=apps.get(latest_rec.getTag());
						app.setLaststartdownLoad(app.getLaststartdownLoad()+rx);
						app.setLaststartupLoad(app.getLaststartupLoad()+tx);
						app.setStartdownLoad(latest_rec.getRx());
						app.setStartupLoad(latest_rec.getTx());
						//
						rm=(long) ((app.getLaststartdownLoad()/1024)*1.0d);
						tm=(long) ((app.getLaststartupLoad()/1024)*1.0d);
						dataHelper.updateApp(app);
						if((rm+tm)>1) dataset.add(app);
					}
				}
				return true;
			}
			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				if (result) {
					Collections.sort(dataset, new CustomComparator());
					lsv.setAdapter(new AppAdapter(getActivity(), R.layout.layout_item_app_monitor,(ArrayList<App>) dataset));
				}
			}
		};
		task.execute();
	}
	public class CustomComparator implements Comparator<App> {
	    @Override
	    public int compare(App o1, App o2) {
	        return (int) ((int)((o2.getLaststartupLoad()+o2.getLaststartdownLoad())-o1.getLaststartdownLoad()+o1.getLaststartupLoad()));
	    }
	}
}
