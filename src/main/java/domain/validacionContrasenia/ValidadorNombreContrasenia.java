package domain.validacionContrasenia;

import domain.excepciones.exepcionesCuenta.ExcepcionContrasenia;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("NC")
public class ValidadorNombreContrasenia extends Validaciones {

  @Override
  public void validarAtributo(String unaContrasenia, String unNombreUsuario) {

    if (unaContrasenia.equals(unNombreUsuario)) {

      throw new ExcepcionContrasenia("La contrasenia no debe ser igual al nombre de usuario");
    }
  }
}
