package com.example.projetrandrianalisoa.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.projetrandrianalisoa.R;
import com.example.projetrandrianalisoa.database.AppDatabase;
import com.example.projetrandrianalisoa.model.SurveyEntity;
import com.example.projetrandrianalisoa.tools.DatabaseInitializer;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppDatabase db;

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

        db = AppDatabase.getInstance(this);

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
        int id = item.getItemId();

        if (id == R.id.menu_scores) {
            Intent intent = new Intent(this, ViewScoreActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.menu_reset) {
            new Thread(() -> {
                // Supprimer tous les scores
                db.scoreDao().deleteAllScores();

                // Mettre tous les questionnaires à completed = false
                List<SurveyEntity> surveys = db.surveyDao().getAll();
                for (SurveyEntity s : surveys) {
                    s.completed = false;
                }
                db.surveyDao().updateAll(surveys);

                // Afficher un message de confirmation sur le thread UI
                runOnUiThread(() ->
                        Toast.makeText(MainActivity.this, "Scores réinitialisés et questionnaires non complétés", Toast.LENGTH_SHORT).show()
                );
            }).start();
            return true;
        }
        else if (id == R.id.menu_admin) {

            Intent intent = new Intent(this, AdminActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.menu_quit) {
            // Sauvegarder les données avant de quitter
            //sauvegarderDonnees();
            finishAffinity(); // Ferme proprement toutes les activités de l'app
            return true;
        }

        return super.onOptionsItemSelected(item);
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