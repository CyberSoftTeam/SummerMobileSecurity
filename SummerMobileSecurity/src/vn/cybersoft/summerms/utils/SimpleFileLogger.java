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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.util.Calendar;

import vn.cybersoft.summerms.Constants;


/**
 * @author vietdung
 *
 */
public class SimpleFileLogger {
	public static final String DEFAULT_FILE_PATH = Constants.APPLICATION_PATH+File.separator+"log.txt";
	private String filePath;
	private static SimpleFileLogger logger = null;

	private SimpleFileLogger() {
		if(filePath==null || filePath.equals("")){
			filePath = DEFAULT_FILE_PATH;
		}
	}

	public static SimpleFileLogger getInstance() {
		if (logger == null) {
			logger = new SimpleFileLogger();
		}
		return logger;
	}

	public boolean write(String text) {
		try {
			Timestamp timestamp = new Timestamp(Calendar.getInstance()
					.getTimeInMillis());
			FileWriter fstream = new FileWriter(filePath, true);
			BufferedWriter fbw = new BufferedWriter(fstream);
			fbw.newLine();
			fbw.write(timestamp+" --> "+text);
			fbw.newLine();
			fbw.close();

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
