package serviciotecnico.servicio;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import serviciotecnico.modelo.EstadoOrden;
import serviciotecnico.modelo.LineaOrden;
import serviciotecnico.modelo.OrdenReparacion;
import serviciotecnico.repositorio.RepositorioTaller;

public class ServicioTaller
{
	private final RepositorioTaller repositorio;

	public ServicioTaller(RepositorioTaller repositorio)
	{
		this.repositorio = repositorio;
	}

	public double importeTotal(OrdenReparacion orden)
	{
		double suma = 0.0;
		for (LineaOrden linea : orden.getLineas())
		{
			suma += linea.calcularImporte();
		}
		return suma;
	}

	public double facturacionTotal(EstadoOrden estado)
	{
		double suma = 0.0;
		for (OrdenReparacion orden : repositorio.listarOrdenes())
		{
			if (orden.getEstado() == estado)
			{
				suma += importeTotal(orden);
			}
		}
		return suma;
	}

	public List<OrdenReparacion> buscarPorEstado(EstadoOrden estado)
	{
		List<OrdenReparacion> resultado = new ArrayList<OrdenReparacion>();
		for (OrdenReparacion orden : repositorio.listarOrdenes())
		{
			if (orden.getEstado() == estado)
			{
				resultado.add(orden);
			}
		}
		return resultado;
	}

	public List<OrdenReparacion> buscarPorTecnico(String nombre)
	{
		List<OrdenReparacion> resultado = new ArrayList<OrdenReparacion>();
		for (OrdenReparacion orden : repositorio.listarOrdenes())
		{
			if (orden.getTecnico().getNombre().equals(nombre))
			{
				resultado.add(orden);
			}
		}
		return resultado;
	}

	public Map<EstadoOrden, Integer> recuentoPorEstado()
	{
		Map<EstadoOrden, Integer> resultado = new HashMap<EstadoOrden, Integer>();
		for (OrdenReparacion orden : repositorio.listarOrdenes())
		{
			EstadoOrden estado = orden.getEstado();
			if (resultado.containsKey(estado))
				resultado.put(estado, resultado.get(estado) + 1);
			else
				resultado.put(estado, 1);
		}
		return resultado;
	}

	public Map<String, Double> facturacionPorTecnico()
	{
		return repositorio.listarOrdenes().stream()
				.filter(o -> o.getEstado() == EstadoOrden.FINALIZADA || o.getEstado() == EstadoOrden.ENTREGADA)
				.collect(Collectors.groupingBy(o -> o.getTecnico().getNombre(),
						Collectors.summingDouble(this::importeTotal)));

		/*
		 * Map<String, Double> resultado = new HashMap<String, Double>();
		 * 
		 * for(OrdenReparacion orden : repositorio.listarOrdenes()) {
		 * if(orden.getEstado() == EstadoOrden.FINALIZADA || orden.getEstado() ==
		 * EstadoOrden.ENTREGADA) { String nombreTecnico =
		 * orden.getTecnico().getNombre(); if(resultado.containsKey(nombreTecnico)) {
		 * double valorAnterior = resultado.get(nombreTecnico); double nuevoValor =
		 * valorAnterior + importeTotal(orden); resultado.put(nombreTecnico,
		 * nuevoValor); } else { resultado.put(nombreTecnico, importeTotal(orden)); } }
		 * } return resultado;
		 */
	}

	public void exportarXML()
	{
		try
		{
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.newDocument();

			Element raiz = doc.createElement("taller");
			doc.appendChild(raiz);

			for (OrdenReparacion orden : repositorio.listarOrdenes())
			{
				Element eOrden = doc.createElement("orden");
				eOrden.setAttribute("id", String.valueOf(orden.getId()));
				eOrden.setAttribute("estado", orden.getEstado().toString());

				Element eTecnico = doc.createElement("tecnico");
				eTecnico.setTextContent(orden.getTecnico().getNombre());
				eOrden.appendChild(eTecnico);

				for (LineaOrden linea : orden.getLineas())
				{
					Element eLinea = doc.createElement("linea");
					eLinea.setAttribute("importe", String.valueOf(linea.calcularImporte()));
					eLinea.setTextContent(linea.getConcepto());
					eOrden.appendChild(eLinea);
				}
				raiz.appendChild(eOrden);
			}

			Transformer transformador = TransformerFactory.newInstance().newTransformer();
			transformador.setOutputProperty(OutputKeys.INDENT, "yes");
			transformador.transform(new DOMSource(doc), new StreamResult(Path.of("Ordenes.xml").toFile()));
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	public List<OrdenReparacion> buscar(Predicate<OrdenReparacion> criterio)
	{
		return repositorio.listarOrdenes().stream().filter(criterio).collect(Collectors.toList());
	}

	public <T> List<T> mapear(Function<OrdenReparacion, T> transformacion)
	{
		return repositorio.listarOrdenes().stream().map(transformacion).collect(Collectors.toList());
	}

	public void aplicarATodas(Consumer<OrdenReparacion> accion)
	{
		repositorio.listarOrdenes().forEach(accion);
	}

	public OrdenReparacion buscarPorId(int id, Supplier<OrdenReparacion> siNoExiste)
	{
		OrdenReparacion encontrada = repositorio.buscarOrdenPorId(id);
		if (encontrada == null)
		{
			return siNoExiste.get();
		}
		return encontrada;
	}

}
