package tk.draganczuk.projekt.db;

import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Contact {

	@PrimaryKey(autoGenerate = true)
	private int id;

	@Nullable
	private String avatar;

	private String firstName;

	private String lastName;

	private String phoneNumber;

	private String email;

	private String gender;

}
