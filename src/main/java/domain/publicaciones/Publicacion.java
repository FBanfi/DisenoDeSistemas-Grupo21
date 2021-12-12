package domain.publicaciones;

import domain.excepciones.excepcionesPublicacion.ExcepcionPublicacion;
import domain.persistence.EntidadPersistente;
import domain.services.serviceMail.ServicioNotificacion;
import domain.usuario.Usuario;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Tipo")
public abstract class Publicacion extends EntidadPersistente {

  @ManyToOne
  @JoinColumn(name = "duenioPublicacion_id")
  public Usuario duenioPublicacion; //quien publico la publicacion

  @ManyToOne
  @JoinColumn(name = "interesadoPublicacion_id")
  private Usuario interesadoPublicacion; //quien pregunto por la publicacion

  @Column
  public boolean visible;

  @Transient
  public ServicioNotificacion servicioNotificacion;

  public Publicacion(Usuario duenioPublicacion, ServicioNotificacion servicioNotificacion) {
    this.duenioPublicacion = duenioPublicacion;
    this.servicioNotificacion = servicioNotificacion;
    this.interesadoPublicacion = null;
    this.visible = false;
  }

  public Publicacion() {

  }


  public void hacerVisible(){
    this.visible = true;
  }

  public boolean esVisible() {
    return visible;
  }

  public void alguienEstaInteresado(Usuario unInteresado){
    validarParametroNull(unInteresado);
    validarVisibilidadPublicacion();
    interesadoPublicacion = unInteresado;
    gestionarNotificacion(unInteresado);
  }

  public void validarParametroNull(Object parametro) {
    if (parametro == null){
      throw new ExcepcionPublicacion("Hay un parametro null");
    }
  }

  public void validarVisibilidadPublicacion() {
    if (!esVisible()) {
      throw new ExcepcionPublicacion("No puede haber alguien interesado en una publicacion que no esta visible");
    }
  }

  protected abstract void gestionarNotificacion(Usuario unInteresado);

  public Boolean esDeInteresadoParaAdoptar() {
    return false;
  }

  public Boolean esDeAdopcion() {
    return false;
  }

}
