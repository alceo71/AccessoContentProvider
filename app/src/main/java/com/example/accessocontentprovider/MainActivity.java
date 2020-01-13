package com.example.accessocontentprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

/**
 * https://developer.android.com/reference/android/provider/UserDictionary
 *
 *
 */
public class MainActivity extends AppCompatActivity {


    private static final String LOG_TAG = "accessocp_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mostraParole();
    }

    /**
     * Popola la list view con le parole presenti nel dizionario
     *
     */
    public void mostraParole() {
        ContentResolver resolver = getContentResolver();
        // Richiedi tutte le parole
        Cursor cursor = resolver.query(UserDictionary.Words.CONTENT_URI, null, null, null, null);

        Log.d(LOG_TAG, "numero parole trovate  " + cursor.getCount());

        // Il nome della colonna da usare
        String[] colonne = new String[] {UserDictionary.Words.WORD, UserDictionary.Words.FREQUENCY};

        // l'id della TextView da usare
        int[] to = new int[] {R.id.parola, R.id.frequenza};

        // Crea l'adapter
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,R.layout.parola, cursor, colonne, to,0 );

        // Ricerca la lista nel layout
        ListView lista = (ListView) findViewById(R.id.lista);

        // Assegna l'adapter
        lista.setAdapter(adapter);
    }


    /**
     * Inserisce una parola nel dizionario
     *
     * @param parola
     */
    public void inserimentoParola(String parola){
        ContentResolver resolver = getContentResolver();

        ContentValues newValues = new ContentValues();

        newValues.put(UserDictionary.Words.APP_ID, "com.example.accessocontentprovider");
        newValues.put(UserDictionary.Words.LOCALE, "it_IT");
        newValues.put(UserDictionary.Words.WORD, parola);
        newValues.put(UserDictionary.Words.FREQUENCY, "100");

        getContentResolver().insert(UserDictionary.Words.CONTENT_URI, newValues);
    }

    /**
     * Aggiungi una parola nel dizionario
     *
     * @param view
     */
    public void aggiungiParola(View view) {
        // Riferimento all'edittext
        EditText nuovaParola = findViewById(R.id.nuovaParola);

        // Recupera la parola
        String parola = nuovaParola.getText().toString();

        // Cerca se la parola esiste già
        if(cercaParola(parola)){
            Toast.makeText(this, "La parola " + parola + " è già presente nel dizionario", Toast.LENGTH_LONG).show();
        } else {
            // Inserisici la parola
            inserimentoParola(parola);
            // Ricarica l'elenco
            mostraParole();
        }
    }

    /**
     * Controlla se una parola esiste nel dizionario
     *
     * @param parola
     * @return
     */
    public boolean cercaParola(String parola){
        boolean esiste = false;
        ContentResolver resolver = getContentResolver();

        String selection = UserDictionary.Words.WORD + " = ?";
        String[] selectionArgs = {parola};

        Cursor cursor = resolver.query(UserDictionary.Words.CONTENT_URI, null, selection, selectionArgs, null);

        if(cursor != null && cursor.getCount() == 1)
            esiste = true;

        return esiste;
    }

}
