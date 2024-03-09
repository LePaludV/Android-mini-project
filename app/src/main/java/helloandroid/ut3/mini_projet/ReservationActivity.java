package helloandroid.ut3.mini_projet;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("MissingInflatedId")
public class ReservationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_reservation);

        NumberPicker numberPicker = findViewById(R.id.number_picker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);
        numberPicker.setValue(1);

        LinearLayout dateLayout = findViewById(R.id.date_layout);
        dateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarView calendarView = new CalendarView(getApplicationContext());
                DatePickerFragment fragment = DatePickerFragment.newInstance(calendarView, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);
                        TextView selectedDateTextView = findViewById(R.id.selected_date);
                        selectedDateTextView.setText(selectedDate);
                    }
                });
                fragment.show(getSupportFragmentManager(), "date_picker");
            }
        });

        LinearLayout timeLayout  = findViewById(R.id.time_layout);
        timeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment fragment = TimePickerFragment.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                        TextView selectedTimeTextView = findViewById(R.id.selected_time);
                        selectedTimeTextView.setText(selectedTime);
                    }
                });
                fragment.show(getSupportFragmentManager(), "time_picker");
            }
        });

        Button confirmButton = findViewById(R.id.button_confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFormData();
            }
        });

        Button returnButton = findViewById(R.id.button_return);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean validateForm() {
        EditText firstNameEditText = findViewById(R.id.firstName);
        EditText lastNameEditText = findViewById(R.id.lastName);
        TextView selectedDateTextView = findViewById(R.id.selected_date);
        TextView selectedTimeTextView = findViewById(R.id.selected_time);

        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String selectedDate = selectedDateTextView.getText().toString();
        String selectedTime = selectedTimeTextView.getText().toString();

        List<String> errors = new ArrayList<>();

        if (firstName.isEmpty()) {
            firstNameEditText.setError("Veuillez entrer un prénom");
            errors.add("Veuillez entrer un prénom");
        }

        if (lastName.isEmpty()) {
            lastNameEditText.setError("Veuillez entrer un nom");
            errors.add("Veuillez entrer un nom");
        }

        if (selectedDate.equals("Date sélectionnée")) {
            errors.add("Veuillez sélectionner une date");
        }

        if (selectedTime.equals("Heure sélectionnée")) {
            errors.add("Veuillez sélectionner une heure");
        }

        if (!errors.isEmpty()) {
            showErrors(errors);
            return false;
        }

        return true;
    }

    private void showErrors(List<String> errors) {
        if (!errors.isEmpty()) {
            for (String error : errors){
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void getFormData() {
        if (validateForm()) {
            EditText lastNameEditText = findViewById(R.id.lastName);
            TextView selectedDateTextView = findViewById(R.id.selected_date);
            TextView selectedTimeTextView = findViewById(R.id.selected_time);
            NumberPicker peopleNumberPicker = findViewById(R.id.number_picker);

            String lastName = lastNameEditText.getText().toString();
            String selectedDate = selectedDateTextView.getText().toString();
            String selectedTime = selectedTimeTextView.getText().toString();
            int peopleNumber = peopleNumberPicker.getValue();

            Intent intent = new Intent(this, ConfirmReservation.class);
            intent.putExtra("LastName", lastName);
            intent.putExtra("Date", selectedDate);
            intent.putExtra("Time", selectedTime);
            intent.putExtra("People", peopleNumber);

            startActivity(intent);
        }
    }


}