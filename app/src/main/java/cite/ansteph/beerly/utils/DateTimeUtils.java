package cite.ansteph.beerly.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    public static String dateTostringsimple(Date date)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date cdate = date;
            String dateTime = dateFormat.format(cdate);
            return dateTime;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
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


    public static String datetoStringShortDateAndTime(String sdate)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d, yyyy HH:mm");

        Date cdate = stringToDate(sdate);

        try {

            String dateTime = dateFormat.format(cdate);
            return dateTime;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }


    public static int countDays (String createdDateString, String expireDateString)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        Date createdConvertedDate = null, expireCovertedDate = null, todayWithZeroTime = null;
        try {
            createdConvertedDate = dateFormat.parse(createdDateString);
            expireCovertedDate = dateFormat.parse(expireDateString);

            Date today = new Date();

            todayWithZeroTime = dateFormat.parse(dateFormat.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int cYear = 0, cMonth = 0, cDay = 0;

        if (createdConvertedDate.after(todayWithZeroTime)) {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(createdConvertedDate);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);

        } else {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(todayWithZeroTime);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);
        }

        Calendar eCal = Calendar.getInstance();
        eCal.setTime(expireCovertedDate);

        int eYear = eCal.get(Calendar.YEAR);
        int eMonth = eCal.get(Calendar.MONTH);
        int eDay = eCal.get(Calendar.DAY_OF_MONTH);

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.clear();
        date1.set(cYear, cMonth, cDay);
        date2.clear();
        date2.set(eYear, eMonth, eDay);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);

        return (int) dayCount ;
    }

}
