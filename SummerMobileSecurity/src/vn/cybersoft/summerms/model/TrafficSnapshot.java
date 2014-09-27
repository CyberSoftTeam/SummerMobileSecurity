/***
	Copyright (c) 2008-2011 CommonsWare, LLC
	Licensed under the Apache License, Version 2.0 (the "License"); you may not
	use this file except in compliance with the License. You may obtain a copy
	of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
	by applicable law or agreed to in writing, software distributed under the
	License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
	OF ANY KIND, either express or implied. See the License for the specific
	language governing permissions and limitations under the License.

	From _Tuning Android Applications_
		http://commonsware.com/AndTuning
 */

package vn.cybersoft.summerms.model;
/**
 * @author Phạm Văn Năm
 */
import java.util.HashMap;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;

public class TrafficSnapshot {
	private TrafficRecord device=null;
	@SuppressLint("UseSparseArrays")
	private HashMap<Integer, TrafficRecord> apps=
			new HashMap<Integer, TrafficRecord>();

	@SuppressLint("UseSparseArrays")
	public TrafficSnapshot(Context ctxt) {
		device=new TrafficRecord();
		HashMap<Integer, String> appNames=new HashMap<Integer, String>();
		for (ApplicationInfo app :
			ctxt.getPackageManager().getInstalledApplications(0)) {
			//String name = (String) ctxt.getPackageManager().getApplicationLabel(app);
			if((app.flags & ApplicationInfo.FLAG_SYSTEM)!=0)
				appNames.put(app.uid, app.packageName);
		}

		for (Integer uid : appNames.keySet()) {
			apps.put(uid, new TrafficRecord(uid, appNames.get(uid)));

		}
	}

	public HashMap<Integer, TrafficRecord> getApps() {
		return apps;
	}

	public void setApps(HashMap<Integer, TrafficRecord> apps) {
		this.apps = apps;
	}

	public TrafficRecord getDevice() {
		return device;
	}

	public void setDevice(TrafficRecord device) {
		this.device = device;
	}
	
}