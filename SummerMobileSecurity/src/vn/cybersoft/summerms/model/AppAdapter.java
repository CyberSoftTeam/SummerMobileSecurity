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
package vn.cybersoft.summerms.model;
/**
 * @author Phạm Văn Năm
 */
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import vn.cybersoft.summerms.R;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AppAdapter extends ArrayAdapter<App>{
	private Context mContext;
	private int id;
	private long maxData;
	private ArrayList<App> apps=new ArrayList<App>();
	
	public AppAdapter(Context context, int resource, ArrayList<App> objects) {
		super(context, resource, objects);
		mContext=context;
		apps=(ArrayList<App>) objects;
		id=resource;
		if(apps!=null){
			maxData=apps.get(0).getLaststartdownLoad()+apps.get(0).getLaststartupLoad();
		}
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(id, null);
		
		TextView tvName=(TextView) convertView.findViewById(R.id.tv_app_name);
		TextView tvTotal=(TextView) convertView.findViewById(R.id.tvTotal);
		TextView tvUpLoad=(TextView) convertView.findViewById(R.id.tv_upload);
		TextView tvDownLoad=(TextView) convertView.findViewById(R.id.tv_download);
		ImageView img=(ImageView) convertView.findViewById(R.id.image_app);
		ProgressBar progressBar=(ProgressBar) convertView.findViewById(R.id.progressBar);
		

		String total;
		String up;
		String dow;
		NumberFormat formatter;
		long a;
		formatter = new DecimalFormat("#.#");
		if((apps.get(position).getLaststartdownLoad()/(1024*1024))!=0){
			a=(apps.get(position).getLaststartdownLoad()/(1024*1024));
			dow=formatter.format(a)+"MB";
		}else{
			a=apps.get(position).getLaststartdownLoad()/1024;
			dow=formatter.format(a)+"KB";
		}
		if((apps.get(position).getLaststartupLoad()/(1024*1024))!=0){
			a= apps.get(position).getLaststartupLoad()/(1024*1024);
			up=formatter.format(a)+"MB";
		}else{
			a=apps.get(position).getLaststartupLoad()/1024;
			up=formatter.format(a)+"KB";
		}
		
		long x=apps.get(position).getLaststartdownLoad()+apps.get(position).getLaststartupLoad();
		progressBar.setMax((int)maxData);
		progressBar.setProgress((int) x);
		if((x/(1024*1024))!=0){
			x=x/(1024*1024);
			total=formatter.format(x)+"MB";
		}else{
			x=(x/1024);
			total=formatter.format(x)+"KB";
		}
		ApplicationInfo applicationInfo;
		String name="";
		Drawable icon=null;
		try {
			Log.d("Adapter", apps.get(position).getAppName());
			applicationInfo=mContext.getPackageManager().getApplicationInfo(apps.get(position).getAppName(),0);
			name=(String) mContext.getPackageManager().getApplicationLabel(applicationInfo);
			icon=mContext.getPackageManager().getApplicationIcon(applicationInfo);
		} catch (NameNotFoundException e) {
			Log.d("Adapter", e.getMessage());
		}
		
		tvName.setText(name);
		tvTotal.setText(total);
		tvUpLoad.setText("Upload: "+up);
		tvDownLoad.setText("Download: "+dow);
		img.setImageDrawable(icon);
		
		return convertView;
	}
}
