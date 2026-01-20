package com.example.projetrandrianalisoa.tools;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * Classe utilitaire pour afficher des messages Toast
 */
public class PopUpHelper {

    /**
     * Affiche un Toast court sur le thread UI
     * @param context Contexte (Activity)
     * @param message Message à afficher
     */
    public static void showMessage(final Context context, final String message) {
        // Assure que le Toast est affiché sur le thread UI
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } else {
            // si appelé depuis un thread secondaire
            new Handler(Looper.getMainLooper()).post(() ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            );
        }
    }
}
