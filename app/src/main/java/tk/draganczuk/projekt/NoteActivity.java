package tk.draganczuk.projekt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import tk.draganczuk.projekt.notepad.AllNotesActivity;
import tk.draganczuk.projekt.notepad.EditNoteActivity;

public class NoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
    }

    public void onNewButton(View view){
        startActivity(new Intent(getApplicationContext(), EditNoteActivity.class));
    }

    public void onShowAll(View view){
        startActivity(new Intent(getApplicationContext(), AllNotesActivity.class));
    }

    private void notImplemented() {
        Toast.makeText(getApplicationContext(), "NOT IMPLEMENTED", Toast.LENGTH_LONG).show();
    }
}
