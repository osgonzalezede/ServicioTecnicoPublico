package serviciotecnico.modelo;

public class Tecnico
{
	private String nombre;
	private double tarifaHora;

	public Tecnico(String nombre, double tarifaHora)
	{
		this.nombre = nombre;
		this.tarifaHora = tarifaHora;
	}

	public String getNombre()
	{
		return nombre;
	}

	public double getTarifaHora()
	{
		return tarifaHora;
	}

}
