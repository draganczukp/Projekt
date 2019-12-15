package tk.draganczuk.projekt.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {Reminder.class}, version = 1, exportSchema = false)
public abstract class ReminderDatabase extends RoomDatabase {
    public abstract ReminderDao reminderDao();

    private static ReminderDatabase INSTANCE = null;

    public static ReminderDatabase get(Context context) {

        if (INSTANCE == null) {

            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    ReminderDatabase.class,
                    "word_database"
            ).build();
        }

        return INSTANCE;
    }
}