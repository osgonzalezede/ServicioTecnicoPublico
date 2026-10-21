package serviciotecnico.modelo;
public class LineaPieza extends LineaOrden {
	private double precioUnitario;
	private int cantidad;	
	public LineaPieza(String concepto, double precioUnitario, int cantidad)	{
		super(concepto);
		this.precioUnitario = precioUnitario;
		this.cantidad = cantidad;
	}
	public double getPrecioUnitario() { return precioUnitario; }	
	public int getCantidad() { return cantidad; }	
	@Override
	public double calcularImporte()	{
		return precioUnitario * cantidad;
	}
}
