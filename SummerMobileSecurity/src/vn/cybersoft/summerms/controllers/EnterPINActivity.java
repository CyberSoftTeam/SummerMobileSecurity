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

import vn.cybersoft.summerms.Constants;
import vn.cybersoft.summerms.Preferences;
import vn.cybersoft.summerms.R;
import vn.cybersoft.summerms.services.AppLockerService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class EnterPINActivity extends Activity {
	public static final String PACK_KEY = "packName";

	private Button btnOK;
	private EditText editPIN;

	private String packName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_enter_pin);

		packName = getIntent().getExtras().getString(PACK_KEY);

		btnOK = (Button) findViewById(R.id.btn_ok);
		editPIN = (EditText) findViewById(R.id.edit_pin);

		editPIN.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					checkPIN();
				}
				return false;
			}
		});

		btnOK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkPIN();
			}


		});
	}

	private void checkPIN() {
		String PIN = Preferences.getInstance().getPIN();
		String pinIn = editPIN.getText().toString();

		// check PIN for another applications
		if (pinIn.equals(PIN)) {
			Intent runIntent = getPackageManager()
					.getLaunchIntentForPackage(packName);
			Preferences.getInstance().saveAppState(packName,
					Constants.STATE_PINPASSED);
			if (runIntent!=null) {
				startActivity(runIntent);
			}
		} else {
			Intent startMain = new Intent(Intent.ACTION_MAIN);
			startMain.addCategory(Intent.CATEGORY_HOME);
			startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(startMain);
		}
		EnterPINActivity.this.finish();
	}

	@Override
	public void onBackPressed() {
		// nothing to do here
	}

	@Override
	protected void onPause() {
		super.onPause();
		EnterPINActivity.this.finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Intent runIntent = new Intent(this, AppLockerService.class);
		startService(runIntent);
	}
}
