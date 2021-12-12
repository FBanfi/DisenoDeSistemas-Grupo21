package domain.excepciones.excepcionesEmail;

public class ExcepcionEmail extends RuntimeException {
  public ExcepcionEmail(String mensaje) {
    super(mensaje);
  }
}
