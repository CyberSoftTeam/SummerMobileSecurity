package vn.cybersoft.summerms.model;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.TextView;

public class AppAdapter extends ArrayAdapter<App>{
	private Context mContext;
	private int id;
	private ArrayList<App> apps=new ArrayList<App>();
	
	public AppAdapter(Context context, int resource, ArrayList<App> objects) {
		super(context, resource, objects);
		mContext=context;
		apps=(ArrayList<App>) objects;
		id=resource;
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

		String total;
		String up;
		String dow;
		if((apps.get(position).getLaststartdownLoad()/(1024*1024))!=0){
			dow= (double)(apps.get(position).getLaststartdownLoad()/(1024*1024))*1.010d+"00000";
			dow=dow.substring(0, 5)+"MB";
		}else{
			dow=(double) (apps.get(position).getLaststartdownLoad()/1024)*1.010d+"00000";
			dow=dow.substring(0, 5)+"KB";
		}
		if((apps.get(position).getLaststartupLoad()/(1024*1024))!=0){
			up= (double)(apps.get(position).getLaststartupLoad()/(1024*1024))*1.010d+"00000";
			up=up.substring(0, 5)+"MB";//10.25
		}else{
			up=(double) (apps.get(position).getLaststartupLoad()/1024)*1.010d+"00000";
			up=up.substring(0, 5)+"KB";
		}
		long x=apps.get(position).getLaststartdownLoad()+apps.get(position).getLaststartupLoad();
		if((x/(1024*1024))!=0){
			total= (double)(x/(1024*1024))*1.010d+"00000";
			total=total.substring(0, 5)+"MB";
		}else{
			total=(double) (x/1024)*1.010d+"00000";
			total=total.substring(0, 5)+"KB";
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
