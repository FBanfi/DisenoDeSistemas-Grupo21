package domain.publicaciones;

import domain.excepciones.excepcionesPublicacion.ExcepcionPublicacion;
import domain.services.serviceMail.ServicioNotificacion;
import domain.usuario.Usuario;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@DiscriminatorValue("I")
public class PublicacionInteresadoAdopcion extends Publicacion {

  @Transient
  private Map<String, String> preferencias;

  @OneToMany(cascade = {CascadeType.ALL})
  @JoinColumn( name = "publicacion_interesado_adopcion_id")
  private List<RespuestaSobreMascota> preferenciasMascota;

  public PublicacionInteresadoAdopcion(Usuario duenioPublicacion,
                                       ServicioNotificacion servicioNotificacion,
                                       List<RespuestaSobreMascota> preferenciasMascota) {
    super(duenioPublicacion, servicioNotificacion);
    this.preferenciasMascota = preferenciasMascota;
  }

  public PublicacionInteresadoAdopcion() {

  }

  @Override
  public Boolean esDeInteresadoParaAdoptar() {
    return true;
  }

  @Override
  public void gestionarNotificacion(Usuario unInteresado) {
    throw new ExcepcionPublicacion("Una publicacion de una interesado en adopcion intenta gestionar notificacion");
  }


  public void notificarRecomendacion(List<PublicacionAdopcion> publicacionesAdopcion){
    List<PublicacionAdopcion> publicacionesFiltradas = publicacionesAdopcion.stream().
        filter(publicacion -> this.esApto2(publicacion)).collect(Collectors.toList());

    servicioNotificacion.enviarNotificacionRecomendacion(duenioPublicacion, publicacionesFiltradas);
  }

  /*
  public Boolean esApto (PublicacionAdopcion publicacionAdopcion){

   return preferencias.entrySet().stream().allMatch(
       entrada -> publicacionAdopcion.esAptaLaPreferencia(entrada.getKey(), entrada.getValue()));
  }
*/
  public Boolean esApto2 (PublicacionAdopcion publicacionAdopcion){
    return preferenciasMascota.stream().allMatch(preferencia ->  publicacionAdopcion.esAptaLaPreferencia2(preferencia));
  }

  public void enviarLinkDeBaja(){
    servicioNotificacion.enviarLinkDeBajaPublicacionInteresado(duenioPublicacion, this);
  }

}
