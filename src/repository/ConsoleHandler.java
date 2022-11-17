package repository;

import java.util.Scanner;

public class ConsoleHandler {
    private Scanner scanner;

    public ConsoleHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public int getUserIntegerChoice() {
        int key = -2;
        try{
            key = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("\nОшибка ввода! Введите число!");
        }

        return key;
    }

    public double getDoubleValueFromUser(String message, Scanner scanner) throws IllegalArgumentException {
        double result;
        try {
            System.out.print("\n" + message);
            result = Double.parseDouble(scanner.nextLine());
        } catch (Exception e) {
            throw new IllegalArgumentException("Неверный формат ожидаемого значения!");
        }

        return result;
    }

    public int getIntegerValueFromUser(String message, Scanner scanner) throws IllegalArgumentException {
        int result;
        try {
            System.out.print("\n" + message);
            result = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            throw new IllegalArgumentException("Неверный формат ожидаемого значения!");
        }

        return result;
    }

    public Scanner getScanner() {
        return scanner;
    }
}
