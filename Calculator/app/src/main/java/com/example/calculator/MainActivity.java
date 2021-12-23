package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.ausgabeZahlen);
        textView.setMovementMethod(new ScrollingMovementMethod());
    }

    public void berechnen(View view) {
        TextView ersterText = findViewById(R.id.ersteEingabe);
        TextView zweiterText = findViewById(R.id.zweiteEingabe);
        TextView ausgabe = findViewById(R.id.ausgabeZahlen);
        Spinner sp = findViewById(R.id.spinner);
        String operator = (String) sp.getSelectedItem();

        if (ersterText.getText().toString().equals("") || zweiterText.getText().toString().equals("")) {
            Snackbar.make(findViewById(R.id.spinner), "Bitte eine Zahl eingeben!!!!", Snackbar.LENGTH_LONG).show();
            return;
        }

    double ersteZahl = Double.parseDouble(ersterText.getText().toString());
    double zweiteZahl = Double.parseDouble(zweiterText.getText().toString());
    double ergebnisZahl = 0;
    String ergebnis = "";

        switch(operator) {
            case "*":
                ergebnisZahl = ersteZahl * zweiteZahl;
                break;
            case "/":
                if(zweiteZahl == 0){
                    Snackbar.make(findViewById(R.id.spinner), "Durch null dividieren geht nicht!!", Snackbar.LENGTH_LONG).show();
                    return;
                }
                ergebnisZahl = ersteZahl / zweiteZahl;
                break;
            case "+":
                ergebnisZahl = ersteZahl + zweiteZahl;
                break;
            case "-":
                ergebnisZahl = ersteZahl - zweiteZahl;
                break;
            case "^":
                ergebnisZahl = Math.pow(ersteZahl, zweiteZahl);
                break;
            case "root":
                if(ersteZahl <= 0 || zweiteZahl == 0){
                    Snackbar.make(findViewById(R.id.spinner), "Unter der Wurzel darf keine Zahl kleiner Null stehen!!", Snackbar.LENGTH_LONG).show();
                    return;
                }
                ergebnisZahl = Math.pow(ersteZahl, (1 / zweiteZahl));
                break;
        }
        ergebnis = String.format("%,.2f %s %,.2f = %,.2f%n",ersteZahl,operator,zweiteZahl, ergebnisZahl);
        ausgabe.append(ergebnis);
    }
}