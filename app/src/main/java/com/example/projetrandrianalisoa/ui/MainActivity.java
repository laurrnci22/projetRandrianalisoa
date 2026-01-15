package com.example.projetrandrianalisoa.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.projetrandrianalisoa.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Récupère la Toolbar et la définit comme ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Masque le titre par défaut de la Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    /**
     * Méthode appelée lorsqu'on clique sur le bouton pour répondre à un questionnaire.
     * Ouvre l'activité SurveyActivity.
     */
    public void onAnswerSurvey(View view) {
        Intent intent = new Intent(this, SurveyActivity.class);
        startActivity(intent);
    }

    ////////////// Gestion des menus ///////////////

    /**
     * Crée le menu de l'activité.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Gère les actions lorsqu'un item du menu est sélectionné.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pour l'instant, aucun traitement spécifique sur les items du menu
        return true;
    }

    ////////////////////////////////////////////////

    /**
     * Méthode appelée lorsqu'on clique sur le bouton annuler.
     * Ferme l'activité et renvoie RESULT_CANCELED à l'appelant.
     */
    public void onCancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}