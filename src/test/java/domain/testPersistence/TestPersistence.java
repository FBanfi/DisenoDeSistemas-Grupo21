
package domain.testPersistence;

import domain.AbstractPersistenceTest;
import domain.asociacion.Asociacion;
import domain.asociacion.PreguntaSobreMascota;
import domain.mascota.CaracteristicaIdeal;
import domain.mascota.Especie;
import domain.mascota.MascotaConChapita;
import domain.mascota.Rescate;
import domain.publicaciones.PublicacionAdopcion;
import domain.publicaciones.PublicacionInteresadoAdopcion;
import domain.publicaciones.PublicacionMascotaPerdida;
import domain.publicaciones.RespuestaSobreMascota;
import domain.repositorios.Repositorio;
import domain.services.serviceHogares.ServicioHogares;
import domain.services.serviceHogares.Ubicacion;
import domain.services.serviceMail.ServicioJavaMail;
import domain.usuario.Contacto;
import domain.usuario.Cuenta;
import domain.usuario.Usuario;
import domain.usuario.documento.Dni;
import domain.usuario.documento.Documento;
import domain.validacionCaracteristicas.ValidadorEntrePosibilidades;
import domain.validacionCaracteristicas.ValidadorEntreValores;
import domain.validacionContrasenia.ValidadorContrasenias;
import domain.validacionContrasenia.ValidadorListaConstrasenias;
import domain.validacionContrasenia.ValidadorLongitud;
import domain.validacionContrasenia.ValidadorNombreContrasenia;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.mock;

public class TestPersistence extends AbstractPersistenceTest {


  PreguntaSobreMascota preguntaPerro = new PreguntaSobreMascota("La mascota a adoptar es un perro?", "Desea adoptar un perro?");
  PreguntaSobreMascota preguntaGrande = new PreguntaSobreMascota("La mascota a adoptar es grande?", "Desea adoptar una mascota grande?");
  PreguntaSobreMascota preguntaAltura = new PreguntaSobreMascota("La mascota a adoptar es alta?", "Desea adoptar una mascota alta?");


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

  Repositorio repositorioCaracteristicas = new Repositorio<CaracteristicaIdeal>("CaracteristicaIdeal");


  MascotaConChapita paquito = new MascotaConChapita("paco","paquito", 2, true, "fachero", Arrays.asList(Paths.get("cualquiera")),
      caracteristicas, Especie.PERRO, usuarioSanchez, new ServicioJavaMail());


  Repositorio repositorioMascota = new Repositorio<MascotaConChapita>("Mascota");
  Repositorio repositorioUsuario = new Repositorio<Usuario>("Usuario");
  Repositorio repositorioDocumento = new Repositorio<Documento>("Documento");


  Asociacion asociacion1 = new Asociacion(new Ubicacion("utn frba medrano", 140.0, 222.0));
  Asociacion asociacion2 = new Asociacion(new Ubicacion("utn frba campus", 50.0, 77.0));

  PreguntaSobreMascota pregunta2 = new PreguntaSobreMascota("la mascota es juguetona?",
      "prefiere una mascota juguetona?", "esJuguetona");

  PreguntaSobreMascota pregunta1 = new PreguntaSobreMascota("la mascota muerde?",
      "esta de acuerdo con que la mascota ocasionalmente lo muerda?", "muerde");

  PublicacionInteresadoAdopcion publicacionInteresadoAdopcion = unaPublicacionDeInteresEnAdoptar();
  PublicacionAdopcion publicacionAdopcion = unaPublicacionDeAdopcion();
  PublicacionMascotaPerdida publicacionMascotaPerdida = unaPublicacionMascotaPerdida();

  Repositorio repositorioAsociacion = new Repositorio<Asociacion>("Asociacion");
  Repositorio repositorioPublicacionesAdopcion = new Repositorio<PublicacionAdopcion>("PublicacionAdopcion");
  Repositorio repositorioPublicacionesInteresadoAdopcion = new Repositorio<PublicacionInteresadoAdopcion>("PublicacionInteresadoAdopcion");
  Repositorio repositorioPublicacionesMascotasPerdidas = new Repositorio<PublicacionMascotaPerdida>("PublicacionMascotaPerdida");
  Repositorio repositorioRescates = new Repositorio<Rescate>("Rescate");
  Repositorio repositorioCuenta = new Repositorio<Cuenta>("Cuenta");

