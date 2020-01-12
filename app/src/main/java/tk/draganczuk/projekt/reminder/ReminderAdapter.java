package tk.draganczuk.projekt.reminder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import lombok.RequiredArgsConstructor;
import tk.draganczuk.projekt.R;
import tk.draganczuk.projekt.db.Reminder;

@RequiredArgsConstructor
public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {

    private static final String TAG = "ReminderAdapter";

    private List<Reminder> reminders;
    private final ReminderViewModel reminderViewModel;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_reminder, parent, false);

        return new ReminderAdapter.ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reminder reminder = reminders.get(position);

        TextView reminderItemTitle = holder.reminderItemTitle;
        TextView reminderItemNote = holder.reminderItemNote;
        TextView reminderItemDateTime = holder.reminderItemDateTime;
        Button reminderItemDeleteButton = holder.reminderItemDeleteButton;

        reminderItemTitle.setText(reminder.getName());
        reminderItemNote.setText(reminder.getNote());

        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(reminder.getDate());

        reminderItemDateTime.setText(time.toInstant().atZone(ZoneOffset.systemDefault())
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));

        Calendar now = Calendar.getInstance();
        Log.d(TAG, String.format("onBindViewHolder: %s, %s", time, now));

        if (time.toInstant().isBefore(now.toInstant())) {
            reminderItemTitle.setPaintFlags(reminderItemTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            reminderItemNote.setPaintFlags(reminderItemNote.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            reminderItemDateTime.setPaintFlags(reminderItemDateTime.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            int color = holder.itemView.getContext().getColor(R.color.gray);

            reminderItemDateTime.setTextColor(color);
            reminderItemTitle.setTextColor(color);
            reminderItemNote.setTextColor(color);
        }

        reminderItemDeleteButton.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "DELETE " + reminder.getName(), Toast.LENGTH_SHORT).show();
            reminderViewModel.deleteReminder(reminder);
            notifyDataSetChanged();
        });

        holder.itemView.setOnClickListener(
                (view) -> {

                    Intent editIntent = new Intent(view.getContext(), EditReminderActivity.class);
                    Bundle bundle = new Bundle();

                    bundle.putInt("editing", reminder.getId());

                    editIntent.putExtras(bundle);

                    view.getContext().startActivity(editIntent);

                });
    }

    @Override
    public int getItemCount() {
        return reminders == null ? 0 : reminders.size();
    }

    void setReminders(List<Reminder> reminders) {
        this.reminders = reminders;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView reminderItemTitle;
        TextView reminderItemNote;
        TextView reminderItemDateTime;
        Button reminderItemDeleteButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            reminderItemTitle = itemView.findViewById(R.id.reminderItemTitle);
            reminderItemNote = itemView.findViewById(R.id.reminderItemNote);
            reminderItemDateTime = itemView.findViewById(R.id.reminderItemDateTime);
            reminderItemDeleteButton = itemView.findViewById(R.id.reminderItemDeleteButton);
        }
    }
}
