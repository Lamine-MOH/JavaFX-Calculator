import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

// import java.math.*;

public class Controller {

    @FXML
    private Button add;

    @FXML
    private Button clearBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button dives;

    @FXML
    private Button eight;

    @FXML
    private Button equal;

    @FXML
    private Button five;

    @FXML
    private Button four;

    @FXML
    private Label input;

    @FXML
    private Button leftArc;

    @FXML
    private Button multi;

    @FXML
    private Button nine;

    @FXML
    private Button one;

    @FXML
    private Label output;

    @FXML
    private Button rightArc;

    @FXML
    private Button seven;

    @FXML
    private Button six;

    @FXML
    private Button sub;

    @FXML
    private Button three;

    @FXML
    private Button two;

    @FXML
    private Button zero;

    boolean validProcess(String text) {
        if (!"0123456789".contains(text.charAt(text.length() - 1) + "")) {
            return false;
        }

        int practices = 0;

        int i = 0;
        while (i < text.length()) {
            if (text.charAt(i) == '(') {
                practices++;
            } else if (text.charAt(i) == ')') {
                practices--;
            }

            i++;
        }

        return practices == 0;

    }

    int getFirstNumStart(String process, int operationIndex) {
        int firstNumStart = 0;
        // get the first num star //
        for (int i = operationIndex - 1; i >= 0; i--) {
            if (!"0123456789.".contains(process.charAt(i) + "")) {
                if (process.charAt(i) == '-' && (i == 0 || "+-*/^".contains(process.charAt(i - 1) + ""))) {
                    firstNumStart = i;
                } else {
                    firstNumStart = i + 1;
                }
                break;
            }
        }
        // //
        return firstNumStart;
    }

    int getSecondNumEnd(String process, int operationIndex) {
        if (process.charAt(operationIndex + 1) == '-') {
            operationIndex++;
        }

        int secondNumEnd = process.length() - 1;
        // get the second num end //
        for (int i = operationIndex + 1; i < process.length(); i++) {
            if (!"0123456789.".contains(process.charAt(i) + "")) {
                secondNumEnd = i - 1;
                break;
            }
        }
        // //
        return secondNumEnd;
    }

    String removePowers(String process) {
        while (process.contains("^")) {
            int powerIndex = 0;
            // get the power index //
            for (int i = process.length() - 1; i >= 0; i--) {
                if (process.charAt(i) == '^') {
                    powerIndex = i;
                    break;
                }
            }
            // //

            int firstNumStart = getFirstNumStart(process, powerIndex);

            int secondNumEnd = getSecondNumEnd(process, powerIndex);
            // //

            double firstNum = Double.parseDouble(process.substring(firstNumStart, powerIndex));
            double secondNum = Double.parseDouble(process.substring(powerIndex + 1, secondNumEnd + 1));

            Double result = Math.pow(firstNum, secondNum);

            String before = 0 == firstNumStart ? "" : process.substring(0, firstNumStart);
            String after = secondNum + 1 == process.length() ? ""
                    : process.substring(secondNumEnd + 1, process.length());

            process = before + result + after;

        }

        return process;
    }

    String removeMultiAndDiv(String process) {
        while (process.contains("*") || process.contains("/")) {
            String op = "";
            int operationIndex = 0;
            // get the power index //
            for (int i = 0; i < process.length(); i++) {
                if ("*/".contains(process.charAt(i) + "")) {
                    op += process.charAt(i);
                    operationIndex = i;
                    break;
                }
            }
            // //

            int firstNumStart = getFirstNumStart(process, operationIndex);

            int secondNumEnd = getSecondNumEnd(process, operationIndex);
            // //

            double firstNum = Double.parseDouble(process.substring(firstNumStart, operationIndex));
            double secondNum = Double.parseDouble(process.substring(operationIndex + 1, secondNumEnd + 1));

            Double result = 0.0;

            switch (op) {
                case "*":
                    result = firstNum * secondNum;
                    break;

                case "/":
                    result = firstNum / secondNum;
                    break;

                default:
                    break;
            }

            String before = 0 == firstNumStart ? "" : process.substring(0, firstNumStart);
            String after = secondNum + 1 == process.length() ? ""
                    : process.substring(secondNumEnd + 1, process.length());

            process = before + result + after;

        }

        return process;

    }

