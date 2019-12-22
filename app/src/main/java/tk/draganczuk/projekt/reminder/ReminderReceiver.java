package tk.draganczuk.projekt.reminder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tk.draganczuk.projekt.R;
import tk.draganczuk.projekt.db.Reminder;
import tk.draganczuk.projekt.db.ReminderDatabase;

public class ReminderReceiver extends BroadcastReceiver {

    private static final String CHANNEL = "reminder";
    private static final String TAG = "ReminderReceiver";

    private ExecutorService executorService = Executors.newSingleThreadExecutor();


    @Override
    public void onReceive(Context context, Intent intent) {

        executorService.execute(() -> {
            Log.d(TAG, "onReceive: Received");
            List<Reminder> reminders = ReminderDatabase.get(context).reminderDao().findByDateAndTime(
                    Calendar.getInstance().getTimeInMillis()
            );

            Log.d(TAG, "onReceive: Reminders: " + reminders.size());

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            for (Reminder reminder : reminders) {
                Log.d(TAG, "onReceive: " + reminder);
                Notification notification = new NotificationCompat.Builder(context, CHANNEL)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle(reminder.getName())
                        .setContentText(reminder.getNote())
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .build();


                manager.notify(0, notification);
            }

        });

    }
}
