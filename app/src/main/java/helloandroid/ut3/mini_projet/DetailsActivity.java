package helloandroid.ut3.mini_projet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import helloandroid.ut3.mini_projet.services.PhotoService;
import helloandroid.ut3.mini_projet.services.RestaurantsService;

public class DetailsActivity extends AppCompatActivity {

    String restaurantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        PhotoService photoService = new PhotoService(getApplicationContext());
        Intent intent = getIntent();
        TextView title = findViewById(R.id.restaurantName);
        TextView type = findViewById(R.id.restaurantType);
        TextView address = findViewById(R.id.restaurantAddress);
        TextView desc = findViewById(R.id.restaurantDescription);
        TextView horaireDuJour = findViewById(R.id.restaurantHoraire);
        TextView note = findViewById(R.id.restaurantNote);

        ImageView image = findViewById(R.id.restaurantImage);

        restaurantId = intent.getStringExtra("Id");
        title.setText(intent.getStringExtra("Titre"));
        type.setText(intent.getStringExtra("Type"));
        address.setText(intent.getStringExtra("Adresse"));
        desc.setText(intent.getStringExtra("Description"));
        horaireDuJour.setText(intent.getStringExtra("HoraireDuJour"));
        note.setText(intent.getStringExtra("Note"));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Map<String, ArrayList<Long>> horaires = (Map<String, ArrayList<Long>>) extras.getSerializable("Horaires");

        }

        String[] photos = intent.getStringArrayExtra("Photos");
        photoService.setPhoto(photos[0],image);
        FloatingActionButton btnBack = findViewById(R.id.backToMain);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void onClickReviewBtn(View v) {
        Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
        intent.putExtra("restaurantId", restaurantId);
        startActivity(intent);
    }
    public void showHorairesPopup(View view) {
        // Créer un Dialog
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.horaires_popup);
        dialog.setTitle("Horaires");

        // Définir la largeur et la hauteur du Dialog
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        // Récupérer la Map des horaires
        Bundle extras = getIntent().getExtras();
        Map<String, ArrayList<Long>> horaires = (Map<String, ArrayList<Long>>) extras.getSerializable("Horaires");

        // Préparer la liste d'éléments pour l'adapter
        List<String> horaireItems = new ArrayList<>();

        // Ajouter un tableau de chaînes contenant les noms des jours de la semaine dans l'ordre souhaité
        String[] joursDeLaSemaine = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};

        // Modifier la boucle for pour parcourir le tableau joursDeLaSemaine et afficher les horaires correspondants
        for (String jour : joursDeLaSemaine) {
            ArrayList<Long> value = horaires.get(jour);
            if (value != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(jour).append(": ");

                // Vérifier s'il y a plus d'un horaire d'ouverture pour le jour actuel
                if (value.size() > 2) {
                    for (int i = 0; i < value.size(); i += 2) {
                        sb.append(String.format("%02d", value.get(i))).append("h - ").append(String.format("%02d", value.get(i + 1))).append("h");

                        // Ajouter une virgule et un espace entre les horaires d'ouverture, sauf pour le dernier
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

        // Créer un ArrayAdapter pour la ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.horaire_item, R.id.horaireText, horaireItems) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView bullet = view.findViewById(R.id.bullet);
                bullet.setTextColor(getResources().getColor(R.color.black)); // Changer la couleur de la puce ici
                return view;
            }
        };

        // Remplir la ListView avec l'adapter
        ListView horairesListView = dialog.findViewById(R.id.horairesListView);
        horairesListView.setAdapter(adapter);

        // Afficher le Dialog
        dialog.show();
    }




}