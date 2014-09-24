package vn.cybersoft.summerms.receivers;

import java.lang.reflect.Method;

import vn.cybersoft.summerms.Preferences;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

public class BlockCallReceiver extends BroadcastReceiver {
	private static final String TAG = "block call receiver";
	Context context;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Bundle myBundle = intent.getExtras();
		if (myBundle != null) {
			Log.d(TAG, "--------Not null-----");

			try {
				if (intent.getAction().equals(
						"android.intent.action.PHONE_STATE")) {
					String state = intent
							.getStringExtra(TelephonyManager.EXTRA_STATE);
					Log.d(TAG, "--------in state-----");
					if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
						// Incoming call
						String incomingNumber = intent
								.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
						Log.d(TAG, "--------------my number---------"
								+ incomingNumber);
						String num1 = incomingNumber.substring(1,
								incomingNumber.length());
						String num2 = incomingNumber.substring(3,
								incomingNumber.length());
						String statusofNum1 = "";
						String statusofNum2 = "";
						statusofNum1 = Preferences.getInstance()
								.getDatabaseContact()
								.getStatusCallofContactByNumber(num1);
						statusofNum2 = Preferences.getInstance()
								.getDatabaseContact()
								.getStatusCallofContactByNumber(num2);
						Log.d(this.getClass().getName(), "num1 stt: "
								+ statusofNum1);

						Log.d(this.getClass().getName(), "num2 stt: "
								+ statusofNum2);
						if (statusofNum1.equals("") && statusofNum2.equals("")) {
							return;
						} else if (statusofNum1.equals("no")
								|| statusofNum2.equals("no")) {
							return;
						} else {
							// this is main section of the code,. could also be
							// use
							// for particular number.
							// Get the boring old TelephonyManager.
							TelephonyManager telephonyManager = (TelephonyManager) context
									.getSystemService(Context.TELEPHONY_SERVICE);

							// Get the getITelephony() method
							Class<?> classTelephony = Class
									.forName(telephonyManager.getClass()
											.getName());
							Method methodGetITelephony = classTelephony
									.getDeclaredMethod("getITelephony");

							// Ignore that the method is supposed to be private
							methodGetITelephony.setAccessible(true);

							// Invoke getITelephony() to get the ITelephony
							// interface
							Object telephonyInterface = methodGetITelephony
									.invoke(telephonyManager);

							// Get the endCall method from ITelephony
							Class<?> telephonyInterfaceClass = Class
									.forName(telephonyInterface.getClass()
											.getName());
							Method methodEndCall = telephonyInterfaceClass
									.getDeclaredMethod("endCall");

							// Invoke endCall()
							methodEndCall.invoke(telephonyInterface);
						}
					}

				}
			} catch (Exception ex) { // Many things can go wrong with reflection
				// calls
				ex.printStackTrace();
			}
		}
	}
}