    String removeAddAndSub(String process) {

        while (process.contains("+") || process.contains("-")) {
            if (process.charAt(0) == '-') {
                boolean test = false;

                for (int i = 1; i < process.length(); i++) {
                    if ("+-".contains(process.charAt(i) + "")) {
                        test = true;
                        break;
                    }
                }

                if (test == false) {
                    break;
                }
            }

            String op = "";
            int operationIndex = 0;
            // get the power index //
            for (int i = 1; i < process.length(); i++) {
                if ("+-".contains(process.charAt(i) + "")) {
                    op += process.charAt(i);
                    operationIndex = i;
                    break;
                }
            }
            // //

            int firstNumStart = getFirstNumStart(process, operationIndex);

            int secondNumEnd = getSecondNumEnd(process, operationIndex);
            // //

            double firstNum = firstNumStart == operationIndex ? 0
                    : Double.parseDouble(process.substring(firstNumStart, operationIndex));
            double secondNum = Double.parseDouble(process.substring(operationIndex + 1, secondNumEnd + 1));

            Double result = 0.0;

            switch (op) {
                case "+":
                    result = firstNum + secondNum;
                    break;

                case "-":
                    result = firstNum - secondNum;
                    break;

                default:
                    break;
            }

            String before = 0 == firstNumStart ? "" : process.substring(0, firstNumStart);
            String after = secondNumEnd + 1 == process.length() ? ""
                    : process.substring(secondNumEnd + 1, process.length());

            process = before + result + after;

        }

        return process;

    }

    String calcProcess(String process) {
        process = removePowers(process);
        process = removeMultiAndDiv(process);
        process = removeAddAndSub(process);

        return process;
    }

    @FXML
    void calculi(ActionEvent event) {
        String text = input.getText();

        if (!validProcess(text)) {
            output.setText("Invalid process");
        } else {

            while (text.contains("(")) {
                int openBracts = -1;
                int closeBracts = text.length();

                for (int i = 0; i < text.length(); i++) {
                    if (text.charAt(i) == '(') {
                        openBracts = i;
                    } else if (text.charAt(i) == ')') {
                        closeBracts = i;
                        break;
                    }
                }

                String process = text.substring(openBracts + 1, closeBracts);
                String result = calcProcess(process);

                String before = 0 == openBracts ? "" : text.substring(0, openBracts);
                String after = closeBracts + 1 == text.length() ? "" : text.substring(closeBracts + 1, text.length());

                text = before + result + after;
            }
            text = calcProcess(text);

            output.setText(text);
        }
    }

    @FXML
    void click(ActionEvent event) {

        String numbs = "0123456789";
        // String op = "+-*/";

        String value = event.getTarget().toString().split("'")[1];
        String text = input.getText();

        if ("0".contains(value)) {
            if (text.length() > 0 && text.charAt(text.length() - 1) == '0'
                    && (text.length() == 1 || !"0123456789.".contains(text.charAt(text.length() - 2) + ""))) {
                text = text.substring(0, text.length() - 1);
            }

            text += "0";

        } else if ("123456789".contains(value)) {
            if (text.length() > 0 && text.charAt(text.length() - 1) == '0'
                    && (text.length() == 1 || !"0123456789".contains(text.charAt(text.length() - 2) + ""))) {
                text = text.substring(0, text.length() - 1);
            }

            text += value;
        } else if ("+-*/".contains(value)) {
            if (text.length() > 0 && "+-*/^".contains(text.charAt(text.length() - 1) + "")) {
                text = text.substring(0, text.length() - 1);
                text += value;
            } else if (text.length() == 0 || !".".contains(text.charAt(text.length() - 1) + "")) {
                if (!(text.length() == 0 && "*/".contains(value))) {
                    text += value;
                }
            }
        } else if (".".contains(value)) {
            if (text.length() > 0 && numbs.contains(text.charAt(text.length() - 1) + "")) {
                boolean test = true;

                int i = text.length() - 2;
                while (i >= 0) {
                    if (".".contains(text.charAt(i) + "")) {
                        test = false;
                        break;
                    } else if (!numbs.contains(text.charAt(i) + "")) {
                        break;
                    }
                    i--;
                }

                if (test) {
                    text += ".";
                }
            }
        } else if ("(".contains(value)) {
            if (text.length() == 0 || "+-*/^(".contains(text.charAt(text.length() - 1) + "")) {
                text += "(";
            }
        } else if (")".contains(value)) {
            if (text.length() > 0 && "0123456789)".contains(text.charAt(text.length() - 1) + "")) {
                text += ")";
            }
        } else if ("^".contains(value)) {
            if (text.length() > 0 && "0123456789)".contains(text.charAt(text.length() - 1) + "")) {
                text += "^";
            }
        }

        input.setText(text);
    }

    @FXML
    void clear(ActionEvent event) {
        input.setText("");
    }

    @FXML
    void delete(ActionEvent event) {
        String text = input.getText();

        if (text.length() > 0) {
            input.setText(text.substring(0, text.length() - 1));
        }
    }

}
