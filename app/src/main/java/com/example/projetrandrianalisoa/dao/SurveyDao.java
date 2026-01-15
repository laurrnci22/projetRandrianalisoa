package com.example.projetrandrianalisoa.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.projetrandrianalisoa.model.SurveyEntity;

import java.util.List;

@Dao
public interface SurveyDao {

    @Insert
    long insert(SurveyEntity survey);

    @Query("SELECT * FROM surveys")
    List<SurveyEntity> getAll();

    @Query("SELECT * FROM surveys WHERE id = :id LIMIT 1")
    SurveyEntity getById(long id);

    @Query("SELECT * FROM surveys WHERE completed = 0")
    List<SurveyEntity> getAvailableSurveys();

    @Update
    void update(SurveyEntity survey);
}
