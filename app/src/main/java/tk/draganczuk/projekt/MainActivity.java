package tk.draganczuk.projekt;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import tk.draganczuk.projekt.contacts.ContactsActivity;
import tk.draganczuk.projekt.multimedia.MultimediaActivity;
import tk.draganczuk.projekt.reminder.ReminderActivity;
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
        startActivity(new Intent(getApplicationContext(), ContactsActivity.class));
    }

    public void onMultimediaButton(View view) {
	    startActivity(new Intent(getApplicationContext(), MultimediaActivity.class));
    }

}
