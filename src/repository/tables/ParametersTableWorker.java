package repository.tables;

import repository.ConsoleHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ParametersTableWorker extends AbstractTableWorker {
    private final Connection connection;
    private final ConsoleHandler consoleHandler;

    public static final String TABLE_NAME = "parameters";

    public ParametersTableWorker(Connection connection, ConsoleHandler consoleHandler) {
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
                int parametersID = resultSet.getInt(1);
                double weight = resultSet.getDouble(2);
                double height = resultSet.getDouble(3);
                String form = resultSet.getString(4);
                String material = resultSet.getString(5);
                System.out.printf("%5d|%-10.2f|%-10.2f|%-20s|%s\n", parametersID, weight, height, form, material);
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
            double weight = consoleHandler.getDoubleValueFromUser("Вес андроида: ", consoleHandler.getScanner());
            double height = consoleHandler.getDoubleValueFromUser("Высота андроида: ", consoleHandler.getScanner());

            System.out.print("Форма андроида: ");
            String form = consoleHandler.getScanner().nextLine();

            System.out.print("Материал андроида: ");
            String material = consoleHandler.getScanner().nextLine();

            String query = "INSERT INTO " + TABLE_NAME + " (weight, height, form, material) VALUES ("
                    + weight + ", " + height + ", \""
                    + form + "\", \"" + material + "\");";

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
                    "Новый вес андроида: ",
                    "UPDATE " + TABLE_NAME + " SET weight = %s WHERE id = " + id
            );
            case 2 -> result = updateDoubleValueOfTable(
                    consoleHandler,
                    connection,
                    "Новая высота андроида: ",
                    "UPDATE " + TABLE_NAME + " SET height = %s WHERE id = " + id
            );
            case 3 -> result = updateStringValueOfTable(
                    connection,
                    "Новая форма андроида: ",
                    "UPDATE " + TABLE_NAME + " SET form = %s WHERE id = " + id,
                    consoleHandler.getScanner()
            );
            case 4 -> result = updateStringValueOfTable(
                    connection,
                    "Новый материал андроида: ",
                    "UPDATE " + TABLE_NAME + " SET material = %s WHERE id = " + id,
                    consoleHandler.getScanner()
            );
            default -> result = -1;
        }

        return result;
    }

    private void initEditMenu() {
        System.out.println("\n\t\t--- Меню редактирования ---");
        System.out.println("1. Редактирование веса андроида");
        System.out.println("2. Редактирование высоты андроида");
        System.out.println("3. Редактирование формы андроида");
        System.out.println("4. Редактирование материала андроида");
        System.out.println("0. Отмена");
        System.out.print("Для продолжения выберите пункт меню: ");
    }
}