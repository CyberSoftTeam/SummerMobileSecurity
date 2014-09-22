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
package vn.cybersoft.summerms.database;

import java.io.File;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;
import android.util.Log;

/**
 * Log all user interface activity into a SQLite database. Logging is disabled
 * by default.
 * 
 * The logging database will be "/sdcard/mobilesecurity/contact/data.db"
 * 
 */
public final class DatabaseContact {
	/**
	 * The maximum size of the scroll action buffer. After it reaches this size,
	 * it will be flushed.
	 */
	public static final String MOBILE_ROOT = Environment.getExternalStorageDirectory()
			+ File.separator + "MobileSecurity";
	public static final String CONTACT_PATH = MOBILE_ROOT + File.separator + "Contact";
	private static final String DATABASE_TABLE = "contact";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "data.db";

	// Database columns
	private static final String ID = "_id";
	private static final String CONTACT_ID = "contact_id";
	private static final String NAME = "name";
	private static final String NUMBER = "number";
	private static final String ISBLOCKSMS = "smsblock";
	private static final String ISBLOCKCALL = "callblock";

	private static final String DATABASE_CREATE = "create table "
			+ DATABASE_TABLE + " (" + ID
			+ " integer primary key autoincrement, " + CONTACT_ID
			+ " integer not null, " + NAME + " text, " + NUMBER + " text, " + ISBLOCKSMS
			+ " text, " + ISBLOCKCALL + " text);"; 

	private DatabaseHelper mDbHelper = null;
	private SQLiteDatabase mDb = null;
	private boolean mIsOpen = false;
	// We buffer scroll actions to make sure there aren't too many pauses
	// during scrolling. This list is flushed every time any other type of
	// action is logged.

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper() {
			super(CONTACT_PATH, DATABASE_NAME, null, DATABASE_VERSION);
			new File(CONTACT_PATH).mkdirs();
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
	}



	public DatabaseContact() {
		open();
	}

	public boolean isOpen() {
		return mIsOpen;
	}

	public void open() throws SQLException {
		if (mIsOpen)
			return;
		try {
			mDbHelper = new DatabaseHelper();
			mDb = mDbHelper.getWritableDatabase();
			mIsOpen = true;
		} catch (SQLiteException e) {
			System.err.println("Error: " + e.getMessage());
			mIsOpen = false;
		}
	}


	/*// DO NOT CALL THIS OUTSIDE OF synchronized(mScrollActions) !!!!
	// DO NOT CALL THIS OUTSIDE OF synchronized(mScrollActions) !!!!
	// DO NOT CALL THIS OUTSIDE OF synchronized(mScrollActions) !!!!
	// DO NOT CALL THIS OUTSIDE OF synchronized(mScrollActions) !!!!
	private String getXPath(FormIndex index) {
		if (index == cachedXPathIndex)
			return cachedXPathValue;

		cachedXPathIndex = index;
		cachedXPathValue = Collect.getInstance().getFormController()
				.getXPath(index);
		return cachedXPathValue;
	}
	 */
	public void insertContact(long contactid, String name, String number, String blockSMS, String blockCall) {
		if (!isOpen())
			return;
		String num = number.substring(1,number.length());
		Log.d("number", "number is " + num);
		ContentValues cv = new ContentValues();
		cv.put(CONTACT_ID, contactid);
		cv.put(NAME, name);
		cv.put(NUMBER, num);
		cv.put(ISBLOCKSMS, blockSMS);
		cv.put(ISBLOCKCALL, blockCall);
		insertContentValues(cv);
	}

	public void upateStatusBlockSMS(long contactid, String blockSMS){
		if(!isOpen())
			return;
		ContentValues cv = new ContentValues();
		cv.put(CONTACT_ID, contactid);
		cv.put(ISBLOCKSMS, blockSMS );
		if(cv!=null)
			mDb.update(DATABASE_TABLE, cv ,CONTACT_ID + " = " + contactid, null);
	}
	
	public void upateStatusBlockCall(long contactid, String blockCall){
		if(!isOpen())
			return;
		ContentValues cv = new ContentValues();
		cv.put(CONTACT_ID, contactid);
		cv.put(ISBLOCKCALL, blockCall );
		if(cv!=null)
			mDb.update(DATABASE_TABLE, cv ,CONTACT_ID + " = " + contactid, null);
	}

	public String getStatusSMSofContactByNumber(String number){
		String num = number.substring(1,number.length());
		Cursor cursor =  mDb.query(DATABASE_TABLE, new String []{ISBLOCKSMS}, NUMBER +" = " + num, null, null, null, null);
		if (cursor.moveToNext()) {
			return cursor.getString(cursor.getColumnIndex(ISBLOCKSMS));
		}
		return "";
	}
	
	public String getStatusCallofContactByNumber(String number){
		String num = number.substring(1,number.length());
		Cursor cursor =  mDb.query(DATABASE_TABLE, new String []{ISBLOCKCALL}, NUMBER +" = " + num, null, null, null, null);
		if (cursor.moveToNext()) {
			return cursor.getString(cursor.getColumnIndex(ISBLOCKCALL));
		}
		return "";
	}
	private void insertContentValues(ContentValues cv) {
		try{
			if (cv != null) {
				mDb.insert(DATABASE_TABLE, null, cv);
			}
		}catch (SQLiteConstraintException e) {
			System.err.println("Error: " + e.getMessage());
		}
	}

}
