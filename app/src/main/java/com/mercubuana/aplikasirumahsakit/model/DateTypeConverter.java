package com.mercubuana.aplikasirumahsakit.model;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.FormatterClosedException;
import java.util.Locale;

import androidx.room.TypeConverter;

public class DateTypeConverter {

    @TypeConverter
    public static Date stringToDate (String value) {
        if (value==null){
            return null;
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat
                    ("dd-MM-yyyy",
                    Locale.US);
            formatter.setLenient(false);
            ParsePosition pos = new ParsePosition(0);
            Date result;
            result = formatter.parse(value, pos);
            if (pos.getIndex()<value.length()){
                throw new FormatterClosedException();
            }
            return result;
        }

    }

    @TypeConverter
    public static String dateToString (Date value) {
        if (value==null){
            return null;
        } else {
            String result = "";
            SimpleDateFormat formatter = new
                    SimpleDateFormat("dd-MM-yyyy");
            result = formatter.format(value);
            return result;
        }
    }
}
