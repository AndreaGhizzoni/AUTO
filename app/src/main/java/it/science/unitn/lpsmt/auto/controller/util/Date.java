package it.science.unitn.lpsmt.auto.controller.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * TODO add doc
 */
public final class Date {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss",
        Locale.getDefault()
    );

    /**
     * TODO add doc
     * @param d
     * @return
     */
    public static String getStringFromDate( java.util.Date d ){
        return d== null? null : dateFormat.format(d);
    }

    public static String getStringFromDate( java.util.Date d, String format ){
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return d == null? null : sdf.format(d);
    }

    /**
     * TODO add doc
     * @param s
     * @return
     */
    public static java.util.Date getDateFromString( String s ){
        try {
            if( s == null ) return null;
            else return dateFormat.parse(s);
        } catch (ParseException e) {
            Log.e(Date.class.getSimpleName(), e.getMessage());
            return null;
        }
    }
}
