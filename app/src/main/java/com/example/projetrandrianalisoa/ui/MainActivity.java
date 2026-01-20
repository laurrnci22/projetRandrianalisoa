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
import androidx.lifecycle.ViewModelProvider;

import com.example.projetrandrianalisoa.R;
import com.example.projetrandrianalisoa.tools.DatabaseInitializer;
import com.example.projetrandrianalisoa.tools.PopUpHelper;
import com.example.projetrandrianalisoa.viewmodel.MainViewModel;


public class MainActivity extends AppCompatActivity {

    // Intermediaire entre les entités et l'UI
    private MainViewModel viewModel;

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

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // Récupère la Toolbar et la définit comme ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Masque le titre par défaut de la Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        new Thread(() -> DatabaseInitializer.init(this)).start();
    }

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
        int id = item.getItemId();

        // Pour voir les scores
        if (id == R.id.menu_scores) {
            Intent intent = new Intent(this, ViewScoreActivity.class);
            startActivity(intent);
            return true;
        }

        // Pour réinitialiser les scores
        else if (id == R.id.menu_reset) {
            viewModel.resetScoresAndSurveys(() ->
                runOnUiThread(() ->
                    PopUpHelper.showMessage(MainActivity.this, "Scores réinitialisés avec succès !")
                )
            );
            return true;
        }

        // Pour l'espace admin, pour ajouter un questionnaire
        else if (id == R.id.menu_admin) {
            Intent intent = new Intent(this, AdminActivity.class);
            startActivity(intent);
            return true;
        }

        // Pour quitter l'application
        else if (id == R.id.menu_quit) {
            // Ferme proprement toutes les activités de l'app
            finishAffinity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Méthode appelée lorsqu'on clique sur le bouton pour répondre à un questionnaire.
     * Ouvre l'activité SurveyActivity.
     */
    public void onAnswerSurvey(View view) {
        Intent intent = new Intent(this, SurveyActivity.class);
        startActivity(intent);
    }
}