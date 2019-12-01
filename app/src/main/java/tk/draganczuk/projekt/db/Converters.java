package tk.draganczuk.projekt.db;

import androidx.room.TypeConverter;

import java.time.LocalDateTime;

public class Converters {

    @TypeConverter
    public String localDateTimeToString(LocalDateTime time){
        return time.toString();
    }

    @TypeConverter
    public LocalDateTime fromString(String s){
        return LocalDateTime.parse(s);
    }
}
