package com.xyworm.autoconnect;

import java.util.Set;


import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothInputDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ConnectService extends Service {

	private static BluetoothDevice remoteDevice = null;
	private BluetoothAdapter adapter = null;
	private Set<BluetoothDevice> devices;
	final static String HID_ADDRESS = "DC:2C:26:01:60:15";
	final static String HID_KEY = "123456";
	private BluetoothInputDevice mService = null;

	private final class InputDeviceServiceListener implements
			BluetoothProfile.ServiceListener {

		public void onServiceConnected(int profile, BluetoothProfile proxy) {
			Log.i("##", "onServiceConnected");
			mService = (BluetoothInputDevice) proxy;

		}

		public void onServiceDisconnected(int profile) {
			Log.i("##", "onServiceDisConnected");
			mService = null;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		adapter = BluetoothAdapter.getDefaultAdapter();
		adapter.getProfileProxy(this, new InputDeviceServiceListener(),
				BluetoothProfile.INPUT_DEVICE);
	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Toast.makeText(this, "ָ����������...", Toast.LENGTH_SHORT).show();
		devices = adapter.getBondedDevices();
		if(devices == null)
			return;
		for (BluetoothDevice dv : devices) {
			if (dv.getAddress().equals(HID_ADDRESS)) {
				Log.i("##", "find had Paired");
				dv.removeBond();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				mConnect(dv);
//				return;
			}
		}
		// δ�ҵ��豸�������
		if (pair(HID_ADDRESS, "123456")) {// pair�ĵ�һ��������Ҫ��Ե�������ַ���ڶ������������ʱԤ�����õ���Կ
//			Toast.makeText(this, "��Գɹ�" + remoteDevice.getAddress(),
//					Toast.LENGTH_SHORT).show();
			new Handler().postDelayed(new Runnable(){
				public void run(){
					mConnect(remoteDevice);
				}
			}, 2000);
			
		} else {
//			Toast.makeText(this, "error:���ʧ��", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private boolean pair(String strAddr, String strPsw) {
		boolean result = false;
		// �����豸������
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		// ȡ�����ֵ�ǰ�豸�Ĺ���
		bluetoothAdapter.cancelDiscovery();
		if (!bluetoothAdapter.isEnabled()) {
			bluetoothAdapter.enable();
		}
		if (!BluetoothAdapter.checkBluetoothAddress(strAddr)) { // ���������ַ�Ƿ���Ч
			Log.d("mylog", "devAdd un effient!");
		}
		// �������豸��ַ�����һ�����豸����
		BluetoothDevice device = bluetoothAdapter.getRemoteDevice(strAddr);
		if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
			try {
				Log.d("mylog", "NOT BOND_BONDED");
				device.cancelPairingUserInput();
				device.setPin(strPsw.getBytes()); // �ֻ��������ɼ������
				result = device.createBond();
				remoteDevice = device; // �����ϾͰ�����豸���󴫸�ȫ�ֵ�remoteDevice
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.d("mylog", "setPiN failed!");
				e.printStackTrace();
			} //

		} else {
			Log.d("mylog", "HAS BOND_BONDED");
			try {
				// ClsUtils�����ĵ����¾�̬��������ͨ��������Ƶõ���Ҫ�ķ���
				device.cancelPairingUserInput();
				device.createBond();// ������
				device.setPin(strPsw.getBytes()); // �ֻ��������ɼ������
				device.createBond();
				remoteDevice = device; // ����󶨳ɹ�����ֱ�Ӱ�����豸���󴫸�ȫ�ֵ�remoteDevice
				result = true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.d("mylog", "setPiN failed!");
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public boolean mConnect(BluetoothDevice btDev) {
//		btDev.setTrust(true);
		if (mService != null) {
			return mService.connect(btDev);
		} else {
			Toast.makeText(this, "error:��������Ϊ��", Toast.LENGTH_SHORT).show();
			return false;
		}
	}

}
