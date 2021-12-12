package domain.excepciones.excepcionesPublicacion;

public class ExcepcionPublicacion extends  RuntimeException{
  public ExcepcionPublicacion(String mensaje) {

    super(mensaje);
  }
}