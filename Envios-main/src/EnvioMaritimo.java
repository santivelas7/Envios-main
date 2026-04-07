package src;

class EnvioMaritimo extends Envio {

    public EnvioMaritimo(String cliente, String codigo, double peso, double distancia) {
        super(cliente, codigo, peso, distancia);
    }

    @Override
    public double calcularTarifa() {
        return (distancia * 800) + (peso * 1000);
    }
}
