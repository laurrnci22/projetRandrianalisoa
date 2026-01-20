package com.example.projetrandrianalisoa.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.projetrandrianalisoa.R;
import com.example.projetrandrianalisoa.model.domain.Question;
import com.example.projetrandrianalisoa.tools.PopUpHelper;
import com.example.projetrandrianalisoa.viewmodel.QuizViewModel;

import java.util.List;

/**
 * QuizActivity gère l'affichage et la logique d'un questionnaire.
 */
public class QuizActivity extends AppCompatActivity {

    // Constantes pour les clés de données passées entre activités
    public final static String SCORE = "com.example.projetrandrianalisoa.SCORE";

    // Composants de l'UI
    private TextView textCategory, textQuestionNumber, textQuestion;
    private RadioGroup radioGroup;
    private RadioButton choice1, choice2, choice3;
    private Button buttonValidate, buttonStop;
    private QuizViewModel viewModel;

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

        // Initialiser ViewModel
        viewModel = new ViewModelProvider(this).get(QuizViewModel.class);

        long surveyId = getIntent().getLongExtra(SurveyActivity.SURVEY_ID, -1);
        if (surveyId == -1) {
            PopUpHelper.showMessage(this, "Erreur : aucun questionnaire sélectionné");
            finish();
            return;
        }

        // Charger les questions via le ViewModel
        viewModel.loadSurvey(surveyId);

        // Observer les LiveData pour mettre à jour l'UI
        viewModel.getQuestionsLiveData().observe(this, questions -> showQuestion());
        viewModel.getCurrentIndexLiveData().observe(this, index -> showQuestion());

        buttonValidate.setOnClickListener(v -> validateAnswer());
        buttonStop.setOnClickListener(v -> stopQuiz());
    }

    /**
     * Affiche la question courante et les choix.
     * Si toutes les questions ont été répondues, termine le quiz.
     */
    private void showQuestion() {
        List<Question> questions = viewModel.getQuestionsLiveData().getValue();
        Integer index = viewModel.getCurrentIndexLiveData().getValue();
        if (questions == null || index == null) return;

        if (index >= questions.size()) {
            endQuiz();
            return;
        }

        Question q = questions.get(index);
        textQuestionNumber.setText("Question " + (index + 1) + "/" + questions.size());
        textQuestion.setText(q.getLabel());
        List<String> choices = q.getChoices();
        choice1.setText(choices.get(0));
        choice2.setText(choices.get(1));
        choice3.setText(choices.get(2));
        radioGroup.clearCheck();
        textCategory.setText(viewModel.getCategory());
    }

    /**
     * Valide la réponse sélectionnée et met à jour le score.
     * Passe à la question suivante.
     */
    private void validateAnswer() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            PopUpHelper.showMessage(this, "Veuillez sélectionner une réponse");
            return;
        }

        int selectedIndex = selectedId == choice1.getId() ? 0 :
                selectedId == choice2.getId() ? 1 : 2;

        viewModel.validateAnswer(selectedIndex);
    }

    /**
     * Arrête le quiz prématurément et renvoie un score nul.
     */
    private void stopQuiz() {
        viewModel.stopQuiz(() -> runOnUiThread(() -> {
            Intent result = new Intent();
            result.putExtra(SCORE, 0);
            setResult(RESULT_OK, result);
            finish();
        }));
    }

    /**
     * Termine le quiz, enregistre le score et marque le questionnaire comme complété.
     */
    private void endQuiz() {
        viewModel.endQuiz(() -> runOnUiThread(() -> {
            Intent result = new Intent();
            result.putExtra(SCORE, viewModel.getScoreLiveData().getValue());
            setResult(RESULT_OK, result);
            finish();
        }));
    }
}
