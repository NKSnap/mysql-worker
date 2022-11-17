package repository.tables;

import repository.ConsoleHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RatingTableWorker extends AbstractTableWorker {
    private final Connection connection;
    private final ConsoleHandler consoleHandler;

    public static final String TABLE_NAME = "rating";

    public RatingTableWorker(Connection connection, ConsoleHandler consoleHandler) {
        this.connection = connection;
        this.consoleHandler = consoleHandler;
    }

    @Override
    public int readAllRecords() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            System.out.println("\nРезультаты:");
            while (resultSet.next()) {
                int ratingID = resultSet.getInt(1);
                double ratingKinopoisk = resultSet.getDouble(2);
                double ratingIvi = resultSet.getDouble(3);
                System.out.printf("%5d|%-5.2f|%5.2f\n", ratingID, ratingKinopoisk, ratingIvi);
            }
        } catch (SQLException e) {
            return -1;
        }

        return 1;
    }

    @Override
    public int deleteRecordByID(int id) {
        String query = "DELETE FROM " + TABLE_NAME + " WHERE id = " + id;
        return executeRequest(query, connection);
    }

    @Override
    public int insertNewRecord() {
        try {
            double kinopoisk = consoleHandler.getDoubleValueFromUser("Рейтинг на \"kinopoisk\": ", consoleHandler.getScanner());
            double ivi = consoleHandler.getDoubleValueFromUser("Рейтинг на \"ivi\": ", consoleHandler.getScanner());

            String query = "INSERT INTO " + TABLE_NAME + " (kinopoisk, ivi) VALUES (" + kinopoisk + ", " + ivi + ");";
            return executeRequest(query, connection);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    @Override
    public int updateNewRecord(int id) {
        initEditMenu();
        int userChoiceResponse = consoleHandler.getUserIntegerChoice();

        int result;
        switch (userChoiceResponse) {
            case 0 -> result = 0;
            case 1 -> result = updateDoubleValueOfTable(
                    consoleHandler,
                    connection,
                    "Новое значение рейтинга на kinopoisk: ",
                    "UPDATE " + TABLE_NAME + " SET kinopoisk = %s WHERE id = " + id
            );
            case 2 -> result = updateDoubleValueOfTable(
                    consoleHandler,
                    connection,
                    "Новое значение рейтинга на ivi: ",
                    "UPDATE " + TABLE_NAME + " SET ivi = %s WHERE id = " + id
            );
            default -> result = -1;
        }

        return result;
    }

    private void initEditMenu() {
        System.out.println("\n\t\t--- Меню редактирования ---");
        System.out.println("1. Редактирование рейтинга на kinopoisk");
        System.out.println("2. Редактирование рейтинга на ivi");
        System.out.println("0. Отмена");
        System.out.print("Для продолжения выберите пункт меню: ");
    }
}