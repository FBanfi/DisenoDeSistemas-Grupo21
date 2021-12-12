package domain.excepciones.exepcionesCuenta;

public class ExcepcionUsuario extends RuntimeException {

  public ExcepcionUsuario(String mensaje) {

    super(mensaje);
  }
}
