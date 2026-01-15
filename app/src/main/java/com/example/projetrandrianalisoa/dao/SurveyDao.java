package com.example.projetrandrianalisoa.dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.projetrandrianalisoa.model.SurveyEntity;

/**
 * DAO pour gérer les questionnaires (Survey).
 */
@Dao
public interface SurveyDao {

    /**
     * Insère un nouveau questionnaire.
     *
     * @param survey le questionnaire à insérer
     * @return l'ID du questionnaire inséré
     */
    @Insert
    long insert(SurveyEntity survey);

    /**
     * Récupère tous les questionnaires.
     *
     * @return liste de tous les questionnaires
     */
    @Query("SELECT * FROM surveys")
    List<SurveyEntity> getAll();

    /**
     * Récupère un questionnaire par son identifiant.
     *
     * @param id l'ID du questionnaire
     * @return le questionnaire correspondant ou null
     */
    @Query("SELECT * FROM surveys WHERE id = :id LIMIT 1")
    SurveyEntity getById(long id);

    /**
     * Récupère tous les questionnaires disponibles (non complétés).
     *
     * @return LiveData contenant la liste des questionnaires disponibles
     */
    @Query("SELECT * FROM surveys WHERE completed = 0")
    LiveData<List<SurveyEntity>> getAvailableSurveys();

    /**
     * Met à jour un questionnaire existant.
     *
     * @param survey le questionnaire à mettre à jour
     */
    @Update
    void update(SurveyEntity survey);
}
