package helloandroid.ut3.mini_projet.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import helloandroid.ut3.mini_projet.R;
import helloandroid.ut3.mini_projet.services.PhotoService;

public class DetailsActivity extends AppCompatActivity {

    String restaurantId;

    private Button btnAvis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        PhotoService photoService = new PhotoService(getApplicationContext());

        Intent intent = getIntent();
        String[] photos = intent.getStringArrayExtra("Photos");
        Bundle extras = intent.getExtras();
        Map<String, ArrayList<Long>> horaires = (Map<String, ArrayList<Long>>) extras.getSerializable("Horaires");

        ImageView image = findViewById(R.id.restaurantImage);
        TextView title = findViewById(R.id.restaurantName);
        TextView type = findViewById(R.id.restaurantType);
        TextView address = findViewById(R.id.restaurantAddress);
        TextView desc = findViewById(R.id.restaurantDescription);
        TextView nextHoraire = findViewById(R.id.restaurantHoraire);
        TextView note = findViewById(R.id.restaurantNote);


        restaurantId = intent.getStringExtra("Id");

        photoService.setPhoto(photos[0],image);
        title.setText(intent.getStringExtra("Titre"));
        type.setText(intent.getStringExtra("Type"));
        address.setText(intent.getStringExtra("Adresse"));
        desc.setText(intent.getStringExtra("Description"));
        nextHoraire.setText(intent.getStringExtra("HoraireDuJour"));
        note.setText(intent.getStringExtra("Note"));




        FloatingActionButton btnBack = findViewById(R.id.backButton);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    findViewById(R.id.reviewBtn).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
            intent.putExtra("restaurantId", restaurantId);
            startActivity(intent);
        }
    });
        Button reservationButton = findViewById(R.id.reservationButton);
        reservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this, ReservationActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("Horaires", (Serializable) horaires);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
    }


    public void showHorairesPopup(View view) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.horaires_popup);
        dialog.setTitle("Horaires");

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        Bundle extras = getIntent().getExtras();
        Map<String, ArrayList<Long>> horaires = (Map<String, ArrayList<Long>>) extras.getSerializable("Horaires");

        List<String> horaireItems = new ArrayList<>();

        String[] joursDeLaSemaine = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};

        for (String jour : joursDeLaSemaine) {
            ArrayList<Long> value = horaires.get(jour);
            if (value != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(jour).append(": ");

                // Vérifier s'il y a plus d'un horaire d'ouverture pour le jour actuel
                if (value.size() > 2) {
                    for (int i = 0; i < value.size(); i += 2) {
                        sb.append(String.format("%02d", value.get(i))).append("h - ").append(String.format("%02d", value.get(i + 1))).append("h");

                        if (i < value.size() - 2) {
                            sb.append(", ");
                        }
                    }
                } else {
                    sb.append(String.format("%02d", value.get(0))).append("h - ").append(String.format("%02d", value.get(1))).append("h");
                }

                horaireItems.add(sb.toString());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.horaire_item, R.id.horaireText, horaireItems) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView bullet = view.findViewById(R.id.bullet);
                bullet.setTextColor(getResources().getColor(R.color.black));
                return view;
            }
        };

        ListView horairesListView = dialog.findViewById(R.id.horairesListView);
        horairesListView.setAdapter(adapter);

        dialog.show();
    }




}