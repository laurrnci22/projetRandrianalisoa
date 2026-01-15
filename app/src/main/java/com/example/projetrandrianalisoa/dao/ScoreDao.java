package com.example.projetrandrianalisoa.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;

import com.example.projetrandrianalisoa.model.ScoreEntity;

import java.util.List;

@Dao
public interface ScoreDao {

    // Insérer un score
    @Insert
    long insertScore(ScoreEntity score);

    // Récupérer tous les scores
    @Query("SELECT * FROM scores")
    List<ScoreEntity> getAllScores();

    // Récupérer les scores pour un questionnaire spécifique
    @Query("SELECT * FROM scores WHERE surveyId = :surveyId")
    List<ScoreEntity> getScoresForSurvey(long surveyId);

    // Récupérer le dernier score pour un questionnaire
    @Query("SELECT * FROM scores WHERE surveyId = :surveyId ORDER BY timestamp DESC LIMIT 1")
    ScoreEntity getLastScoreForSurvey(long surveyId);

    // Récupérer la moyenne de tous les scores
    @Query("SELECT AVG(score) FROM scores")
    float getAverageScore();

    // Supprimer tous les scores (réinitialisation)
    @Query("DELETE FROM scores")
    void deleteAllScores();

    // Supprimer les scores pour un questionnaire spécifique
    @Query("DELETE FROM scores WHERE surveyId = :surveyId")
    void deleteScoresForSurvey(long surveyId);
}
