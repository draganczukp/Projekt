package tk.draganczuk.projekt.notepad;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.IOException;

import tk.draganczuk.projekt.R;

public class EditNoteActivity extends AppCompatActivity {
    private static final String TAG = "EditNoteActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null && !extras.isEmpty()){

            TextView titleView = findViewById(R.id.title);
            TextView contentView = findViewById(R.id.content);

            titleView.setText(extras.getString("note.title"));
            contentView.setText(extras.getString("note.content"));
        }
    }

    public void onSaveButtonClick(View view) {
        TextView titleView = findViewById(R.id.title);
        TextView contentView = findViewById(R.id.content);

        CharSequence title = titleView.getText();
        CharSequence text = contentView.getText();

        try (FileOutputStream out = openFileOutput(title + ".txt", Context.MODE_PRIVATE)) {
            out.write(text.toString().getBytes());
        } catch (IOException e) {
            Log.e(TAG, "onSaveButtonClick: Error saving", e);
        }

    }

    public void onSaveAndExitButtonClick(View view){
        onSaveButtonClick(view);
        finish();
    }

    public void onExitButtonClick(View view){
        finish();
    }

}
