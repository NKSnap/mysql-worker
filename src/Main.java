import repository.ConsoleHandler;
import repository.DatabaseConnector;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        DatabaseConnector databaseConnector = initDatabaseConnection();
        if (databaseConnector == null) { return; }

        Worker worker = new Worker(databaseConnector, new ConsoleHandler(new Scanner(System.in)));
        initBaseUI();

        while (true) {
            if (!worker.doWork()) return;
        }
    }

    public static DatabaseConnector initDatabaseConnection() {
        DatabaseConnector databaseConnector = null;
        try {
            databaseConnector = DatabaseConnector.getDatabaseConnector();
        } catch (ClassNotFoundException e) {
            System.out.println("Не удалось подключить драйвер!");
        } catch (SQLException e) {
            System.out.println("Не удалось получить соединение с базой данных!");
        }

        return databaseConnector;
    }

    private static void initBaseUI() {
        System.out.println("\t\t---- Меню базы данных \"Андроиды в американском кино\" ----\n");
        System.out.println("\t1. Работа с таблицей \"producer\"");
        System.out.println("\t2. Работа с таблицей \"rating\"");
        System.out.println("\t3. Работа с таблицей \"sponsor\"");
        System.out.println("\t4. Работа с таблицей \"parameters\"");
        System.out.println("\t5. Работа с таблицей \"movie\"");
        System.out.println("\t6. Работа с таблицей \"kinocompany\"");
        System.out.println("\t7. Работа с таблицей \"creator\"");
        System.out.println("\t8. Работа с таблицей \"copyright\"");
        System.out.println("\t9. Работа с таблицей \"actor\"");
        System.out.println("\t10. Работа с таблицей \"android\"");
        System.out.println("\n\t0. Пользовательский запрос");
        System.out.println("\t-1. Выйти");
        System.out.print("\n\nДля продолжения работы, выберите пункт главного меню: ");
    }
}
