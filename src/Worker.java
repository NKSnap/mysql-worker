import repository.ConsoleHandler;
import repository.DatabaseConnector;
import repository.interfacies.TableWorker;
import repository.tables.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Locale;

public class Worker {
    private final DatabaseConnector databaseConnector;
    private final ConsoleHandler consoleHandler;

    public Worker(DatabaseConnector databaseConnector, ConsoleHandler consoleHandler) {
        this.databaseConnector = databaseConnector;
        this.consoleHandler = consoleHandler;
    }

    public boolean doWork() {
        int key = consoleHandler.getUserIntegerChoice();
        if (key == -1) {
            System.out.println("Программа завершается!");
            databaseConnector.closeConnection();
            return false;
        } else if (key == -2) {
            System.out.print("Для продолжения работы, выберите пункт главного меню: ");
        } else if (key < 0 || key > 10) {
            System.out.println("Ключ не соответсвует ни одному возможному варианту! Попробуйте еще раз!");
            System.out.print("Для продолжения работы, выберите пункт главного меню: ");
            return true;
        }

        Connection connection = databaseConnector.getDbConnection();
        switch (key) {
            case 0 -> customRequest();
            case 1 -> workWithTable(ProducerTableWorker.TABLE_NAME, new ProducerTableWorker(connection, consoleHandler));
            case 2 -> workWithTable(RatingTableWorker.TABLE_NAME, new RatingTableWorker(connection, consoleHandler));
            case 3 -> workWithTable(SponsorTableWorker.TABLE_NAME, new SponsorTableWorker(connection, consoleHandler));
            case 4 -> workWithTable(ParametersTableWorker.TABLE_NAME, new ParametersTableWorker(connection, consoleHandler));
            case 5 -> workWithTable(MovieTableWorker.TABLE_NAME, new MovieTableWorker(connection, consoleHandler));
            case 6 -> workWithTable(KinocompanyTableWorker.TABLE_NAME, new KinocompanyTableWorker(connection, consoleHandler));
            case 7 -> workWithTable(CreatorTableWorker.TABLE_NAME, new CreatorTableWorker(connection, consoleHandler));
            case 8 -> workWithTable(CopyrightTableWorker.TABLE_NAME, new CopyrightTableWorker(connection, consoleHandler));
            case 9 -> workWithTable(ActorTableWorker.TABLE_NAME, new ActorTableWorker(connection, consoleHandler));
            case 10 -> workWithTable(AndroidTableWorker.TABLE_NAME, new AndroidTableWorker(connection, consoleHandler));
        }

        System.out.print("\nДля продолжения работы, выберите пункт главного меню: ");
        return true;
    }

    private void customRequest() {
        System.out.println("Введите запрос ниже. Примечание: если хотите записать текстовое значение, нужно брать его в двойные кавычки.\n"
        + "Например: INSERT INTO producer (producer_name, producer_nominations) VALUES (\"name\", \"nominations\");");
        String request = consoleHandler.getScanner().nextLine();
        String requestLower = request.toLowerCase(Locale.ROOT);

        if (!requestLower.startsWith("select")) {
            try {
                Statement statement = databaseConnector.getDbConnection().createStatement();
                statement.executeUpdate(request);
                System.out.println("Запрос выполнен успешно!");
            } catch (SQLException e) {
                System.out.println("Не удалось выполнить запрос!");
            }
        } else {
            System.out.print("Введите количество ожидаемых типов данных: ");
            int count = consoleHandler.getUserIntegerChoice();
            if (count < 1) {
                System.out.println("Недопустимое значение!");
            } else {
                System.out.println("Ниже введите типы ожидаемых данных по порядку считывания через пробел (Примечание: типы, поддерживаемые языком).");
                String[] types = consoleHandler.getScanner().nextLine().toLowerCase(Locale.ROOT).split(" ");

                try {
                    Statement statement = databaseConnector.getDbConnection().createStatement();
                    ResultSet resultSet = statement.executeQuery(request);

                    while (resultSet.next()) {
                        ArrayList<String> list = new ArrayList<>();
                        for (int j = 0; j < count; j++) {
                                switch (types[j]) {
                                    case "string" -> list.add(resultSet.getString(j + 1));
                                    case "int" -> list.add(String.valueOf(resultSet.getInt(j + 1)));
                                    case "double" -> list.add(String.valueOf(resultSet.getDouble(j + 1)));
                                    default -> {
                                        System.out.println("Неизвестный тип! Завершение выполнения запроса!");
                                        return;
                                    }
                                }
                        }

                        System.out.println(String.join(" | ", list));
                    }
                } catch (SQLException e) {
                    System.out.println("Не удалось выполнить запрос!");
                }
            }
        }
    }

    private void workWithTable(String tableName, TableWorker tableWorker) {
        initTableUI(tableName);
        while (true) {
            System.out.print("\nКлюч введите здесь: ");
            int key = consoleHandler.getUserIntegerChoice();
            if (key == 0) {
                System.out.println("Выход к главному меню!");
                System.out.print("Для продолжения работы, выберите пункт главного меню: ");
                break;
            } else if (!(key < 1 || key > 4)) {
                selectWorkByKey(key, tableWorker);
            } else {
                System.out.println("Ключ не соответсвует ни одному возможному варианту! Попробуйте еще раз!");
            }
        }
    }

    private void selectWorkByKey(int key, TableWorker tableWorker) {
        switch (key) {
            case 1 -> {
                int response = tableWorker.readAllRecords();
                requestFailed(response, false);
            }
            case 2 -> {
                int response = tableWorker.insertNewRecord();
                requestFailed(response, true);
            }
            case 3 -> {
                System.out.print("Введите идентификатор записи для удаления: ");
                int response = tableWorker.deleteRecordByID(consoleHandler.getUserIntegerChoice());
                requestFailed(response, true);
            }
            case 4 -> {
                System.out.print("Введите идентификатор записи для редактирования: ");
                int response = tableWorker.updateNewRecord(consoleHandler.getUserIntegerChoice());
                requestFailed(response, true);
            }
        }
    }

    private void requestFailed(int response, boolean key) {
        if (response == -1) {
            System.out.println("Ошибка выполнения запроса!");
        } else if (key && response != 0) {
            System.out.println("Запрос выполнен успешно!");
        }
    }

    private void initTableUI(String tableName) {
        System.out.printf("Выберите операцию для работы с таблицей %s:\n", tableName);
        System.out.println("\t1. Чтение всех данных");
        System.out.println("\t2. Добавление новой записи");
        System.out.println("\t3. Удаление записи");
        System.out.println("\t4. Редактирование записи");
        System.out.println("\t0. Выход к главному меню");
    }
}