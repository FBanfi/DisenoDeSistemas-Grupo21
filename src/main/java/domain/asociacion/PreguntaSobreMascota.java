package domain.asociacion;

import domain.persistence.EntidadPersistente;
import domain.validacionCaracteristicas.ValidadorBooleano;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table
@Embeddable
public class PreguntaSobreMascota extends EntidadPersistente {

  @Column
  private String preguntaPonerEnAdopcion; // Es la mascota juguetona?

  @Column
  private String preguntaInteresadoEnAdoptar; // Prefiere a una mascota juguetona?

  @Transient
  private String key; // jugueton


  public PreguntaSobreMascota(String preguntaPonerEnAdopcion, String preguntaInteresadoEnAdoptar, String key) {
    this.preguntaPonerEnAdopcion = preguntaPonerEnAdopcion;
    this.preguntaInteresadoEnAdoptar = preguntaInteresadoEnAdoptar;
    this.key = key;
  }

  public PreguntaSobreMascota() {

  }

  public PreguntaSobreMascota(String preguntaPonerEnAdopcion, String preguntaInteresadoEnAdoptar) {
    this.preguntaPonerEnAdopcion = preguntaPonerEnAdopcion;
    this.preguntaInteresadoEnAdoptar = preguntaInteresadoEnAdoptar;
  }


  public void validarRespuesta(Map<String, String> respuesta) {
    ValidadorBooleano validador = new ValidadorBooleano();
    validador.validar(respuesta.get(key));
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public void setPreguntaPonerEnAdopcion(String preguntaPonerEnAdopcion) {
    this.preguntaPonerEnAdopcion = preguntaPonerEnAdopcion;
  }

  public void setPreguntaInteresadoEnAdoptar(String preguntaInteresadoEnAdoptar) {
    this.preguntaInteresadoEnAdoptar = preguntaInteresadoEnAdoptar;
  }

}
