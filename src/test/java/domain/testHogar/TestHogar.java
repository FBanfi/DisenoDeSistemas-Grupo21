package domain.testHogar;


import domain.AbstractPersistenceTest;
import domain.asociacion.Asociacion;
import domain.mascota.Especie;
import domain.mascota.MascotaSinChapita;
import domain.mascota.Rescate;
import domain.publicaciones.PublicacionMascotaPerdida;
import domain.repositorios.RepositorioAsociaciones;
import domain.services.serviceHogares.*;
import domain.services.serviceMail.ServicioJavaMail;
import domain.usuario.Contacto;
import domain.usuario.Usuario;
import domain.usuario.documento.Dni;
import domain.usuario.documento.Documento;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestHogar extends AbstractPersistenceTest {

  HogarTransitorio hogarLean = new HogarTransitorio(
      "1",
      "deptoDeLean",
      new Ubicacion("avenida siempre viva", 10.0, 10.0),
      "124",
      new Admisiones(true, false),
      2,2,
      false,
      Arrays.asList("tranquilo", "pequenio"));

  HogarTransitorio hogarNico = new HogarTransitorio(
      "2",
      "deptoDeNico",
      new Ubicacion("palermo cheto", 5.0, 5.5),
      "111",
      new Admisiones(true, true),
      10,8,
      false,
      Arrays.asList("amigable", "grande"));

  HogarTransitorio hogarFran = new HogarTransitorio(
      "3",
      "casaFran",
      new Ubicacion("las lomitas", 22.0, 52.5),
      "54321",
      new Admisiones(true, true),
      2,1,
      true,
      Arrays.asList("dormilon"));

  List<HogarTransitorio> hogares = Arrays.asList(hogarFran, hogarLean, hogarNico);

  HashMap<String, String> caracteristicas = new HashMap<String, String>();

  ServicioHogares servicioHogares;
  ServicioJavaMail servicioJavaMail = mock(ServicioJavaMail.class);

  Usuario usuario = new Usuario("Pedro", "Sanchez", "pedritosanchez@gmail.com" ,"Falsa 2021", new Documento(new Dni(), "12345678"), LocalDate.of(2000, 1, 1) ,
      Arrays.asList(new Contacto("Juana", "Sanchez" , 121212, "juana@gmail.com")));

  Rescate nuevoRescate = new Rescate(usuario, Arrays.asList(Paths.get("cualquiera")), "Lo encontre yendo a trabajar, estaba asustado", new Ubicacion("Plaza de Mayo", 10.3333, 13.73217));

    @BeforeEach
    public void init(){
        servicioHogares = mock(ServicioHogares.class);
      }



    @Test
    void elHogarDeLeanAceptaMascotasPequeniasYTranquilas() {
      caracteristicas.put("actitud", "tranquilo");
      caracteristicas.put("tamanio", "pequenio");

      Assertions.assertTrue(hogarLean.aceptaCaracteristicas(caracteristicas));
    }

  @Test
  void elHogarDeLeanNoAceptaMascotasGrande() {
    caracteristicas.put("actitud", "tranquilo");
    caracteristicas.put("tamanio", "grande");

    Assertions.assertFalse(hogarLean.aceptaCaracteristicas(caracteristicas));
  }

  @Test
  void elHogarDeLeanAceptaPerrosPeroNoAceptaGatos() {
      Assertions.assertTrue(hogarLean.getAdmisiones().aceptaEspecie(Especie.PERRO));
      Assertions.assertFalse(hogarLean.getAdmisiones().aceptaEspecie(Especie.GATO));
  }

  @Test
  void elHogarDeNicoSoloAceptaMascotasPequeniasPorqueNoTienePatio() {
    Assertions.assertTrue(hogarNico.aceptaTamanio("pequenio"));
    Assertions.assertFalse(hogarNico.aceptaTamanio("mediano"));
  }

  @Test
  void elHogarDeFranNoTieneProblemaConElTamanioDeLasMascotasYaQueTienePatio() {
    Assertions.assertTrue(hogarFran.aceptaTamanio("grande"));
    Assertions.assertTrue(hogarFran.aceptaTamanio("pequenio"));
    Assertions.assertTrue(hogarFran.aceptaTamanio("mediano"));
  }

  @Test
  void hogarDeFranTieneLugarDisponible() {
    Assertions.assertTrue(hogarFran.tieneLugaresDisponibles());
  }

  @Test
  void hogarDeNicoNoTieneLugarDisponible() {
    Assertions.assertTrue(hogarNico.tieneLugaresDisponibles());
  }

  @Test
  void elHogarDeLeanEstaDentroDelRadio() {
      Assertions.assertTrue(hogarLean.estaDentroDelRadio(new Ubicacion("centro", 10.0, 5.0), 6 ));
  }

  @Test
  void elHogarDeFranNoEstaDentroDelRadio() {
    Assertions.assertFalse(hogarFran.estaDentroDelRadio(new Ubicacion("centro", 10.0, 5.0), 6 ));
  }

  @Test
  void hogaresAceptanAMascota() {
    caracteristicas.put("actitud", "tranquilo");
    caracteristicas.put("tamanio", "pequenio");
    RepositorioAsociaciones.instance().vaciarRepo();
    Asociacion asociacion = new Asociacion(new Ubicacion("ddd", -32.0, 34.0));
    RepositorioAsociaciones.instance().agregarAsociacion(asociacion);
    MascotaSinChapita paquito = new MascotaSinChapita(true , caracteristicas,Especie.PERRO, servicioJavaMail);

    paquito.serRescatado(nuevoRescate, servicioHogares);

    ListadoHogares listadoHogares = new ListadoHogares(3,1,hogares);
    when(servicioHogares.listadoHogares()).thenReturn(listadoHogares);

    PublicacionMascotaPerdida publicacion = (PublicacionMascotaPerdida) asociacion.getPublicaciones().get(0);

    Assertions.assertEquals(Arrays.asList(hogarLean), publicacion.buscarPosiblesHogares(6));
  }

}
