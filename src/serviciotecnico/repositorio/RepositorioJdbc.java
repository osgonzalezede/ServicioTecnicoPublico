package serviciotecnico.repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import serviciotecnico.modelo.EstadoOrden;
import serviciotecnico.modelo.LineaManoDeObra;
import serviciotecnico.modelo.LineaOrden;
import serviciotecnico.modelo.LineaPieza;
import serviciotecnico.modelo.OrdenReparacion;
import serviciotecnico.modelo.Tecnico;

public class RepositorioJdbc implements RepositorioTaller
{
	List<OrdenReparacion> ordenes = new ArrayList<OrdenReparacion>();

	private List<LineaOrden> obtenerLineas(int idOrden)
	{
		List<LineaOrden> resultado = new ArrayList<LineaOrden>();
		String sql = "SELECT * FROM linea_orden WHERE id_orden = ?";
		try (Connection con = Conexion.obtener(); 
				PreparedStatement st = con.prepareStatement(sql);)
		{
			st.setInt(1, idOrden);
			ResultSet rs = st.executeQuery();
			while (rs.next())
			{
				String tipo = rs.getString(ConstantesBD.LineaOrden.TIPO);
				String concepto = rs.getString(ConstantesBD.LineaOrden.CONCEPTO);
				double precioUnitario = rs.getDouble(ConstantesBD.LineaOrden.PRECIO_UNITARIO);
				int cantidad = rs.getInt(ConstantesBD.LineaOrden.CANTIDAD);
				double horas = rs.getDouble(ConstantesBD.LineaOrden.HORAS);
				double tarifa_hora = rs.getDouble(ConstantesBD.LineaOrden.TARIFA_HORA);

				switch (tipo) {
				case "PIEZA":
					resultado.add(new LineaPieza(concepto, precioUnitario, cantidad));
					break;
				case "MANO_DE_OBRA":
					resultado.add(new LineaManoDeObra(concepto, horas, tarifa_hora));
					break;
				}
			}
		} catch (SQLException ex)
		{
			System.out.println("Excepcion en listarOrdenes: " + ex.getMessage());
		}
		return resultado;
	}
	
	public List<OrdenReparacion> listarOrdenes()
	{
		if(ordenes.isEmpty())
			obtenerOrdenes();
		return ordenes;
	}

	public void obtenerOrdenes()
	{
		String sql = "SELECT " + "o.id, " + "o.descripcion_averia, " + "o.estado, " + "t.nombre, " + "t.tarifa_hora "
				+ "FROM orden_reparacion o " + "INNER JOIN tecnico t " + "ON o.id_tecnico = t.id";
		try (Connection con = Conexion.obtener(); PreparedStatement st = con.prepareStatement(sql);)
		{			
			ResultSet rs = st.executeQuery();
			while (rs.next())
			{
				int idOrden = rs.getInt(ConstantesBD.OrdenReparacion.ID);
				String descripcion = rs.getString(ConstantesBD.OrdenReparacion.DESCRIPCION);
				String estado = rs.getString(ConstantesBD.OrdenReparacion.ESTADO);
				String nombre = rs.getString(ConstantesBD.Tecnico.NOMBRE);
				double tarifaHora = rs.getDouble(ConstantesBD.Tecnico.TARIFA);
				Tecnico tecnico = new Tecnico(nombre, tarifaHora);
				OrdenReparacion orden = new OrdenReparacion(idOrden, tecnico, descripcion);
				orden.setEstado(EstadoOrden.valueOf(estado));
								
				List<LineaOrden> lineas = obtenerLineas(orden.getId());
				
				for (LineaOrden linea : lineas)
				{
					orden.agregarLinea(linea);
				}
				ordenes.add(orden);
			}
		} 
		catch (SQLException ex)
		{
			System.out.println("Excepcion en listarOrdenes: " + ex.getMessage());
		}		
	}

	public OrdenReparacion buscarOrdenPorId(int id)
	{
		String sql = "SELECT " + "o.id, " + "o.descripcion_averia, " + "o.estado, " + "t.nombre, " + "t.tarifa_hora "
				+ "FROM orden_reparacion o " + "INNER JOIN tecnico t " + "ON o.id_tecnico = t.id " + "WHERE o.id = ?";
		try (Connection con = Conexion.obtener(); PreparedStatement st = con.prepareStatement(sql);)
		{
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();
			if (rs.next())
			{
				int idOrden = rs.getInt(ConstantesBD.OrdenReparacion.ID);
				String descripcion = rs.getString(ConstantesBD.OrdenReparacion.DESCRIPCION);
				String estado = rs.getString(ConstantesBD.OrdenReparacion.ESTADO);
				String nombre = rs.getString(ConstantesBD.Tecnico.NOMBRE);
				double tarifaHora = rs.getDouble(ConstantesBD.Tecnico.TARIFA);
				Tecnico tecnico = new Tecnico(nombre, tarifaHora);
				OrdenReparacion orden = new OrdenReparacion(idOrden, tecnico, descripcion);
				List<LineaOrden> lineas = obtenerLineas(orden.getId());
				for (LineaOrden linea : lineas)
				{
					orden.agregarLinea(linea);
				}
				return orden;
			}
		} catch (SQLException ex)
		{
			System.out.println("Excepcion en listarOrdenes: " + ex.getMessage());
		}
		return null;
	}
}
