package com.example.projetrandrianalisoa.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.projetrandrianalisoa.model.QuestionEntity;

import java.util.List;

@Dao
public interface QuestionDao {
    @Insert
    void insertAll(List<QuestionEntity> questions);

    @Query("SELECT * FROM questions WHERE surveyId = :surveyId")
    List<QuestionEntity> getBySurvey(long surveyId);

    @Query("SELECT COUNT(*) FROM questions WHERE surveyId = :surveyId")
    int getQuestionCountBySurvey(long surveyId);
}
