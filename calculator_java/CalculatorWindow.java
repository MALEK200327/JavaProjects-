import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class CalculatorWindow extends JFrame {

    private JTextField outputTextField;
    private Stack<Double> operandStack;
    private Operator currentOperator;
    private boolean newInput;

    // Enum to represent arithmetic operators
    enum Operator {
        ADD, SUBTRACT, MULTIPLY, DIVIDE
    }

    public CalculatorWindow(String text) {
        super(text);
        operandStack = new Stack<>();
        currentOperator = null;
        newInput = true;

        outputTextField = new JTextField();
        outputTextField.setEditable(false);
        outputTextField.setMaximumSize(new Dimension(225, 40));
        outputTextField.setFont(new Font("Monospaced", Font.BOLD, 20));
        outputTextField.setDisabledTextColor(new Color(0, 0, 0));
        outputTextField.setMargin(new Insets(0, 5, 0, 0));

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4));

        // Create and add buttons for digits 0-9
        for (int i = 0; i <= 9; i++) {
            addButton(panel, String.valueOf(i));
        }

        // Create and add operator buttons
        addButton(panel, "+");
        addButton(panel, "-");
        addButton(panel, "*");
        addButton(panel, "/");

        // Create equals and clear buttons
        addButton(panel, "=");
        addButton(panel, "C");

        this.add(outputTextField, BorderLayout.NORTH);
        this.add(panel, BorderLayout.CENTER);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
    }

    private void addButton(JPanel panel, String label) {
        JButton button = new JButton(label);
        button.setFont(new Font("Monospaced", Font.BOLD, 22));
        // Add an ActionListener to handle button clicks
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleButtonClick(label);
            }
        });
        panel.add(button);
    }

    private void handleButtonClick(String label) {
        if (label.matches("[0-9]")) {
            if (newInput) {
                outputTextField.setText(label);
                newInput = false;
                operandStack.push(Double.parseDouble(label));
            } else {
                newInput = false;
                outputTextField.setText(outputTextField.getText() + label);
                double currentNumber = operandStack.pop();
                // Handle appending digits to the current number
                operandStack.push((currentNumber * 10) + Double.parseDouble(label));
            }
        } else if (label.equals("C")) {
            // Handle clearing the calculator
            outputTextField.setText("");
            operandStack.clear();
            currentOperator = null;
            newInput = true;
        } else if (label.equals("=")) {
            if (!newInput && currentOperator != null) {
                double operand2 = operandStack.pop();
                double operand1 = operandStack.pop();
                // Perform the calculation and display the result
                double result = performOperation(operand1, operand2, currentOperator);
                outputTextField.setText(String.valueOf(result));
                operandStack.push(result);
                currentOperator = null;
                newInput = true;
            }
        } else {
            // Handle operator selection
            Operator operator = getOperatorFromLabel(label);
            // If not a new input and a previous operator exists, evaluate the previous
            // expression
            if (!newInput && currentOperator != null) {
                // Evaluate the previous expression
                handleButtonClick("=");
            }
            currentOperator = operator;
            newInput = true;
        }
    }

    // Method to map a button label to an Operator enum value
    private Operator getOperatorFromLabel(String label) {
        switch (label) {
            case "+":
                return Operator.ADD;
            case "-":
                return Operator.SUBTRACT;
            case "*":
                return Operator.MULTIPLY;
            case "/":
                return Operator.DIVIDE;
            default:
                throw new IllegalArgumentException("Invalid operator: " + label);
        }
    }

    // Method to perform arithmetic operations
    private double performOperation(double operand1, double operand2, Operator operator) {
        switch (operator) {
            case ADD:
                return operand1 + operand2;
            case SUBTRACT:
                return operand1 - operand2;
            case MULTIPLY:
                return operand1 * operand2;
            case DIVIDE:
                if (operand2 != 0) {
                    return operand1 / operand2;
                } else {
                    // Handle division by zero error with a JOptionPane dialog
                    JOptionPane.showMessageDialog(this, "Division by zero", "Error", JOptionPane.ERROR_MESSAGE);
                    return Double.NaN;
                }
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }

}