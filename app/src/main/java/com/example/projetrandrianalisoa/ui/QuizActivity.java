package com.example.projetrandrianalisoa.ui;

import android.content.Intent;
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

/**
 * QuizActivity gère l'affichage et la logique d'un questionnaire.
 */
public class QuizActivity extends AppCompatActivity {

    // ------------------------
    // Constantes pour les clés de données passées entre activités
    // ------------------------
    public final static String SCORE = "com.example.projetrandrianalisoa.SCORE";

    // ------------------------
    // Composants de l'UI
    // ------------------------
    private TextView textCategory, textQuestionNumber, textQuestion;
    private RadioGroup radioGroup;
    private RadioButton choice1, choice2, choice3;
    private Button buttonValidate, buttonStop;

    // ------------------------
    // Données du quiz
    // ------------------------
    private List<Question> questions;   // Liste des questions
    private int currentIndex = 0;       // Question courante
    private int score = 0;              // Score accumulé
    private String category;            // Catégorie du quiz
    private AppDatabase db;             // Instance de la base Room
    private long surveyId;              // ID du questionnaire

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

        // Récupération des composants UI
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

        // Récupérer l’ID du questionnaire depuis l'intent
        surveyId = getIntent().getLongExtra(SurveyActivity.SURVEY_ID, -1);
        if (surveyId == -1) {
            Toast.makeText(this, "Erreur : aucun questionnaire sélectionné", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Récupérer la catégorie du questionnaire
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

        // Conversion en modèle Question et mélange
        questions = new ArrayList<>();
        for (QuestionEntity qe : entities) {
            List<String> choices = new ArrayList<>();
            choices.add(qe.choice1);
            choices.add(qe.choice2);
            choices.add(qe.choice3);
            questions.add(new Question(qe.label, choices, qe.correctIndex));
        }
        Collections.shuffle(questions);

        // Afficher la première question
        showQuestion();

        // Gestion des boutons
        buttonValidate.setOnClickListener(v -> validateAnswer());
        buttonStop.setOnClickListener(v -> stopQuiz());
    }

    /**
     * Affiche la question courante et les choix.
     * Si toutes les questions ont été répondues, termine le quiz.
     */
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

    /**
     * Valide la réponse sélectionnée et met à jour le score.
     * Passe à la question suivante.
     */
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

    /**
     * Arrête le quiz prématurément et renvoie un score nul.
     */
    private void stopQuiz() {
        score = 0;
        Intent result = new Intent();
        result.putExtra(SCORE, score);
        setResult(RESULT_OK, result);
        finish();
    }

    /**
     * Termine le quiz, enregistre le score et marque le questionnaire comme complété.
     */
    private void endQuiz() {
        new Thread(() -> {
            // Enregistrer le score
            ScoreEntity s = new ScoreEntity();
            s.surveyId = surveyId;
            s.score = score;
            s.timestamp = System.currentTimeMillis();
            db.scoreDao().insertScore(s);

            // Marquer le questionnaire comme terminé
            SurveyEntity survey = db.surveyDao().getById(surveyId);
            survey.completed = true;
            db.surveyDao().update(survey);

            // Retourner le score à SurveyActivity sur le thread UI
            runOnUiThread(() -> {
                Intent result = new Intent();
                result.putExtra(SCORE, score);
                setResult(RESULT_OK, result);
                finish();
            });
        }).start();
    }
}
