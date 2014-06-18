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

import java.util.Arrays;

import vn.cybersoft.summerms.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * @author vietdung
 *
 */
public class MainMenuFragment extends ListFragment {
	public static final int APP_MANAGER = 0;
	public static final int BLOCKER = 1;
	public static final int DATA_MONITOR = 2;
	public static final int VIRUS_SCANNER = 3;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.menu_list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		String[] items = getResources().getStringArray(R.array.main_menu);
		
		MenuAdapter menuAdapter = new MenuAdapter(getActivity(), 
				R.layout.menu_item, R.id.menu_item_text, Arrays.asList(items));
		setListAdapter(menuAdapter);
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Fragment newContent = null;
		
		// switch fragment here (newContent)
		switch (position) {
		case APP_MANAGER:
			break;
		case DATA_MONITOR:
			break;
		case BLOCKER:
			break;
		case VIRUS_SCANNER:
			break;
		}
			
	}

	// the meat of switching the above fragment
	private void switchFragment(Fragment fragment, String title) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof MainActivity) {
			MainActivity mainActivity = (MainActivity) getActivity();
			mainActivity.switchContent(fragment);
		} 
	}
	
}