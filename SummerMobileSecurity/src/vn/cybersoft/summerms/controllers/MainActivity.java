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

import vn.cybersoft.summerms.Preferences;
import vn.cybersoft.summerms.R;
import vn.cybersoft.summerms.controllers.ContactsListFragment.OnContactsInteractionListener;
import vn.cybersoft.summerms.services.AppLockerService;
import vn.cybersoft.summerms.services.MyScheduleReceiver;
import vn.cybersoft.summerms.utils.StringUtil;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends MainBaseActivity implements OnContactsInteractionListener {
	private Fragment mContent;
	private MyScheduleReceiver alarm = new MyScheduleReceiver();
	private ContactDetailFragment mContactDetailFragment;

	// If true, this is a larger screen device which fits two panes
	private boolean isTwoPaneLayout;

	// True if this activity instance is a search result view (used on pre-HC devices that load
	// search results in a separate instance of the activity rather than loading results in-line
	// as the query is typed.
	private boolean isSearchResultView = false;

	public MainActivity() {
		super(R.string.app_name);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		alarm.setAlarm(this);
		if (StringUtil.isEmptyOrNull(Preferences.getInstance().getPIN())) {
			Intent initPin = new Intent(this, InitPINActivity.class);
			startActivity(initPin);
		}
		 
		// set the Above View
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
		if (mContent==null) {
			mContent = new AboutApp();
		}

		// set the Above View
		setContentView(R.layout.content_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, mContent)
		.commit();

		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame, new MainMenuFragment())
		.commit();

		// customize the SlidingMenu
		Display display = getWindowManager().getDefaultDisplay();
		// Point size = new Point();
		// display.getSize(size);
		int w = display.getWidth();
		getSlidingMenu().setBehindWidth(w/2);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

		
		if (!AppLockerService.isRunning) {
			Intent intentService = new Intent(this, AppLockerService.class);
			startService(intentService);
		}

	}

	public void switchContent(Fragment fragment) {
		// revert to default sliding menu mode
		getSlidingMenu().setMode(SlidingMenu.LEFT);

		// switch content
		mContent = fragment;
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, fragment)
		.commit();
		getSlidingMenu().showContent();
	}
	
	@Override
	public void onContactSelected(Uri contactUri) {
		Log.d(this.getLocalClassName(), " onContactSelected  " + contactUri.toString());
		String [] s = contactUri.toString().split("/");
		if (s[4].equals("lookup")) {
			mContactDetailFragment = ContactDetailFragment.newInstance(contactUri);
			switchContent(mContactDetailFragment);
		}
		// Otherwise single pane layout, start a new ContactDetailActivity with
		// the contact Uri
		/* Intent intent = new Intent(this, ContactDetailActivity.class);
	            intent.setData(contactUri);
	            startActivity(intent);*/
		
	}

	@Override
	public void onSelectionCleared() {
		//if (Constants.DEBUG) {
		Log.d(this.getLocalClassName(), "on selection cleared");
		//}
		if (isTwoPaneLayout && mContactDetailFragment != null) {
			mContactDetailFragment.setContact(null);
		}
	}

	@Override
	public boolean onSearchRequested() {
		// Don't allow another search if this activity instance is already showing
		// search results. Only used pre-HC.
		return !isSearchResultView && super.onSearchRequested();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.reset_pin:
			Intent resetPin = new Intent(this, ResetPINActivity.class);
			startActivity(resetPin);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
