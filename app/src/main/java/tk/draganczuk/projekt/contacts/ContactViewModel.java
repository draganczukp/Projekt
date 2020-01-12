package tk.draganczuk.projekt.contacts;

import android.app.Application;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import tk.draganczuk.projekt.db.AppDatabase;
import tk.draganczuk.projekt.db.Contact;
import tk.draganczuk.projekt.db.ContactDao;

public class ContactViewModel extends AndroidViewModel {

	private final ContactDao contactDao;
	private final ExecutorService executorService;

	public ContactViewModel(@NonNull Application application) {
		super(application);

		contactDao = AppDatabase.get(application.getApplicationContext()).contactDao();
		executorService = Executors.newSingleThreadExecutor();

	}

	public LiveData<List<Contact>> getAllContacts(){
		return contactDao.getAll();
	}

	void saveContact(Contact contact){
		executorService.execute(()->contactDao.insertAll(contact));
	}

	void deleteContact(Contact contact){
		executorService.execute(() -> contactDao.delete(contact));
	}

	public void update(Contact contact) {
		executorService.execute(() -> contactDao.update(contact));
	}
}
