package tk.draganczuk.projekt.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import tk.draganczuk.projekt.R;

public class ContactsActivity extends AppCompatActivity {
	private ContactViewModel contactViewModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), EditContactActivity.class)));

		contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);

		RecyclerView recyclerView = findViewById(R.id.contactsRecycler);
		
		ContactAdapter contactAdapter = new ContactAdapter(contactViewModel);
		recyclerView.setAdapter(contactAdapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		contactViewModel.getAllContacts().observe(this, contactAdapter::setContacts);

		TextView filterText = findViewById(R.id.contactsFilter);

		filterText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int start,
			                              int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
				contactAdapter.filter(charSequence.toString());
			}

			@Override
			public void afterTextChanged(Editable editable) {
			}
		});

	}


}
