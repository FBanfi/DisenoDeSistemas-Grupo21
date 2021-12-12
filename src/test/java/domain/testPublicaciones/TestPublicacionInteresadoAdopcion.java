package domain.testPublicaciones;

import domain.AbstractPersistenceTest;
import domain.asociacion.Asociacion;
import domain.asociacion.PreguntaSobreMascota;
import domain.mascota.Especie;
import domain.mascota.MascotaConChapita;
import domain.publicaciones.PublicacionAdopcion;
import domain.publicaciones.PublicacionInteresadoAdopcion;
import domain.publicaciones.RespuestaSobreMascota;
import domain.services.serviceHogares.Ubicacion;
import domain.services.serviceMail.ServicioJavaMail;
import domain.usuario.Contacto;
import domain.usuario.Usuario;
import domain.usuario.documento.Dni;
import domain.usuario.documento.Documento;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TestPublicacionInteresadoAdopcion extends AbstractPersistenceTest {

  ServicioJavaMail servicioJavaMail = mock(ServicioJavaMail.class);
  //ServicioJavaMail servicioJavaMail = new ServicioJavaMail();

  Asociacion asociacion1 = new Asociacion(new Ubicacion("utn frba medrano", 140.0, 222.0));

  Usuario usuario = new Usuario("Juan", "Sanchez", "juana@gmail.com",
      "Falsa 2021", new Documento(new Dni(), "12345678"), LocalDate.of(2000, 1, 1),
      Arrays.asList(new Contacto("Juana", "Sanchez" , 121212, "juana@gmail.com")));

  PreguntaSobreMascota preguntaPerro = new PreguntaSobreMascota("La mascota a adoptar es un perro?", "Desea adoptar un perro?");
  PreguntaSobreMascota preguntaGrande = new PreguntaSobreMascota("La mascota a adoptar es grande?", "Desea adoptar una mascota grande?");



  @Test
  void elDuenioDeLaPublicacionRecibioElLinkParaDarDeBajaLaPublicacion() {
    PublicacionInteresadoAdopcion publicacionInteresadoAdopcion = this.unaPublicacionDeInteresEnAdoptar();
    publicacionInteresadoAdopcion.enviarLinkDeBaja();
    verify(servicioJavaMail).enviarLinkDeBajaPublicacionInteresado(any(),any());
  }

  @Test
  void elDuenioDeLaPublicacionRecibeLasRecomendacionesSegunSusPreferenciasYComodidades() {
    PublicacionInteresadoAdopcion publicacionInteresadoAdopcion = this.unaPublicacionDeInteresEnAdoptar();
    asociacion1.registrarPublicacionInteresadoAdopcion(publicacionInteresadoAdopcion);
    publicacionInteresadoAdopcion.hacerVisible();

    PublicacionAdopcion publicacionAdopcion = this.adopcionPerroChico();
    asociacion1.registrarPublicacionAdopcion(publicacionAdopcion);
    publicacionAdopcion.hacerVisible();

    asociacion1.recomendacionSemanal();

    List<PublicacionAdopcion> recomendacion = Arrays.asList(publicacionAdopcion);

    verify(servicioJavaMail).enviarNotificacionRecomendacion(any(), eq(recomendacion));
  }

  @Test
  void elUsuarioQuiereAdoptarUnPerroYSoloAceptaraPerrosQueNoSeanGrandes() {
    PublicacionAdopcion adopcionApta = adopcionPerroChico();
    PublicacionAdopcion adopcionNoApta = adopcionMascotaGrande();
    PublicacionInteresadoAdopcion interesadoAdopcion = unaPublicacionDeInteresEnAdoptar();

    Assertions.assertTrue(interesadoAdopcion.esApto2(adopcionApta));
    Assertions.assertFalse(interesadoAdopcion.esApto2(adopcionNoApta));
  }

  public PublicacionInteresadoAdopcion unaPublicacionDeInteresEnAdoptar(){

    List<RespuestaSobreMascota> preferenciasMascota = new ArrayList<RespuestaSobreMascota>();
    preferenciasMascota.add(new RespuestaSobreMascota(preguntaPerro, "true"));
    preferenciasMascota.add(new RespuestaSobreMascota(preguntaGrande, "false"));

    return new PublicacionInteresadoAdopcion(usuario, servicioJavaMail, preferenciasMascota);
  }

  public PublicacionAdopcion adopcionPerroChico(){

    List<RespuestaSobreMascota> respuestasMascota = new ArrayList<RespuestaSobreMascota>();
    respuestasMascota.add(new RespuestaSobreMascota(preguntaPerro, "true"));
    respuestasMascota.add(new RespuestaSobreMascota(preguntaGrande, "false"));

    HashMap<String, String> caracteristicas = new HashMap<String, String>();
    caracteristicas.put("altura en centimetros", "35");
    caracteristicas.put("castrado", "true");

    MascotaConChapita paquito = new MascotaConChapita("paco","paquito", 2, true, "fachero", Arrays.asList(Paths.get("cualquiera")),
        caracteristicas, Especie.PERRO, usuario, servicioJavaMail);

    return new PublicacionAdopcion(usuario, servicioJavaMail, paquito, respuestasMascota);
  }

  public PublicacionAdopcion adopcionMascotaGrande(){

    HashMap<String, String> respuestas = new HashMap<String, String>();
    respuestas.put("grande", "true");
    List<RespuestaSobreMascota> respuestasMascota = new ArrayList<RespuestaSobreMascota>();
    respuestasMascota.add(new RespuestaSobreMascota(preguntaGrande, "true"));

    HashMap<String, String> caracteristicas = new HashMap<String, String>();
    caracteristicas.put("temperamento", "2");
    caracteristicas.put("pelo", "corto");

    MascotaConChapita paquito = new MascotaConChapita("paco","paquito", 2, true, "fachero", Arrays.asList(Paths.get("cualquiera")),
        caracteristicas, Especie.PERRO, usuario, servicioJavaMail);

    return new PublicacionAdopcion(usuario, servicioJavaMail, paquito,  respuestasMascota);
  }
}
