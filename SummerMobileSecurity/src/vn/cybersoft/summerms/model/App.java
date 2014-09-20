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
package vn.cybersoft.summerms.model;

public class App {
	private String appName;
	private int appid;
	private long startupLoad;
	private long startdownLoad;
	private long laststartupLoad;
	private long laststartdownLoad;
	
	public App(String appName, int appid, long startupLoad, long startdownLoad,
			long laststartupLoad, long laststartdownLoad) {
		super();
		this.appName = appName;
		this.appid = appid;
		this.startupLoad = startupLoad;
		this.startdownLoad = startdownLoad;
		this.laststartupLoad = laststartupLoad;
		this.laststartdownLoad = laststartdownLoad;
	}

	public long getLaststartupLoad() {
		return laststartupLoad;
	}

	public void setLaststartupLoad(long laststartupLoad) {
		this.laststartupLoad = laststartupLoad;
	}

	public long getLaststartdownLoad() {
		return laststartdownLoad;
	}

	public void setLaststartdownLoad(long laststartdownLoad) {
		this.laststartdownLoad = laststartdownLoad;
	}

	public String getAppName() {
		return appName;
	}
	
	public App(String appName, int appid) {
		super();
		this.appName = appName;
		this.appid = appid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appName == null) ? 0 : appName.hashCode());
		result = prime * result + appid;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		App other = (App) obj;
		if (appName == null) {
			if (other.appName != null)
				return false;
		} else if (!appName.equals(other.appName))
			return false;
		if (appid != other.appid)
			return false;
		return true;
	}

	public int getAppid() {
		return appid;
	}

	public void setAppid(int appid) {
		this.appid = appid;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
	public long getStartupLoad() {
		return startupLoad;
	}
	public void setStartupLoad(long startupLoad) {
		this.startupLoad = startupLoad;
	}
	public long getStartdownLoad() {
		return startdownLoad;
	}
	public void setStartdownLoad(long startdownLoad) {
		this.startdownLoad = startdownLoad;
	}
	public App(int id,String appName, long startupLoad, long startdownLoad) {
		super();
		this.appid=id;
		this.appName = appName;
		this.startupLoad = startupLoad;
		this.startdownLoad = startdownLoad;
	}
	public App() {
	}
}
