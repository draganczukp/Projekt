package tk.draganczuk.projekt.notepad;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import tk.draganczuk.projekt.R;

/**
 * Implementation of App Widget functionality.
 */
public class NotesWidget extends AppWidgetProvider {

    private static final String ON_CLICK_TAG = "newNoteOnClick";
    private static final String TAG = "NotesWidget";

    private static List<NoteModel> notes;
    private static int idx = 0;


    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {

        notes = getNotes(context);

        NoteModel note = notes.get(idx % notes.size());

        Log.d(TAG, "updateAppWidget: " + note.toString());

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.notes_widget);

        views.setTextViewText(R.id.noteTitle, note.getTitle());
        views.setTextViewText(R.id.noteWidgetContent, note.getContent());

        views.setOnClickPendingIntent(R.id.noteTitle, getPendingSelfIntent(context, ON_CLICK_TAG));
        views.setOnClickPendingIntent(R.id.noteWidgetContent, getPendingSelfIntent(context, ON_CLICK_TAG));


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    void update(Context context){
        Log.d(TAG, "update: update");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisAppWidgetComponentName = new ComponentName(context.getPackageName(), getClass().getName());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidgetComponentName);
        onUpdate(context, appWidgetManager, appWidgetIds);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(TAG, "onReceive: " + intent.getAction());
        if (ON_CLICK_TAG.equals(intent.getAction())) {

            idx++;
            Log.d(TAG, "onReceive: " + idx);
            update(context);
        }

    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    private List<NoteModel> getNotes(Context context) {
        String[] files = context.fileList();

        return Arrays.stream(Objects.requireNonNull(files))
                .filter(s -> s.endsWith(".txt"))
                .map(s -> new File(context.getFilesDir(), s))
                .map(NoteModel::fromFile)
                .collect(Collectors.toList());
    }
}

