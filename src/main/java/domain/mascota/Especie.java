package domain.mascota;

import domain.persistence.EntidadPersistente;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

public enum Especie {
  PERRO,
  GATO;

  Especie() {
  }
}