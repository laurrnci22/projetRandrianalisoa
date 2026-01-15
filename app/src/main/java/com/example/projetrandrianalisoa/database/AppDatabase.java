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

@Database(entities = {SurveyEntity.class, QuestionEntity.class, ScoreEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract SurveyDao surveyDao();
    public abstract QuestionDao questionDao();
    public abstract ScoreDao scoreDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "survey_db")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
