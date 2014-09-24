package vn.cybersoft.summerms.receivers;

import vn.cybersoft.summerms.Preferences;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class BlockSMSReceiver extends BroadcastReceiver {
	/** Called when the activity is first created. */
	private static final String ACTION_SEND_SMS = "android.provider.Telephony.SEND_SMS";
	private static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	public static int MSG_TPE = 0;

	public void onReceive(Context context, Intent intent) {
		String MSG_TYPE = intent.getAction();
		if (MSG_TYPE.equals(ACTION_SMS_RECEIVED)) {
			Toast toast1 = Toast.makeText(context,"SMS Received: "+MSG_TYPE ,
			Toast.LENGTH_LONG);
			toast1.show();

			Bundle bundle = intent.getExtras();
			Object messages[] = (Object[]) bundle.get("pdus");
			SmsMessage smsMessage[] = new SmsMessage[messages.length];
			for (int n = 0; n < messages.length; n++) {
				smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
			}

			// show first message
			Toast toast = Toast.makeText(context, "BLOCKED Received SMS: "
					+ smsMessage[0].getMessageBody() + "address: " + smsMessage[0].getOriginatingAddress(), Toast.LENGTH_LONG);
			toast.show();
			Log.d(this.getClass().getName(),"address: " + smsMessage[0].getOriginatingAddress());
			String number =  smsMessage[0].getOriginatingAddress();
			String num1 = number.substring(1, number.length());
			String num2 = number.substring(3, number.length());
			String statusofNum1 = "";
			String statusofNum2 = "";
			statusofNum1 = Preferences.getInstance().getDatabaseContact().getStatusSMSofContactByNumber(num1);
			statusofNum2 = Preferences.getInstance().getDatabaseContact().getStatusSMSofContactByNumber(num2);
			Log.d(this.getClass().getName(),"num1 stt: " + statusofNum1);
			
			Log.d(this.getClass().getName(),"num2 stt: " + statusofNum2);
			if (statusofNum1.equals("") && statusofNum2.equals("")) {
				abortBroadcast();
			}else if (statusofNum1.equals("no") || statusofNum2.equals("no")){
				abortBroadcast();
			} else 
				return;
			/*for (int i = 0; i < 8; i++) {
				System.out.println("Blocking SMS **********************");
			}*/

		} else if (MSG_TYPE.equals(ACTION_SEND_SMS)) {
			/*Bundle bundle = intent.getExtras();
			Object messages[] = (Object[]) bundle.get("pdus");
			SmsMessage smsMessage[] = new SmsMessage[messages.length];
			for (int n = 0; n < messages.length; n++) {
				smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
			}

			// show first message
			Toast toast = Toast.makeText(context, "BLOCKED Received SMS: "
					+ smsMessage[0].getMessageBody() + "address: " + smsMessage[0].getOriginatingAddress(), Toast.LENGTH_LONG);
			toast.show();
			Toast toast1 = Toast.makeText(context, "SMS SENT: " + MSG_TYPE,
					Toast.LENGTH_LONG);
			toast1.show();
			Log.d(this.getClass().getName()," sdt " + smsMessage[0].getDisplayOriginatingAddress());
			Log.d(this.getClass().getName(),"address: " + smsMessage[0].getOriginatingAddress());
			Log.d(this.getClass().getName(),"index: " + smsMessage[0].getIndexOnSim());
			Log.d(this.getClass().getName(),"getServiceCenterAddress: " + smsMessage[0].getServiceCenterAddress());
			Log.d(this.getClass().getName(),"getStatus: " + smsMessage[0].getStatus());
			abortBroadcast();
			for (int i = 0; i < 8; i++) {
				System.out.println("Blocking SMS **********************");
			}*/

		} else {
			Toast toast = Toast.makeText(context, "SIN ELSE: " + MSG_TYPE,
					Toast.LENGTH_LONG);
			toast.show();
			abortBroadcast();
			for (int i = 0; i < 8; i++) {
				System.out.println("Blocking SMS **********************");
			}

		}

	}

}