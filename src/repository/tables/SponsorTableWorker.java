package repository.tables;

import repository.ConsoleHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class SponsorTableWorker extends AbstractTableWorker {
    private final Connection connection;
    private final ConsoleHandler consoleHandler;

    public static final String TABLE_NAME = "sponsor";

    public SponsorTableWorker(Connection connection, ConsoleHandler consoleHandler) {
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
                int sponsorID = resultSet.getInt(1);
                String sponsorName = resultSet.getString(2);
                String sponsorDesc = resultSet.getString(3);
                System.out.printf("%5d|%-20s|%s\n", sponsorID, sponsorName, sponsorDesc);
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
        System.out.print("Имя спонсора фильма: ");
        String sponsorName = consoleHandler.getScanner().nextLine();
        System.out.print("\nДополнительная информация о спонсоре фильма: ");
        String sponsorDesc = consoleHandler.getScanner().nextLine();

        String query = "INSERT INTO " + TABLE_NAME + " (sponsor_name, sponsor_description) VALUES (\""
                + sponsorName + "\", \""
                + sponsorDesc + "\");";

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
                    "Новое имя спонсора фильма: ",
                    "UPDATE " + TABLE_NAME + " SET sponsor_name = %s WHERE id = " + id,
                    consoleHandler.getScanner()
            );
            case 2 -> result = updateStringValueOfTable(
                    connection,
                    "Новая дополнительная информация о спонсоре фильма: ",
                    "UPDATE " + TABLE_NAME + " SET sponsor_description = %s WHERE id = " + id,
                    consoleHandler.getScanner()
            );
            default -> result = -1;
        }

        return result;
    }

    private void initEditMenu() {
        System.out.println("\n\t\t--- Меню редактирования ---");
        System.out.println("1. Редактирование имени спонсора фильма");
        System.out.println("2. Редактирование дополнительной информации и спонсоре фильма");
        System.out.println("0. Отмена");
        System.out.print("Для продолжения выберите пункт меню: ");
    }

}