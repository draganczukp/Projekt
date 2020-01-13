package tk.draganczuk.projekt.contacts;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import tk.draganczuk.projekt.R;
import tk.draganczuk.projekt.db.Contact;

@RequiredArgsConstructor
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

	private static final String TAG = "ContactAdapter";

	private List<Contact> contacts;
	private final ContactViewModel contactViewModel;

	private String filter = "";
	private Pattern filterPattern;
	private List<Contact> filtered;

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		Context context = parent.getContext();
		LayoutInflater inflater = LayoutInflater.from(context);

		View contactView = inflater.inflate(R.layout.contact_item, parent, false);

		return new ContactAdapter.ViewHolder(contactView);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		Contact contact = filtered.get(position);

		ImageView contactAvatarView = holder.contactAvatarView;
		TextView contactNameView = holder.contactNameView;
		TextView contactNumberView = holder.contactNumberView;
		TextView contactEmailView = holder.contactEmailView;
		ImageButton contactDeleteButton = holder.contactDeleteButton;
		ImageButton contactCallButton = holder.contactCallButton;

		if (contact.getAvatar() == null) {
			contactAvatarView.setImageDrawable(holder.itemView.getContext().getDrawable(
					contact.getGender().equalsIgnoreCase("male") ? R.drawable.man : R.drawable.woman)
			);
		} else {
			Picasso.get()
					.load(Uri.parse(contact.getAvatar()))
					.resizeDimen(R.dimen.avatarDim, R.dimen.avatarDim)
					.centerCrop()
					.into(contactAvatarView);
		}

		contactNameView.setText(String.format("%s %s", contact.getFirstName(), contact.getLastName()));

		contactEmailView.setText(contact.getEmail());
		contactNumberView.setText(contact.getPhoneNumber());

		contactDeleteButton.setOnClickListener(v -> {
			Toast.makeText(v.getContext(), String.format("DELETE %s %s", contact.getFirstName(), contact.getLastName()), Toast.LENGTH_SHORT).show();
			contactViewModel.deleteContact(contact);
			notifyDataSetChanged();
		});

		contactCallButton.setOnClickListener(view -> {
			Intent callIntent = new Intent(Intent.ACTION_DIAL);
			callIntent.setData(Uri.parse("tel:123456789"));
			view.getContext().startActivity(callIntent);
		});

		holder.itemView.setOnClickListener((view) -> {

			Intent editIntent = new Intent(view.getContext(), EditContactActivity.class);
			Bundle bundle = new Bundle();

			bundle.putInt("editing", contact.getId());

			editIntent.putExtras(bundle);

			view.getContext().startActivity(editIntent);

		});
	}

	@Override
	public int getItemCount() {
		return filtered == null ? 0 : filtered.size();
	}

	void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
		filter(this.filter);
//		notifyDataSetChanged();
	}

	void filter(String filterText) {
		this.filter = filterText;
		this.filterPattern = Pattern.compile(filterText, Pattern.CASE_INSENSITIVE);
		this.filtered = this.contacts.stream()
				.filter(this::contactFilter)
				.collect(Collectors.toList());
		notifyDataSetChanged();
	}

	private boolean contactFilter(Contact contact) {
		return filterPattern.matcher(contact.getEmail()).find()
				|| filterPattern.matcher(contact.getFirstName()).find()
				|| filterPattern.matcher(contact.getLastName()).find()
				|| filterPattern.matcher(contact.getPhoneNumber()).find();
	}

	class ViewHolder extends RecyclerView.ViewHolder {
		TextView contactNameView;
		TextView contactNumberView;
		TextView contactEmailView;
		ImageView contactAvatarView;
		ImageButton contactDeleteButton;
		ImageButton contactCallButton;


		ViewHolder(@NonNull View itemView) {
			super(itemView);

			contactAvatarView = itemView.findViewById(R.id.contactItemAvatar);
			contactEmailView = itemView.findViewById(R.id.contactItemEmail);
			contactNameView = itemView.findViewById(R.id.contactItemName);
			contactNumberView = itemView.findViewById(R.id.contactItemNumber);
			contactDeleteButton = itemView.findViewById(R.id.contactItemDelete);
			contactCallButton = itemView.findViewById(R.id.contactCallButton);
		}
	}
}
