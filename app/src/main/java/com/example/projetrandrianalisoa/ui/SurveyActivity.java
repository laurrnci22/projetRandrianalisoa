package com.example.projetrandrianalisoa.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projetrandrianalisoa.R;
import com.example.projetrandrianalisoa.database.AppDatabase;
import com.example.projetrandrianalisoa.model.SurveyEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * SurveyActivity affiche la liste des questionnaires disponibles.
 */
public class SurveyActivity extends AppCompatActivity {

    // ------------------------
    // Constantes pour les clés de données passées entre activités
    // ------------------------
    public final static String SURVEY_ID = "com.example.projetrandrianalisoa.SURVEY_ID";

    private ListView listView;
    private AppDatabase db;
    private List<SurveyEntity> currentSurveys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_survey);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listView = findViewById(R.id.listViewCategories);
        db = AppDatabase.getInstance(this);

        // Adapter pour la ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, new ArrayList<>());
        listView.setAdapter(adapter);

        // Observer les questionnaires disponibles via LiveData
        db.surveyDao().getAvailableSurveys().observe(this, surveys -> {
            currentSurveys.clear();
            currentSurveys.addAll(surveys);

            List<String> displayList = new ArrayList<>();
            for (SurveyEntity survey : surveys) {
                int count = db.questionDao().getQuestionCountBySurvey(survey.id);
                displayList.add(survey.category + " (" + count + " question(s))");
            }

            adapter.clear();
            adapter.addAll(displayList);
            adapter.notifyDataSetChanged();
        });

        // Gestion du clic sur un questionnaire
        listView.setOnItemClickListener((parent, view, position, id) -> {
            SurveyEntity selectedSurvey = currentSurveys.get(position);
            Intent intent = new Intent(this, QuizActivity.class);
            intent.putExtra(SURVEY_ID, selectedSurvey.id);
            dynamicLauncher.launch(intent);
        });
    }

    /**
     * Méthode appelée lorsqu'on clique sur le bouton retour.
     */
    public void onCancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    // --------------------------
    // Launcher dynamique pour récupérer les résultats du quiz
    // --------------------------
    private final ActivityResultLauncher<Intent> dynamicLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        int score = data.getIntExtra(QuizActivity.SCORE, 0);
                        Toast.makeText(this, "Score final : " + score, Toast.LENGTH_LONG).show();
                    }
                }
            }
    );
}
