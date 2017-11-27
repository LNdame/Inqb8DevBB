package cite.ansteph.beerly.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by loicstephan on 2017/11/27.
 */

public class DateTimeUtils {




    public static Date  stringToDate(String sdate)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(sdate);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void dateTostringsimple(Date date)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date cdate = date;
            String dateTime = dateFormat.format(cdate);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String datetoStringShort(String sdate)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");

        Date cdate = stringToDate(sdate);

        try {

            String dateTime = dateFormat.format(cdate);
            return dateTime;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

}
