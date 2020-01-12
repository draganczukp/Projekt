package tk.draganczuk.projekt.reminder;

import android.app.Application;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import tk.draganczuk.projekt.db.AppDatabase;
import tk.draganczuk.projekt.db.Reminder;
import tk.draganczuk.projekt.db.ReminderDao;

public class ReminderViewModel extends AndroidViewModel {

    private final ReminderDao reminderDao;
    private final ExecutorService executorService;

    public ReminderViewModel(@NonNull Application application) {
        super(application);

        reminderDao = AppDatabase.get(application.getApplicationContext()).reminderDao();
        executorService = Executors.newSingleThreadExecutor();

    }

    public LiveData<List<Reminder>> getAllReminders(){
        return reminderDao.getAll();
    }

    void saveReminder(Reminder reminder){
        executorService.execute(()->reminderDao.insertAll(reminder));
    }

    void deleteReminder(Reminder reminder){
        executorService.execute(() -> reminderDao.delete(reminder));
    }

    public void update(Reminder reminder) {
        executorService.execute(() -> reminderDao.update(reminder));
    }
}
