package tk.draganczuk.projekt.multimedia;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

import tk.draganczuk.projekt.R;

public class MusicPlayerActivity extends AppCompatActivity {

	private MusicPlayerService player;
	boolean serviceBound = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music_player);

		RecyclerView musicRecycler = findViewById(R.id.musicRecycler);

		List<Music> mockMusicList = createMockMusic();

		MusicAdapter musicAdapter = new MusicAdapter(mockMusicList, this::playAudio);
		musicRecycler.setAdapter(musicAdapter);
		musicRecycler.setLayoutManager(new LinearLayoutManager(this));

	}

	private ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// We've bound to LocalService, cast the IBinder and get LocalService instance
			MusicPlayerService.LocalBinder binder = (MusicPlayerService.LocalBinder) service;
			player = binder.getService();
			serviceBound = true;

			Toast.makeText(MusicPlayerActivity.this, "Service Bound", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			serviceBound = false;
		}
	};

	private void playAudio(String media) {
		//Check is service is active
		if (!serviceBound) {
			Intent playerIntent = new Intent(this, MusicPlayerService.class);
			playerIntent.putExtra("media", media);
			startService(playerIntent);
			bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
		} else {
			//Service is active
			//Send media with BroadcastReceiver
		}
	}

	private List<Music> createMockMusic() {
		return Arrays.asList(
				new Music("test1", "/sdcard/Download/Alexander Ehlers - Flags.mp3", "test1"),
				new Music("test2", "/sdcard/Download/Alexander Ehlers - Flags.mp3", "test2"),
				new Music("test3", "/sdcard/Download/Alexander Ehlers - Flags.mp3", "test3")
		);
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putBoolean("ServiceState", serviceBound);
		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		serviceBound = savedInstanceState.getBoolean("ServiceState");
	}

	public void onPlayPauseButton(View view) {
		player.togglePlaying();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (serviceBound) {
			unbindService(serviceConnection);
			//service is active
			player.stopSelf();
		}
	}
}
