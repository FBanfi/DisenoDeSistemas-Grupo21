package domain.validacionContrasenia;

import domain.persistence.EntidadPersistente;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Tipo")
public abstract class Validaciones extends EntidadPersistente {

  public void validarAtributo(String contrasenia, String nombreUsuario) {

  }
}
