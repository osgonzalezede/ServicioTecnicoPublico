package serviciotecnico.modelo;
public abstract class LineaOrden
{
	private String concepto;	
	public LineaOrden(String concepto)
	{
		this.concepto = concepto;
	}
	public abstract double calcularImporte();	
	public String getConcepto() { return concepto; }	
	@Override
	public String toString()
	{
		return concepto + ": " + calcularImporte() + " EUR";
	}
}
