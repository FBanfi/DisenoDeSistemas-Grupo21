package domain.usuario.documento;

import domain.persistence.EntidadPersistente;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Tipo")
public abstract class TipoDocumento extends EntidadPersistente {

  public void validarNumeroDocuento(String numero) { }

}
