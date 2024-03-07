package helloandroid.ut3.mini_projet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ReservationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_reservation);

        Button confirmButton = findViewById(R.id.button_confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFormData();
            }
        });

    }

    private void getFormData() {
        EditText firstNameEditText = findViewById(R.id.firstName);
        EditText lastNameEditText = findViewById(R.id.lastName);

        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();

        // Ajoutez les autres champs de formulaire ici (par exemple, date, heure, etc.)

        // Créez un Intent pour envoyer les données à l'activité de confirmation
        Intent intent = new Intent(this, ConfirmReservation.class);
        intent.putExtra("firstName", firstName);
        intent.putExtra("lastName", lastName);
        // Ajoutez les autres données de formulaire à l'Intent

        startActivity(intent);
    }

}