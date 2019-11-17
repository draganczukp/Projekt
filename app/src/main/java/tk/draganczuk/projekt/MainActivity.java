package tk.draganczuk.projekt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onNotepadButton(View view) {
        startActivity(new Intent(getApplicationContext(), NoteActivity.class));
    }

    public void onReminderButton(View view){
        notImplemented();
    }

    public void onPhonebookButton(View view){
        notImplemented();
    }

    public void onMultimediaButton(View view){
        notImplemented();
    }

    private void notImplemented() {
        Toast.makeText(getApplicationContext(), "NOT IMPLEMENTED", Toast.LENGTH_LONG).show();
    }
}
