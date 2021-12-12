package domain.mascota;

import domain.excepciones.excepcionesCaracteristica.ExcepcionCaracteristica;
import domain.persistence.EntidadPersistente;
import domain.validacionCaracteristicas.CriterioDeValidacion;

import javax.persistence.*;
import java.util.Map;
import java.util.Objects;

@Entity
@Table
public class CaracteristicaIdeal  extends EntidadPersistente {

  @Column
  private Boolean esObligatorio;

  @OneToOne(cascade = {CascadeType.ALL})                    //Que estrategia de herencia o como hacer?
  private CriterioDeValidacion validadorCaracteristica;

  @Column
  private String nombreCaracteristica;

  public CaracteristicaIdeal(String nombreCaracteristica, Boolean esObligatorio, CriterioDeValidacion validadorCaracteristica) {
    validarParametroOpcional(esObligatorio);
    validarParametroValidador(validadorCaracteristica);
    validarParametroNombre(nombreCaracteristica);
    this.esObligatorio = esObligatorio;
    this.validadorCaracteristica = validadorCaracteristica;
    this.nombreCaracteristica = nombreCaracteristica;
  }

  public CaracteristicaIdeal() {

  }

  public void cambiarObligatoriedad(Boolean esObligatorio) {
    this.esObligatorio = esObligatorio;
  }

  public void validarCaracteristicaSensible(Map<String, String> caracteristicasSensible) {
    String caracteristicaSensible = caracteristicasSensible.get(nombreCaracteristica);
    validarObligatoriedad(caracteristicaSensible);
    if (!Objects.isNull(caracteristicaSensible)) {
        validadorCaracteristica.validar(caracteristicaSensible);
     }
  }

  public void validarObligatoriedad(Object caracteristicaSensible) {
    if (esObligatorio && Objects.isNull(caracteristicaSensible)) {
      throw new ExcepcionCaracteristica("La caracteristica es obligatoria");
    }
  }


  //  VALIDACIONES DE LAS INSTANCIACION

  private void validarParametroValidador(CriterioDeValidacion validadorCaracteristica) {
    if (Objects.isNull(validadorCaracteristica)) {
      throw new ExcepcionCaracteristica("El validador no puede ser null");
    }
  }

  private void validarParametroOpcional(Boolean esObligatorio) {
    if (Objects.isNull(esObligatorio)) {
      throw new ExcepcionCaracteristica("La opcionalidad no puede ser null");
    }
  }

  private void validarParametroNombre(String nombreCaracteristica) {
    if (Objects.isNull(nombreCaracteristica)) {
      throw new ExcepcionCaracteristica("El nombre de la caracteristica no puede ser null");
    }
  }

  public boolean tieneNombre(String nombre) {
    return nombreCaracteristica.equals(nombre);
  }

  public String getNombreCaracteristica() {
    return nombreCaracteristica;
  }

  public Boolean getEsObligatorio() {
    return esObligatorio;
  }

  public CriterioDeValidacion getValidadorCaracteristica() {
    return validadorCaracteristica;
  }
}
