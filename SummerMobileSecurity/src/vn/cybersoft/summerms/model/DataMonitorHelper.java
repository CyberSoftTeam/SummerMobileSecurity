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

import java.util.ArrayList;
import java.util.HashMap;

import vn.cybersoft.summerms.Constants;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataMonitorHelper extends SQLiteOpenHelper{
	private static final String NAME="datamonitor";
	private static final int VERSION=1;

	private static final String TABLE_DAY="day";
	private static final String TABLE_APP="app";
	private static final String ID="id_";

	public DataMonitorHelper(Context context) {
		super(context, NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_DAY + " ("
				+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ Constants.DATE + " TEXT ," 
				+ Constants.VALUE_START_UPLOAD + " BIGINT," 
				+ Constants.VALUE_START_DOWNLOAD + " BIGINT);");

		db.execSQL("CREATE TABLE " + TABLE_APP + " ("
				+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ Constants.APP_ID + " INTEGER ," 
				+ Constants.APP_NAME + " TEXT ," 
				+ Constants.VALUE_START_UPLOAD_LAST + " BIGINT," 
				+ Constants.VALUE_START_DOWNLOAD_LAST + " BIGINT,"
				+ Constants.VALUE_START_UPLOAD + " BIGINT," 
				+ Constants.VALUE_START_DOWNLOAD + " BIGINT);");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out.println("Upgrading db from " + oldVersion + " to new version: " + newVersion);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAY);
		onCreate(db);

	}

	public ArrayList<DateTraffic> getDAY(){
		ArrayList<DateTraffic> arr=new ArrayList<DateTraffic>();
		SQLiteDatabase sd=getWritableDatabase();
		Cursor c=sd.rawQuery("select * from "+TABLE_DAY, null);

		while (c.moveToNext()) {
			DateTraffic result = new DateTraffic();
			result.setDate(c.getString((c.getColumnIndex(Constants.DATE))));
			result.setStartdownLoad(c.getInt(c.getColumnIndex(Constants.VALUE_START_DOWNLOAD)));
			result.setStartupLoad(c.getInt(c.getColumnIndex(Constants.VALUE_START_UPLOAD)));
			arr.add(result);
		}
		return arr;
	}
	public DateTraffic getDAY(String time){
		DateTraffic result=new DateTraffic();
		SQLiteDatabase sd = getWritableDatabase();
		Cursor c = sd.rawQuery("select * from "+TABLE_DAY
				+" where "+Constants.DATE+"="+time, null);
		c.moveToNext();
		Log.d("getDay",c.getCount()+"-----"+time);//long > int
		if(c.getCount() !=0){
			Log.d("getDay",c.getString((c.getColumnIndex(Constants.DATE))) );
			result.setDate(c.getString((c.getColumnIndex(Constants.DATE))));
			result.setStartdownLoad(c.getInt(c.getColumnIndex(Constants.VALUE_START_DOWNLOAD)));
			Log.e("Data", c.getInt(c.getColumnIndex(Constants.VALUE_START_DOWNLOAD))+"");
			Log.e("Data", c.getLong(c.getColumnIndex(Constants.VALUE_START_DOWNLOAD))+"");
			result.setStartupLoad(c.getInt(c.getColumnIndex(Constants.VALUE_START_UPLOAD)));
		}
		return result;
	}

	public int addDAY(DateTraffic dateTraffic){

		ContentValues values = new ContentValues();
		values.put(Constants.DATE, dateTraffic.getDate());
		values.put(Constants.VALUE_START_DOWNLOAD, dateTraffic.getStartdownLoad());
		values.put(Constants.VALUE_START_UPLOAD, dateTraffic.getStartupLoad());
		Log.d("DataHelper",dateTraffic.getDate());
		return (int) getWritableDatabase().insert(TABLE_DAY,Constants.DATE, values);

	}
	public void updateDAY(DateTraffic dateTraffic){

		ContentValues values = new ContentValues();
		values.put(Constants.DATE, dateTraffic.getDate());
		values.put(Constants.VALUE_START_DOWNLOAD, dateTraffic.getStartdownLoad());
		values.put(Constants.VALUE_START_UPLOAD, dateTraffic.getStartupLoad());
		Log.d("DataHelper",dateTraffic.getDate());
		getWritableDatabase().update(TABLE_DAY, values, Constants.DATE+"="+dateTraffic.getDate(), null);

	}
	
	public HashMap<String, App> getallAPP(){
		HashMap<String, App> apps=new HashMap<String, App>();
		SQLiteDatabase sd=getWritableDatabase();
		Cursor c=sd.rawQuery("select * from "+TABLE_APP, null);

		while (c.moveToNext()) {
			App result = new App();
			result.setAppid(c.getInt(c.getColumnIndex(Constants.APP_ID)));
			result.setAppName(c.getString(c.getColumnIndex(Constants.APP_NAME)));
			result.setStartdownLoad(c.getInt(c.getColumnIndex(Constants.VALUE_START_DOWNLOAD)));
			result.setStartupLoad(c.getInt(c.getColumnIndex(Constants.VALUE_START_UPLOAD)));
			result.setLaststartdownLoad(c.getInt(c.getColumnIndex(Constants.VALUE_START_DOWNLOAD_LAST)));
			result.setLaststartupLoad(c.getInt(c.getColumnIndex(Constants.VALUE_START_UPLOAD_LAST)));
			apps.put(result.getAppName(), result);

		}
		return apps;
	}
	public App getaAppbyID(int id){
		App result=new App();
		SQLiteDatabase sd = getWritableDatabase();
		Cursor c = sd.rawQuery("select * from "+TABLE_APP
				+" where "+Constants.DATE+"="+id, null);
		c.moveToNext();
		if(c.getCount() !=0){
			result.setAppid(c.getInt(c.getColumnIndex(Constants.APP_ID)));
			result.setAppName(c.getString(c.getColumnIndex(Constants.APP_NAME)));
			result.setStartdownLoad(c.getInt(c.getColumnIndex(Constants.VALUE_START_DOWNLOAD)));
			result.setStartupLoad(c.getInt(c.getColumnIndex(Constants.VALUE_START_UPLOAD)));
			result.setLaststartdownLoad(c.getInt(c.getColumnIndex(Constants.VALUE_START_DOWNLOAD_LAST)));
			result.setLaststartupLoad(c.getInt(c.getColumnIndex(Constants.VALUE_START_UPLOAD_LAST)));
		}
		return result;
	}
	public int addApp(App app){
		ContentValues values = new ContentValues();
		values.put(Constants.APP_ID, app.getAppid());
		values.put(Constants.APP_NAME, app.getAppName());
		values.put(Constants.VALUE_START_DOWNLOAD, app.getStartdownLoad());
		values.put(Constants.VALUE_START_UPLOAD, app.getStartupLoad());
		values.put(Constants.VALUE_START_DOWNLOAD_LAST, app.getLaststartdownLoad());
		values.put(Constants.VALUE_START_UPLOAD_LAST, app.getLaststartupLoad());
		Log.d("DataHelper",app.getAppName()+"" );
		return (int) getWritableDatabase().insert(TABLE_APP,ID, values);
	}
	/*public App getAPP(int id){
		App result=new App();
		SQLiteDatabase sd = getWritableDatabase();
		Cursor c = sd.rawQuery("select * from "+TABLE_APP
				+" where "+Constants.APP_ID+"="+id, null);
		c.moveToNext();
		if(c.getCount() !=0){
			result.setAppid(c.getInt(c.getColumnIndex(Constants.APP_ID)));
			result.setAppName(c.getString(c.getColumnIndex(Constants.APP_NAME)));
			result.setStartdownLoad(c.getInt(c.getColumnIndex(Constants.VALUE_START_DOWNLOAD)));
			result.setStartupLoad(c.getInt(c.getColumnIndex(Constants.VALUE_START_UPLOAD)));
		}
		return result;
	}*/
	public void updateApp(App app){
		SQLiteDatabase sd = getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(Constants.APP_ID, app.getAppid());
		values.put(Constants.APP_NAME, app.getAppName());
		values.put(Constants.VALUE_START_DOWNLOAD, app.getStartdownLoad());
		values.put(Constants.VALUE_START_UPLOAD, app.getStartupLoad());
		values.put(Constants.VALUE_START_DOWNLOAD_LAST, app.getLaststartdownLoad());
		values.put(Constants.VALUE_START_UPLOAD_LAST, app.getLaststartupLoad());
		Log.d("DataHelper--update",app.getAppName()+"" );
		sd.update(TABLE_APP, values, Constants.APP_ID+"="+app.getAppid(), null);
	} 

	public boolean isAppContain(int id){
		SQLiteDatabase sd = getWritableDatabase();
		Cursor c = sd.rawQuery("select * from "+TABLE_APP
				+" where "+Constants.APP_ID+" = "+id, null);
		if(c.getCount() !=0){
			return true;
		}
		return false;
	}
	public void deteleAllTable(){
		SQLiteDatabase db=getWritableDatabase();
		db.delete(TABLE_DAY, null, null);
	}
}
