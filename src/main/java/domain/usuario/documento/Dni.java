package domain.usuario.documento;

import domain.excepciones.exepcionesCuenta.ExcepcionUsuario;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("D")
public class Dni extends TipoDocumento{

  @Override
  public void validarNumeroDocuento(String numero) {
    if(numero.length() != 8){
      throw new ExcepcionUsuario("El numero de dni no es valido");
    }
  }
}
