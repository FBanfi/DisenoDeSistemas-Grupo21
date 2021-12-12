package domain.excepciones.exepcionesCuenta;

public class ExcepcionContrasenia extends RuntimeException {

  public ExcepcionContrasenia(String mensaje) {
  
    super(mensaje);
  }
}
