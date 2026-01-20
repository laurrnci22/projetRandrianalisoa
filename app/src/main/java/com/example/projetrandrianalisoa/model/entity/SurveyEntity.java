package com.example.projetrandrianalisoa.model.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "surveys")
public class SurveyEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;
    public String category;
    public boolean completed;

    public SurveyEntity(String category) {
        this.category = category;
        this.completed = false;
    }
}
