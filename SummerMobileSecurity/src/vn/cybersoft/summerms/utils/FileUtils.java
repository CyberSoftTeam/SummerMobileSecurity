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
package vn.cybersoft.summerms.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.util.Log;

public class FileUtils {
	private static final String TAG="FileUtils";

	public static void copyFile(String src, String dst) throws IOException {
		InputStream inputStream = new FileInputStream(src);

		OutputStream outputStream = new FileOutputStream(dst);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = inputStream.read(buffer)) > 0) {
			outputStream.write(buffer, 0, length);
		}

		outputStream.flush();
		outputStream.close();
		inputStream.close();
	}

	public static boolean deleteFile(String filePath){
		File file = new File(filePath);
		//check file already exists
		if(file.exists()){
			if (file.delete()) {
				return true;
			}
			return false;
		}
		return false;
	}

	//save file
	public static boolean saveDataToFile(String filePath, String data, Context context){
		//Context context = Preferences.getInstance().getApplicationContext();
		try {
			FileWriter out = new FileWriter(new File(filePath),true);
			out.write(data);
			out.close();
		} catch (IOException e) {
			Log.i(TAG, ""+e);

		}
		return true;
	}

	public static boolean deleteDirectory(File root) {
		if (root.isDirectory()) {
			String[] children = root.list();
			for (int i=0; i<children.length; i++) {
				boolean success = deleteDirectory(new File(root, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return root.delete();
	}

}
