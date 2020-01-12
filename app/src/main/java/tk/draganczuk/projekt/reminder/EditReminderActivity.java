package tk.draganczuk.projekt.reminder;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.concurrent.Executors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import tk.draganczuk.projekt.R;
import tk.draganczuk.projekt.db.AppDatabase;
import tk.draganczuk.projekt.db.Reminder;
import tk.draganczuk.projekt.db.ReminderDao;

public class EditReminderActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "EditReminderActivity";
    private ReminderViewModel reminderViewModel;

    private Reminder current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reminder);

        TextView reminderName = findViewById(R.id.reminderName);
        TextView reminderNote = findViewById(R.id.reminderNote);
        TimePicker reminderTime = findViewById(R.id.reminderTimePick);
        DatePicker reminderDate = findViewById(R.id.reminderDatePick);

        reminderTime.setIs24HourView(true);

        reminderViewModel = ViewModelProviders
                .of(this)
                .get(ReminderViewModel.class);

        if (getIntent().hasExtra("editing")) {
            ReminderDao dao = AppDatabase.get(this).reminderDao();
            int id = getIntent().getIntExtra("editing", -1);
            Calendar cur = Calendar.getInstance();

            // Hack, bo baza danych nie lubi być na głównym wątku
            Executors.newSingleThreadExecutor().execute(() -> {
                current = dao.findById(id);
                cur.setTimeInMillis(current.getDate());
                reminderName.setText(current.getName());
                reminderNote.setText(current.getNote());
                reminderDate.updateDate(cur.get(Calendar.YEAR),
                        cur.get(Calendar.MONTH),
                        cur.get(Calendar.DAY_OF_MONTH));

                reminderTime.setHour(cur.get(Calendar.HOUR_OF_DAY));
                reminderTime.setMinute(cur.get(Calendar.MINUTE));
            });
        }
    }

    public void onSaveButtonClick(View view) {
        TextView reminderName = findViewById(R.id.reminderName);
        TextView reminderNote = findViewById(R.id.reminderNote);
        TimePicker reminderTime = findViewById(R.id.reminderTimePick);
        DatePicker reminderDate = findViewById(R.id.reminderDatePick);

        String name = reminderName.getText().toString();
        String note = reminderNote.getText().toString();

        Calendar calendar = Calendar.getInstance();
        calendar.set(reminderDate.getYear(),
                reminderDate.getMonth(),
                reminderDate.getDayOfMonth(),
                reminderTime.getHour(),
                reminderTime.getMinute());


        Reminder reminder = Reminder.builder()
                .name(name)
                .date(calendar.getTimeInMillis())
                .note(note)
                .build();

        Log.d(TAG, "onSaveButtonClick: " + calendar);

        if (current != null) {
            reminder.setId(current.getId());
            reminderViewModel.update(reminder);
        } else {
            reminderViewModel.saveReminder(reminder);
        }
    }

    public void onSaveAndExitButtonClick(View view) {
        onSaveButtonClick(view);
        finish();
    }

    public void onExitButtonClick(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
