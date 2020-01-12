package tk.draganczuk.projekt.contacts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.PermissionChecker;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.util.StringUtil;
import tk.draganczuk.projekt.R;
import tk.draganczuk.projekt.db.AppDatabase;
import tk.draganczuk.projekt.db.Contact;
import tk.draganczuk.projekt.db.ContactDao;

public class EditContactActivity extends AppCompatActivity {

	private static final String TAG = "EditContactActivity";
	private static final int GALLERY_REQUEST_CODE = 1;
	private ContactViewModel contactViewModel;

	private Contact current;
	ExecutorService executor;
	private Uri selectedImage;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_contact);

		contactViewModel = ViewModelProviders
				.of(this)
				.get(ContactViewModel.class);

		executor = Executors.newSingleThreadExecutor();

		Switch contactGenderSwitch = findViewById(R.id.contactGenderSwitch);
		TextView male = findViewById(R.id.contactMaleText);
		TextView female = findViewById(R.id.contactFemaleText);

		contactGenderSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> updateGenderInfo(isChecked));

		male.setOnClickListener(v -> {
			updateGenderInfo(false);
			contactGenderSwitch.setChecked(false);
		});

		female.setOnClickListener(v -> {
			updateGenderInfo(true);
			contactGenderSwitch.setChecked(true);
		});


		if (getIntent().hasExtra("editing")) {

			TextView contactFirstName = findViewById(R.id.contactFirstName);
			TextView contactLastName = findViewById(R.id.contactLastName);
			TextView contactEmail = findViewById(R.id.contactEmail);
			TextView contactNumber = findViewById(R.id.contactNumber);


			ContactDao dao = AppDatabase.get(this).contactDao();
			int id = getIntent().getIntExtra("editing", -1);

			// Hack, bo baza danych nie lubi być na głównym wątku
			try {
				current = executor.submit(() -> dao.findById(id)).get();

				contactFirstName.setText(current.getFirstName());
				contactLastName.setText(current.getLastName());
				contactEmail.setText(current.getEmail());
				contactNumber.setText(current.getPhoneNumber());

				boolean checked = current.getGender().equalsIgnoreCase("female");

				contactGenderSwitch.setChecked(checked);
				updateGenderInfo(checked);

				selectedImage = Optional.ofNullable(current).map(Contact::getAvatar).map(Uri::parse).orElse(null);
				setAvatarImage(selectedImage);
			} catch (ExecutionException | InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			updateGenderInfo(false);
		}

	}

	private void setAvatarImage(Uri imagePath) {
		ImageButton avatarButton = findViewById(R.id.contactAvatarButton);

		Picasso.get().setLoggingEnabled(true);
		Picasso.get().setIndicatorsEnabled(true);

		Picasso.get()
				.load(imagePath)
				.resizeDimen(R.dimen.avatarDim, R.dimen.avatarDim)
				.centerCrop()
				.into(avatarButton);

		Log.d(TAG, String.format("setAvatarImage: imagePath%s", imagePath));
	}

	public void pickFromGallery(View view) {


		//Create an Intent with action as ACTION_PICK
		Intent intent = new Intent(Intent.ACTION_PICK);
		// Sets the type as image/*. This ensures only components of type image are selected
		intent.setType("image/*");
		//We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
		String[] mimeTypes = {"image/jpeg", "image/png", "image/*"};
		intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
		// Launching the Intent
		startActivityForResult(intent, GALLERY_REQUEST_CODE);
	}

	private void updateGenderInfo(boolean femaleSelected) {

		boolean defaultAvatar = !Optional.ofNullable(current).map(Contact::getAvatar).isPresent();

		TextView male = findViewById(R.id.contactMaleText);
		TextView female = findViewById(R.id.contactFemaleText);
		ImageButton avatarButton = findViewById(R.id.contactAvatarButton);

		if (femaleSelected) {
			male.setTextColor(0x80_FA_FA_FA);
			male.setPaintFlags(male.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));

			female.setTextColor(0xFF_FA_FA_FA);
			female.setPaintFlags(female.getPaintFlags() | (Paint.UNDERLINE_TEXT_FLAG));

			if (defaultAvatar) {
				avatarButton.setImageResource(R.drawable.woman);
			}
		} else {
			male.setTextColor(0xFF_FA_FA_FA);
			male.setPaintFlags(male.getPaintFlags() | (Paint.UNDERLINE_TEXT_FLAG));

			female.setTextColor(0x80_FA_FA_FA);
			female.setPaintFlags(female.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));

			if (defaultAvatar) {
				avatarButton.setImageResource(R.drawable.man);
			}
		}
	}

	public void onSaveButtonClick(View view) {

		TextView contactFirstName = findViewById(R.id.contactFirstName);
		TextView contactLastName = findViewById(R.id.contactLastName);
		TextView contactEmail = findViewById(R.id.contactEmail);
		TextView contactNumber = findViewById(R.id.contactNumber);
		Switch contactGenderChip = findViewById(R.id.contactGenderSwitch);


		String firstName = contactFirstName.getText().toString();
		String lastName = contactLastName.getText().toString();
		String email = contactEmail.getText().toString();
		String number = contactNumber.getText().toString();



		String avatar = Optional.ofNullable(selectedImage).map(Uri::toString).orElse(null);

		Contact contact = Contact.builder()
				.firstName(firstName)
				.lastName(lastName)
				.avatar(avatar)
				.email(email)
				.gender(contactGenderChip.isChecked() ? "female" : "male")
				.phoneNumber(number)
				.build();

		if (current != null) {
			contact.setId(current.getId());
			contactViewModel.update(contact);
		} else {
			contactViewModel.saveContact(contact);
			current = contact;
		}

		Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
	}

	public void onSaveAndExitButtonClick(View view) {
		onSaveButtonClick(view);
		finish();
	}

	public void onExitButtonClick(View view) {
		finish();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Result code is RESULT_OK only if the user selects an Image
		if (resultCode == Activity.RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {//data.getData returns the content URI for the selected Image
			selectedImage = data.getData();
			setAvatarImage(selectedImage);
		}
	}
}
