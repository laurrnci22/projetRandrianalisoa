package com.example.projetrandrianalisoa.model.domain;

import java.util.List;

/**
 * Représente une question d'un questionnaire.
 */
public class Question {

    /** Le texte de la question */
    private final String label;

    /** La liste des choix possibles pour cette question */
    private final List<String> choices;

    /** L'indice (dans la liste choices) de la réponse correcte */
    private final int correctIndex;

    /**
     * Constructeur pour créer une question.
     *
     * @param label le texte de la question
     * @param choices la liste des choix possibles
     * @param correctIndex l'indice de la réponse correcte
     */
    public Question(String label, List<String> choices, int correctIndex) {
        this.label = label;
        this.choices = choices;
        this.correctIndex = correctIndex;
    }

    /** @return le texte de la question */
    public String getLabel() {
        return label;
    }

    /** @return la liste des choix possibles */
    public List<String> getChoices() {
        return choices;
    }

    /** @return l'indice de la réponse correcte */
    public int getCorrectIndex() {
        return correctIndex;
    }
}