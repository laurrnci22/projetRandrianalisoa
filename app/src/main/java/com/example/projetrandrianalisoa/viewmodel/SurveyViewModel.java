package com.example.projetrandrianalisoa.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.projetrandrianalisoa.database.AppDatabase;
import com.example.projetrandrianalisoa.model.entity.SurveyEntity;

import java.util.List;

public class SurveyViewModel extends AndroidViewModel {

    private final AppDatabase db;
    private final LiveData<List<SurveyEntity>> availableSurveys;

    public SurveyViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getInstance(application);

        // LiveData depuis Room
        availableSurveys = db.surveyDao().getAvailableSurveys();
    }

    public LiveData<List<SurveyEntity>> getAvailableSurveys() {
        return availableSurveys;
    }

    /** Récupère le nombre de questions d’un questionnaire */
    public int getQuestionCount(SurveyEntity survey) {
        return db.questionDao().getQuestionCountBySurvey(survey.id);
    }
}
