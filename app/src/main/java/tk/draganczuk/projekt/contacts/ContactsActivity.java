package tk.draganczuk.projekt.contacts;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
	}


}
