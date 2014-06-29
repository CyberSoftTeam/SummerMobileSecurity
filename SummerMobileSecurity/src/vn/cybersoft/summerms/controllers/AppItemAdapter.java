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

import java.util.List;

import vn.cybersoft.summerms.Constants;
import vn.cybersoft.summerms.Preferences;
import vn.cybersoft.summerms.R;
import vn.cybersoft.summerms.controllers.AppListFragment.AppItem;
import vn.cybersoft.summerms.widgets.AnimatedExpandableListView.AnimatedExpandableListAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * @author vietdung
 *
 */
public class AppItemAdapter extends AnimatedExpandableListAdapter {
	private LayoutInflater inflater;
	private PackageManager pm;
	private List<AppItem> groupItems;

	public AppItemAdapter(Context context, PackageManager pm) {
		this.inflater = LayoutInflater.from(context);
		this.pm = pm;
	}

	public void setData(List<AppItem> groupItems){
		this.groupItems = groupItems;
	}

	@Override
	public String getChild(int groupPosition, int childPosition) {
		if (groupItems.get(groupPosition).permissions!=null) {
			return groupItems.get(groupPosition).permissions[childPosition];
		}
		return "Empty";

	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public AppItem getGroup(int groupPosition) {
		return groupItems.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groupItems.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		AppItem item = getGroup(groupPosition);
		final TextView packageName;
		final TextView version;
		final ImageView icon;
		final ToggleButton locker;
		convertView = inflater.inflate(R.layout.app_list_item, parent, false);
		packageName = (TextView) convertView.findViewById(R.id.appLabel);
		version = (TextView) convertView.findViewById(R.id.appVersion);
		icon = (ImageView) convertView.findViewById(R.id.appIcon);
		locker = (ToggleButton) convertView.findViewById(R.id.btn_locker);


		try {
			final ResolveInfo info = item.info;
			CharSequence label = info.loadLabel(pm);
			String versionName = pm.getPackageInfo(info.activityInfo.packageName,
					PackageManager.GET_PERMISSIONS).versionName;
			Drawable drawable = info.loadIcon(pm);
			packageName.setText(label);
			version.setText("Version "+versionName);
			icon.setImageDrawable(drawable);

			int state = Preferences.getInstance().getAppState(info.activityInfo.packageName);
			locker.setChecked(state!=Constants.STATE_UNLOCKED);
			locker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					int newState = isChecked?Constants.STATE_LOCKED: Constants.STATE_UNLOCKED;
					Preferences.getInstance().saveAppState(info.activityInfo.packageName, newState);
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public View getRealChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildHolder holder;
		String item = getChild(groupPosition, childPosition);
		if (convertView == null) {
			holder = new ChildHolder();
			convertView = inflater.inflate(R.layout.permission_item, parent, false);
			holder.permission = (TextView) convertView.findViewById(R.id.permission_item);
			convertView.setTag(holder);
		} else {
			holder = (ChildHolder) convertView.getTag();
		}

		holder.permission.setText(item);

		return convertView;
	}

	@Override
	public int getRealChildrenCount(int groupPosition) {
		if (groupItems.get(groupPosition).permissions!=null) {
			return groupItems.get(groupPosition).permissions.length;
		}
		return 1;
	}

	private class ChildHolder {
		TextView permission;
	}

}


