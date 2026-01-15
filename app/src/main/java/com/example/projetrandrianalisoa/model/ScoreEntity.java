package com.example.projetrandrianalisoa.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "scores")
public class ScoreEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public long surveyId;
    public int score;
    public long timestamp;
}
