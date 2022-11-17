package repository.tables;

import repository.ConsoleHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AndroidTableWorker extends AbstractTableWorker {
    private final Connection connection;
    private final ConsoleHandler consoleHandler;

    public static final String TABLE_NAME = "android";

    public AndroidTableWorker(Connection connection, ConsoleHandler consoleHandler) {
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
                int androidID = resultSet.getInt(1);
                String androidName = resultSet.getString(2);
                String androidSpecification = resultSet.getString(3);
                int copyrightID = resultSet.getInt(4);
                int parametersID = resultSet.getInt(5);
                int actorID = resultSet.getInt(6);
                int movieID = resultSet.getInt(7);
                System.out.printf("%5d|%-20s|%-20s|%5d|%5d|%5d|%5d\n", androidID, androidName,
                        androidSpecification, copyrightID, parametersID, actorID, movieID);
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
        System.out.print("Название андроида: ");
        String androidName = consoleHandler.getScanner().nextLine();
        System.out.print("Спецификация андроида: ");
        String androidSpecification = consoleHandler.getScanner().nextLine();

        int copyrightID = consoleHandler.getIntegerValueFromUser("Идентификатор авторского права: ", consoleHandler.getScanner());
        int parametersID = consoleHandler.getIntegerValueFromUser("Идентификатор параметров тела: ", consoleHandler.getScanner());
        int actorID = consoleHandler.getIntegerValueFromUser("Идентификатор актера, который сыграл андроида: ", consoleHandler.getScanner());
        int movieID = consoleHandler.getIntegerValueFromUser("Идентификатор фильма: ", consoleHandler.getScanner());

        String query = "INSERT INTO " + TABLE_NAME + " (name, specification, copyright_id, parameters_id, actor_id, movie_id) VALUES (\""
                + androidName + "\", \""
                + androidSpecification + "\", "
                + copyrightID + ", "
                + parametersID + ", "
                + actorID + ", "
                + movieID + ");";

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
                    "Новое название андроида: ",
                    "UPDATE " + TABLE_NAME + " SET name = %s WHERE id = " + id,
                    consoleHandler.getScanner());
            case 2 -> result = updateStringValueOfTable(
                    connection,
                    "Новая спецификация андроида: ",
                    "UPDATE " + TABLE_NAME + " SET specification = %s WHERE id = " + id,
                    consoleHandler.getScanner());
            case 3 -> result = updateIntegerValueOfTable(
                    consoleHandler,
                    connection,
                    "Новый идентификатор авторского права: ",
                    "UPDATE " + TABLE_NAME + " SET copyright_id = %s WHERE id = " + id);
            case 4 -> result = updateIntegerValueOfTable(
                    consoleHandler,
                    connection,
                    "Новый идентификатор параметров тела: ",
                    "UPDATE " + TABLE_NAME + " SET parameters_id = %s WHERE id = " + id);
            case 5 -> result = updateIntegerValueOfTable(
                    consoleHandler,
                    connection,
                    "Новый идентификатор актера, который сыграл андроида: ",
                    "UPDATE " + TABLE_NAME + " SET actor_id = %s WHERE id = " + id);
            case 6 -> result = updateIntegerValueOfTable(
                    consoleHandler,
                    connection,
                    "Новый идентификатор фильма: ",
                    "UPDATE " + TABLE_NAME + " SET movie_id = %s WHERE id = " + id);
            default -> result = -1;
        }

        return result;
    }

    private void initEditMenu() {
        System.out.println("\n\t\t--- Меню редактирования ---");
        System.out.println("1. Редактирование названия андроида: ");
        System.out.println("2. Редактирование спецификации андроида: ");
        System.out.println("3. Редактирование идентификатора авторского права: ");
        System.out.println("4. Редактирование идентификатора параметров тела: ");
        System.out.println("5. Редактирование идентификатора актера, который сыграл андроида: ");
        System.out.println("6. Редактирование идентификатора фильма: ");
        System.out.println("0. Отмена");
        System.out.print("Для продолжения выберите пункт меню: ");
    }
}
