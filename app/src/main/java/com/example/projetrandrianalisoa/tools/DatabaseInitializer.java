package com.example.projetrandrianalisoa.tools;

import android.content.Context;

import com.example.projetrandrianalisoa.database.AppDatabase;
import com.example.projetrandrianalisoa.model.QuestionEntity;
import com.example.projetrandrianalisoa.model.SurveyEntity;

import java.util.List;

/**
 * Classe responsable de l'initialisation de la base de données
 * avec des données par défaut (QCM).
 */
public final class DatabaseInitializer {

    private DatabaseInitializer() { }

    /**
     * Initialise la base si aucun questionnaire n'existe.
     */
    public static void init(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);

        if (!db.surveyDao().getAll().isEmpty()) {
            return;
        }

        insertSurvey(db, context, "qcm01.txt");
        insertSurvey(db, context, "qcm02.txt");
        insertSurvey(db, context, "qcm03.txt");
    }

    public static void resetScores(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        db.scoreDao().deleteAllScores();
    }

    private static void insertSurvey(AppDatabase db, Context context, String fileName) {
        List<String> lines = FileReader.read(context, fileName);
        if (lines.isEmpty()) return;

        String category = lines.get(0);

        SurveyEntity survey = new SurveyEntity(category);
        long surveyId = db.surveyDao().insert(survey);

        List<QuestionEntity> questions = FileParser.parse(surveyId, lines);
        db.questionDao().insertAll(questions);
    }
}
