import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

public class calculator {

    private static final String BUTTON_LABEL_CLEAR = "CLR";
    private static final String BUTTON_LABEL_PLUS = "+";
    private static final String BUTTON_LABEL_MINUS = "-";
    private static final String BUTTON_LABEL_MULTIPLY = "*";
    private static final String BUTTON_LABEL_DIVIDE = "/";
    private static final String BUTTON_LABEL_MODULO = "%";
    private static final String BUTTON_LABEL_EQUAL = "=";
    private static final String BUTTON_LABEL_0 = "0";
    private static final String BUTTON_LABEL_1 = "1";
    private static final String BUTTON_LABEL_2 = "2";
    private static final String BUTTON_LABEL_3 = "3";
    private static final String BUTTON_LABEL_4 = "4";
    private static final String BUTTON_LABEL_5 = "5";
    private static final String BUTTON_LABEL_6 = "6";
    private static final String BUTTON_LABEL_7 = "7";
    private static final String BUTTON_LABEL_8 = "8";
    private static final String BUTTON_LABEL_9 = "9";

    private static boolean ongoingCalculation;
    private static boolean ongoingInput;
    private static int firstNumber;
    private static int secondNumber;
    private static Operator operator;
    private static final JFormattedTextField outputBox = new JFormattedTextField();
    private static final List<JButton> buttonList = new ArrayList<>();

    public static void main(String[] args) {
        initialize();
        layoutUi();
    }

    private static void initialize() {
        ongoingCalculation = false;
        firstNumber = 0;

        outputBox.setEditable(false);
        outputBox.setValue("");
        outputBox.setPreferredSize(new Dimension(250, 30));
        outputBox.setHorizontalAlignment(SwingConstants.RIGHT);
        outputBox.setFont(new Font("Arial", Font.PLAIN, 24));

        addButton(BUTTON_LABEL_0);
        addButton(BUTTON_LABEL_1);
        addButton(BUTTON_LABEL_2);
        addButton(BUTTON_LABEL_3);
        addButton(BUTTON_LABEL_4);
        addButton(BUTTON_LABEL_5);
        addButton(BUTTON_LABEL_6);
        addButton(BUTTON_LABEL_7);
        addButton(BUTTON_LABEL_8);
        addButton(BUTTON_LABEL_9);
        addButton(BUTTON_LABEL_MODULO);
        addButton(BUTTON_LABEL_PLUS);
        addButton(BUTTON_LABEL_MINUS);
        addButton(BUTTON_LABEL_MULTIPLY);
        addButton(BUTTON_LABEL_DIVIDE);
        addButton(BUTTON_LABEL_EQUAL);
        addButton(BUTTON_LABEL_CLEAR, 250, 30);
    }

