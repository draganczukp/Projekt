package tk.draganczuk.projekt.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
import java.util.Optional;

@Dao
public
interface ReminderDao {
    @Query("SELECT * FROM reminder")
    LiveData<List<Reminder>> getAll();

    @Query("SELECT * FROM reminder WHERE id IN (:ids)")
    LiveData<List<Reminder>> findAllById(int[] ids);

    @Query("SELECT * FROM reminder WHERE id = :id")
    LiveData<Reminder> findById(int id);

    @Insert
    void insertAll(Reminder... reminders);

    @Delete
    void delete(Reminder reminder);
}
