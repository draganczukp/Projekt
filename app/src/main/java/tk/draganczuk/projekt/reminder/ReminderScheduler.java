package tk.draganczuk.projekt.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.Objects;

public class ReminderScheduler extends BroadcastReceiver {
    static final String TAG = "ReminderScheduler";


    public static void schedule(Context context) {
        Log.d(TAG, "schedule: scheduling");

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.setAction("reminder.notify");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Objects.requireNonNull(alarmManager).setRepeating(AlarmManager.RTC_WAKEUP,
                cal.getTimeInMillis(),
                60_000,
                pendingIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Objects.equals(intent.getAction(), "android.intent.action.BOOT_COMPLETED")){
            schedule(context);
        }
    }
}
