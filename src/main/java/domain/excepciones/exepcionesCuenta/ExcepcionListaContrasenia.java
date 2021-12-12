package domain.excepciones.exepcionesCuenta;

import java.io.IOException;

public class ExcepcionListaContrasenia extends RuntimeException {
  public ExcepcionListaContrasenia(IOException e) {
    super(e);
  }
}
