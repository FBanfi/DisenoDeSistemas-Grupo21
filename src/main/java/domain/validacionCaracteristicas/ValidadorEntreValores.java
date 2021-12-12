package domain.validacionCaracteristicas;

import domain.excepciones.excepcionesCaracteristica.ExcepcionCaracteristica;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class ValidadorEntreValores extends CriterioDeValidacion {

  @Column
  private Integer maximo;

  @Column
  private Integer minimo;

  public ValidadorEntreValores(Integer maximo, Integer minimo) {
    this.maximo = maximo;
    this.minimo = minimo;
    this.inputEntreMaxMin = true;
  }

  public ValidadorEntreValores() {

  }

  public void validar(String caracteristicaSensible) {

    Integer valor = null;
    try {
      valor = Integer.parseInt(caracteristicaSensible);
    }
    catch (NumberFormatException e){
      throw new ExcepcionCaracteristica("La caracteristica no tiene un valor");
    }
    if (valor < minimo || valor > maximo) {
      throw new ExcepcionCaracteristica("La caracteristica sobrepasa los limites");
    }
  }

  public Integer getMaximo() {
    return maximo;
  }

  public Integer getMinimo() {
    return minimo;
  }
}