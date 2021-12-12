package domain.validacionCaracteristicas;

import domain.excepciones.excepcionesCaracteristica.ExcepcionCaracteristica;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ValidadorEntrePosibilidades extends CriterioDeValidacion {

  @ElementCollection
  List<String> posibilidades = new ArrayList<String>();

  public ValidadorEntrePosibilidades(List<String> posibilidades) {
    this.posibilidades = posibilidades;
    this.inputEntrePosibilidades = true;
  }

  public ValidadorEntrePosibilidades() {
  }

  public void validar(String caracteristicaSensible){
    if(!posibilidades.contains(caracteristicaSensible)){
      throw new ExcepcionCaracteristica("La caracteristica no esta dentro de las posibilidades");
    }
  }

  public List<String> getPosibilidades() {
    return posibilidades;
  }
}
