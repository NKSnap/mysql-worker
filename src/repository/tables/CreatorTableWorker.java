package repository.tables;

import repository.ConsoleHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CreatorTableWorker extends AbstractTableWorker {
    private final Connection connection;
    private final ConsoleHandler consoleHandler;

    public static final String TABLE_NAME = "creator";

    public CreatorTableWorker(Connection connection, ConsoleHandler consoleHandler) {
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
                int creatorID = resultSet.getInt(1);
                String creatorName = resultSet.getString(2);
                String creatorRace = resultSet.getString(3);
                System.out.printf("%5d|%-20s|%s\n", creatorID, creatorName, creatorRace);
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
        System.out.print("Имя создателя андроида: ");
        String creatorName = consoleHandler.getScanner().nextLine();
        System.out.print("\nРаса создателя андроида: ");
        String creatorRace = consoleHandler.getScanner().nextLine();

        String query = "INSERT INTO " + TABLE_NAME + " (creator_name, creator_race) VALUES (\""
                + creatorName + "\", \""
                + creatorRace + "\");";

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
                    "Новое имя создателя андроида: ",
                    "UPDATE " + TABLE_NAME + " SET creator_name = %s WHERE id = " + id,
                    consoleHandler.getScanner()
            );
            case 2 -> result = updateStringValueOfTable(
                    connection,
                    "Новая раса создателя андроида: ",
                    "UPDATE " + TABLE_NAME + " SET creator_race = %s WHERE id = " + id,
                    consoleHandler.getScanner()
            );
            default -> result = -1;
        }

        return result;
    }

    private void initEditMenu() {
        System.out.println("\n\t\t--- Меню редактирования ---");
        System.out.println("1. Редактирование имени создателя андроида");
        System.out.println("2. Редактирование расы создателя андроида");
        System.out.println("0. Отмена");
        System.out.print("Для продолжения выберите пункт меню: ");
    }
}
