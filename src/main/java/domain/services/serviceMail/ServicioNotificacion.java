package domain.services.serviceMail;

import domain.mascota.Rescate;
import domain.persistence.EntidadPersistente;
import domain.publicaciones.Publicacion;
import domain.publicaciones.PublicacionAdopcion;
import domain.publicaciones.PublicacionInteresadoAdopcion;
import domain.publicaciones.PublicacionMascotaPerdida;
import domain.usuario.Usuario;
import org.hibernate.annotations.Tables;

import javax.persistence.*;
import java.util.List;

public interface ServicioNotificacion {

  void enviarNotificacionInteresado(Usuario usuario, PublicacionAdopcion publicacionAdopcion);

  void enviarNotificacionRecomendacion(Usuario usuario, List<PublicacionAdopcion> publicacionesRecomendadas);

  void enviarNotificacionMascotaSinChapitaEncontrada(Usuario usuario, PublicacionMascotaPerdida publicacionMascotaPerdida);

  void enviarNotificacionMascotaConChapitaRescatada(Usuario usuario, Rescate rescate);

  void enviarLinkDeBajaPublicacionInteresado(Usuario usuario, PublicacionInteresadoAdopcion publicacionInteresadoAdopcion);
}
