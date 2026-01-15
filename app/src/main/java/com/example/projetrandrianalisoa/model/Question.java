package com.example.projetrandrianalisoa.model;

import java.util.List;

public class Question {
    private final String label;
    private final List<String> choices;
    private final int correctIndex;

    public Question(String label, List<String> choices, int correctIndex) {
        this.label = label;
        this.choices = choices;
        this.correctIndex = correctIndex;
    }

    public String getLabel() { return label; }
    public List<String> getChoices() { return choices; }
    public int getCorrectIndex() { return correctIndex; }
}