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

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * @author vietdung
 *
 */
public class MenuAdapter extends ArrayAdapter<String>{
	private Context context;
	private LayoutInflater mInflater;
	private int mViewResId;
	private int textViewResourceId;
	private List<String> menuItems;
	
	/**
	 * @param context
	 * @param resource
	 * @param textViewResourceId
	 * @param objects
	 */
	public MenuAdapter(Context context, int resource, int textViewResourceId,
			List<String> items) {
		super(context, resource, textViewResourceId, items);
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
		this.textViewResourceId = textViewResourceId;
		this.mViewResId = resource;
		this.menuItems = items;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = mInflater.inflate(mViewResId, null);
		String item = menuItems.get(position);
		TextView menuText = (TextView) convertView.findViewById(textViewResourceId);
		menuText.setText(item);
		
		int drawId = android.R.drawable.btn_star;
		
		menuText.setCompoundDrawablesWithIntrinsicBounds(
				context.getResources().getDrawable(drawId), null, null, null);
		
		return convertView;
	}
}
