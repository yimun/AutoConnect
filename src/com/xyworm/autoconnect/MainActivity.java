package com.xyworm.autoconnect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnClickListener {

	private Button btnOpen;
	private Button btnClose;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnOpen = (Button) findViewById(R.id.button1);
		btnClose = (Button) findViewById(R.id.button1);
		btnOpen.setOnClickListener(this);
		btnClose.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent in = new Intent(this, ConnectService.class);
		switch (v.getId()) {
		case R.id.button1:
			this.startService(in);
			Toast.makeText(this, "open", Toast.LENGTH_SHORT).show();
			break;
		case R.id.button2:
			this.stopService(in);
			Toast.makeText(this, "close", Toast.LENGTH_SHORT).show();
			break;
		}
	}
}
