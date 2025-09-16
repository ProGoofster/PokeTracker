package com.example.poketracker;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setDefaultValues();
    }

    private void setDefaultValues() {
        // National Number
        EditText etNationalNumber = findViewById(R.id.et_national_number);
        etNationalNumber.setText("896");

        // Name
        EditText etName = findViewById(R.id.et_name);
        etName.setText("Glastrier");

        // Species
        EditText etSpecies = findViewById(R.id.et_species);
        etSpecies.setText("Wild Horse Pokémon");

        // Height (with unit)
        EditText etHeight = findViewById(R.id.et_height);
        etHeight.setText("2.20");

        // Weight (with unit)
        EditText etWeight = findViewById(R.id.et_weight);
        etWeight.setText("800.00");

        // HP
        EditText etHp = findViewById(R.id.et_hp);
        etHp.setText("0");

        // Attack
        EditText etAttack = findViewById(R.id.et_attack);
        etAttack.setText("0");

        // Defense
        EditText etDefense = findViewById(R.id.et_defense);
        etDefense.setText("0");

        RadioGroup rgGender = findViewById(R.id.rg_gender);
    }
}