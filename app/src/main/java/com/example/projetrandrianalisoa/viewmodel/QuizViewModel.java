package com.example.projetrandrianalisoa.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.projetrandrianalisoa.database.AppDatabase;
import com.example.projetrandrianalisoa.model.domain.Question;
import com.example.projetrandrianalisoa.model.entity.ScoreEntity;
import com.example.projetrandrianalisoa.model.entity.SurveyEntity;
import com.example.projetrandrianalisoa.model.entity.QuestionEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;

public class QuizViewModel extends AndroidViewModel {

    private final AppDatabase db;

    // Modification possible des données via MutableLiveData
    private final MutableLiveData<List<Question>> questionsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> currentIndexLiveData = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> scoreLiveData = new MutableLiveData<>(0);
    private long surveyId;
    private String category;

    public QuizViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
    }

    public MutableLiveData<List<Question>> getQuestionsLiveData() {
        return questionsLiveData;
    }

    public MutableLiveData<Integer> getCurrentIndexLiveData() {
        return currentIndexLiveData;
    }

    public MutableLiveData<Integer> getScoreLiveData() {
        return scoreLiveData;
    }

    public String getCategory() {
        return category;
    }

    /**
     * Méthode permettant de charger les questions
     */
    public void loadSurvey(long surveyId) {
        this.surveyId = surveyId;
        Executors.newSingleThreadExecutor().execute(() -> {
            SurveyEntity survey = db.surveyDao().getById(surveyId);
            if (survey == null) return;
            this.category = survey.category;

            List<QuestionEntity> entities = db.questionDao().getBySurvey(surveyId);
            List<Question> questions = new ArrayList<>();
            for (QuestionEntity qe : entities) {
                List<String> choices = new ArrayList<>();
                choices.add(qe.choice1);
                choices.add(qe.choice2);
                choices.add(qe.choice3);
                questions.add(new Question(qe.label, choices, qe.correctIndex));
            }
            Collections.shuffle(questions);
            questionsLiveData.postValue(questions);
        });
    }

    /**
     * Méthode permettant de valider les choix
     */
    public void validateAnswer(int selectedIndex) {
        List<Question> questions = questionsLiveData.getValue();
        if (questions == null || currentIndexLiveData.getValue() == null) return;

        int index = currentIndexLiveData.getValue();
        int score = scoreLiveData.getValue() != null ? scoreLiveData.getValue() : 0;

        if (selectedIndex == questions.get(index).getCorrectIndex()) {
            score++;
            scoreLiveData.postValue(score);
        }

        index++;
        currentIndexLiveData.postValue(index);
    }

    /**
     * Méthode permettant d'arreter prématurément le questionnaire
     */
    public void stopQuiz(Runnable onFinished) {
        scoreLiveData.postValue(0);
        if (onFinished != null) onFinished.run();
    }

    /**
     * Méthode permettant de finaliser le questionnaire
     */
    public void endQuiz(Runnable onFinished) {
        Integer score = scoreLiveData.getValue();
        if (score == null) score = 0;

        Integer finalScore = score;

        Executors.newSingleThreadExecutor().execute(() -> {
            ScoreEntity s = new ScoreEntity();
            s.surveyId = surveyId;
            s.score = finalScore;
            s.timestamp = System.currentTimeMillis();
            db.scoreDao().insertScore(s);

            SurveyEntity survey = db.surveyDao().getById(surveyId);
            if (survey != null) {
                survey.completed = true;
                db.surveyDao().update(survey);
            }

            if (onFinished != null) onFinished.run();
        });
    }
}
