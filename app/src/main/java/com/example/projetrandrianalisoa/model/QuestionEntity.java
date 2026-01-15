package com.example.projetrandrianalisoa.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "questions", foreignKeys = @ForeignKey(entity = SurveyEntity.class, parentColumns = "id", childColumns = "surveyId", onDelete = ForeignKey.CASCADE))
public class QuestionEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;
    public long surveyId;
    public String label;
    public String choice1;
    public String choice2;
    public String choice3;
    public int correctIndex;

    public QuestionEntity(long surveyId, String label, String choice1, String choice2, String choice3, int correctIndex) {
        this.surveyId = surveyId;
        this.label = label;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.correctIndex = correctIndex;
    }
}
