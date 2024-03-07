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

        Intent intent = getIntent();
        TextView firstNameView = findViewById(R.id.firstName_confirmed);
        TextView lastNameView = findViewById(R.id.lastName_confirmed);

        firstNameView.setText(intent.getStringExtra("firstName"));
        lastNameView.setText(intent.getStringExtra("lastName"));



    }

}