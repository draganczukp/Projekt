package tk.draganczuk.projekt.notepad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import tk.draganczuk.projekt.R;

public class AllNotesActivity extends AppCompatActivity {

    public static final String TAG = "AllNotesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notes);

        RecyclerView recycler = findViewById(R.id.all_notes_recycler);

        String[] files = fileList();

        List<NoteModel> notesList = Arrays.stream(Objects.requireNonNull(files))
                .filter(s -> s.endsWith(".txt"))
                .map(s -> new File(getFilesDir(), s))
                .map(NoteModel::fromFile)
                .peek(note -> Log.d(TAG, note.toString()))
                .collect(Collectors.toList());

        NotesAdapter notesAdapter = new NotesAdapter(notesList);
        recycler.setAdapter(notesAdapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        TextView filterInput = findViewById(R.id.filter);
        Button filterButton = findViewById(R.id.filterButton);

        filterInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                notesAdapter.filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        filterButton.setOnClickListener((view) -> notesAdapter.filter(filterInput.getText().toString()));
    }

}
