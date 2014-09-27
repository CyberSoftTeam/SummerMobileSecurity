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
/**
 * @author Phạm Văn Năm
 */
public class DateTraffic {
	private String date ;
	private long startupLoad;
	private long startdownLoad;
	public DateTraffic(String date2, long tx, long rx) {
		super();
		this.date = date2;
		this.startupLoad = tx;
		this.startdownLoad = rx;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
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
	public DateTraffic() {
		// TODO Auto-generated constructor stub
	}
}
