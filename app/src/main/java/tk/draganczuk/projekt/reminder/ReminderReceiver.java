package tk.draganczuk.projekt.reminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.core.app.NotificationCompat;
import tk.draganczuk.projekt.R;
import tk.draganczuk.projekt.db.AppDatabase;
import tk.draganczuk.projekt.db.Reminder;

public class ReminderReceiver extends BroadcastReceiver {

    private static final String CHANNEL = "reminder";
    private static final String TAG = "ReminderReceiver";

    private ExecutorService executorService = Executors.newSingleThreadExecutor();


    @Override
    public void onReceive(Context context, Intent intent) {

        executorService.execute(() -> {
            Log.d(TAG, "onReceive: Received");
            List<Reminder> reminders = AppDatabase.get(context).reminderDao().findByDateAndTime(
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
