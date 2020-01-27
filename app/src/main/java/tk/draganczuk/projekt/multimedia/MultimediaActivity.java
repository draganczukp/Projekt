package tk.draganczuk.projekt.multimedia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import tk.draganczuk.projekt.R;

public class MultimediaActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multimedia);
	}

	public void onPhotoButtonClick(View view) {
		startActivity(new Intent(this, TakePhotoActivity.class));
	}

	//TODO: Last
	public void onPaintButtonClick(View view) {
//		startActivity(new Intent(this, PaintActivity.class));
	}

	public void onMusicButtonClick(View view) {
		startActivity(new Intent(this, MusicPlayerActivity.class));
	}

	public void onWebsiteButtonClick(View view) {
		startActivity(new Intent(this, WebsiteActivity.class));
	}
}
