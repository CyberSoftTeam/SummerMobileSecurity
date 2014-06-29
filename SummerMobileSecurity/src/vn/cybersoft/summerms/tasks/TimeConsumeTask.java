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
package vn.cybersoft.summerms.tasks;

import vn.cybersoft.summerms.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

/**
 * An asyncTask to do long time background works
 * @author vietdung
 * 
 */
public class TimeConsumeTask extends AsyncTask<Void, Void, Boolean> {
	protected Activity activity;
	protected ProgressDialog progressDialog;

	/**
	 * Constructor
	 * 
	 * @param activity
	 *            Activity
	 */
	public TimeConsumeTask(Activity activity) {
		this.activity = activity;
	}


	@Override
	protected Boolean doInBackground(Void... params) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// Show progress dialog
				progressDialog = ProgressDialog.show(activity, "",
						activity.getString(R.string.processing), true);
				progressDialog.setCancelable(false);
			}
		});
		
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		progressDialog.dismiss();
	}
}
