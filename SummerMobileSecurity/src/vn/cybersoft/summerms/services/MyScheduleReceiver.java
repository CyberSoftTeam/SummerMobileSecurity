package vn.cybersoft.summerms.services;
import java.util.Calendar;

import java.util.HashSet;

import vn.cybersoft.summerms.database.DataMonitorHelper;
import vn.cybersoft.summerms.model.App;
import vn.cybersoft.summerms.model.DateTraffic;
import vn.cybersoft.summerms.model.TrafficRecord;
import vn.cybersoft.summerms.model.TrafficSnapshot;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;

public class MyScheduleReceiver extends BroadcastReceiver {
	public static final String TAG="MyScheduleReceiver";
	private TrafficSnapshot latest=null;
	private DataMonitorHelper dataHelper;
	// restart service every 30 seconds
	private AlarmManager alarmMgr;
	private PendingIntent alarmIntent;
	private Context mContext;
	@Override
	public void onReceive(Context context, Intent intent) {
		mContext=context;
		dataHelper=new DataMonitorHelper(mContext);
		Log.d(TAG, "Start Traffic Day");
		getData();
	}
	private void getData() {
		Calendar calendar = Calendar.getInstance();
		int year=calendar.get(Calendar.YEAR);
		calendar.set(year, Calendar.getInstance().get(Calendar.MONTH), 2);
		int dayNumber=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		int month=Calendar.getInstance().get(Calendar.MONTH)+1;
		int day=Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		
		latest=new TrafficSnapshot(mContext);
		DateTraffic dateTraffic=new DateTraffic();
		
		dateTraffic=dataHelper.getDAY((day-1)+"."+month);
		if((day>1) && (dateTraffic.getDate() != null)){
			long dataR=latest.getDevice().getRx()-dateTraffic.getStartdownLoad();
			long dataT=latest.getDevice().getTx()-dateTraffic.getStartupLoad();
			if(dataR>-1 && dataT>-1)
				dataHelper.updateDAY(new DateTraffic(day+"."+month,dataT,dataR));
			else{
				dataHelper.updateDAY(new DateTraffic(day+"."+month,latest.getDevice().getTx(),latest.getDevice().getRx()));
			}
			Log.d(TAG, "Update new-"+dataR +"-"+dataT+"-"+day);
		}else{
			Log.d(TAG, "day >1");
			putSharedPreferences(0, day,latest.getDevice().getTx(),latest.getDevice().getRx());
			dataHelper.deteleAllTable();
			dataHelper=new DataMonitorHelper(mContext);
			
			for (int i = 1; i <=dayNumber; i++) {
				if(dataHelper.addDAY(new DateTraffic(i+"."+month,0,0))!=-1){
					Log.d(TAG, "Add Day1-"+latest.getDevice().getRx() +"-"+latest.getDevice().getTx());
				}
			}
			dataHelper.updateDAY(new DateTraffic(day+"."+month,latest.getDevice().getTx(),latest.getDevice().getRx()));
			
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
	public void setAlarm(Context context) {
		alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, MyScheduleReceiver.class);
		alarmIntent = PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		// Set the alarm's trigger time to 00:30 a.m.
		calendar.set(Calendar.HOUR_OF_DAY, 1);
		calendar.set(Calendar.MINUTE, 01);
		alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,  
				calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, alarmIntent);

		// Enable {@code SampleBootReceiver} to automatically restart the alarm when the
		// device is rebooted.
		ComponentName receiver = new ComponentName(context, MyBootReceiver.class);
		PackageManager pm = context.getPackageManager();

		pm.setComponentEnabledSetting(receiver,
				PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
				PackageManager.DONT_KILL_APP);           
	}
	public void putSharedPreferences(long plan,int day,long startUP,long startDow){

		SharedPreferences pre=mContext.getSharedPreferences("my_data", Context.MODE_PRIVATE);
		SharedPreferences.Editor edit=pre.edit();
		if(plan != 0)	
			edit.putLong("plan",plan);
		else {
			Log.d(TAG,"Add Start--"+day+"-"+startUP);
			edit.putInt("daystart",day);
			edit.putLong("datastartup",startUP);
			edit.putLong("datastartdow",startDow);
			
		}
		edit.commit();

	}
} 