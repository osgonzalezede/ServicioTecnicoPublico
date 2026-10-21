package serviciotecnico.repositorio;

import java.util.ArrayList;
import java.util.List;

import serviciotecnico.modelo.OrdenReparacion;

public class RepositorioEnMemoria implements RepositorioTaller
{
	private List<OrdenReparacion> ordenes = new ArrayList<OrdenReparacion>();
	
	public List<OrdenReparacion> listarOrdenes()
	{
		return ordenes;
	}
	public OrdenReparacion buscarOrdenPorId(int id)
	{
		for(OrdenReparacion orden : ordenes)
		{			
			if(orden.getId() == id)
			{
				return orden;
			}		
		}
		return null;
	}
	
	public void agregarOrden(OrdenReparacion orden)
	{
		ordenes.add(orden);
	}
}
