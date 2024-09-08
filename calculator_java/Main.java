/**
 * Author : Ayeshmantha Wijayagunethilake
 * Date : 5/10/2023
 * This class is used to launch a new Calculator Window.
 */

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        final String text = "Calculator";
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final CalculatorWindow window = new CalculatorWindow(text);
                window.setVisible(true);
            }
        });
    }
}