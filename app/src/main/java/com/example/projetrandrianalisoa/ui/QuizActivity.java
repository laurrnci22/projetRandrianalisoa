package com.example.projetrandrianalisoa.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView textCategory, textQuestionNumber, textQuestion;
    private RadioGroup radioGroup;
    private RadioButton choice1, choice2, choice3;
    private Button buttonValidate, buttonStop;

    private List<Question> questions;
    private int currentIndex = 0;
    private int score = 0;
    private String category;

    private AppDatabase db;
    private long surveyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textCategory = findViewById(R.id.textCategory);
        textQuestionNumber = findViewById(R.id.textQuestionNumber);
        textQuestion = findViewById(R.id.textQuestion);
        radioGroup = findViewById(R.id.radioGroupChoices);
        choice1 = findViewById(R.id.radioChoice1);
        choice2 = findViewById(R.id.radioChoice2);
        choice3 = findViewById(R.id.radioChoice3);
        buttonValidate = findViewById(R.id.buttonValidate);
        buttonStop = findViewById(R.id.buttonStop);

        db = AppDatabase.getInstance(this);

        // Récupérer l’ID du survey depuis SurveyActivity
        surveyId = getIntent().getLongExtra("surveyId", -1);
        if (surveyId == -1) {
            Toast.makeText(this, "Erreur : aucun questionnaire sélectionné", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Lire la catégorie
        SurveyEntity surveyEntity = db.surveyDao().getById(surveyId);
        if (surveyEntity == null) {
            Toast.makeText(this, "Erreur : questionnaire introuvable", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        category = surveyEntity.category;
        textCategory.setText(category);

        // Charger les questions depuis Room
        List<QuestionEntity> entities = db.questionDao().getBySurvey(surveyId);
        if (entities.isEmpty()) {
            Toast.makeText(this, "Aucune question dans ce questionnaire", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        questions = new ArrayList<>();
        for (QuestionEntity qe : entities) {
            List<String> choices = new ArrayList<>();
            choices.add(qe.choice1);
            choices.add(qe.choice2);
            choices.add(qe.choice3);
            questions.add(new Question(qe.label, choices, qe.correctIndex));
        }

        Collections.shuffle(questions); // Mélanger les questions

        showQuestion();

        buttonValidate.setOnClickListener(v -> validateAnswer());
        buttonStop.setOnClickListener(v -> stopQuiz());
    }

    private void showQuestion() {
        if (currentIndex >= questions.size()) {
            endQuiz();
            return;
        }

        Question q = questions.get(currentIndex);
        textQuestionNumber.setText("Question " + (currentIndex + 1) + "/" + questions.size());
        textQuestion.setText(q.getLabel());
        List<String> choices = q.getChoices();
        choice1.setText(choices.get(0));
        choice2.setText(choices.get(1));
        choice3.setText(choices.get(2));

        radioGroup.clearCheck();
    }

    private void validateAnswer() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Veuillez sélectionner une réponse", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedIndex = -1;
        if (selectedId == choice1.getId()) selectedIndex = 0;
        else if (selectedId == choice2.getId()) selectedIndex = 1;
        else if (selectedId == choice3.getId()) selectedIndex = 2;

        if (selectedIndex == questions.get(currentIndex).getCorrectIndex()) {
            score++;
        }

        currentIndex++;
        showQuestion();
    }

    private void stopQuiz() {
        score = 0;
        endQuiz();
    }

    private void endQuiz() {
        new Thread(() -> {
            ScoreEntity s = new ScoreEntity();
            s.surveyId = surveyId;
            s.score = score;
            s.timestamp = System.currentTimeMillis();
            db.scoreDao().insertScore(s);

            SurveyEntity survey = db.surveyDao().getById(surveyId);
            survey.completed = true;
            db.surveyDao().update(survey);

            // Retourner le score à SurveyActivity (doit se faire sur le thread UI)
            runOnUiThread(() -> {
                Intent result = new Intent();
                result.putExtra("score", score);
                setResult(RESULT_OK, result);
                finish();
            });
        }).start();
    }
}
