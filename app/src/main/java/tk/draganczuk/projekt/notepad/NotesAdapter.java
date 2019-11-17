package tk.draganczuk.projekt.notepad;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import tk.draganczuk.projekt.R;

//@AllArgsConstructor
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private List<NoteModel> notes;

    private List<NoteModel> filtered;

    NotesAdapter(List<NoteModel> notes) {
        this.notes = notes;
        filtered = notes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_note, parent, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NoteModel note = filtered.get(position);

        TextView noteTitleView = holder.noteTitleView;
        TextView noteContentView = holder.noteContentView;
        Button editButton = holder.editButton;
        Button deleteButton = holder.deleteButton;

        noteTitleView.setText(note.getTitle());
        noteContentView.setText(note.getContent());

        editButton.setOnClickListener((view) -> {

            Intent editIntent = new Intent(view.getContext(), EditNoteActivity.class);
            Bundle bundle = new Bundle();

            bundle.putString("note.title", note.getTitle());
            bundle.putString("note.content", note.getContent());

            editIntent.putExtras(bundle);

            view.getContext().startActivity(editIntent);
        });

        deleteButton.setOnClickListener((view) -> {
            File filesDir = view.getContext().getFilesDir();
            File file = new File(filesDir, note.getTitle() + ".txt");

            file.delete();

            notes.remove(position);

        });
    }

    void filter(String filter) {
        this.filtered = this.notes.stream()
                .filter(s -> s.getTitle().toLowerCase().contains(filter.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public int getItemCount() {
        return filtered.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView noteTitleView;
        TextView noteContentView;
        Button editButton;
        Button deleteButton;

        ViewHolder(View itemView) {
            super(itemView);
            noteTitleView = itemView.findViewById(R.id.noteListTitle);
            noteContentView = itemView.findViewById(R.id.noteListContent);
            editButton = itemView.findViewById(R.id.noteListEditButton);
            deleteButton = itemView.findViewById(R.id.noteListDeleteButton);
        }
    }
}