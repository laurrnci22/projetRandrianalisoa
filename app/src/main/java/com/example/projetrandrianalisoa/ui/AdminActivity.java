package com.example.projetrandrianalisoa.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projetrandrianalisoa.R;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);
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
        EditText input = findViewById(R.id.fieldPassword);
        String password = input.getText().toString().trim();

        if ("MDP".equals(password)) {
            // Mot de passe correct, ouvrir l'activité d'ajout
            Intent intent = new Intent(this, AddSurveyActivity.class);
            dynamicLauncher.launch(intent);
        } else {
            Toast.makeText(this, "Mot de passe incorrect", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Méthode appelée lorsqu'on clique sur le bouton annuler.
     * Ferme l'activité et renvoie RESULT_CANCELED à l'appelant.
     */
    public void onCancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }


    // --------------------------
    // Launcher dynamique
    // --------------------------
    private final ActivityResultLauncher<Intent> dynamicLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Toast.makeText(this, "Questionnaire enregistré avec succès !", Toast.LENGTH_SHORT).show();

                    setResult(RESULT_OK, new Intent());
                    finish();
                }
            }
    );
}