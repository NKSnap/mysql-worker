package repository.tables;

import repository.ConsoleHandler;
import repository.interfacies.TableWorker;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public abstract class AbstractTableWorker implements TableWorker {
    public int updateIntegerValueOfTable(ConsoleHandler consoleHandler, Connection connection, String info, String queryPart) {
        int result;
        try {
            int newValue = consoleHandler.getIntegerValueFromUser(info, consoleHandler.getScanner());
            String query = String.format(queryPart, newValue);
            result = executeRequest(query, connection);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            result = -1;
        }

        return result;
    }

    public int updateDoubleValueOfTable(ConsoleHandler consoleHandler, Connection connection, String info, String queryPart) {
        int result;
        try {
            double newValue = consoleHandler.getDoubleValueFromUser(info, consoleHandler.getScanner());
            String query = String.format(queryPart, String.valueOf(newValue).replace(",", "."));
            result = executeRequest(query, connection);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            result = -1;
        }

        return result;
    }

    public int updateStringValueOfTable(Connection connection, String info, String queryPart, Scanner scanner) {
        int result;
        try {
            System.out.print(info);
            String newValue = scanner.nextLine();
            String query = String.format(queryPart, String.format("\"%s\"", newValue));
            result = executeRequest(query, connection);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            result = -1;
        }

        return result;
    }

    public int executeRequest(String query, Connection connection) {
        int response = 1;
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            response = -1;
        }

        return response;
    }
}