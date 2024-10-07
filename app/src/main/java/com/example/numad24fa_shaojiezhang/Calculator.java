package com.example.numad24fa_shaojiezhang;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Calculator extends AppCompatActivity {

    private TextView displayText;
    private StringBuilder expression = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        displayText = findViewById(R.id.display_text);

        // Set up button listeners
        setupButtonListeners();
    }

    private void setupButtonListeners() {
        // Numeric buttons
        findViewById(R.id.button_0).setOnClickListener(this::appendText);
        findViewById(R.id.button_1).setOnClickListener(this::appendText);
        findViewById(R.id.button_2).setOnClickListener(this::appendText);
        findViewById(R.id.button_3).setOnClickListener(this::appendText);
        findViewById(R.id.button_4).setOnClickListener(this::appendText);
        findViewById(R.id.button_5).setOnClickListener(this::appendText);
        findViewById(R.id.button_6).setOnClickListener(this::appendText);
        findViewById(R.id.button_7).setOnClickListener(this::appendText);
        findViewById(R.id.button_8).setOnClickListener(this::appendText);
        findViewById(R.id.button_9).setOnClickListener(this::appendText);

        // Operator buttons
        findViewById(R.id.button_plus).setOnClickListener(this::appendText);
        findViewById(R.id.button_minus).setOnClickListener(this::appendText);
        findViewById(R.id.button_multiply).setOnClickListener(this::appendText);

        // Equals button
        findViewById(R.id.button_equals).setOnClickListener(v -> evaluateExpression());
    }

    private void appendText(View view) {
        Button button = (Button) view;
        expression.append(button.getText().toString());
        displayText.setText(expression.toString());
    }

    private void evaluateExpression() {
        try {
            double result = evaluate(expression.toString());
            expression.setLength(0); // Clear the current expression

            // Check if the result is a whole number
            if (result == Math.floor(result)) {
                // If it is a whole number, format it as an integer
                expression.append((int) result);
            } else {
                // If it has a fractional part, format it as a double
                expression.append(result);
            }

            displayText.setText(expression.toString());
        } catch (Exception e) {
            displayText.setText("Error");
            expression.setLength(0); // Clear the expression on error
        }
    }


    // Simple evaluation function that handles +, -, and * operators
    private double evaluate(String expression) {
        String[] tokens;
        double result = 0;

        if (expression.contains("+")) {
            tokens = expression.split("\\+");
            result = Double.parseDouble(tokens[0]) + Double.parseDouble(tokens[1]);
        } else if (expression.contains("-")) {
            tokens = expression.split("\\-");
            result = Double.parseDouble(tokens[0]) - Double.parseDouble(tokens[1]);
        } else if (expression.contains("x")) {
            tokens = expression.split("x");
            result = Double.parseDouble(tokens[0]) * Double.parseDouble(tokens[1]);
        }
        return result;
    }
}
