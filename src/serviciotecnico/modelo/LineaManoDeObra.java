package serviciotecnico.modelo;
public class LineaManoDeObra extends LineaOrden
{
	private double horas;
	private double tarifaHora;
	public LineaManoDeObra(String concepto, double horas, double tarifaHora)
	{
		super(concepto);
		this.horas = horas;
		this.tarifaHora = tarifaHora;
	}	
	public double getHoras() { return horas; }
	public double getTarifaHora() { return tarifaHora; }
	@Override
	public double calcularImporte()
	{
		return horas * tarifaHora;
	}
}
