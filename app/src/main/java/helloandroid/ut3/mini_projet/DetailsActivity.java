package helloandroid.ut3.mini_projet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int id = getIntent().getIntExtra("id", -1);

        setContentView(R.layout.activity_details);

        //TODO get restaurant infos with id
    }

    public void onClickReserver(View view) {
        //TODO change activity
    }
}