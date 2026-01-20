package com.example.projetrandrianalisoa.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.projetrandrianalisoa.database.AppDatabase;
import com.example.projetrandrianalisoa.model.domain.Question;
import com.example.projetrandrianalisoa.model.entity.QuestionEntity;
import com.example.projetrandrianalisoa.model.entity.SurveyEntity;

import java.util.List;
import java.util.concurrent.Executors;

public class AddSurveyViewModel extends AndroidViewModel {

    private final AppDatabase db;

    public AddSurveyViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
    }

    /**
     * Sauvegarde un questionnaire et ses questions dans la base
     */
    public void saveSurvey(String category, List<Question> questions, Runnable onFinished) {
        Executors.newSingleThreadExecutor().execute(() -> {

            SurveyEntity survey = new SurveyEntity(category);
            long surveyId = db.surveyDao().insert(survey);

            for (Question q : questions) {
                if (q.getChoices().size() < 3) continue;

                QuestionEntity entity = new QuestionEntity(
                        surveyId,
                        q.getLabel(),
                        q.getChoices().get(0),
                        q.getChoices().get(1),
                        q.getChoices().get(2),
                        q.getCorrectIndex()
                );

                db.questionDao().insert(entity);
            }

            if (onFinished != null) onFinished.run();
        });
    }
}