  ServicioJavaMail servicioJavaMail = mock(ServicioJavaMail.class);


  @Test
  public void puedePersistirYRecuperarUnaMascota() {


    repositorioDocumento.agregar(documento1);
    repositorioCaracteristicas.agregar(temperamento);
    repositorioCaracteristicas.agregar(pelo);


    repositorioUsuario.agregar(usuarioSanchez);
    repositorioMascota.agregar(paquito);

    //EntityManagerHelper.commit();

    Assertions.assertEquals(1, repositorioMascota.cantidad());
  }

  @Test
  public void puedePersistirYRecuperarUnaAsociacion() {


    repositorioDocumento.agregar(documento1);
    repositorioCaracteristicas.agregar(temperamento);
    repositorioCaracteristicas.agregar(pelo);


    repositorioUsuario.agregar(usuarioSanchez);
    repositorioMascota.agregar(paquito);

    asociacion1.registrarPublicacionInteresadoAdopcion(publicacionInteresadoAdopcion);
    asociacion1.registrarPublicacionAdopcion(publicacionAdopcion);
    asociacion1.registrarPublicacionMascotaPerdida(publicacionMascotaPerdida);

    repositorioRescates.agregar(publicacionMascotaPerdida.getRescate());
    repositorioPublicacionesAdopcion.agregar(publicacionAdopcion);
    repositorioPublicacionesInteresadoAdopcion.agregar(publicacionInteresadoAdopcion);
    repositorioPublicacionesMascotasPerdidas.agregar(publicacionMascotaPerdida);
    repositorioAsociacion.agregar(asociacion1);

    //EntityManagerHelper.commit();

    Assertions.assertEquals(1, repositorioAsociacion.cantidad());
    Assertions.assertEquals(1, repositorioPublicacionesAdopcion.cantidad());
  }

  @Test
  void sePuedePersistirYRecuperarUnaCuenta() {


    ValidadorListaConstrasenias validadorListaConstrasenias =  new ValidadorListaConstrasenias();
    ValidadorLongitud validadorLongitud = new ValidadorLongitud();
    ValidadorNombreContrasenia validadorNombreContrasenia = new ValidadorNombreContrasenia();

    ValidadorContrasenias.instance().agregarValidadores
        (Arrays.asList(validadorListaConstrasenias, validadorLongitud, validadorNombreContrasenia));

    Cuenta cuenta = new Cuenta("sanchezzz", "cont22441", usuarioSanchez);

    repositorioUsuario.agregar(usuarioSanchez);
    repositorioCuenta.agregar(cuenta);

    Assertions.assertEquals(1, repositorioCuenta.cantidad());

  }

  public PublicacionInteresadoAdopcion unaPublicacionDeInteresEnAdoptar(){

    List<RespuestaSobreMascota> preferenciasMascota = new ArrayList<RespuestaSobreMascota>();
    RespuestaSobreMascota respuesta =new RespuestaSobreMascota(preguntaPerro, "true");
    preferenciasMascota.add(respuesta);
    preferenciasMascota.add(new RespuestaSobreMascota(preguntaGrande, "false"));

    return new PublicacionInteresadoAdopcion(usuarioSanchez, servicioJavaMail, preferenciasMascota);
  }

  public PublicacionAdopcion unaPublicacionDeAdopcion(){

    List<RespuestaSobreMascota> respuestas = new ArrayList<RespuestaSobreMascota>();

   respuestas.add(new RespuestaSobreMascota(preguntaAltura, "true"));

    return new PublicacionAdopcion(usuarioSanchez, servicioJavaMail, paquito, respuestas);
  }

  public PublicacionMascotaPerdida unaPublicacionMascotaPerdida(){

    Rescate rescate = new Rescate(usuarioSanchez, Arrays.asList(Paths.get("cualquiera")),
        "Lo encontre yendo a trabajar, estaba asustado",
        new Ubicacion("Plaza de Mayo", -34.3333, +36.73217));

    rescate.setMascotaEncontrada(paquito);

    return new PublicacionMascotaPerdida(rescate.getRescatista(), servicioJavaMail, rescate, new ServicioHogares());
  }
}
