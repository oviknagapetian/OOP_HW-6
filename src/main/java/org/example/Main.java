package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CalculatorView view = new CalculatorView();
            CalculatorPresenter presenter = new CalculatorPresenter(view, new BasicCalculator());
            presenter.start();
        });
    }

    // Интерфейс для арифметических операций
    public interface CalculatorOperation {
        double calculate(double number1, double number2) throws ArithmeticException;
    }

    // Классы реализации арифметических операций
    public static class AdditionOperation implements CalculatorOperation {
        @Override
        public double calculate(double number1, double number2) {
            return number1 + number2;
        }
    }

    public static class SubtractionOperation implements CalculatorOperation {
        @Override
        public double calculate(double number1, double number2) {
            return number1 - number2;
        }
    }

    public static class MultiplicationOperation implements CalculatorOperation {
        @Override
        public double calculate(double number1, double number2) {
            return number1 * number2;
        }
    }

    public static class DivisionOperation implements CalculatorOperation {
        @Override
        public double calculate(double number1, double number2) throws ArithmeticException {
            if (number2 != 0) {
                return number1 / number2;
            } else {
                throw new ArithmeticException("Ошибка: Нельзя делить на ноль.");
            }
        }
    }

    // Модель (Model) для калькулятора
    public static class BasicCalculator {
        // Метод для выполнения арифметической операции
        public double calculate(double num1, double num2, String operator) throws IllegalArgumentException {
            CalculatorOperation operation;

            switch (operator) {
                case "+":
                    operation = new AdditionOperation();
                    break;
                case "-":
                    operation = new SubtractionOperation();
                    break;
                case "*":
                    operation = new MultiplicationOperation();
                    break;
                case "/":
                    operation = new DivisionOperation();
                    break;
                default:
                    throw new IllegalArgumentException("Ошибка: Неверный оператор.");
            }

            return operation.calculate(num1, num2);
        }
    }

    // Представление (View) для калькулятора с графическим интерфейсом
    public static class CalculatorView extends JFrame {
        private JTextField numberField1;
        private JTextField numberField2;
        private JLabel resultLabel;
        private JComboBox<String> operatorComboBox;
        private JButton calculateButton;

        public CalculatorView() {
            setTitle("Калькулятор"); // Заголовок окна
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Действие при закрытии окна
            setLayout(new FlowLayout()); // Определение компоновки элементов интерфейса

            // Создание элементов интерфейса
            numberField1 = new JTextField(10);
            numberField2 = new JTextField(10);
            resultLabel = new JLabel("Результат: ");
            operatorComboBox = new JComboBox<>(new String[]{"+", "-", "*", "/"}); // Выпадающий список с операциями
            calculateButton = new JButton("Вычислить"); // Кнопка для вычисления результата

            // Обработчик события нажатия на кнопку "Вычислить"
            calculateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String operator = (String) operatorComboBox.getSelectedItem(); // Получаем выбранную операцию
                    double num1 = Double.parseDouble(numberField1.getText()); // Получаем первое число
                    double num2 = Double.parseDouble(numberField2.getText()); // Получаем второе число

                    try {
                        double result = calculateResult(num1, num2, operator); // Вычисляем результат
                        resultLabel.setText("Результат: " + result); // Выводим результат на метку
                    } catch (ArithmeticException ex) {
                        resultLabel.setText(ex.getMessage()); // Выводим сообщение об ошибке, если она возникла
                    } catch (NumberFormatException ex) {
                        resultLabel.setText("Ошибка: Некорректные входные данные."); // Выводим сообщение об ошибке, если введены некорректные данные
                    }
                }
            });

            // Добавление элементов интерфейса на окно
            add(numberField1);
            add(operatorComboBox);
            add(numberField2);
            add(calculateButton);
            add(resultLabel);

            setSize(400, 150); // Установка статических размеров окна (ширина и высота)
            setLocationRelativeTo(null); // Окно будет по центру экрана
            setVisible(true); // Отображаем окно
        }

        // Метод для выполнения арифметической операции в зависимости от выбранного оператора
        private double calculateResult(double num1, double num2, String operator) throws ArithmeticException {
            BasicCalculator calculator = new BasicCalculator();

            switch (operator) {
                case "+":
                    return calculator.calculate(num1, num2, "+");
                case "-":
                    return calculator.calculate(num1, num2, "-");
                case "*":
                    return calculator.calculate(num1, num2, "*");
                case "/":
                    return calculator.calculate(num1, num2, "/");
                default:
                    throw new IllegalArgumentException("Ошибка: Неверный оператор.");
            }
        }
    }

    // Презентер (Presenter) для калькулятора
    public static class CalculatorPresenter {
        private CalculatorView view;
        private BasicCalculator calculator;

        public CalculatorPresenter(CalculatorView view, BasicCalculator calculator) {
            this.view = view;
            this.calculator = calculator;
        }

        public void start() {
            view.setVisible(true); // Показываем окно с интерфейсом калькулятора
        }
    }
}
