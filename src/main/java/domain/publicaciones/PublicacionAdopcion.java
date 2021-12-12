package domain.publicaciones;

import domain.mascota.MascotaConChapita;
import domain.services.serviceMail.ServicioNotificacion;
import domain.usuario.Usuario;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@DiscriminatorValue("A")
public class PublicacionAdopcion extends Publicacion {

  @OneToOne(cascade = {CascadeType.ALL})
  public MascotaConChapita mascotaEnAdopcion;


  @OneToMany(cascade = {CascadeType.ALL})
  @JoinColumn( name = "publicacion_adopcion_id")
  public List<RespuestaSobreMascota> respuestas;

  public PublicacionAdopcion(Usuario duenioPublicacion, ServicioNotificacion servicioNotificacion, MascotaConChapita mascotaEnAdopcion,
                             List<RespuestaSobreMascota> respuestas) {
    super(duenioPublicacion, servicioNotificacion);
    this.mascotaEnAdopcion = mascotaEnAdopcion;
    this.respuestas = respuestas;
  }

  public PublicacionAdopcion() {
    super();
  }

  @Override
  protected void gestionarNotificacion(Usuario unInteresado) {
    servicioNotificacion.enviarNotificacionInteresado(unInteresado, this);
  }

  /*
  boolean esAptaLaPreferencia(String key, String value) {
    return respuestasSobreMascota.get(key) == value;
  }
*/
  public boolean esAptaLaPreferencia2(RespuestaSobreMascota preferencia) {
    return respuestas.stream().anyMatch(respuestaSobreMascota -> respuestaSobreMascota.tieneMismaRespuesta(preferencia));
  }

  @Override
  public Boolean esDeAdopcion() {
    return true;
  }

  /*
  public Map<String, String> getRespuestasSobreMascota() {
    return respuestasSobreMascota;
  }
*/

}
