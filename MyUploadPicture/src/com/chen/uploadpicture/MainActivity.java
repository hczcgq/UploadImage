package com.chen.uploadpicture;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button network,nativep;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		network=(Button) findViewById(R.id.network);
		nativep=(Button) findViewById(R.id.nativep);
		
		network.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(MainActivity.this,Network.class);
				startActivity(intent);
			}
		});
		
		nativep.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(MainActivity.this,Nativep.class);
				startActivity(intent);
			}
		});
		
	}
}
