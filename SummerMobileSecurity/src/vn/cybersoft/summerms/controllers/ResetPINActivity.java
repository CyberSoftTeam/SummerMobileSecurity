/*******************************************************************************
 * Copyright 2014 VietDung Vu, IUH.CyberSoft Team (http://cyberso.wordpress.com/)
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
import vn.cybersoft.summerms.utils.MessageUtil;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class ResetPINActivity extends Activity {
	private EditText oldPIN;
	private EditText newPIN;
	private EditText confirmPIN;
	private Button btnOK;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_reset_pin);

		btnOK = (Button) findViewById(R.id.btn_ok);
		oldPIN = (EditText) findViewById(R.id.old_pin);	
		newPIN = (EditText) findViewById(R.id.edit_pin);	
		confirmPIN = (EditText) findViewById(R.id.confirm_pin);	

		confirmPIN.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					verifyPIN();
				}
				return false;
			}
		});

		btnOK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				verifyPIN();
			}
		});
	}

	private void verifyPIN() {
		String old = oldPIN.getText().toString();
		String nPin = newPIN.getText().toString();
		String confirm = confirmPIN.getText().toString();
		
		// 1st step: old PIN is true ?
		if (old.equalsIgnoreCase(Preferences.getInstance().getPIN())) {
			// 2nd step: two PIN value is match ?
			if (nPin.equalsIgnoreCase(confirm)) {
				Preferences.getInstance().savePIN(nPin);
				Preferences.getInstance()
				.saveAppState(Constants.CURRENT_PACKAGE, Constants.STATE_LOCKED);
				ResetPINActivity.this.finish();
			} else {
				MessageUtil.showToastInfo(ResetPINActivity.this,
						R.string.unmatch_pin);
				newPIN.requestFocus();
			}
		} else {
			MessageUtil.showToastInfo(ResetPINActivity.this,
					R.string.invalid_pin);
			finish();
		}
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		ResetPINActivity.this.finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
