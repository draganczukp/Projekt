package tk.draganczuk.projekt.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Entity
@Data
@TypeConverters({Converters.class})
@Builder
public
class Reminder {
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "time")
    private LocalDateTime time;


}
