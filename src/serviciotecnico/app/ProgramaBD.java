package serviciotecnico.app;

import java.util.Locale;

import serviciotecnico.modelo.EstadoOrden;
import serviciotecnico.modelo.LineaOrden;
import serviciotecnico.modelo.OrdenReparacion;
import serviciotecnico.repositorio.RepositorioJdbc;
import serviciotecnico.repositorio.RepositorioTaller;
import serviciotecnico.servicio.ServicioTaller;

public class ProgramaBD
{

	public static void main(String[] args)
	{
		RepositorioTaller repositorio = new RepositorioJdbc();
		
		ServicioTaller servicio = new ServicioTaller(repositorio);			
		System.out.println("=== Todas las ordenes ===");		
		for(OrdenReparacion orden : repositorio.listarOrdenes())
		{
			/*
			StringBuffer sb = new StringBuffer();
			sb.append("Orden ");
			sb.append(orden.getId());
			sb.append(" (");
			sb.append(orden.getTecnico().getNombre())
			sb.append(", ");
			sb.append(orden.getEstado());
			sb.append("): ");
			sb.append(servicio.importeTotal(orden));
			sb.append(" EUR");
			System.out.println(sb.toString());
			*/
			
			System.out.printf(Locale.US, "Orden %d (%s, %s): %.2f EUR%n",
					orden.getId(), 
					orden.getTecnico().getNombre(),
					orden.getEstado(),
					servicio.importeTotal(orden));
			for(LineaOrden linea : orden.getLineas())
			{
				System.out.println("   " + linea);
			}
			
			/*
			System.out.println("Orden" 
			+ orden.getId() 
			+ "(" 
			+ orden.getTecnico().getNombre() 
			+ ", " + orden.getEstado() + "): " 
			+ servicio.importeTotal(orden) 
			+ " EUR");
			*/
		}
		System.out.println("=== Ordenes FINALIZADA ===");
		
		for(OrdenReparacion orden : servicio.buscarPorEstado(EstadoOrden.FINALIZADA))
		{
			System.out.printf(Locale.US, "Orden %d: %s%n", orden.getId(), orden.getDescripcionAveria());
		}
		
		System.out.println("=== Buscar la orden 2 ===");
		OrdenReparacion orden = repositorio.buscarOrdenPorId(2);
		if(orden == null)
			System.out.println("Orden no encontrada");		
		else		
			System.out.println(orden.getDescripcionAveria());
	}

}
