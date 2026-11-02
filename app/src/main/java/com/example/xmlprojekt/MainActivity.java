package com.example.xmlprojekt;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText liczbaKostekInput;
    private GridLayout gridKostki;
    private TextView wyniki;
    private final List<Integer> wylosowaneKostki = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        liczbaKostekInput = findViewById(R.id.editTextLiczbaKostek);
        gridKostki = findViewById(R.id.gridKostki);
        wyniki = findViewById(R.id.textViewWyniki);

        Button losuj = findViewById(R.id.buttonLosuj);
        Button rosnaco = findViewById(R.id.buttonRosnaco);
        Button malejaco = findViewById(R.id.buttonMalejaco);

        losuj.setOnClickListener(v -> losujKostki());
        rosnaco.setOnClickListener(v -> sortujKostki(false));
        malejaco.setOnClickListener(v -> sortujKostki(true));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void losujKostki() {
        String wpisanaLiczba = liczbaKostekInput.getText().toString().trim();

        if (wpisanaLiczba.isEmpty()) {
            pokazBlad(getString(R.string.error_empty_dice_count));
            return;
        }

        final int liczbaKostek;

        try {
            liczbaKostek = Integer.parseInt(wpisanaLiczba);
        } catch (NumberFormatException exception) {
            pokazBlad(getString(R.string.error_invalid_dice_count));
            return;
        }

        if (liczbaKostek <= 0) {
            pokazBlad(getString(R.string.error_invalid_dice_count));
            return;
        }

        wylosowaneKostki.clear();

        for (int i = 0; i < liczbaKostek; i++) {
            wylosowaneKostki.add((int) (Math.random() * 6) + 1);
        }

        wyswietlWyniki();
    }

    private void sortujKostki(boolean malejaco) {
        if (wylosowaneKostki.isEmpty()) {
            Toast.makeText(this, R.string.error_no_dice_to_sort, Toast.LENGTH_SHORT).show();
            return;
        }

        Collections.sort(wylosowaneKostki);

        if (malejaco) {
            Collections.reverse(wylosowaneKostki);
        }

        wyswietlWyniki();
    }

    private void wyswietlWyniki() {
        gridKostki.removeAllViews();

        StringBuilder tekstWynikow = new StringBuilder();

        for (int i = 0; i < wylosowaneKostki.size(); i++) {
            int wynik = wylosowaneKostki.get(i);

            if (i > 0) {
                tekstWynikow.append("  ");
            }

            tekstWynikow.append(wynik);

            TextView kostka = new TextView(this);
            kostka.setText(getString(R.string.die_result, i + 1, wynik));
            kostka.setGravity(Gravity.CENTER);
            kostka.setTextSize(16);
            kostka.setTextColor(Color.BLACK);
            kostka.setPadding(16, 16, 16, 16);

            GradientDrawable tloKostki = new GradientDrawable();
            tloKostki.setColor(Color.WHITE);
            tloKostki.setStroke(2, Color.DKGRAY);
            tloKostki.setCornerRadius(12);
            kostka.setBackground(tloKostki);

            GridLayout.LayoutParams parametry = new GridLayout.LayoutParams();
            parametry.width = 0;
            parametry.height = GridLayout.LayoutParams.WRAP_CONTENT;
            parametry.columnSpec = GridLayout.spec(i % 3, 1, 1f);
            parametry.setMargins(8, 8, 8, 8);

            gridKostki.addView(kostka, parametry);
        }

        wyniki.setText(tekstWynikow.toString());
    }

    private void pokazBlad(String komunikat) {
        liczbaKostekInput.setError(komunikat);
        wyniki.setText(komunikat);
        Toast.makeText(this, komunikat, Toast.LENGTH_SHORT).show();
    }
}