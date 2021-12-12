package domain.validacionCaracteristicas;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class CriterioDeValidacion {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  private int id;

  @Column
  public Boolean inputBoolean = false;

  @Column
  public Boolean inputEntreMaxMin = false;

  @Column
  public Boolean inputEntrePosibilidades = false;

  @Column
  public Boolean inputCantidadCaracteres = false;

  public void validar(String caracteristicaSensible) {}

  public int getId() {
    return id;
  }

  public Boolean getInputBoolean() {
    return inputBoolean;
  }

  public Boolean getInputEntreMaxMin() {
    return inputEntreMaxMin;
  }

  public Boolean getInputEntrePosibilidades() {
    return inputEntrePosibilidades;
  }

  public Boolean getInputCantidadCaracteres() {
    return inputCantidadCaracteres;
  }
}
