package com.example.poketracker;

import android.graphics.Color;
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setDefaultValues();

        Button resetButton = findViewById(R.id.reset_button);
        resetButton.setOnClickListener(v -> setDefaultValues());

        // Add submit button validation
        Button submitButton = findViewById(R.id.save_button);
        submitButton.setOnClickListener(v -> validateForm());
    }

    private void validateForm() {
        // Reset all label colors to black first
        resetLabelColors();

        boolean allValid = true;
        StringBuilder errorMessage = new StringBuilder();

        // Check if all fields are filled
        if (!areAllFieldsFilled()) {
            errorMessage.append("All fields must be filled\n");
            allValid = false;
            highlightEmptyFields();
        }

        // Validate Name
        if (!isNameValid()) {
            errorMessage.append("Name must be 3-12 alphabetical characters\n");
            allValid = false;
            TextView nameLabel = findViewById(R.id.tv_name_label);
            nameLabel.setTextColor(Color.RED);
        }

        // Validate Gender
        if (!isGenderValid()) {
            errorMessage.append("Please select a valid gender (Male/Female)\n");
            allValid = false;
            TextView genderLabel = findViewById(R.id.tv_gender_label);
            genderLabel.setTextColor(Color.RED);
        }

        // Validate National Number
        if (!isNationalNumberValid()) {
            errorMessage.append("National number must be a positive number\n");
            allValid = false;
            TextView nationalNumberLabel = findViewById(R.id.tv_national_number_label);
            nationalNumberLabel.setTextColor(Color.RED);
        }

        // Validate Species
        if (!isSpeciesValid()) {
            errorMessage.append("Species must not be empty\n");
            allValid = false;
            TextView speciesLabel = findViewById(R.id.tv_species_label);
            speciesLabel.setTextColor(Color.RED);
        }

        // Validate Height
        if (!isHeightValid()) {
            errorMessage.append("Height must be between 0.2 and 169.99\n");
            allValid = false;
            TextView heightLabel = findViewById(R.id.tv_height_label);
            heightLabel.setTextColor(Color.RED);
        }

        // Validate Weight
        if (!isWeightValid()) {
            errorMessage.append("Weight must be between 0.1 and 992.7\n");
            allValid = false;
            TextView weightLabel = findViewById(R.id.tv_weight_label);
            weightLabel.setTextColor(Color.RED);
        }

        // Validate Level
        if (!isLevelValid()) {
            errorMessage.append("Level must be a positive number\n");
            allValid = false;
            TextView levelLabel = findViewById(R.id.tv_level_label);
            levelLabel.setTextColor(Color.RED);
        }

        // Validate HP
        if (!isHpValid()) {
            errorMessage.append("HP must be between 1 and 362\n");
            allValid = false;
            TextView hpLabel = findViewById(R.id.tv_hp_label);
            hpLabel.setTextColor(Color.RED);
        }

        // Validate Attack
        if (!isAttackValid()) {
            errorMessage.append("Attack must be between 0 and 526\n");
            allValid = false;
            TextView attackLabel = findViewById(R.id.tv_attack_label);
            attackLabel.setTextColor(Color.RED);
        }

        // Validate Defense
        if (!isDefenseValid()) {
            errorMessage.append("Defense must be between 10 and 614\n");
            allValid = false;
            TextView defenseLabel = findViewById(R.id.tv_defense_label);
            defenseLabel.setTextColor(Color.RED);
        }

        if (allValid) {
            Toast.makeText(this, "Form submitted successfully! Information stored in database.", Toast.LENGTH_SHORT).show();
        } else {
            // Remove the last newline character if it exists
            if (errorMessage.length() > 0 && errorMessage.charAt(errorMessage.length() - 1) == '\n') {
                errorMessage.setLength(errorMessage.length() - 1);
            }
            Toast.makeText(this, errorMessage.toString(), Toast.LENGTH_LONG).show();
        }
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