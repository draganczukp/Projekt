package tk.draganczuk.projekt.notepad;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
class NoteModel {
    private String title;
    private String content;

    static NoteModel fromFile(File file) {

        String title = file.getName().replaceAll("\\.txt$", "");
        String content = "";
        try {
            content = String.join("\n", Files.readAllLines(file.toPath()));

        } catch (IOException e) {
            Log.e("NoteModel", "Error reading note", e);
            e.printStackTrace();
        }

        return NoteModel.builder()
                .title(title)
                .content(content)
                .build();
    }
}
