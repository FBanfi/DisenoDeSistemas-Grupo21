package domain.publicaciones;

import domain.mascota.Rescate;
import domain.services.serviceHogares.HogarTransitorio;
import domain.services.serviceHogares.ListadoHogares;
import domain.services.serviceHogares.ServicioHogares;
import domain.services.serviceMail.ServicioNotificacion;
import domain.usuario.Usuario;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@DiscriminatorValue("P")
public class PublicacionMascotaPerdida extends Publicacion{

  @OneToOne
  private Rescate rescate;

  @ManyToOne
  @JoinColumn( name = "hogar_id")
  private HogarTransitorio hogarDeTransito;

  @Transient
  private ServicioHogares servicioHogares;

  public PublicacionMascotaPerdida(Usuario duenioPublicacion, ServicioNotificacion servicioNotificacion, Rescate rescate, ServicioHogares servicioHogares) {
    super(duenioPublicacion, servicioNotificacion);
    this.rescate = rescate;
    this.visible = false;
    this.servicioHogares = servicioHogares;
  }

  public PublicacionMascotaPerdida() {

  }

  public List<HogarTransitorio> buscarPosiblesHogares(Integer radio) {
    ListadoHogares listadoHogares = servicioHogares.listadoHogares();
    return
        listadoHogares.getHogares().stream().
        filter(hogar1 -> hogar1.esApto(rescate.getMascotaEncontrada(), rescate.getLugar(), radio)).
        collect(Collectors.toList());
  }


  @Override
  public void gestionarNotificacion(Usuario unInteresado) {
    servicioNotificacion.enviarNotificacionMascotaSinChapitaEncontrada(unInteresado, this);
  }

  public void hacerVisible(){
    this.visible = true;
  }

  //GETTERS Y SETTERS

  public void setHogarDeTransito(HogarTransitorio hogarDeTransito) {
    this.hogarDeTransito = hogarDeTransito;
  }

  public boolean esVisible() {
    return visible;
  }

  public Rescate getRescate() {
    return rescate;
  }
}