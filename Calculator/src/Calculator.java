import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class Calculator {
    int boardWidth = 400;
    int boardHeight = 600;

    Color customLightGrey = new Color(212, 212, 210);
    Color customDarkGrey = new Color(80, 80, 80);
    Color customBlack = new Color(28, 28, 28);
    Color customOrange = new Color(255, 149, 0);
    
    String[] buttonValues = {
        "AC", "+/-", "%", "÷",
        "7", "8", "9", "×",
        "4", "5", "6", "-",
        "1", "2", "3", "+",
        "0", ".", "√", "="
    };
    String[] rightSymbols = {"÷", "×", "-", "+", "="};
    String[] topSymbols = {"AC", "+/-", "%"};

    JFrame frame = new JFrame("Calculator");
    JLabel displayLabel = new JLabel();
    JPanel displayPanel = new JPanel();
    JPanel buttonsPanel = new JPanel();

    // A+B, A-B, A*B, A/B
    String A = "0";
    String operator = null;
    String B = null;
    boolean startNewNumber = false;

    Calculator(){
        //frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        displayLabel.setBackground(customBlack);
        displayLabel.setForeground(Color.white);
        displayLabel.setFont(new Font("Arial", Font.PLAIN, 80));
        displayLabel.setHorizontalAlignment(JLabel.RIGHT);
        displayLabel.setText("0");
        displayLabel.setOpaque(true);


        displayPanel.setLayout(new BorderLayout());
        displayPanel.add(displayLabel);
        frame.add(displayPanel, BorderLayout.NORTH);

        buttonsPanel.setLayout(new GridLayout(5, 4));
        buttonsPanel.setBackground(customBlack);
        frame.add(buttonsPanel);

        for (int i = 0; i < buttonValues.length; i++) {
            JButton button = new JButton();
            String buttonValue = buttonValues[i];
            button.setFont(new Font("Arial", Font.PLAIN, 20));
            button.setText(buttonValue);
            button.setFocusable(false);
            button.setOpaque(true);
            button.setBorderPainted(true);
            button.setBorder(new LineBorder(customBlack, 1));
            if(Arrays.asList(topSymbols).contains(buttonValue)){
                button.setBackground(customLightGrey);
                button.setForeground(customBlack);
            }else if (Arrays.asList(rightSymbols).contains(buttonValue)){
                button.setBackground(customOrange);
                button.setForeground(Color.white);
            }else{
                button.setBackground(customDarkGrey);
                button.setForeground(Color.white);
            }
            buttonsPanel.add(button);

            button.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    JButton button = (JButton) e.getSource();
                    String buttonValue = button.getText();

                    if ("Error".equals(displayLabel.getText()) && "AC".equals(buttonValue)){
                        clearAll();
                        displayLabel.setText("0");
                    }

                    if (Arrays.asList(rightSymbols).contains(buttonValue)){
                        if ("=".equals(buttonValue)){
                            if (operator != null){
                                B = displayLabel.getText();
                                displayLabel.setText(calculate(A, operator, B));
                                clearAll();
                                startNewNumber = true;
                            }
                        }
                        else {
                            if (operator == null && !startNewNumber){
                                displayLabel.setText(calculate(A, operator, displayLabel.getText()));
                            }
                            A = displayLabel.getText();
                            operator = buttonValue;
                            startNewNumber = true;
                            
                        }
                    }else if (Arrays.asList(topSymbols).contains(buttonValue)){
                        if ("AC".equals(buttonValue)){
                            clearAll();
                            displayLabel.setText("0");
                        }else if ("+/-".equals(buttonValue)) {
                            double numDisplay = Double.parseDouble(displayLabel.getText());
                            numDisplay *= -1;
                            displayLabel.setText(removeZeroDecimal(numDisplay));
                        }else if ("%".equals(buttonValue)){
                            double numDisplay = Double.parseDouble(displayLabel.getText());
                            numDisplay /= 100;
                            displayLabel.setText(removeZeroDecimal(numDisplay));
                        }
                    } else { // digits or .
                        if("√".equals(buttonValue)){
                            double numDisplay = Double.parseDouble(displayLabel.getText());
                            if (numDisplay < 0){
                                displayLabel.setText("Error");
                                clearAll();
                            }else{
                                displayLabel.setText(removeZeroDecimal(Math.sqrt(numDisplay)));
                                startNewNumber = true;
                            }
                        }else if (".".equals(buttonValue)){
                            if (startNewNumber){
                                displayLabel.setText("0.");
                                startNewNumber = false;
                            }else if(!displayLabel.getText().contains(".")){
                                displayLabel.setText(displayLabel.getText() + buttonValue);
                            }
                        }else if ("0123456789".contains(buttonValue)){
                            if (startNewNumber || "0".equals(displayLabel.getText())){
                                displayLabel.setText(buttonValue);
                                startNewNumber = false;
                            }else if (displayLabel.getText().length() < 9){
                                displayLabel.setText(displayLabel.getText() + buttonValue);
                            }
                        } 
                    }
                }
            });
        }
        frame.setVisible(true);
    }

    String calculate(String a, String op, String b){
        double numA = Double.parseDouble(a);
        double numB = Double.parseDouble(b);

        if ("+".equals(op)) return removeZeroDecimal(numA+numB);
        if ("-".equals(op)) return removeZeroDecimal(numA-numB);
        if ("×".equals(op)) return removeZeroDecimal(numA*numB);
        if ("÷".equals(op)){
            if (numB == 0) return "Error";
            return removeZeroDecimal(numA/numB);
        }
        return b;
        
    }

    void clearAll(){
        A = "0";
        operator = null;
        B = null;
        startNewNumber = false;
    }

    String removeZeroDecimal(double numDisplay){
        if(numDisplay % 1 == 0){
            return Integer.toString((int) numDisplay);
        }
        return Double.toString(numDisplay);
    }
}
