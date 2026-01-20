package com.example.projetrandrianalisoa.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.projetrandrianalisoa.R;
import com.example.projetrandrianalisoa.model.domain.ScoreWithTotal;
import com.example.projetrandrianalisoa.viewmodel.ViewScoreViewModel;

import java.util.ArrayList;
import java.util.List;

public class ViewScoreActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ViewScoreViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_score);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listView = findViewById(R.id.listViewScores);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listView.setAdapter(adapter);

        // Initialiser le ViewModel
        viewModel = new ViewModelProvider(this).get(ViewScoreViewModel.class);

        // Observer les scores
        viewModel.getScoresLiveData().observe(this, this::updateList);
        // Observer la moyenne
        viewModel.getAverageLiveData().observe(this, this::updateListWithAverage);

        // Charger les scores
        viewModel.loadScores();
    }

    /**
     * Mise à jour de la liste des notes de chaque questionnaire
     */
    private void updateList(List<ScoreWithTotal> scores) {
        List<String> displayList = new ArrayList<>();
        for (ScoreWithTotal s : scores) {
            displayList.add("Note questionnaire " + s.category + " : " + s.score + "/" + s.totalQuestions);
        }
        adapter.clear();
        adapter.addAll(displayList);
        adapter.notifyDataSetChanged();
    }

    /**
     * Mise à jour de la moyenne
     */
    private void updateListWithAverage(Float average) {
        // Ajouter la moyenne à la fin
        adapter.add("Note moyenne : " + String.format("%.2f", average));
        adapter.notifyDataSetChanged();
    }

    /**
     * Méthode appelée lorsqu'on clique sur le bouton retour.
     * Ferme l'activité et renvoie RESULT_CANCELED à l'appelant.
     */
    public void onCancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}