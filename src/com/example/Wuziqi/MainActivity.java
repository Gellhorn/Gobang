package com.example.Wuziqi;


import java.io.IOException;

import com.example.Wuziqi.R;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.res.AssetManager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
	
	private 	MediaPlayer mpMediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    	mpMediaPlayer = new MediaPlayer();

    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){

		case R.id.palyMusic:
			playMusic();
			break;
		case R.id.stopMusic:
			stopMusic();
			break;
		}

		return super.onOptionsItemSelected(item);
	}
	private void stopMusic() {
		// TODO Auto-generated method stub

       mpMediaPlayer.stop();
	}
	private void playMusic() {
		// TODO Auto-generated method stub
	
        AssetManager am = getAssets();
        try {
            mpMediaPlayer.setDataSource(am.openFd("a.mp3").getFileDescriptor());
            mpMediaPlayer.prepare();
            mpMediaPlayer.start();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}

   
}
