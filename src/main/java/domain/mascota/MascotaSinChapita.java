package domain.mascota;

import domain.asociacion.Asociacion;
import domain.publicaciones.PublicacionMascotaPerdida;
import domain.repositorios.RepositorioAsociaciones;
import domain.services.serviceHogares.ServicioHogares;
import domain.services.serviceMail.ServicioNotificacion;
import domain.usuario.Usuario;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Map;

@Entity
@DiscriminatorValue("S")
public class MascotaSinChapita extends Mascota{

  public MascotaSinChapita(Boolean esMacho, Map<String, String> caracteristicas,
                           Especie especie, ServicioNotificacion servicioNotificacion) {
    super(esMacho, caracteristicas, especie, servicioNotificacion);
  }

  public MascotaSinChapita() {

  }

  @Override
   void gestionarRescate(Rescate rescate, ServicioHogares servicioHogares){//con la interfaz de comuniacion no necesitamos pasar email por parametro
    Asociacion asociacion = RepositorioAsociaciones.instance().getAsociacionMasCercanaA(rescate.getLugar());
    PublicacionMascotaPerdida publicacion = new PublicacionMascotaPerdida(rescate.getRescatista(), servicioNotificacion, rescate, servicioHogares);
    asociacion.registrarPublicacionMascotaPerdida(publicacion);
  }

  @Override
  protected Usuario getDuenio() {
    return null;
  }


}