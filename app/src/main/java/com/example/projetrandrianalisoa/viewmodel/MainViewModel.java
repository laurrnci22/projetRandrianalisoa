package com.example.projetrandrianalisoa.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.projetrandrianalisoa.database.AppDatabase;
import com.example.projetrandrianalisoa.model.entity.SurveyEntity;

import java.util.List;
import java.util.concurrent.Executors;

public class MainViewModel extends AndroidViewModel {

    private final AppDatabase db;

    public MainViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
    }

    /**
     * Réinitialise tous les scores et remet les questionnaires en non complétés.
     */
    public void resetScoresAndSurveys(Runnable onFinished) {
        Executors.newSingleThreadExecutor().execute(() -> {

            // Supprimer tous les scores
            db.scoreDao().deleteAllScores();

            // Réinitialiser les questionnaires
            List<SurveyEntity> surveys = db.surveyDao().getAll();
            for (SurveyEntity s : surveys) {
                s.completed = false;
            }
            db.surveyDao().updateAll(surveys);

            // Callback pour prévenir l'UI
            if (onFinished != null) {
                onFinished.run();
            }
        });
    }
}
