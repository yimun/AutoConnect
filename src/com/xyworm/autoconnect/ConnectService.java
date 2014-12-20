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
		Toast.makeText(this, "指环正在连接...", Toast.LENGTH_SHORT).show();
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
		// 未找到设备，先配对
		if (pair(HID_ADDRESS, "123456")) {// pair的第一个参数是要配对的蓝牙地址，第二个参数是配对时预先设置的密钥
//			Toast.makeText(this, "配对成功" + remoteDevice.getAddress(),
//					Toast.LENGTH_SHORT).show();
			new Handler().postDelayed(new Runnable(){
				public void run(){
					mConnect(remoteDevice);
				}
			}, 2000);
			
		} else {
//			Toast.makeText(this, "error:配对失败", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private boolean pair(String strAddr, String strPsw) {
		boolean result = false;
		// 蓝牙设备适配器
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		// 取消发现当前设备的过程
		bluetoothAdapter.cancelDiscovery();
		if (!bluetoothAdapter.isEnabled()) {
			bluetoothAdapter.enable();
		}
		if (!BluetoothAdapter.checkBluetoothAddress(strAddr)) { // 检查蓝牙地址是否有效
			Log.d("mylog", "devAdd un effient!");
		}
		// 由蓝牙设备地址获得另一蓝牙设备对象
		BluetoothDevice device = bluetoothAdapter.getRemoteDevice(strAddr);
		if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
			try {
				Log.d("mylog", "NOT BOND_BONDED");
				device.cancelPairingUserInput();
				device.setPin(strPsw.getBytes()); // 手机和蓝牙采集器配对
				result = device.createBond();
				remoteDevice = device; // 配对完毕就把这个设备对象传给全局的remoteDevice
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.d("mylog", "setPiN failed!");
				e.printStackTrace();
			} //

		} else {
			Log.d("mylog", "HAS BOND_BONDED");
			try {
				// ClsUtils这个类的的以下静态方法都是通过反射机制得到需要的方法
				device.cancelPairingUserInput();
				device.createBond();// 创建绑定
				device.setPin(strPsw.getBytes()); // 手机和蓝牙采集器配对
				device.createBond();
				remoteDevice = device; // 如果绑定成功，就直接把这个设备对象传给全局的remoteDevice
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
			Toast.makeText(this, "error:蓝牙服务为空", Toast.LENGTH_SHORT).show();
			return false;
		}
	}

}
