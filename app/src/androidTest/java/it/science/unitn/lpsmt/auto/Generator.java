package it.science.unitn.lpsmt.auto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class Generator {
    private Generator(){}

    public static Date getTomorrowDate(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, 1);
        return c.getTime();
    }

    public static Date getDate( String yyyymmdd ){
        SimpleDateFormat s = new SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault()
        );
        try {
            return s.parse(yyyymmdd);
        } catch (ParseException e) {
            return null;
        }
    }
}
