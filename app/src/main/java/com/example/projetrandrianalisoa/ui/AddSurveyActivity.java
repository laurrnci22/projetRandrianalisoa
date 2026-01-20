package com.example.projetrandrianalisoa.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
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
import com.example.projetrandrianalisoa.model.Question;
import com.example.projetrandrianalisoa.model.QuestionEntity;
import com.example.projetrandrianalisoa.model.ScoreEntity;
import com.example.projetrandrianalisoa.model.SurveyEntity;
import com.example.projetrandrianalisoa.tools.FileParser;

import java.util.ArrayList;
import java.util.List;

public class AddSurveyActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> questionTitles;
    private ArrayList<Question> questions;
    private ArrayAdapter<String> adapter;
    private AppDatabase db;             // Instance de la base Room

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_survey);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = AppDatabase.getInstance(this);

        listView = findViewById(R.id.listAddedQuestion);

        questions = new ArrayList<>();

        // Liste des titres de questions
        questionTitles = new ArrayList<>();

        // Adapter simple (un titre par ligne)
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, questionTitles);

        // Lier l’adapter au ListView
        listView.setAdapter(adapter);
    }

    /**
     * Méthode appelée lorsqu'on clique sur le bouton ajouter une question.
     */
    public void onAddQuestion(View view) {
        Intent intent = new Intent(this, AddQuestionActivity.class);
        dynamicLauncher.launch(intent);
    }

    /**
     * Méthode appelée lorsqu'on clique sur le bouton valider.
     */
    public void onValidate(View view) {
        TextView categoryView = findViewById(R.id.addCategory);
        String category = categoryView.getText().toString().trim();

        if (category.isEmpty()) {
            Toast.makeText(this, "Veuillez renseigner une catégorie avant de valider.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (questions.isEmpty()) {
            Toast.makeText(this, "Veuillez ajouter au moins une question avant de valider.", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            SurveyEntity survey = new SurveyEntity(category);
            long surveyId = db.surveyDao().insert(survey);

            for (Question question : questions) {
                if(question.getChoices().size() < 3) continue;

                QuestionEntity questionEntity = new QuestionEntity(
                        surveyId,
                        question.getLabel(),
                        question.getChoices().get(0),
                        question.getChoices().get(1),
                        question.getChoices().get(2),
                        question.getCorrectIndex()
                );

                db.questionDao().insert(questionEntity);
            }

            // Retourner le score à MainActivity sur le thread UI
            runOnUiThread(() -> {
                Intent result = new Intent();
                setResult(RESULT_OK, result);
                finish();
            });
        }).start();
    }

    /**
     * Méthode appelée lorsqu'on clique sur le bouton retour.
     */
    public void onCancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    // --------------------------
    // Launcher dynamique pour récupérer la question
    // --------------------------
    private final ActivityResultLauncher<Intent> dynamicLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {

                        String label = data.getStringExtra(AddQuestionActivity.QUESTION_LABEL);

                        String choice1 = data.getStringExtra(AddQuestionActivity.CHOICE_1);
                        String choice2 = data.getStringExtra(AddQuestionActivity.CHOICE_2);
                        String choice3 = data.getStringExtra(AddQuestionActivity.CHOICE_3);

                        int correctIndex = data.getIntExtra(AddQuestionActivity.CORRECT_ANSWER_INDEX, 0);

                        // Construire la liste des choix
                        List<String> choices = new ArrayList<>();
                        choices.add(choice1);
                        choices.add(choice2);
                        choices.add(choice3);

                        // Créer l'objet Question
                        Question question = new Question(label, choices, correctIndex - 1);

                        // Stocker la question complète
                        questions.add(question);

                        // Afficher uniquement le label
                        questionTitles.add(label);

                        adapter.notifyDataSetChanged();
                    }
                }
            }
    );
}