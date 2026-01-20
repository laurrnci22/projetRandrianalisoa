package com.example.projetrandrianalisoa.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projetrandrianalisoa.R;
import com.example.projetrandrianalisoa.tools.PopUpHelper;

public class AddQuestionActivity extends AppCompatActivity {

    // ------------------------
    // Constantes pour les clés de données passées entre activités
    // ------------------------
    public final static String QUESTION_LABEL = "com.example.projetrandrianalisoa.QUESTION_LABEL";
    public final static String CHOICE_1 = "com.example.projetrandrianalisoa.CHOICE_1";
    public final static String CHOICE_2 = "com.example.projetrandrianalisoa.CHOICE_2";
    public final static String CHOICE_3 = "com.example.projetrandrianalisoa.CHOICE_3";
    public final static String CORRECT_ANSWER_INDEX = "com.example.projetrandrianalisoa.CORRECT_ANSWER_INDEX";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_question);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Méthode appelée lorsqu'on clique sur le bouton valider.
     */
    public void onValidate(View view) {
        // Récupération du libellé de la question
        TextView questionLabelView = findViewById(R.id.addQuestionLabel);
        String label = questionLabelView.getText().toString().trim();

        // Récupération du choix 1
        TextView choice1View = findViewById(R.id.addChoice1);
        String choice1 = choice1View.getText().toString().trim();

        // Récupération du choix 2
        TextView choice2View = findViewById(R.id.addChoice2);
        String choice2 = choice2View.getText().toString().trim();

        // Récupération du choix 3
        TextView choice3View = findViewById(R.id.addChoice3);
        String choice3 = choice3View.getText().toString().trim();

        // Récupération de l'indice de réponse correcte
        TextView correctIndexAnswerView = findViewById(R.id.correctIndexAnswer);
        String correctIndexAnswer = correctIndexAnswerView.getText().toString().trim();

        // Vérifie si le champ est vide
        if (label.isEmpty() || choice1.isEmpty() || choice2.isEmpty() || choice3.isEmpty() || correctIndexAnswer.isEmpty()) {
            PopUpHelper.showMessage(this, "Veuillez renseigner tous les champs avant de valider.");
            return;
        }

        int index = Integer.parseInt(correctIndexAnswer);

        if(index != 1 && index != 2 && index != 3) {
            PopUpHelper.showMessage(this, "Veuillez choisir une indice soit 1, 2 ou 3 !.");
            return;
        }

        // Création de l'intention pour renvoyer les données
        Intent intention = new Intent(this, MainActivity.class);
        intention.putExtra(QUESTION_LABEL, label);
        intention.putExtra(CHOICE_1, choice1);
        intention.putExtra(CHOICE_2, choice2);
        intention.putExtra(CHOICE_3, choice3);
        intention.putExtra(CORRECT_ANSWER_INDEX, index);

        // Envoi du résultat OK et fermeture de l'activité
        setResult(RESULT_OK, intention);
        finish();
    }

    /**
     * Méthode appelée lorsqu'on clique sur le bouton retour.
     */
    public void onCancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}