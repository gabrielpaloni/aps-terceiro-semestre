package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private static final String URL = "jdbc:mysql://187.59.23.114:3306/amazonia";
    private static final String USUARIO = "gabriel_user";
    private static final String SENHA = "Gb#74qPl!Zx3";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}

