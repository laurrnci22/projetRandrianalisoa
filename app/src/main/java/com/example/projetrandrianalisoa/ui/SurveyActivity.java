package com.example.projetrandrianalisoa.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projetrandrianalisoa.R;
import com.example.projetrandrianalisoa.database.AppDatabase;
import com.example.projetrandrianalisoa.model.QuestionEntity;
import com.example.projetrandrianalisoa.model.SurveyEntity;

import java.util.ArrayList;
import java.util.List;

public class SurveyActivity extends AppCompatActivity {
    private ListView listView;
    private AppDatabase db;
    private List<SurveyEntity> surveys = new ArrayList<>();

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

        /*db = AppDatabase.getInstance(this);

        SurveyEntity survey1 = new SurveyEntity("Géographie");
        long surveyId = db.surveyDao().insert(survey1);

        List<QuestionEntity> questions = new ArrayList<>();

        questions.add(new QuestionEntity(
                surveyId,
                "Capitale de l'Italie ?",
                "Rome",
                "Milan",
                "Naples",
                0
        ));

        questions.add(new QuestionEntity(
                surveyId,
                "Capitale de l'Espagne ?",
                "Madrid",
                "Barcelone",
                "Valence",
                0
        ));

        db.questionDao().insertAll(questions);*/

        listView = findViewById(R.id.listViewCategories);
        db = AppDatabase.getInstance(this);

        // Adapter pour la ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, new ArrayList<>());
        listView.setAdapter(adapter);

        // Observer les surveys
        // Lecture des surveys et questions dans un thread
        new Thread(() -> {
            surveys = db.surveyDao().getAvailableSurveys();

            List<String> displayList = new ArrayList<>();

            // Lire le nombre de questions pour chaque survey
            for (SurveyEntity survey : surveys) {
                int count = db.questionDao().getQuestionCountBySurvey(survey.id);
                displayList.add(survey.category + " (" + count + " question(s))");
            }

            // Mettre à jour la ListView sur le thread UI
            runOnUiThread(() -> {
                adapter.clear();
                adapter.addAll(displayList);
                adapter.notifyDataSetChanged();
            });

        }).start();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            SurveyEntity selectedSurvey = surveys.get(position);
            Intent intent = new Intent(this, QuizActivity.class);
            intent.putExtra("surveyId", selectedSurvey.id);
            dynamicLauncher.launch(intent);
        });
    }

    // --------------------------
    // Launcher dynamique pour récupérer les résultats
    // --------------------------
    private final ActivityResultLauncher<Intent> dynamicLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        int score = data.getIntExtra("score", 0);
                        Toast.makeText(this, "Score final : " + score, Toast.LENGTH_LONG).show();
                    }
                }
            }
    );
}
