package domain.validacionCaracteristicas;

import domain.excepciones.excepcionesCaracteristica.ExcepcionCaracteristica;

import javax.persistence.Entity;

@Entity
public class ValidadorBooleano extends CriterioDeValidacion {

  public ValidadorBooleano() {
    this.inputBoolean = true;
  }

  public void validar(Object caracterisicaSensible) {
    if(!esBooleano(caracterisicaSensible)){
      throw new ExcepcionCaracteristica("El value de la key no es booleana, o no es null");
    }
  }

  private boolean esBooleano(Object caracterisicaSensible) {
    return caracterisicaSensible.toString().equals("true") || caracterisicaSensible.toString().equals("false");
  }

}
