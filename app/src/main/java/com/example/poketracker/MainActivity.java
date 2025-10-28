package com.example.poketracker;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
        setContentView(R.layout.table_layout);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        setDefaultValues();

        Button resetButton = findViewById(R.id.reset_button);
        resetButton.setOnClickListener(v -> setDefaultValues());

        // Add submit button validation
        Button submitButton = findViewById(R.id.save_button);
        submitButton.setOnClickListener(v -> validateAndSubmitForm());

        Button viewPokedexButton = findViewById(R.id.view_pokedex_button); // Make sure you add this button to your layout
        viewPokedexButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PokedexListActivity.class);
            startActivity(intent);
        });
    }

    private void validateAndSubmitForm() {
        resetLabelColors();

        if (!validateForm()) {
            // Validation failed, toast messages are shown within validateForm
            return;
        }

        ContentValues values = new ContentValues();
        values.put(PokedexContentProvider.COL_NATNUM, ((EditText) findViewById(R.id.et_national_number)).getText().toString());
        values.put(PokedexContentProvider.COL_NAME, ((EditText) findViewById(R.id.et_name)).getText().toString());
        values.put(PokedexContentProvider.COL_SPECIES, ((EditText) findViewById(R.id.et_species)).getText().toString());
        values.put(PokedexContentProvider.COL_HEIGHT, ((EditText) findViewById(R.id.et_height)).getText().toString());
        values.put(PokedexContentProvider.COL_WEIGHT, ((EditText) findViewById(R.id.et_weight)).getText().toString());
        values.put(PokedexContentProvider.COL_LEVEL, ((EditText) findViewById(R.id.et_level)).getText().toString());
        values.put(PokedexContentProvider.COL_HP, ((EditText) findViewById(R.id.et_hp)).getText().toString());
        values.put(PokedexContentProvider.COL_ATTACK, ((EditText) findViewById(R.id.et_attack)).getText().toString());
        values.put(PokedexContentProvider.COL_DEFENSE, ((EditText) findViewById(R.id.et_defense)).getText().toString());

        RadioGroup rgGender = findViewById(R.id.rg_gender);
        RadioButton selectedRadioButton = findViewById(rgGender.getCheckedRadioButtonId());
        values.put(PokedexContentProvider.COL_GENDER, selectedRadioButton.getText().toString());

        // Requirement: Check for duplicates before inserting
        if (isDuplicateEntry(values)) {
            Toast.makeText(this, "This Pokémon entry already exists in the database.", Toast.LENGTH_LONG).show();
            return;
        }

        // Use ContentResolver to insert data via the ContentProvider
        Uri newUri = getContentResolver().insert(PokedexContentProvider.CONTENT_URI, values);

        if (newUri != null) {
            Toast.makeText(this, "Form submitted successfully! Information stored in database.", Toast.LENGTH_SHORT).show();
            setDefaultValues(); // Clear form on success
        } else {
            Toast.makeText(this, "Error: Failed to save data.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isDuplicateEntry(ContentValues values) {
        String selection = PokedexContentProvider.COL_NATNUM + " = ? AND " +
                PokedexContentProvider.COL_NAME + " = ? AND " +
                PokedexContentProvider.COL_SPECIES + " = ?";
        String[] selectionArgs = {
                values.getAsString(PokedexContentProvider.COL_NATNUM),
                values.getAsString(PokedexContentProvider.COL_NAME),
                values.getAsString(PokedexContentProvider.COL_SPECIES)
        };

        Cursor cursor = getContentResolver().query(PokedexContentProvider.CONTENT_URI, null, selection, selectionArgs, null);

        boolean exists = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) {
            cursor.close();
        }
        return exists;
    }

    // Combines validation logic into one method that returns a boolean
    private boolean validateForm() {
        boolean allValid = true;
        StringBuilder errorMessage = new StringBuilder("Please fix the following errors:\n");

        if (!areAllFieldsFilled()) {
            errorMessage.append("- All fields must be filled\n");
            allValid = false;
            highlightEmptyFields();
        }
        if (!isNameValid()) {
            errorMessage.append("- Name must be 3-12 alphabetical characters\n");
            allValid = false;
            ((TextView) findViewById(R.id.tv_name_label)).setTextColor(Color.RED);
        }
        if (!isGenderValid()) {
            errorMessage.append("- Please select a valid gender\n");
            allValid = false;
            ((TextView) findViewById(R.id.tv_gender_label)).setTextColor(Color.RED);
        }
        if (!isNationalNumberValid()) {
            errorMessage.append("- National number must be a positive number\n");
            allValid = false;
            ((TextView) findViewById(R.id.tv_national_number_label)).setTextColor(Color.RED);
        }
        if (!isSpeciesValid()) {
            errorMessage.append("- Species must not be empty\n");
            allValid = false;
            ((TextView) findViewById(R.id.tv_species_label)).setTextColor(Color.RED);
        }
        if (!isHeightValid()) {
            errorMessage.append("- Height must be between 0.2 and 169.99\n");
            allValid = false;
            ((TextView) findViewById(R.id.tv_height_label)).setTextColor(Color.RED);
        }
        if (!isWeightValid()) {
            errorMessage.append("- Weight must be between 0.1 and 992.7\n");
            allValid = false;
            ((TextView) findViewById(R.id.tv_weight_label)).setTextColor(Color.RED);
        }
        if (!isLevelValid()) {
            errorMessage.append("- Level must be a positive number\n");
            allValid = false;
            ((TextView) findViewById(R.id.tv_level_label)).setTextColor(Color.RED);
        }
        if (!isHpValid()) {
            errorMessage.append("- HP must be between 1 and 362\n");
            allValid = false;
            ((TextView) findViewById(R.id.tv_hp_label)).setTextColor(Color.RED);
        }
        if (!isAttackValid()) {
            errorMessage.append("- Attack must be between 0 and 526\n");
            allValid = false;
            ((TextView) findViewById(R.id.tv_attack_label)).setTextColor(Color.RED);
        }
        if (!isDefenseValid()) {
            errorMessage.append("- Defense must be between 10 and 614\n");
            allValid = false;
            ((TextView) findViewById(R.id.tv_defense_label)).setTextColor(Color.RED);
        }

        if (!allValid) {
            Toast.makeText(this, errorMessage.toString().trim(), Toast.LENGTH_LONG).show();
        }
        return allValid;
    }

    private void resetLabelColors() {
        int[] labelIds = {
                R.id.tv_national_number_label,
                R.id.tv_name_label,
                R.id.tv_species_label,
                R.id.tv_gender_label,
                R.id.tv_height_label,
                R.id.tv_weight_label,
                R.id.tv_level_label,
                R.id.tv_hp_label,
                R.id.tv_attack_label,
                R.id.tv_defense_label
        };

        for (int id : labelIds) {
            TextView label = findViewById(id);
            label.setTextColor(Color.BLACK);
        }
    }

    private void highlightEmptyFields() {
        // Check all EditText fields
        int[] editTextIds = {
                R.id.et_national_number,
                R.id.et_name,
                R.id.et_species,
                R.id.et_height,
                R.id.et_weight,
                R.id.et_level,
                R.id.et_hp,
                R.id.et_attack,
                R.id.et_defense
        };

        int[] labelIds = {
                R.id.tv_national_number_label,
                R.id.tv_name_label,
                R.id.tv_species_label,
                0, // No label for gender here
                R.id.tv_height_label,
                R.id.tv_weight_label,
                R.id.tv_level_label,
                R.id.tv_hp_label,
                R.id.tv_attack_label,
                R.id.tv_defense_label
        };

        for (int i = 0; i < editTextIds.length; i++) {
            EditText et = findViewById(editTextIds[i]);
            if (et.getText().toString().trim().isEmpty()) {
                if (labelIds[i] != 0) {
                    TextView label = findViewById(labelIds[i]);
                    label.setTextColor(Color.RED);
                }
            }
        }

        // Check RadioGroup (Gender)
        RadioGroup rgGender = findViewById(R.id.rg_gender);
        if (rgGender.getCheckedRadioButtonId() == -1) {
            TextView genderLabel = findViewById(R.id.tv_gender_label);
            genderLabel.setTextColor(Color.RED);
        }
    }

    private boolean areAllFieldsFilled() {
        // Check all EditText fields
        int[] editTextIds = {
                R.id.et_national_number,
                R.id.et_name,
                R.id.et_species,
                R.id.et_height,
                R.id.et_weight,
                R.id.et_level,
                R.id.et_hp,
                R.id.et_attack,
                R.id.et_defense
        };

        for (int id : editTextIds) {
            EditText et = findViewById(id);
            if (et.getText().toString().trim().isEmpty()) {
                return false;
            }
        }

        // Check RadioGroup (Gender)
        RadioGroup rgGender = findViewById(R.id.rg_gender);
        return rgGender.getCheckedRadioButtonId() != -1;
    }

    private boolean isNameValid() {
        EditText etName = findViewById(R.id.et_name);
        String name = etName.getText().toString().trim();

        // Check if empty
        if (name.isEmpty()) {
            return false;
        }

        // Check length
        if (name.length() < 3 || name.length() > 12) {
            return false;
        }

        // Check if contains only alphabetical characters
        return name.matches("[a-zA-Z]+");
    }

    private boolean isGenderValid() {
        RadioGroup rgGender = findViewById(R.id.rg_gender);
        int selectedId = rgGender.getCheckedRadioButtonId();

        if (selectedId == -1) {
            return false; // No selection
        }

        RadioButton selectedRadioButton = findViewById(selectedId);
        String gender = selectedRadioButton.getText().toString();

        // Check if gender is either "Male" or "Female"
        return gender.equals("Male") || gender.equals("Female");
    }

    private boolean isNationalNumberValid() {
        EditText etNationalNumber = findViewById(R.id.et_national_number);
        String nationalNumberText = etNationalNumber.getText().toString().trim();

        if (nationalNumberText.isEmpty()) {
            return false;
        }

        try {
            int nationalNumber = Integer.parseInt(nationalNumberText);
            return nationalNumber > 0;
        } catch (NumberFormatException e) {
            return false; // Not a valid number
        }
    }

    private boolean isSpeciesValid() {
        EditText etSpecies = findViewById(R.id.et_species);
        String species = etSpecies.getText().toString().trim();
        return !species.isEmpty();
    }

    private boolean isHeightValid() {
        EditText etHeight = findViewById(R.id.et_height);
        String heightText = etHeight.getText().toString().trim();

        if (heightText.isEmpty()) {
            return false;
        }

        try {
            double height = Double.parseDouble(heightText);
            return height >= 0.2 && height <= 169.99;
        } catch (NumberFormatException e) {
            return false; // Not a valid number
        }
    }

    private boolean isWeightValid() {
        EditText etWeight = findViewById(R.id.et_weight);
        String weightText = etWeight.getText().toString().trim();

        if (weightText.isEmpty()) {
            return false;
        }

        try {
            double weight = Double.parseDouble(weightText);
            return weight >= 0.1 && weight <= 992.7;
        } catch (NumberFormatException e) {
            return false; // Not a valid number
        }
    }

    private boolean isLevelValid() {
        EditText etLevel = findViewById(R.id.et_level);
        String levelText = etLevel.getText().toString().trim();

        if (levelText.isEmpty()) {
            return false;
        }

        try {
            int level = Integer.parseInt(levelText);
            return level > 0;
        } catch (NumberFormatException e) {
            return false; // Not a valid number
        }
    }

    private boolean isHpValid() {
        EditText etHp = findViewById(R.id.et_hp);
        String hpText = etHp.getText().toString().trim();

        if (hpText.isEmpty()) {
            return false;
        }

        try {
            int hp = Integer.parseInt(hpText);
            return hp >= 1 && hp <= 362;
        } catch (NumberFormatException e) {
            return false; // Not a valid number
        }
    }

    private boolean isAttackValid() {
        EditText etAttack = findViewById(R.id.et_attack);
        String attackText = etAttack.getText().toString().trim();

        if (attackText.isEmpty()) {
            return false;
        }

        try {
            int attack = Integer.parseInt(attackText);
            return attack >= 0 && attack <= 526;
        } catch (NumberFormatException e) {
            return false; // Not a valid number
        }
    }

    private boolean isDefenseValid() {
        EditText etDefense = findViewById(R.id.et_defense);
        String defenseText = etDefense.getText().toString().trim();

        if (defenseText.isEmpty()) {
            return false;
        }

        try {
            int defense = Integer.parseInt(defenseText);
            return defense >= 10 && defense <= 614;
        } catch (NumberFormatException e) {
            return false; // Not a valid number
        }
    }

    private void setDefaultValues() {
        // Reset all label colors to black
        resetLabelColors();

        // National Number
        EditText etNationalNumber = findViewById(R.id.et_national_number);
        etNationalNumber.setText("896");

        // Name
        EditText etName = findViewById(R.id.et_name);
        etName.setText("Glastrier");

        // Species
        EditText etSpecies = findViewById(R.id.et_species);
        etSpecies.setText("Wild Horse Pokémon");

        // Height
        EditText etHeight = findViewById(R.id.et_height);
        etHeight.setText("2.2");

        // Weight
        EditText etWeight = findViewById(R.id.et_weight);
        etWeight.setText("800.0");

        // Level
        EditText etLevel = findViewById(R.id.et_level);
        etLevel.setText("");

        // HP
        EditText etHp = findViewById(R.id.et_hp);
        etHp.setText("0");

        // Attack
        EditText etAttack = findViewById(R.id.et_attack);
        etAttack.setText("0");

        // Defense
        EditText etDefense = findViewById(R.id.et_defense);
        etDefense.setText("0");

        // Clear gender selection
        RadioGroup rgGender = findViewById(R.id.rg_gender);
        rgGender.check(R.id.rb_unknown);
    }
}