package helloandroid.ut3.mini_projet;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ConfirmReservation extends AppCompatActivity {

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_reservation);

        TextView confirmText = findViewById(R.id.confirmText);

        Intent intent = getIntent();
        String lastName = intent.getStringExtra("LastName");
        String date = intent.getStringExtra("Date");
        String time = intent.getStringExtra("Time");

        int peopleNumber = intent.getIntExtra("People", 1);
        String people = (peopleNumber == 1) ? "personne" : "personnes";

        String confirmedMessage = String.format("Votre réservation au nom de %s le %s à %s pour %d %s a bien été prise en compte",
                lastName, date, time, peopleNumber, people);

        confirmText.setText(confirmedMessage);

    }

}