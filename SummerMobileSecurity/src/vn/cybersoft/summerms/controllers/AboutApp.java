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

import vn.cybersoft.summerms.R;
import vn.cybersoft.summerms.utils.Common;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class AboutApp extends Fragment {
	private WebView webview;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.about, container, false);

		// Load content
		webview = (WebView) view.findViewById(R.id.webview);
		webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		webview.setBackgroundColor(0x00000000);

		// Get html content
		String helpFileName="about_us.html";
		/*if (Preferences.getInstance().getLanguage().equals("en"))
			helpFileName="about_us.html";
		else 
			helpFileName="about_us_vi.html";*/
		String html = Common.getFileContent(inflater.getContext(),
				helpFileName);

		webview.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);

		return view;
	}

	@Override
	public void onDestroy() {
		if (webview != null) {
			webview.destroy();
		}

		super.onDestroy();
	}
}