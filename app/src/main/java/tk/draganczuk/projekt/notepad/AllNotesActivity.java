package tk.draganczuk.projekt.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import tk.draganczuk.projekt.R;

public class AllNotesActivity extends AppCompatActivity {

    public static final String TAG = "AllNotesActivity";
    private NotesAdapter notesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notes);

        RecyclerView recycler = findViewById(R.id.all_notes_recycler);

        List<NoteModel> notesList = getNotes();

        notesAdapter = new NotesAdapter(notesList);
        recycler.setAdapter(notesAdapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        TextView filterInput = findViewById(R.id.filter);
        Button filterButton = findViewById(R.id.filterButton);

        filterInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                notesAdapter.filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        filterButton.setOnClickListener((view) -> notesAdapter.filter(filterInput.getText().toString()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        notesAdapter.setNotes(getNotes());
        notesAdapter.notifyDataSetChanged();
    }

    private List<NoteModel> getNotes() {
        String[] files = fileList();

        return Arrays.stream(Objects.requireNonNull(files))
                .filter(s -> s.endsWith(".txt"))
                .sorted(Comparator.naturalOrder())
                .map(s -> new File(getFilesDir(), s))
                .map(NoteModel::fromFile)
                .peek(note -> Log.d(TAG, note.toString()))
                .collect(Collectors.toList());
    }

}
