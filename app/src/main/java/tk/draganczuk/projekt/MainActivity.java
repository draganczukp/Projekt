package tk.draganczuk.projekt;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import tk.draganczuk.projekt.reminder.ReminderActivity;
import tk.draganczuk.projekt.reminder.ReminderReceiver;
import tk.draganczuk.projekt.reminder.ReminderScheduler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(new NotificationChannel("reminder", "reminder", NotificationManager.IMPORTANCE_HIGH));

        ReminderScheduler.schedule(this);
    }

    public void onNotepadButton(View view) {
        startActivity(new Intent(getApplicationContext(), NoteActivity.class));
    }

    public void onReminderButton(View view) {
        startActivity(new Intent(getApplicationContext(), ReminderActivity.class));
    }

    public void onPhonebookButton(View view) {
        notImplemented();
    }

    public void onMultimediaButton(View view) {
        notImplemented();
    }

    private void notImplemented() {
        Toast.makeText(getApplicationContext(), "NOT IMPLEMENTED", Toast.LENGTH_LONG).show();
    }
}
