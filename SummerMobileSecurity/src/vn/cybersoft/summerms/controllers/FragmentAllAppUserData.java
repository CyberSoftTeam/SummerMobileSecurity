package vn.cybersoft.summerms.controllers;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import vn.cybersoft.summerms.R;
import vn.cybersoft.summerms.model.App;
import vn.cybersoft.summerms.model.AppAdapter;
import vn.cybersoft.summerms.model.DataMonitorHelper;
import vn.cybersoft.summerms.model.TrafficRecord;
import vn.cybersoft.summerms.model.TrafficSnapshot;
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
		current=new TrafficSnapshot(getActivity());
		HashSet<Integer> intersection=new HashSet<Integer>(current.getApps().keySet());
		HashMap<String, App> apps=dataHelper.getallAPP();
		ArrayList<App> dataset=new ArrayList<App>();
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
		lsv.setAdapter(new AppAdapter(getActivity(), R.layout.layout_item_app_monitor, dataset));
	}
}
