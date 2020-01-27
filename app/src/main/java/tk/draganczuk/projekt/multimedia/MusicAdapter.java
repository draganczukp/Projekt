package tk.draganczuk.projekt.multimedia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.function.Consumer;

import lombok.RequiredArgsConstructor;
import tk.draganczuk.projekt.R;

@RequiredArgsConstructor
class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {

	private final List<Music> musicList;
	private final Consumer<String> playMusic;

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		Context context = parent.getContext();
		LayoutInflater inflater = LayoutInflater.from(context);

		View view = inflater.inflate(R.layout.item_music, parent, false);

		return new MusicAdapter.ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		TextView titleTextView = holder.titleTextView;
		TextView authorTextView = holder.authorTextView;

		Music music = musicList.get(position);

		titleTextView.setText(music.getName());
		authorTextView.setText(music.getAuthor());

		holder.itemView.setOnClickListener(v -> playMusic.accept(music.getFile()));

	}

	@Override
	public int getItemCount() {
		return musicList.size();
	}

	class ViewHolder extends RecyclerView.ViewHolder {
		TextView titleTextView;
		TextView authorTextView;

		ViewHolder(@NonNull View itemView) {
			super(itemView);
			authorTextView = itemView.findViewById(R.id.musicItemAuthor);
			titleTextView = itemView.findViewById(R.id.musicItemTitle);
		}
	}
}
