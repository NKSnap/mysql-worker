package repository.tables;

import repository.ConsoleHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CopyrightTableWorker extends AbstractTableWorker {
    private final Connection connection;
    private final ConsoleHandler consoleHandler;

    public static final String TABLE_NAME = "copyright";

    public CopyrightTableWorker(Connection connection, ConsoleHandler consoleHandler) {
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
                int copyrightID = resultSet.getInt(1);
                String copyrightName = resultSet.getString(2);
                int copyrightPrice = resultSet.getInt(3);
                int kinocompanyID = resultSet.getInt(4);
                System.out.printf("%5d|%-20s|%16d$|%5d\n", copyrightID, copyrightName, copyrightPrice, kinocompanyID);
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
        System.out.print("Название авторского права: ");
        String copyrightName = consoleHandler.getScanner().nextLine();
        int copyrightPrice = consoleHandler.getIntegerValueFromUser("Цена авторского права на адроида: ", consoleHandler.getScanner());
        int kinocompanyID = consoleHandler.getIntegerValueFromUser("Идентификатор кинокомпании, владеющей авторским правом на андроида: ", consoleHandler.getScanner());

        String query = "INSERT INTO " + TABLE_NAME + " (copyright_name, copyright_price, kinocompany_id) VALUES (\""
                + copyrightName + "\", "
                + copyrightPrice + ", "
                + kinocompanyID + ");";

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
                    "Новое название авторского права: ",
                    "UPDATE " + TABLE_NAME + " SET copyright_name = %s WHERE id = " + id,
                    consoleHandler.getScanner()
                    );
            case 2 -> result = updateIntegerValueOfTable(
                    consoleHandler,
                    connection,
                    "Новая цена авторского права на андроида: ",
                    "UPDATE " + TABLE_NAME + " SET copyright_price = %s WHERE id = " + id
            );
            case 3 -> result = updateIntegerValueOfTable(
                    consoleHandler,
                    connection,
                    "Новый идентификатор кинокомпании, владеющей авторским правом на андроида: ",
                    "UPDATE " + TABLE_NAME + " SET kinocompany_id = %s WHERE id = " + id
            );
            default -> result = -1;
        }

        return result;
    }

    private void initEditMenu() {
        System.out.println("\n\t\t--- Меню редактирования ---");
        System.out.println("1. Редактирование названия авторского права на андроида: ");
        System.out.println("2. Редактирование цены авторского права на андроида: ");
        System.out.println("3. Редактирование идентификатора кинокомпании, владеющей авторским правом на андроида: ");
        System.out.println("0. Отмена");
        System.out.print("Для продолжения выберите пункт меню: ");
    }
}
