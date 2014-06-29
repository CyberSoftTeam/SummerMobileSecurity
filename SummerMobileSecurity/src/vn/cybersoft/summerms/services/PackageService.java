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
package vn.cybersoft.summerms.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import vn.cybersoft.summerms.Constants;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

/**
 * @author vietdung
 *
 */
public class PackageService {
	private static PackageService packageService;
	
	private PackageService() {
		// nothing to do here
	}
	
	public static PackageService getInstance() {
		if (packageService==null) {
			packageService = new PackageService();
		}
		return packageService;
	}
	
	/**
	 * Get list applications
	 * 
	 * @param pm
	 * @return list application info
	 */
	public List<ResolveInfo> getApps(PackageManager pm) {
		// Create filter to search applications
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);

		// Get list applications
		List<ResolveInfo> listApps = pm.queryIntentActivities(intent,
				PackageManager.PERMISSION_GRANTED);

		Map<String, ResolveInfo> map = new HashMap<String, ResolveInfo>();
		for (ResolveInfo info : listApps) {
			map.put(info.activityInfo.packageName, info);
		}
		
		// Remove current app
		map.remove(Constants.CURRENT_PACKAGE);

		List<ResolveInfo> result = new ArrayList<ResolveInfo>();
		Iterator<Entry<String, ResolveInfo>> iterator = map.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, ResolveInfo> mapEntry = (Entry<String, ResolveInfo>) iterator
					.next();
			Log.d(Constants.TAG, mapEntry.getValue().activityInfo.name);
			result.add(mapEntry.getValue());
		}

		// Sort by application name
		Collections.sort(result, new ResolveInfo.DisplayNameComparator(pm));

		return result;
	}
	
	/**
	 * @param pm
	 * @param info
	 * @return string array of permission of an application
	 * @throws NameNotFoundException
	 */
	public String[] getAppPermissions(PackageManager pm, ResolveInfo info) throws NameNotFoundException {
		PackageInfo packageInfo = null;
		packageInfo = pm.getPackageInfo(info.activityInfo.packageName, PackageManager.GET_PERMISSIONS);
		return packageInfo.requestedPermissions;
	}
}
