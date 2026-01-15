package com.example.projetrandrianalisoa.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.projetrandrianalisoa.model.QuestionEntity;

/**
 * DAO pour accéder aux questions d'un questionnaire (Survey).
 */
@Dao
public interface QuestionDao {

    /**
     * Récupère toutes les questions associées à un questionnaire donné.
     *
     * @param surveyId l'identifiant du questionnaire
     * @return liste de questions pour ce questionnaire
     */
    @Query("SELECT * FROM questions WHERE surveyId = :surveyId")
    List<QuestionEntity> getBySurvey(long surveyId);

    /**
     * Compte le nombre de questions pour un questionnaire donné.
     *
     * @param surveyId l'identifiant du questionnaire
     * @return le nombre de questions
     */
    @Query("SELECT COUNT(*) FROM questions WHERE surveyId = :surveyId")
    int getQuestionCountBySurvey(long surveyId);
}
