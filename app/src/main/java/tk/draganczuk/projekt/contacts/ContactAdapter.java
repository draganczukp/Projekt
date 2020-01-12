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

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import lombok.RequiredArgsConstructor;
import tk.draganczuk.projekt.R;
import tk.draganczuk.projekt.db.Contact;

@RequiredArgsConstructor
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

	private static final String TAG = "ContactAdapter";

	private List<Contact> contacts;
	private final ContactViewModel contactViewModel;

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
		Contact contact = contacts.get(position);

		ImageView contactAvatarView = holder.contactAvatarView;
		TextView contactNameView = holder.contactNameView;
		TextView contactNumberView = holder.contactNumberView;
		TextView contactEmailView = holder.contactEmailView;
		ImageButton contactDeleteButton = holder.contactDeleteButton;

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

		holder.itemView.setOnClickListener(
				(view) -> {

					Intent editIntent = new Intent(view.getContext(), EditContactActivity.class);
					Bundle bundle = new Bundle();

					bundle.putInt("editing", contact.getId());

					editIntent.putExtras(bundle);

					view.getContext().startActivity(editIntent);

				});
	}

	@Override
	public int getItemCount() {
		return contacts == null ? 0 : contacts.size();
	}

	void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
		notifyDataSetChanged();
	}

	class ViewHolder extends RecyclerView.ViewHolder {
		TextView contactNameView;
		TextView contactNumberView;
		TextView contactEmailView;
		ImageView contactAvatarView;
		ImageButton contactDeleteButton;


		ViewHolder(@NonNull View itemView) {
			super(itemView);

			contactAvatarView = itemView.findViewById(R.id.contactItemAvatar);
			contactEmailView = itemView.findViewById(R.id.contactItemEmail);
			contactNameView = itemView.findViewById(R.id.contactItemName);
			contactNumberView = itemView.findViewById(R.id.contactItemNumber);
			contactDeleteButton = itemView.findViewById(R.id.contactItemDelete);
		}
	}
}
