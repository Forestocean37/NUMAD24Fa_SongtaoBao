package com.example.numad24fa_songtaobao;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class QuickCalcActivity extends AppCompatActivity {

    private TextView displayText;
    private StringBuilder expression;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_calc);

        displayText = findViewById(R.id.displayText);
        expression = new StringBuilder();

        setButtonClickListeners();
    }

    private void setButtonClickListeners() {
        // Set listeners for number buttons
        for (int i = 0; i <= 9; i++) {
            int resId = getResources().getIdentifier("button" + i, "id", getPackageName());
            findViewById(resId).setOnClickListener(this::onButtonClick);
        }

        // Set listeners for operator buttons
        findViewById(R.id.buttonPlus).setOnClickListener(this::onButtonClick);
        findViewById(R.id.buttonMinus).setOnClickListener(this::onButtonClick);

        // Equals button listener
        findViewById(R.id.buttonEquals).setOnClickListener(v -> {
            // Evaluate the expression using Exp4j
            try {
                String result = evaluateExpression(expression.toString());
                displayText.setText(result);
                expression.setLength(0);  // Reset the expression after showing the result
            } catch (Exception e) {
                displayText.setText("Error");
            }
        });

        // Clear button listener
        findViewById(R.id.buttonClear).setOnClickListener(v -> {
            if (expression.length() > 0) {
                expression.deleteCharAt(expression.length() - 1);  // Remove last character
                displayText.setText(expression.toString());
            } else {
                displayText.setText("");  // Clear display if no more characters left
            }
        });
    }

    private void onButtonClick(View v) {
        Button button = (Button) v;
        expression.append(button.getText().toString());
        displayText.setText(expression.toString());
    }

    private String evaluateExpression(String expression) {
        try {
            // Use Exp4j to evaluate the expression
            Expression exp = new ExpressionBuilder(expression).build();
            double result = exp.evaluate();
            return String.valueOf(result);
        } catch (Exception e) {
            return "Error";
        }
    }
}
