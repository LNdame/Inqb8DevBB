package cite.ansteph.beerly.view.beerlylover.registration;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;



import java.util.Calendar;
import java.util.Date;

import cite.ansteph.beerly.R;

/**
 * Created by loicstephan on 2017/02/26.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Use the current date as default date in picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month,day);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        AutoCompleteTextView mDOB = (AutoCompleteTextView) getActivity().findViewById(R.id.dateofbirth);

        String monthSt, day;
        if((month+1) <10)
        {
            monthSt ="0"+  String.valueOf(month+1);
        }else{monthSt = String.valueOf(month+1);}

        if(dayOfMonth<10)
        {
            day="0"+ String.valueOf(dayOfMonth);
        }else{day= String.valueOf(dayOfMonth);}

        if(isOlderthan18(year)){
            mDOB.setText(String.valueOf(year) +"/"+ monthSt +"/"+  day);
        }else{
            mDOB.setText("Must be 18 years old or older");
        }




    }



    boolean isOlderthan18(int year)
    {
        Calendar calendar = Calendar.getInstance();

       int currentyear = calendar.get(Calendar.YEAR);

        int age = currentyear - year;

        if (age>=18)
            return true;
        else
            return false;


    }
}
