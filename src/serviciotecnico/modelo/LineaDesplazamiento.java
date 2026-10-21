package serviciotecnico.modelo;
public class LineaDesplazamiento extends LineaOrden
{
	private double kilometros;
	private double precioKm;
	public LineaDesplazamiento(String concepto, double kilometros, double precioKm)
	{
		super(concepto);
		this.kilometros = kilometros;
		this.precioKm = precioKm;
	}	
	public double getKilometros() { return kilometros; }
	public double getPrecioKm() { return precioKm; }
	@Override
	public double calcularImporte()
	{			
		if(kilometros * precioKm >= 20.0)
			return kilometros * precioKm;
		return 20.0;				
	}
}
