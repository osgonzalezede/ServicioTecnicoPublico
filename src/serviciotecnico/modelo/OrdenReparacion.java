package serviciotecnico.modelo;

import java.util.ArrayList;
import java.util.List;

public class OrdenReparacion
{
	private int id;
	private Tecnico tecnico;
	private String descripcionAveria;
	private EstadoOrden estado;
	private List<LineaOrden> lineas;
	
	public OrdenReparacion(int id, Tecnico tecnico, String descripcionAveria)
	{
		this.id = id;
		this.tecnico = tecnico;
		this.descripcionAveria = descripcionAveria;
		this.estado = EstadoOrden.RECIBIDA;
		this.lineas = new ArrayList<LineaOrden>();
	}

	public int getId()
	{
		return id;
	}

	public Tecnico getTecnico()
	{
		return tecnico;
	}

	public String getDescripcionAveria()
	{
		return descripcionAveria;
	}

	public EstadoOrden getEstado()
	{
		return estado;
	}

	public List<LineaOrden> getLineas()
	{
		return lineas;
	}

	public void setEstado(EstadoOrden estado)
	{
		this.estado = estado;
	}
	
	public void agregarLinea(LineaOrden linea)
	{
		lineas.add(linea);
	}
	
	
}
