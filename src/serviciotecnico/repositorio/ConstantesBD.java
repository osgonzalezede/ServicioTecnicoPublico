package serviciotecnico.repositorio;

public class ConstantesBD
{
	public static final String URL = "jdbc:mysql://localhost:3306/taller";
	public static final String USUARIO = "root";
	public static final String CONTRASENA = "root";
    
	class Tecnico {
		final static String ID = "id";
		final static String NOMBRE = "nombre";
		final static String TARIFA = "tarifa_hora";
	}	
	class OrdenReparacion	{
		final static String ID = "id";
		final static String IDTECNICO = "id_tecnico";
		final static String DESCRIPCION = "descripcion_averia";
		final static String ESTADO = "estado";
	}
	
	class LineaOrden {
		final static String ID = "id";
		final static String IDORDEN = "id_orden";
		final static String TIPO = "tipo";
		final static String CONCEPTO = "concepto";
		final static String PRECIO_UNITARIO = "precio_unitario";
		final static String CANTIDAD = "cantidad";
		final static String HORAS = "horas";
		final static String TARIFA_HORA = "tarifa_hora";
	}

}
