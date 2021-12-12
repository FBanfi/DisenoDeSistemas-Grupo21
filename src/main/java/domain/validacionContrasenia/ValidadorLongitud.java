package domain.validacionContrasenia;

import domain.excepciones.exepcionesCuenta.ExcepcionContrasenia;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("LG")
public class ValidadorLongitud extends Validaciones {

  @Override
  public void validarAtributo(String contrasenia, String nombreUsuario) {

    if (contrasenia.length() < 8) {

      throw new ExcepcionContrasenia("La contrasenia debe tener al menos 8 caracteres");
    }
  }
}
