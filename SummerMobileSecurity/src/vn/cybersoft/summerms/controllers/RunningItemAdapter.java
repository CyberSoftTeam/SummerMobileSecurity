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

import vn.cybersoft.summerms.R;
import vn.cybersoft.summerms.controllers.RunningListFragment.RunningItem;
import vn.cybersoft.summerms.services.PackageService;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author vietdung
 *
 */
public class RunningItemAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private PackageManager pm;
	private Context context;
	private List<RunningItem> items;

	public RunningItemAdapter(Context context, PackageManager pm) {
		this.inflater = LayoutInflater.from(context);
		this.context = context;
		this.pm = pm;
	}

	public void setData(List<RunningItem> items){
		this.items = items;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public RunningItem getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final RunningItem item = getItem(position);
		final TextView label;
		final TextView importance;
		final ImageView icon;
		final Button sysInfo;
		convertView = inflater.inflate(R.layout.running_list_item, parent, false);
		
		label = (TextView) convertView.findViewById(R.id.appLabel);
		importance = (TextView) convertView.findViewById(R.id.importance);
		icon = (ImageView) convertView.findViewById(R.id.appIcon);
		sysInfo = (Button) convertView.findViewById(R.id.btn_sys_info);
		
		label.setText(item.label);
		importance.setText(item.importance);
		icon.setImageDrawable(item.icon);
		sysInfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (item.packageName != null) {
					PackageService.getInstance()
					.showInstalledAppDetails(context, item.packageName);
				}
			}
		});
		
		return convertView;
	}

}


