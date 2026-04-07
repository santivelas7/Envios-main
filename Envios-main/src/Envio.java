package src;

// Archivo: Envio.java
// Debe ser abstract para que el polimorfismo funcione
public abstract class Envio {
    // Atributos que comparten todos los envíos
    protected String cliente;
    protected String codigo;
    protected double peso;
    protected double distancia;

    // Constructor corregido
    public Envio(String cliente, String codigo, double peso, double distancia) {
        this.cliente = cliente;
        this.codigo = codigo;
        this.peso = peso;
        this.distancia = distancia;
    }

    // Método abstracto: Obliga a Aereo, Terrestre y Maritimo a calcular su tarifa
    public abstract double calcularTarifa();

    // MÉTODOS GETTER: Estos son los que "limpian" los errores rojos en Logistica.java
    public String getCodigo() { return codigo; }
    public String getCliente() { return cliente; }
    public double getPeso() { return peso; }
    public double getDistancia() { return distancia; }

    @Override
    public String toString() {
        return "Cliente: " + cliente + " | Código: " + codigo + " | Tarifa: " + calcularTarifa();
    }
}