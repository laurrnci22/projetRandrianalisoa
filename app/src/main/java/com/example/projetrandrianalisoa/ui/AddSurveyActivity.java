package com.example.projetrandrianalisoa.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.projetrandrianalisoa.R;
import com.example.projetrandrianalisoa.model.domain.Question;
import com.example.projetrandrianalisoa.tools.PopUpHelper;
import com.example.projetrandrianalisoa.viewmodel.AddSurveyViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddSurveyActivity extends AppCompatActivity {
    private ArrayList<String> questionTitles;
    private ArrayList<Question> questions;
    private ArrayAdapter<String> adapter;

    private AddSurveyViewModel viewModel;

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

        ListView listView = findViewById(R.id.listAddedQuestion);

        questions = new ArrayList<>();
        questionTitles = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, questionTitles);
        listView.setAdapter(adapter);

        // Initialiser le ViewModel
        viewModel = new ViewModelProvider(this).get(AddSurveyViewModel.class);
    }

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
            PopUpHelper.showMessage(this, "Veuillez renseigner une catégorie avant de valider.");
            return;
        }

        if (questions.isEmpty()) {
            PopUpHelper.showMessage(this, "Veuillez ajouter au moins une question avant de valider.");
            return;
        }

        // Utiliser le ViewModel pour sauvegarder
        viewModel.saveSurvey(category, questions, () -> runOnUiThread(() -> {
            setResult(RESULT_OK);
            finish();
        }));
    }

    /**
     * Méthode appelée lorsqu'on clique sur le bouton retour.
     * Ferme l'activité et renvoie RESULT_CANCELED à l'appelant.
     */
    public void onCancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    /**
     * Lanceur d'activité dynamique
     */
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

                    List<String> choices = new ArrayList<>();
                    choices.add(choice1);
                    choices.add(choice2);
                    choices.add(choice3);

                    Question question = new Question(label, choices, correctIndex - 1);

                    questions.add(question);
                    questionTitles.add(label);

                    adapter.notifyDataSetChanged();
                }
            }
        }
    );
}
