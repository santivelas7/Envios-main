package src;

class EnvioAereo extends Envio {

    public EnvioAereo(String cliente, String codigo, double peso, double distancia) {
        super(cliente, codigo, peso, distancia);
    }

    @Override
    public double calcularTarifa() {
        return (distancia * 5000) + (peso * 4000);
    }
}