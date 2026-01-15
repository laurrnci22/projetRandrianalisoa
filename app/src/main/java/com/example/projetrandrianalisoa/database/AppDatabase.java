package com.example.projetrandrianalisoa.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.projetrandrianalisoa.dao.QuestionDao;
import com.example.projetrandrianalisoa.dao.ScoreDao;
import com.example.projetrandrianalisoa.dao.SurveyDao;
import com.example.projetrandrianalisoa.model.QuestionEntity;
import com.example.projetrandrianalisoa.model.ScoreEntity;
import com.example.projetrandrianalisoa.model.SurveyEntity;

/**
 * Classe de base pour la base de données Room.
 * Elle gère les DAO et fournit une instance singleton de la base.
 */
@Database(entities = {SurveyEntity.class, QuestionEntity.class, ScoreEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    // Instance unique pour l'application (singleton)
    private static AppDatabase instance;

    /**
     * DAO pour accéder aux questionnaires (Survey).
     */
    public abstract SurveyDao surveyDao();

    /**
     * DAO pour accéder aux questions.
     */
    public abstract QuestionDao questionDao();

    /**
     * DAO pour accéder aux scores.
     */
    public abstract ScoreDao scoreDao();

    /**
     * Retourne l'instance singleton de la base de données.
     * Si elle n'existe pas encore, elle est créée.
     *
     * @param context le contexte de l'application
     * @return l'instance de AppDatabase
     */
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "survey_db" // Nom du fichier de base de données
                    )
                    .allowMainThreadQueries() // Autorise les requêtes sur le thread principal
                    .build();
        }
        return instance;
    }
}
