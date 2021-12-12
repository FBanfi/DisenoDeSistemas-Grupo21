
package domain.testPublicaciones;

import domain.AbstractPersistenceTest;
import domain.asociacion.PreguntaSobreMascota;
import domain.mascota.Especie;
import domain.mascota.MascotaConChapita;
import domain.publicaciones.PublicacionAdopcion;
import domain.publicaciones.RespuestaSobreMascota;
import domain.services.serviceMail.ServicioJavaMail;
import domain.usuario.Contacto;
import domain.usuario.Usuario;
import domain.usuario.documento.Dni;
import domain.usuario.documento.Documento;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TestPublicacionAdopcion extends AbstractPersistenceTest {

  ServicioJavaMail servicioJavaMail = mock(ServicioJavaMail.class);

  PreguntaSobreMascota preguntaAltura = new PreguntaSobreMascota("La mascota a adoptar es alta?", "Desea adoptar una mascota alta?");

  @Test
  void elDuenioDeLaPublicacionRecibioLaNotificacionDeQueAlguienEstaInteresadoEnAdoptarSuMascota() {
    Usuario juan = new Usuario("Juan", "Sanchez", "pedritosanchez@gmail.com",
        "Falsa 2021", new Documento(new Dni(), "12345678"), LocalDate.of(2000, 1, 1),
        Arrays.asList(new Contacto("Juana", "Sanchez" , 121212, "juana@gmail.com")));

    PublicacionAdopcion publicacionAdopcion = this.unaPublicacionDeAdopcion();
    publicacionAdopcion.hacerVisible();
    publicacionAdopcion.alguienEstaInteresado(juan);

    verify(servicioJavaMail).enviarNotificacionInteresado(any(),any());
  }

  public PublicacionAdopcion unaPublicacionDeAdopcion(){


    Usuario usuario = new Usuario("Juan", "Sanchez", "pedritosanchez@gmail.com",
        "Falsa 2021", new Documento(new Dni(), "12345678"), LocalDate.of(2000, 1, 1),
        Arrays.asList(new Contacto("Juana", "Sanchez" , 121212, "juana@gmail.com")));

    List<RespuestaSobreMascota> respuestas = new ArrayList<RespuestaSobreMascota>();
    respuestas.add(new RespuestaSobreMascota(preguntaAltura, "false"));

    HashMap<String, String> caracteristicas = new HashMap<String, String>();
    caracteristicas.put("temperamento", "2");
    caracteristicas.put("pelo", "corto");

    MascotaConChapita paquito = new MascotaConChapita("paco","paquito", 2, true, "fachero", Arrays.asList(Paths.get("cualquiera")),
        caracteristicas, Especie.PERRO, usuario, servicioJavaMail);

    return new PublicacionAdopcion(usuario, servicioJavaMail, paquito, respuestas);
  }

}
