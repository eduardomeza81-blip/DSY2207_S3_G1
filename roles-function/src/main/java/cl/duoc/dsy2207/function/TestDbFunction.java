package cl.duoc.dsy2207.function;

import cl.duoc.dsy2207.config.DatabaseConfig;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

public class TestDbFunction {

    @FunctionName("testdb")
    public HttpResponseMessage run(
            @HttpTrigger(
                    name = "req",
                    methods = {HttpMethod.GET},
                    authLevel = AuthorizationLevel.ANONYMOUS,
                    route = "testdb"
            )
            HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        context.getLogger().info("Probando conexión Oracle...");

        try (
                Connection connection = DatabaseConfig.getConnection();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT SYSDATE FROM DUAL")
        ) {

            if (rs.next()) {

                String fechaOracle = rs.getString(1);

                return request
                        .createResponseBuilder(HttpStatus.OK)
                        .header("Content-Type", "application/json")
                        .body(
                                "{"
                                + "\"estado\":\"OK\","
                                + "\"mensaje\":\"Conexion Oracle exitosa\","
                                + "\"fechaOracle\":\"" + fechaOracle + "\""
                                + "}"
                        )
                        .build();
            }

            return request
                    .createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"estado\":\"ERROR\"}")
                    .build();

        } catch (Exception e) {

            context.getLogger().severe(
                    "Error de conexión Oracle: " + e.getMessage()
            );

            return request
                    .createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("Content-Type", "application/json")
                    .body(
                            "{"
                            + "\"estado\":\"ERROR\","
                            + "\"mensaje\":\"" + e.getMessage().replace("\"", "'") + "\""
                            + "}"
                    )
                    .build();
        }
    }
}