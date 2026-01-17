package com.example.projetrandrianalisoa.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

    /**
     * Méthode appelée lorsqu'on clique sur le bouton pour voir les scores.
     * Ouvre l'activité ViewScoreActivity.
     */
    public void onViewScore(View view) {
        Intent intent = new Intent(this, ViewScoreActivity.class);
        startActivity(intent);
    }

    /**
     * Méthode appelée lorsqu'on clique sur le bouton pour réinitialiser les scores.
     */
    public void onResetScores(View view) {
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
    }

    /**
     * Méthode appelée lorsqu'on clique sur le bouton pour ajouter un questionnaire.
     */
    public void onAddSurvey(View view) {
        // Créer un EditText pour saisir le mot de passe
        EditText input = new EditText(this);
        input.setHint("Mot de passe administrateur");

        new AlertDialog.Builder(this)
                .setTitle("Accès administrateur")
                .setMessage("Veuillez entrer le mot de passe pour ajouter un questionnaire")
                .setView(input)
                .setPositiveButton("Valider", (dialog, which) -> {
                    String password = input.getText().toString().trim();
                    if ("MDP".equals(password)) { // Mot de passe fixe
                        // Mot de passe correct, ouvrir l'activité d'ajout
                        Intent intent = new Intent(MainActivity.this, AddSurveyActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Mot de passe incorrect", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Annuler", null)
                .show();
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