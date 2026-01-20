package com.example.projetrandrianalisoa.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.projetrandrianalisoa.model.entity.ScoreEntity;
import com.example.projetrandrianalisoa.model.domain.ScoreWithTotal;

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
    @Query("SELECT (SUM(sc.score) * 20.0) / " +
            "(SELECT COUNT(*) FROM questions q JOIN scores s ON q.surveyId = s.surveyId) " +
            "FROM scores sc")
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

    // Récupérer tous les scores avec le nom du questionnaire
    @Query("SELECT s.category as category, sc.score as score, " +
            "(SELECT COUNT(*) FROM questions WHERE surveyId = s.id) as totalQuestions " +
            "FROM scores sc " +
            "JOIN surveys s ON sc.surveyId = s.id")
    List<ScoreWithTotal> getScoresWithTotal();
}
