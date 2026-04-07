package src;

class EnvioTerrestre extends Envio {

    public EnvioTerrestre(String cliente, String codigo, double peso, double distancia) {
        super(cliente, codigo, peso, distancia);
    }

    @Override
    public double calcularTarifa() {
        return (distancia * 1500) + (peso * 2000);
    }
}