package tk.draganczuk.projekt.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public
interface ReminderDao {
    @Query("SELECT * FROM reminder")
    LiveData<List<Reminder>> getAll();

    @Query("SELECT * FROM reminder WHERE id IN (:ids)")
    LiveData<List<Reminder>> findAllById(int[] ids);

    @Query("SELECT * FROM reminder WHERE id = :id")
    Reminder findById(int id);

    @Insert
    void insertAll(Reminder... reminders);

    @Delete
    void delete(Reminder reminder);

    @Update
    void update(Reminder reminder);

    @Query("SELECT * FROM reminder WHERE date BETWEEN (:date - 5000) AND (:date + 5000)")
    List<Reminder> findByDateAndTime(long date);
}
