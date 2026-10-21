package serviciotecnico.app;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import serviciotecnico.modelo.EstadoOrden;
import serviciotecnico.modelo.LineaDesplazamiento;
import serviciotecnico.modelo.LineaEquipoSustitucion;
import serviciotecnico.modelo.LineaManoDeObra;
import serviciotecnico.modelo.LineaOrden;
import serviciotecnico.modelo.LineaPieza;
import serviciotecnico.modelo.OrdenReparacion;
import serviciotecnico.modelo.Tecnico;
import serviciotecnico.repositorio.RepositorioEnMemoria;
import serviciotecnico.servicio.ServicioTaller;

public class Programa
{

	public static void main(String[] args)
	{
		Tecnico marta = new Tecnico("Marta", 35.0);
		Tecnico ibai = new Tecnico("Ibai", 30.0);
		
		OrdenReparacion orden1 = new OrdenReparacion(1, marta, "El portatil no arranca");
		orden1.setEstado(EstadoOrden.FINALIZADA);
		orden1.agregarLinea(new LineaPieza("Fuente de alimentación", 45.0, 1));
		orden1.agregarLinea(new LineaManoDeObra("Diagnóstico y sustitución", 1.5 , marta.getTarifaHora()));
		orden1.agregarLinea(new LineaEquipoSustitucion("Portátil de sustitución", 4, 30.0));
		orden1.agregarLinea(new LineaDesplazamiento("Desplazamiento a casa del cliente", 30.0, 2.0));
		
		OrdenReparacion orden2 = new OrdenReparacion(2, ibai, "Pantalla Rota");
		orden2.setEstado(EstadoOrden.ENTREGADA);
		orden2.agregarLinea(new LineaPieza("Pantalla 15,6 pulgadas", 89.90, 1));
		orden2.agregarLinea(new LineaManoDeObra("Montaje", 1.0 , ibai.getTarifaHora()));
				
		OrdenReparacion orden3 = new OrdenReparacion(3, marta, "Ampliar memoria RAM");
		orden3.setEstado(EstadoOrden.FINALIZADA);
		orden3.agregarLinea(new LineaPieza("RAM 8GB", 32.0, 2));
		orden3.agregarLinea(new LineaManoDeObra("Instalación", 0.5 , marta.getTarifaHora()));
		
		OrdenReparacion orden4 = new OrdenReparacion(4, ibai, "Ruido en el ventilador");
		
		RepositorioEnMemoria repositorio = new RepositorioEnMemoria();
		repositorio.agregarOrden(orden1);
		repositorio.agregarOrden(orden2);
		repositorio.agregarOrden(orden3);
		repositorio.agregarOrden(orden4);
		
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
		System.out.println(repositorio.buscarOrdenPorId(2).getDescripcionAveria());
		
		System.out.println("=== Buscar las ordenes de Marta ===");
		/*
		for(OrdenReparacion orden : servicio.buscarPorTecnico("Marta"))
		{
			System.out.printf(Locale.US, "Orden %d: %s%n", orden.getId(), orden.getDescripcionAveria());
		}*/		
		servicio.buscarPorTecnico("Marta").forEach(orden -> {
			System.out.printf(Locale.US, "Orden %d: %s%n", orden.getId(), orden.getDescripcionAveria());
		});
		double suma = 0.0;
		
		System.out.println("=== Facturación total de ordenes FINALIZADA ===");
		double total = servicio.facturacionTotal(EstadoOrden.FINALIZADA);
		System.out.println(total + " EUR");
		System.out.println("=== Recuento por estado ===");
		
		Map<EstadoOrden, Integer> recuento = servicio.recuentoPorEstado();
				
		Set<EstadoOrden> claves = recuento.keySet();		
		for(EstadoOrden clave : claves)
		{
			System.out.println(clave + ": " + recuento.get(clave));
		}
		
		System.out.println("=== RECUENTO POR TECNICO ===");
		Map<String, Double> totalPorTecnico = servicio.facturacionPorTecnico();		
		for(Entry<String, Double> entrada : totalPorTecnico.entrySet())
		{
			System.out.println(entrada.getKey() + ": " + entrada.getValue());
		}		
		System.out.println("Última línea de mi código");
		
		servicio.exportarXML();
		
		System.out.println("=== PREDICATE ===");
		List<OrdenReparacion> porEstado = servicio.buscar(x -> x.getEstado() == EstadoOrden.RECIBIDA);
		//porEstado.forEach(o -> System.out.println(o.getDescripcionAveria()));
		List<OrdenReparacion> porTecnico = servicio.buscar(x -> 
		{
			return x.getTecnico().getNombre().equals("Marta");	
		});
		porTecnico.forEach(o -> System.out.println(o.getDescripcionAveria()));
		
		System.out.println("=== FUNCTION ===");
		List<String> averias = servicio.mapear(OrdenReparacion::getDescripcionAveria);
		averias.forEach(a -> System.out.println(a));
		
		System.out.println("=== CONSUMER ===");
		servicio.aplicarATodas(o -> o.setEstado(EstadoOrden.FINALIZADA));
		
		System.out.println("=== SUPPLIER ===");
		OrdenReparacion noExiste = servicio.buscarPorId(1,
				() -> new OrdenReparacion(0, new Tecnico("Sin asignar", 0), "ORDEN NO ENCONTRADA"));
		System.out.println(noExiste.getDescripcionAveria());
	}
}
