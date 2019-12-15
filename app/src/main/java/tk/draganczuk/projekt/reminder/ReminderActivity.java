package tk.draganczuk.projekt.reminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import tk.draganczuk.projekt.R;

public class ReminderActivity extends AppCompatActivity {

    private ReminderViewModel reminderViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        reminderViewModel = ViewModelProviders.of(this)
                            .get(ReminderViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.reminderRecyclerView);

        ReminderAdapter adapter = new ReminderAdapter(reminderViewModel);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reminderViewModel.getAllReminders().observe(this, adapter::setReminders);


    }

    public void onNewReminderClick(View view) {
        startActivity(new Intent(this, EditReminderActivity.class));
    }
}
