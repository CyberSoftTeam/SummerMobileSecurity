/***
	Copyright (c) 2008-2011 CommonsWare, LLC
	Licensed under the Apache License, Version 2.0 (the "License"); you may not
	use this file except in compliance with the License. You may obtain a copy
	of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
	by applicable law or agreed to in writing, software distributed under the
	License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
	OF ANY KIND, either express or implied. See the License for the specific
	language governing permissions and limitations under the License.
	
	From _Tuning Android Applications_
		http://commonsware.com/AndTuning
*/

package vn.cybersoft.summerms.model;
/**
 * @author Phạm Văn Năm
 */
import android.net.TrafficStats;

public class TrafficRecord {
	private long tx=0;
	private long rx=0;
	private String tag=null;
	
	public TrafficRecord() {
		tx=TrafficStats.getTotalTxBytes();
		rx=TrafficStats.getTotalRxBytes();
	}
	
	public TrafficRecord(int uid, String tag) {
		tx=TrafficStats.getUidTxBytes(uid);
		rx=TrafficStats.getUidRxBytes(uid);
		this.tag=tag;
	}

	public long getTx() {
		return tx;
	}
	

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void setTx(long tx) {
		this.tx = tx;
	}

	public long getRx() {
		return rx;
	}

	public void setRx(long rx) {
		this.rx = rx;
	}
	
}