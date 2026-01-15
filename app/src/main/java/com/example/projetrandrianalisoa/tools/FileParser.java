package com.example.projetrandrianalisoa.tools;

import com.example.projetrandrianalisoa.model.QuestionEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe chargée d'interpréter le contenu d'un fichier QCM
 * et de créer les entités QuestionEntity.
 */
public final class FileParser {

    private FileParser() { }

    /**
     * Parse un questionnaire depuis une liste de lignes.
     */
    public static List<QuestionEntity> parse(long surveyId, List<String> lines) {
        List<QuestionEntity> questions = new ArrayList<>();

        int i = 1; // ligne 0 = catégorie
        while (i < lines.size()) {
            String label = lines.get(i++);
            String choice1 = lines.get(i++);
            String choice2 = lines.get(i++);
            String choice3 = lines.get(i++);

            int correctIndex = -1;

            if (choice1.endsWith("x")) {
                correctIndex = 0;
                choice1 = choice1.replace("x", "").trim();
            } else if (choice2.endsWith("x")) {
                correctIndex = 1;
                choice2 = choice2.replace("x", "").trim();
            } else if (choice3.endsWith("x")) {
                correctIndex = 2;
                choice3 = choice3.replace("x", "").trim();
            }

            questions.add(new QuestionEntity(
                    surveyId,
                    label,
                    choice1,
                    choice2,
                    choice3,
                    correctIndex
            ));
        }

        return questions;
    }
}
