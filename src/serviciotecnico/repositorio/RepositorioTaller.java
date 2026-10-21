package serviciotecnico.repositorio;

import java.util.List;

import serviciotecnico.modelo.OrdenReparacion;

public interface RepositorioTaller
{
	List<OrdenReparacion> listarOrdenes();
	OrdenReparacion buscarOrdenPorId(int id);
}
