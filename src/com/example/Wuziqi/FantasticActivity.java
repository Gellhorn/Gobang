package com.example.Wuziqi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class FantasticActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_activity);
		
		Handler handler = new Handler();//��������һ��handler���Ͱ��˵�ǰ����handler���̡߳�
		handler.postDelayed(new MyRunnable(), 3000);
	}
	
	class MyRunnable implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Intent intent = new Intent(FantasticActivity.this,MainActivity.class);
			startActivity(intent);
			FantasticActivity.this.finish();
		}
		
	}
}
