package serviciotecnico.repositorio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    public static Connection obtener() throws SQLException {
        return DriverManager.getConnection(ConstantesBD.URL, 
        		ConstantesBD.USUARIO, 
        		ConstantesBD.CONTRASENA);
    }
}

