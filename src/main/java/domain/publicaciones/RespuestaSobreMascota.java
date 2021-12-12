package domain.publicaciones;

import domain.asociacion.PreguntaSobreMascota;
import domain.persistence.EntidadPersistente;

import javax.persistence.*;

@Entity
@Table
public class RespuestaSobreMascota extends EntidadPersistente {

  @ManyToOne(cascade = {CascadeType.ALL})
  @JoinColumn( name = "pregunta_id")
  private PreguntaSobreMascota pregunta;
  @Column
  private String respuesta;

  public RespuestaSobreMascota(PreguntaSobreMascota pregunta, String respuesta) {
    this.pregunta = pregunta;
    this.respuesta = respuesta;
  }

  public RespuestaSobreMascota() {
  }

  public boolean tieneMismaRespuesta(RespuestaSobreMascota preferencia) {
    return preferencia.getPregunta() == this.getPregunta() && preferencia.getRespuesta().equals(this.getRespuesta());
  }

  public PreguntaSobreMascota getPregunta() {
    return pregunta;
  }

  public String getRespuesta() {
    return respuesta;
  }
}