    private static void layoutUi() {
        JPanel panel1 = new JPanel();
        panel1.setLayout(new FlowLayout());
        panel1.add(outputBox, BorderLayout.CENTER);

        JPanel panel2 = new JPanel();
        panel2.add(getButton(BUTTON_LABEL_MODULO));
        panel2.add(getButton(BUTTON_LABEL_MINUS));
        panel2.add(getButton(BUTTON_LABEL_PLUS));
        panel2.add(getButton(BUTTON_LABEL_DIVIDE));

        JPanel panel3 = new JPanel();
        panel3.add(getButton(BUTTON_LABEL_5));
        panel3.add(getButton(BUTTON_LABEL_3));
        panel3.add(getButton(BUTTON_LABEL_1));
        panel3.add(getButton(BUTTON_LABEL_EQUAL));

        JPanel panel4 = new JPanel();
        panel4.add(getButton(BUTTON_LABEL_6));
        panel4.add(getButton(BUTTON_LABEL_7));
        panel4.add(getButton(BUTTON_LABEL_4));
        panel4.add(getButton(BUTTON_LABEL_MULTIPLY));

        JPanel panel5 = new JPanel();
        panel5.add(getButton(BUTTON_LABEL_0));
        panel5.add(getButton(BUTTON_LABEL_8));
        panel5.add(getButton(BUTTON_LABEL_2));
        panel5.add(getButton(BUTTON_LABEL_9));

        JPanel panel6 = new JPanel();
        panel1.add(outputBox, BorderLayout.SOUTH);
        panel6.add(getButton(BUTTON_LABEL_CLEAR));

        JPanel outer = new JPanel(new FlowLayout());
        outer.add(panel1);
        outer.add(panel2);
        outer.add(panel3);
        outer.add(panel4);
        outer.add(panel5);
        outer.add(panel6);

        JFrame frame = new JFrame("Calculator");
        frame.add(outer);
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private static JButton getButton(String label) {
        return buttonList.stream().filter(b -> label.equals(b.getText())).findAny().orElse(null);
    }

    private static void addButton(String label) {
        addButton(label, 60, 30);
    }

    private static void addButton(String label, int width, int height) {
        JButton button = createButton(label);
        button.setPreferredSize(new Dimension(width, height));
        button.addActionListener(e -> processClickedButton(e.getActionCommand()));
        buttonList.add(button);
    }

    private static JButton createButton(String label) {
        JButton button = new JButton();
        button.setText(label);
        button.setFont(new Font("Arial", Font.PLAIN, 24));
        return button;
    }

    private static void processClickedButton(String label) {
        switch (label) {
            case BUTTON_LABEL_CLEAR:
                outputBox.setValue("");
                reset();
                break;
            case BUTTON_LABEL_PLUS:
                processSecondNumber(Operator.ADD);
                break;
            case BUTTON_LABEL_MINUS:
                if (!ongoingCalculation && outputBox.getValue() == "") {
                    outputBox.setValue("-");
                    ongoingInput = true;
                } else {
                    processSecondNumber(Operator.SUBTRACT);
                }
                break;
            case BUTTON_LABEL_MULTIPLY:
                processSecondNumber(Operator.MULTIPLY);
                break;
            case BUTTON_LABEL_DIVIDE:
                processSecondNumber(Operator.DIVIDE);
                break;
            case BUTTON_LABEL_MODULO:
                processSecondNumber(Operator.MODULO);
                break;
            case BUTTON_LABEL_EQUAL:
                if (outputBox.getText().equals("ERROR")) return;
                if (firstNumber < 0 || Integer.parseInt(outputBox.getText()) < 0) {
                    outputBox.setValue("ERROR");
                    reset();
                    break;
                }

                int result;
                if (ongoingCalculation) {
                    secondNumber = Integer.parseInt(outputBox.getText());
                    ongoingCalculation = false;
                }
                result = calculate();
                outputBox.setValue(result);
                System.out.println(firstNumber + " [" + operator + "] " + secondNumber + " = " + result);
                firstNumber = Integer.parseInt(outputBox.getText());
                ongoingInput = false;
                break;
            case BUTTON_LABEL_0:
            case BUTTON_LABEL_1:
            case BUTTON_LABEL_2:
            case BUTTON_LABEL_3:
            case BUTTON_LABEL_4:
            case BUTTON_LABEL_5:
            case BUTTON_LABEL_6:
            case BUTTON_LABEL_7:
            case BUTTON_LABEL_8:
            case BUTTON_LABEL_9:
                if (ongoingInput) {
                    outputBox.setValue(outputBox.getText() + label);
                } else {
                    outputBox.setValue(label);
                    ongoingInput = true;
                }
        }
    }

    private static void reset() {
        ongoingCalculation = false;
        ongoingInput = false;
        firstNumber = 0;
        secondNumber = 0;
    }

    private static void processSecondNumber(Operator operator) {
        if (outputBox.getText().equals("ERROR")) return;
        firstNumber = Integer.parseInt(outputBox.getText());
        secondNumber = 0;
        ongoingCalculation = true;
        ongoingInput = false;
        calculator.operator = operator;
    }

    private static int calculate() {
        switch (operator) {
            case ADD:
                return firstNumber + secondNumber;
            case SUBTRACT:
                return firstNumber - secondNumber;
            case MULTIPLY:
                return firstNumber * secondNumber;
            case DIVIDE:
                return firstNumber / secondNumber;
            case MODULO:
                return Math.floorMod(firstNumber, secondNumber);
            default:
                return 0;
        }
    }

    private enum Operator {
        ADD, SUBTRACT, MULTIPLY, DIVIDE, MODULO
    }
}
