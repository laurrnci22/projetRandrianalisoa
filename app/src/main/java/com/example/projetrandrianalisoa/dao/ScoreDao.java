package com.example.projetrandrianalisoa.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.projetrandrianalisoa.model.ScoreEntity;

/**
 * DAO pour gérer les scores des questionnaires.
 */
@Dao
public interface ScoreDao {

    /**
     * Insère un score dans la base de données.
     *
     * @param score le score à insérer
     */
    @Insert
    void insertScore(ScoreEntity score);

    /**
     * Récupère tous les scores présents dans la base.
     *
     * @return liste de tous les scores
     */
    @Query("SELECT * FROM scores")
    List<ScoreEntity> getAllScores();

    /**
     * Récupère les scores pour un questionnaire spécifique.
     *
     * @param surveyId l'identifiant du questionnaire
     * @return liste des scores pour ce questionnaire
     */
    @Query("SELECT * FROM scores WHERE surveyId = :surveyId")
    List<ScoreEntity> getScoresForSurvey(long surveyId);

    /**
     * Récupère le dernier score enregistré pour un questionnaire.
     *
     * @param surveyId l'identifiant du questionnaire
     * @return dernier score pour ce questionnaire
     */
    @Query("SELECT * FROM scores WHERE surveyId = :surveyId ORDER BY timestamp DESC LIMIT 1")
    ScoreEntity getLastScoreForSurvey(long surveyId);

    /**
     * Calcule la moyenne de tous les scores.
     *
     * @return moyenne des scores
     */
    @Query("SELECT AVG(score) FROM scores")
    float getAverageScore();

    /**
     * Supprime tous les scores de la base de données.
     */
    @Query("DELETE FROM scores")
    void deleteAllScores();

    /**
     * Supprime les scores pour un questionnaire spécifique.
     *
     * @param surveyId l'identifiant du questionnaire
     */
    @Query("DELETE FROM scores WHERE surveyId = :surveyId")
    void deleteScoresForSurvey(long surveyId);
}
