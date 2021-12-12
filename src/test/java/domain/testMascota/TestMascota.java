package domain.testMascota;

import domain.AbstractPersistenceTest;
import domain.asociacion.Asociacion;
import domain.excepciones.expecionesMascota.ExcepcionMascota;
import domain.mascota.*;
import domain.repositorios.Repositorio;
import domain.repositorios.RepositorioAsociaciones;
import domain.repositorios.RepositorioCaracteristicasIdeales;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class TestMascota extends AbstractPersistenceTest {

  ValidadorEntrePosibilidades validadorEntrePosibilidadesPelo = new ValidadorEntrePosibilidades(Arrays.asList("largo", "corto"));
  ValidadorEntreValores validadorEntreValores = new ValidadorEntreValores(3,0);

  Usuario usuario = new Usuario("Pedro", "Sanchez", "pedritosanchez@gmail.com" ,"Falsa 2021", new Documento(new Dni(), "12345678"), LocalDate.of(2000, 1, 1) ,
      Arrays.asList(new Contacto("Juana", "Sanchez" , 121212, "juana@gmail.com")));

  CaracteristicaIdeal temperamento = new CaracteristicaIdeal("temperamento", false, validadorEntreValores);
  CaracteristicaIdeal pelo = new CaracteristicaIdeal("pelo", false, validadorEntrePosibilidadesPelo);

  HashMap<String, String> caracteristicas = new HashMap<String, String>();

  {
    caracteristicas.put("temperamento", "2");
    caracteristicas.put("pelo", "corto");
  }

  ServicioJavaMail servicioJavaMail = mock(ServicioJavaMail.class);

  MascotaConChapita paquito = new MascotaConChapita("paco","paquito", 2, true, "fachero", Arrays.asList(Paths.get("cualquiera")),
      caracteristicas, Especie.PERRO, usuario, servicioJavaMail);

  MascotaConChapita gero = new MascotaConChapita("geronimo","gero", 7, true, "duerme mucho", Arrays.asList(Paths.get("cualquiera")),
      caracteristicas, Especie.PERRO, usuario, servicioJavaMail);

  Rescate nuevoRescate = new Rescate(usuario, Arrays.asList(Paths.get("cualqqiera")), "Lo encontre yendo a trabajar, estaba asustado", new Ubicacion("Plaza de Mayo", -34.3333, +36.73217));
  ServicioHogares servicioHogares = mock(ServicioHogares.class);



  @BeforeEach
  public void agregarCaracteristicas() {

    RepositorioCaracteristicasIdeales.instance().agregarCaracteristicaIdeal(temperamento);
    RepositorioCaracteristicasIdeales.instance().agregarCaracteristicaIdeal(pelo);
  }



  @Test
  public void unaMascotaNoPuedeTenerAtributosNulos() {

    ExcepcionMascota e = Assertions.assertThrows(ExcepcionMascota.class, () -> new MascotaConChapita(null, "gero", 7, true, "duerme mucho", Arrays.asList(Paths.get("cualqqiera")),
        caracteristicas, Especie.PERRO, usuario, servicioJavaMail));
    assertEquals("Debe especificar el nombre de la mascota", e.getMessage());
  }

  @Test
  public void unaMascotaRescatadaApareceEnElListadoDeMascotasRecientes() {
    Repositorio<Usuario> repositorioUsuarios = new Repositorio<Usuario>("Usuario");
    Repositorio<Rescate> repositorioRescates = new Repositorio<Rescate>("Rescate");
    repositorioUsuarios.agregar(usuario);
    nuevoRescate.setMascotaEncontrada(gero);
    repositorioRescates.agregar(nuevoRescate);

    assertTrue(repositorioRescates.todos().stream().
        filter(Rescate::esFechaReciente).
        anyMatch(rescate -> rescate.getMascotaEncontrada() == gero));
  }


  @Test
  public void cuandoSeRescataUnaMascotaConChapitaSeLeNotificaAlDuenio() {

    paquito.serRescatado(nuevoRescate, servicioHogares);

    verify(servicioJavaMail).enviarNotificacionMascotaConChapitaRescatada(any(), any());
  }

  @Test
  public void cuandoSeRescataUnaMascotaSinChapitaSeCreaUnaPublicacionDeMascotaPerdida(){
    Asociacion asociacion1 = new Asociacion(new Ubicacion("ddd", -32.0, 34.0));
    Asociacion asociacion2 = new Asociacion(new Ubicacion("asdasd",9999999.00,999999.00));

    RepositorioAsociaciones.instance().agregarAsociacion(asociacion1);
    RepositorioAsociaciones.instance().agregarAsociacion(asociacion2);

    MascotaSinChapita pepe = new MascotaSinChapita(true, caracteristicas, Especie.PERRO, servicioJavaMail);
    Asociacion asociacionCercana = RepositorioAsociaciones.instance().getAsociacionMasCercanaA(nuevoRescate.getLugar());

    //agregar metodo de distancia en ubicacion y que se compare las distacias de todas las asos y se elijan la mas cercana

    Integer cantidadInicial = asociacionCercana.getPublicaciones().size();
    pepe.serRescatado(nuevoRescate, servicioHogares);

    Assertions.assertEquals(asociacionCercana.getPublicaciones().size(), cantidadInicial + 1);
  }
}
