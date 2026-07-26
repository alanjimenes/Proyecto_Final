package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=Clinica;encrypt=true;trustServerCertificate=true;";
    private static final String USER = ""; //cambiar el usuario
    private static final String PASSWORD = ""; //la contraseña
    private static Connection conexion = null;

    public static Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Error fatal conectando a la base de datos: " + e.getMessage(), e);
        }
        return conexion;
    }

    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}