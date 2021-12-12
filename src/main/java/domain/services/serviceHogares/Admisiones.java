package domain.services.serviceHogares;

import domain.mascota.Especie;

import javax.persistence.Embeddable;

@Embeddable
public class Admisiones {

  public boolean perros;
  public boolean gatos;

  public Admisiones(boolean perros, boolean gatos) {
    this.perros = perros;
    this.gatos = gatos;
  }

  public Admisiones() {

  }

  public boolean aceptaEspecie(Especie especie) {
    if (especie == Especie.PERRO) {
      return perros;
    } else {
      return gatos;
    }
  }
}
