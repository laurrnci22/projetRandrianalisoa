package com.example.projetrandrianalisoa.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.projetrandrianalisoa.database.AppDatabase;
import com.example.projetrandrianalisoa.model.domain.ScoreWithTotal;

import java.util.List;
import java.util.concurrent.Executors;

public class ViewScoreViewModel extends AndroidViewModel {

    private final AppDatabase db;

    // Modification possible des données via MutableLiveData
    private final MutableLiveData<List<ScoreWithTotal>> scoresLiveData = new MutableLiveData<>();

    // Modification possible des données via MutableLiveData
    private final MutableLiveData<Float> averageLiveData = new MutableLiveData<>();

    public ViewScoreViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
    }

    /**
     * Récupère les scores
     */
    public LiveData<List<ScoreWithTotal>> getScoresLiveData() {
        return scoresLiveData;
    }

    /**
     * Récupère la moyenne
     */
    public LiveData<Float> getAverageLiveData() {
        return averageLiveData;
    }

    /**
     * Charge les scores et la moyenne depuis la base
     */
    public void loadScores() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<ScoreWithTotal> scores = db.scoreDao().getScoresWithTotal();
            float average = db.scoreDao().getAverageScore();

            scoresLiveData.postValue(scores);
            averageLiveData.postValue(average);
        });
    }
}
