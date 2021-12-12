package domain.usuario.documento;

import domain.persistence.EntidadPersistente;

import javax.persistence.*;

@Entity
@Table
public class Documento extends EntidadPersistente {

  @ManyToOne(cascade = {CascadeType.ALL})
  private TipoDocumento tipo;

  @Column
  private String numero;

  public Documento(TipoDocumento tipo, String numero) {
    this.tipo = tipo;
    tipo.validarNumeroDocuento(numero);
    this.numero = numero;
  }

  public Documento() {

  }
}
