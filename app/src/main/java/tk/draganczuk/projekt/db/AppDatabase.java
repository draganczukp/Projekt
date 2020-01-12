package tk.draganczuk.projekt.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {Reminder.class, Contact.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ReminderDao reminderDao();
    public abstract ContactDao contactDao();

    private static AppDatabase INSTANCE = null;

    public static AppDatabase get(Context context) {

        if (INSTANCE == null) {

            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "word_database"
            ).build();
        }

        return INSTANCE;
    }
}