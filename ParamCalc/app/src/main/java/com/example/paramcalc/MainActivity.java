package com.example.paramcalc;

/*
 *  Parampal Singh
 *  7003114
 */

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private TextView resultDisplay;
    private TextView savedValueDisplay;
    private String currentOperand = "";
    private String previousOperand = "";
    private String operator = "";
    private String storedValue = "0";
    private boolean isResultDisplayed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EdgeToEdge.enable(this);
        resultDisplay = findViewById(R.id.result);

        setNumberButtons();
        setOperatorButtons();

        findViewById(R.id.clear).setOnClickListener(v -> clearLast());
        findViewById(R.id.allClear).setOnClickListener(v -> allClear());
        findViewById(R.id.equals).setOnClickListener(v -> doCalculation());
        findViewById(R.id.saveValue).setOnClickListener(v -> saveValue());
        findViewById(R.id.retrieveValue).setOnClickListener(v -> retrieveValue());
        savedValueDisplay = findViewById(R.id.savedValueDisplay);
    }

    // This saves all the values that has to be retained when screen is rotated
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("currentOperand", currentOperand);
        outState.putString("previousOperand", previousOperand);
        outState.putString("operator", operator);
        outState.putString("storedValue", storedValue);
        outState.putBoolean("isResultDisplayed", isResultDisplayed);
    }


    // This provides back the values after the screen is rotated
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore the data
        if (savedInstanceState != null) {
            currentOperand = savedInstanceState.getString("currentOperand");
            previousOperand = savedInstanceState.getString("previousOperand");
            operator = savedInstanceState.getString("operator");
            storedValue = savedInstanceState.getString("storedValue");
            isResultDisplayed = savedInstanceState.getBoolean("isResultDisplayed");
            updateDisplay();
            updateSavedValueDisplay();
        }
    }


    // This method sets the onClick listeners for all number buttons and the decimal button
    private void setNumberButtons() {
        int[] numberIds = {R.id.zero, R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine, R.id.decimal};
        View.OnClickListener numberListener = v -> appendNumber(((Button) v).getText().toString());
        for (int id : numberIds) { findViewById(id).setOnClickListener(numberListener); }
    }


    // This method sets the onClick listeners for all operator buttons
    private void setOperatorButtons() {
        int[] operatorIds = {R.id.plus, R.id.subtract, R.id.multiply, R.id.divide};
        View.OnClickListener operatorListener = v -> selectOperator(((Button) v).getText().toString());
        for (int id : operatorIds) { findViewById(id).setOnClickListener(operatorListener); }
    }


    // This appends the numbers given by the user in the calculator
    private void appendNumber(String number) {
        if (number.equals(".") && currentOperand.contains(".")) return;
        if (isResultDisplayed) {
            currentOperand = "";
            isResultDisplayed = false;
        }
        currentOperand += number;
        updateDisplay();
    }


    // This method handles the logic for selecting an operator
    private void selectOperator(String selectedOperator) {
        if (!currentOperand.isEmpty()) {
            if (!operator.isEmpty()) doCalculation();
            operator = selectedOperator;
            previousOperand = currentOperand;
            currentOperand = "";
        } else if (!previousOperand.isEmpty()) { operator = selectedOperator; } //update operator if previous one exists
        updateDisplay();
    }


    // This method performs the calculations on the operands
    private void doCalculation() {
        if (currentOperand.isEmpty() || previousOperand.isEmpty() || operator.isEmpty()) return;

        double firstOperand = Double.parseDouble(previousOperand);
        double secondOperand = Double.parseDouble(currentOperand);
        double result;

        switch (operator) {
            case "+":
                result = firstOperand + secondOperand;
                break;
            case "-":
                result = firstOperand - secondOperand;
                break;
            case "*":
                result = firstOperand * secondOperand;
                break;
            case "/":
                if (secondOperand != 0) {
                    result = firstOperand / secondOperand;
                } else {
                    result = 0;
                }
                break;
            default:
                return;
        }
        currentOperand = formatResult(result);
        previousOperand = "";
        operator = "";
        updateDisplay();
    }


    // This method clears the recent char
    private void clearLast() {
        if (!currentOperand.isEmpty()) {
            currentOperand = currentOperand.substring(0, currentOperand.length() - 1);
        } else if (!operator.isEmpty()) operator = "";
          else if (!previousOperand.isEmpty())  previousOperand = "";
        updateDisplay();
    }


    // This method clears all the values
    private void allClear() {
        currentOperand = "";
        previousOperand = "";
        storedValue = "0";
        operator = "";
        resultDisplay.setText("0");
        savedValueDisplay.setText("Saved Value: 0");
    }


    // This method displays the values in the textField of the calculator
    private void updateDisplay() {
        String displayText = previousOperand + " " + operator + " " + currentOperand;
        if (displayText.trim().isEmpty()) resultDisplay.setText("0"); // If display empty, set to 0
        else resultDisplay.setText(displayText.trim());
    }


    // This method formats the result in desired way
    private String formatResult(double result) {
        DecimalFormat df = new DecimalFormat("#.###");
        return df.format(result);
    }


    // This method saves the value in the variable for implementing 'Save' feature
    private void saveValue() {
        if (!currentOperand.isEmpty()) {
            storedValue = currentOperand;
            updateSavedValueDisplay();
            }
    }


    // This method is used to retrieve the stored value and assign it to current operand
    private void retrieveValue() {
        currentOperand = storedValue;
        updateDisplay();
    }


    // This method shows the saved value in the small text field of the calculator
    private void updateSavedValueDisplay() {
        savedValueDisplay.setText("Saved Value: " + storedValue);
    }
}
