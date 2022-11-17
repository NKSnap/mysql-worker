package repository.tables;

import repository.ConsoleHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ProducerTableWorker extends AbstractTableWorker {
    private final Connection connection;
    private final ConsoleHandler consoleHandler;

    public static final String TABLE_NAME = "producer";

    public ProducerTableWorker(Connection connection, ConsoleHandler consoleHandler) {
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
                int producerID = resultSet.getInt(1);
                String producerName = resultSet.getString(2);
                String producerNominations = resultSet.getString(3);
                System.out.printf("%5d|%-20s|%s\n", producerID, producerName, producerNominations);
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
        System.out.print("Имя режиссера: ");
        String producerName = consoleHandler.getScanner().nextLine();
        System.out.print("\nНоминации режиссера: ");
        String producerNominations = consoleHandler.getScanner().nextLine();

        String query = "INSERT INTO " + TABLE_NAME + " (producer_name, producer_nominations) VALUES (\""
                + producerName + "\", \""
                + producerNominations + "\");";

        return executeRequest(query, connection);
    }

    @Override
    public int updateNewRecord(int id) {
        initEditMenu();
        int userChoiceResponse = consoleHandler.getUserIntegerChoice();

        int result;
        switch (userChoiceResponse) {
            case 0 -> result = 0;
            case 1 -> result = updateStringValueOfTable(
                    connection,
                    "Новое имя режиссера: ",
                    "UPDATE " + TABLE_NAME + " SET producer_name = %s WHERE id = " + id,
                    consoleHandler.getScanner()
            );
            case 2 -> result = updateStringValueOfTable(
                    connection,
                    "Новые номинации режиссера: ",
                    "UPDATE " + TABLE_NAME + " SET producer_nominations = %s WHERE id = " + id,
                    consoleHandler.getScanner()
            );
            default -> result = -1;
        }

        return result;
    }

    private void initEditMenu() {
        System.out.println("\n\t\t--- Меню редактирования ---");
        System.out.println("1. Редактирование имени режиссера");
        System.out.println("2. Редактирование номинаций режиссера");
        System.out.println("0. Отмена");
        System.out.print("Для продолжения выберите пункт меню: ");
    }

}