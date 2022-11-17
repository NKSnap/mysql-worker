package repository;

import resources.ConnectionConst;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private Connection dbConnection;
    private static DatabaseConnector databaseConnector;

    private DatabaseConnector(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public static DatabaseConnector getDatabaseConnector() throws ClassNotFoundException, SQLException {
        if (databaseConnector == null) {
            Class.forName(ConnectionConst.DATABASE_DRIVER.getValue());
            String connectionUrl = "jdbc:mysql://"
                    + ConnectionConst.DATABASE_HOST.getValue() + ":"
                    + ConnectionConst.DATABASE_PORT.getValue() + "/"
                    + ConnectionConst.DATABASE_NAME.getValue();

            databaseConnector = new DatabaseConnector(initDBConnection(connectionUrl));
        }

        return databaseConnector;
    }

    public Connection getDbConnection() {
        return dbConnection;
    }

    private static Connection initDBConnection(String url) throws SQLException {
        return DriverManager.getConnection(
                url, ConnectionConst.USER.getValue(),
                ConnectionConst.PASSWORD.getValue()
        );
    }

    public void closeConnection() {
        try {
            if (!dbConnection.isClosed()) dbConnection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
