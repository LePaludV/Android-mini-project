package helloandroid.ut3.mini_projet;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private CalendarView calendarView;
    private DatePickerDialog.OnDateSetListener listener;
    private long minDate = -1;

    public static DatePickerFragment newInstance(CalendarView calendarView, DatePickerDialog.OnDateSetListener listener) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.calendarView = calendarView;
        fragment.listener = listener;
        return fragment;
    }

    public void setMinDate(long minDate) {
        this.minDate = minDate;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
        DatePicker datePicker = dialog.getDatePicker();
        if (minDate != -1) {
            datePicker.setMinDate(minDate);
        }
        return dialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        if (listener != null) {
            listener.onDateSet(view, year, month, dayOfMonth);
        }
    }
}

