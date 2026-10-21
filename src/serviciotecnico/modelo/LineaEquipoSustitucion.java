package serviciotecnico.modelo;

public class LineaEquipoSustitucion extends LineaOrden
{
	private int dias;	
	private double precioDia;
	
	public LineaEquipoSustitucion(String concepto, int dias, double precioDia)
	{
		super(concepto);
		this.dias = dias;
		this.precioDia = precioDia;
	}
	
	@Override
	public double calcularImporte()
	{
		if(dias <= 5)
			return dias * precioDia;
		else
			return 5 * precioDia;
	}
	
	public int getDias()
	{
		return dias;
	}

	public double getPrecioDia()
	{
		return precioDia;
	}	
}
