package tk.draganczuk.projekt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import tk.draganczuk.projekt.db.ReminderDatabase;

public class ReminderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
    }

    public void onNewReminderClick(View view) {
        startActivity(new Intent(this, NewReminderActivity.class));
    }
}
