package cat.infoserveis.jordi.projectejordi;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.admin.SystemUpdatePolicy;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by jordi on 26/01/16.
 */
public class DatePickerJordi  implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    EditText _editText;
    private int _day;
    private int _month;
    private int _birthYear;
    private Context _context;

    public DatePickerJordi(Context context, int editTextViewID)
    {
        Activity act = (Activity)context;
        this._editText = (EditText)act.findViewById(editTextViewID);
        this._editText.setOnClickListener(this);
        this._context = context;

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        this._birthYear = c.get(Calendar.YEAR);
        this._month = c.get(Calendar.MONTH);
        this._day = c.get(Calendar.DAY_OF_MONTH);
        updateDisplay();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        _birthYear = year;
        _month = monthOfYear;
        _day = dayOfMonth;
        updateDisplay();
    }
    @Override
    public void onClick(View v) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog =  new DatePickerDialog(_context, this, mYear, mMonth, mDay);
        dialog.show();

    }

    private void updateDisplay() {

        _editText.setText(new StringBuilder()
                .append(_day).append("/").append(_month + 1).append("/").append(_birthYear).append(" "));
    }

    public int get_birthYear() {
        return _birthYear;
    }

    public int get_month() {
        return _month;
    }

    public int get_day() {
        return _day;
    }
}