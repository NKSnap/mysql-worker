package repository.tables;

import repository.ConsoleHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MovieTableWorker extends AbstractTableWorker {
    private final Connection connection;
    private final ConsoleHandler consoleHandler;

    public static final String TABLE_NAME = "movie";

    public MovieTableWorker(Connection connection, ConsoleHandler consoleHandler) {
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
                int movieID = resultSet.getInt(1);
                String movieName = resultSet.getString(2);
                String movieGenre = resultSet.getString(3);
                int movieYear = resultSet.getInt(4);
                int producerID = resultSet.getInt(5);
                int sponsorID = resultSet.getInt(6);
                int ratingID = resultSet.getInt(7);
                System.out.printf("%5d|%-20s|%-20s|%5d|%5d|%5d|%5d\n", movieID, movieName, movieGenre, movieYear, producerID, sponsorID, ratingID);
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
        System.out.print("Название фильма: ");
        String movieName = consoleHandler.getScanner().nextLine();
        System.out.print("Жанр фильма: ");
        String movieGenre = consoleHandler.getScanner().nextLine();

        int movieYear = consoleHandler.getIntegerValueFromUser("Год выхода фильма: ", consoleHandler.getScanner());
        int producerID = consoleHandler.getIntegerValueFromUser("Идентификатор режиссера: ", consoleHandler.getScanner());
        int sponsorID = consoleHandler.getIntegerValueFromUser("Идентификатор спонсора: ", consoleHandler.getScanner());
        int ratingID = consoleHandler.getIntegerValueFromUser("Идентификатор рейтинга: ", consoleHandler.getScanner());

        String query = "INSERT INTO " + TABLE_NAME + " (movie_name, genre, year, producer_id, sponsor_id, rating_id) VALUES (\""
                + movieName + "\", \""
                + movieGenre + "\", "
                + movieYear + ", "
                + producerID + ", "
                + sponsorID + ", "
                + ratingID + ");";

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
                    "Новое название фильма: ",
                    "UPDATE " + TABLE_NAME + " SET movie_name = %s WHERE id = " + id,
                    consoleHandler.getScanner());
            case 2 -> result = updateStringValueOfTable(
                    connection,
                    "Новый жанр фильма: ",
                    "UPDATE " + TABLE_NAME + " SET genre = %s WHERE id = " + id,
                    consoleHandler.getScanner());
            case 3 -> result = updateIntegerValueOfTable(
                    consoleHandler,
                    connection,
                    "Новый год выхода фильма: ",
                    "UPDATE " + TABLE_NAME + " SET year = %s WHERE id = " + id);
            case 4 -> result = updateIntegerValueOfTable(
                    consoleHandler,
                    connection,
                    "Новый идентификатор режиссера: ",
                    "UPDATE " + TABLE_NAME + " SET producer_id = %s WHERE id = " + id);
            case 5 -> result = updateIntegerValueOfTable(
                    consoleHandler,
                    connection,
                    "Новый идентификатор спонсора: ",
                    "UPDATE " + TABLE_NAME + " SET sponsor_id = %s WHERE id = " + id);
            case 6 -> result = updateIntegerValueOfTable(
                    consoleHandler,
                    connection,
                    "Новый идентификатор рейтинга: ",
                    "UPDATE " + TABLE_NAME + " SET rating_id = %s WHERE id = " + id);
            default -> result = -1;
        }

        return result;
    }

    private void initEditMenu() {
        System.out.println("\n\t\t--- Меню редактирования ---");
        System.out.println("1. Редактирование названия фильма: ");
        System.out.println("2. Редактирование жанра фильма: ");
        System.out.println("3. Редактирование года выхода фильма: ");
        System.out.println("4. Редактирование идентификатора режиссера: ");
        System.out.println("5. Редактирование идентификатора спонсора: ");
        System.out.println("6. Редактирование идентификатора рейтинга: ");
        System.out.println("0. Отмена");
        System.out.print("Для продолжения выберите пункт меню: ");
    }
}