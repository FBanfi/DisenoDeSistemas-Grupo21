package domain.testPublicaciones;

import domain.AbstractPersistenceTest;
import domain.mascota.CaracteristicaIdeal;
import domain.mascota.Especie;
import domain.mascota.MascotaConChapita;
import domain.mascota.Rescate;
import domain.publicaciones.PublicacionMascotaPerdida;
import domain.services.serviceHogares.ServicioHogares;
import domain.services.serviceHogares.Ubicacion;
import domain.services.serviceMail.ServicioJavaMail;
import domain.usuario.Contacto;
import domain.usuario.Usuario;
import domain.usuario.documento.Dni;
import domain.usuario.documento.Documento;
import domain.validacionCaracteristicas.ValidadorEntrePosibilidades;
import domain.validacionCaracteristicas.ValidadorEntreValores;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;

import static org.mockito.Mockito.mock;

public class TestPublicacion extends AbstractPersistenceTest {


  ServicioJavaMail servicioJavaMail = mock(ServicioJavaMail.class);

  ValidadorEntrePosibilidades validadorEntrePosibilidadesPelo = new ValidadorEntrePosibilidades(Arrays.asList("largo", "corto"));
  ValidadorEntreValores validadorEntreValores = new ValidadorEntreValores(3,0);

  Documento documento1 = new Documento(new Dni(), "12345678");

  Usuario usuarioSanchez = new Usuario("Juan", "Sanchez", "pedritosanchez@gmail.com","Falsa 2021", documento1, LocalDate.of(2000, 1, 1),
      Arrays.asList(new Contacto("Juana", "Sanchez" , 121212, "juana@gmail.com")));

  CaracteristicaIdeal temperamento = new CaracteristicaIdeal("temperamento", false, validadorEntreValores);
  CaracteristicaIdeal pelo = new CaracteristicaIdeal("pelo", false, validadorEntrePosibilidadesPelo);

  HashMap<String, String> caracteristicas = new HashMap<String, String>();

  {
    caracteristicas.put("temperamento", "2");
    caracteristicas.put("pelo", "corto");
  }


  MascotaConChapita paquito = new MascotaConChapita("paco","paquito", 2, true, "fachero", Arrays.asList(Paths.get("cualquiera")),
      caracteristicas, Especie.PERRO, usuarioSanchez, new ServicioJavaMail());

  public PublicacionMascotaPerdida unaPublicacionMascotaPerdida(){

      Rescate rescate = new Rescate(usuarioSanchez, Arrays.asList(Paths.get("cualquiera")),
          "Lo encontre yendo a trabajar, estaba asustado",
          new Ubicacion("Plaza de Mayo", -34.3333, +36.73217));

      rescate.setMascotaEncontrada(paquito);

      return new PublicacionMascotaPerdida(rescate.getRescatista(), servicioJavaMail, rescate, new ServicioHogares());
    }

  PublicacionMascotaPerdida publicacionMascotaPerdida = unaPublicacionMascotaPerdida();

  @Test
  void unaPublicacionPuedeHacerseVisible() {
    publicacionMascotaPerdida.hacerVisible();
    Assertions.assertTrue(publicacionMascotaPerdida.esVisible());
  }

}
