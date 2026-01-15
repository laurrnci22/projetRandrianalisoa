package com.example.projetrandrianalisoa.tools;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe utilitaire permettant de lire un fichier QCM
 * stocké dans le dossier assets (ex: qcm01.txt).
 */
public final class FileReader {

    private FileReader() {}

    /**
     * Lit un fichier texte ligne par ligne.
     */
    public static List<String> read(Context context, String fileName) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(context.getAssets().open(fileName)))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line.trim());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }
}
