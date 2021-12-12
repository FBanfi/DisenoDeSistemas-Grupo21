package domain.validacionCaracteristicas;

import domain.excepciones.excepcionesCaracteristica.ExcepcionCaracteristica;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class ValidadorCantidadCaracteres extends CriterioDeValidacion {

  @Column
  private int cantidadMaximaCaracteres;

  public ValidadorCantidadCaracteres(int cantidadMaximaCaracteres) {
    this.cantidadMaximaCaracteres = cantidadMaximaCaracteres;
    this.inputCantidadCaracteres = true;
  }

  public ValidadorCantidadCaracteres() {

  }

  public void validar(String caracteristicaSensible){
    if(caracteristicaSensible.length() > cantidadMaximaCaracteres){
      throw new ExcepcionCaracteristica("La caracteristica sobrepasa la cantidad de caracteres");
    }
  }

  public int getCantidadMaximaCaracteres() {
    return cantidadMaximaCaracteres;
  }
}
