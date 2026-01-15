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

import com.example.projetrandrianalisoa.R;
import com.example.projetrandrianalisoa.database.AppDatabase;
import com.example.projetrandrianalisoa.model.ScoreWithTotal;

import java.util.ArrayList;
import java.util.List;

public class ViewScoreActivity extends AppCompatActivity {

    private AppDatabase db;
    private ListView listView;
    private ArrayAdapter<String> adapter;

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

        db = AppDatabase.getInstance(this);
        listView = findViewById(R.id.listViewScores);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listView.setAdapter(adapter);

        new Thread(() -> {
            List<ScoreWithTotal> scores = db.scoreDao().getScoresWithTotal();
            float average = db.scoreDao().getAverageScore();

            List<String> displayList = new ArrayList<>();
            for (ScoreWithTotal s : scores) {
                displayList.add("Note questionnaire " + s.category + " : " + s.score + "/" + s.totalQuestions);
            }
            displayList.add("Note moyenne : " + String.format("%.2f", average));

            runOnUiThread(() -> {
                adapter.clear();
                adapter.addAll(displayList);
                adapter.notifyDataSetChanged();
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
}