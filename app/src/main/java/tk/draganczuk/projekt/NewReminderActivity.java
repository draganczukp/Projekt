package tk.draganczuk.projekt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

import tk.draganczuk.projekt.db.Reminder;
import tk.draganczuk.projekt.db.ReminderDao;
import tk.draganczuk.projekt.db.ReminderDatabase;

public class NewReminderActivity extends AppCompatActivity {

    private static final String TAG = "NewReminderActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reminder);
    }

    public void onSaveButtonClick(View view) {
        ReminderDao dao = ReminderDatabase.get(this).reminderDao();
        TextView reminderName = findViewById(R.id.reminderName);
        TextView reminderDate = findViewById(R.id.reminderDate);
        TextView reminderTime = findViewById(R.id.reminderTime);

        String name = reminderName.getText().toString();
        String date = reminderDate.getText().toString();
        String time = reminderTime.getText().toString();

        Log.d(TAG, String.format("onSaveButtonClick: %sT%s", date, time));

        Reminder reminder = Reminder.builder()
                .name(name)
                .time(LocalDateTime.parse(date + "T" + time))
                .build();

        dao.insertAll(reminder);

    }

    public void onSaveAndExitButtonClick(View view){
        onSaveButtonClick(view);
        finish();
    }

    public void onExitButtonClick(View view){
        finish();
    }
}
