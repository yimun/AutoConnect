package com.xyworm.autoconnect;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

public class MyReceiver extends BroadcastReceiver {
	

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("##", "#Receiver get");
		BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
		if(intent.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
			if (ba.getState() == BluetoothAdapter.STATE_ON) {
				Log.i("##", "on");
				context.startService(new Intent(context,ConnectService.class));
			}
		}
		
		if(intent.getAction().equals(Intent.ACTION_USER_PRESENT)){
				
			if (ba.getConnectionState() == BluetoothAdapter.STATE_CONNECTED) {
				Log.i("##", "connected");
				return;
			}
			Log.i("##", "not connected");
			if (ba.getState() == BluetoothAdapter.STATE_ON) {
				Log.i("##", "on");
				context.startService(new Intent(context,ConnectService.class));
			}
			if (ba.getState() == BluetoothAdapter.STATE_OFF) {
				Log.i("##", "off");
				ba.enable();
			}
			

		}
	}

}
