package cl.duoc.dsy2207.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    private static final String DB_USER = "DSY2207";

    private static final String DB_PASSWORD =
            System.getenv("DB_PASSWORD");

    private static final String WALLET_PATH =
            "C:/Ingenieria/DSY2207/S3/Wallet_A8QYE6TA3DEUCH8T";

    private static final String DB_URL =
            "jdbc:oracle:thin:@a8qye6ta3deuch8t_high";

    private DatabaseConfig() {
    }

    public static Connection getConnection() throws SQLException {

        if (DB_PASSWORD == null || DB_PASSWORD.isBlank()) {
            throw new SQLException(
                    "La variable DB_PASSWORD no está configurada."
            );
        }

        // Indica explícitamente dónde están
        // tnsnames.ora, sqlnet.ora y el Wallet
        System.setProperty(
                "oracle.net.tns_admin",
                WALLET_PATH
        );

        return DriverManager.getConnection(
                DB_URL,
                DB_USER,
                DB_PASSWORD
        );
    }
}