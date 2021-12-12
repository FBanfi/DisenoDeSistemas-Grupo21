package domain.testMascota;

import domain.AbstractPersistenceTest;
import domain.excepciones.excepcionesCaracteristica.ExcepcionCaracteristica;
import domain.mascota.CaracteristicaIdeal;
import domain.mascota.Especie;
import domain.mascota.MascotaConChapita;
import domain.repositorios.RepositorioCaracteristicasIdeales;
import domain.services.serviceMail.ServicioJavaMail;
import domain.usuario.Contacto;
import domain.usuario.Usuario;
import domain.usuario.documento.Dni;
import domain.usuario.documento.Documento;
import domain.validacionCaracteristicas.ValidadorBooleano;
import domain.validacionCaracteristicas.ValidadorCantidadCaracteres;
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
import java.util.Map;

import static org.mockito.Mockito.mock;

public class TestCaracteristicas extends AbstractPersistenceTest {
  Usuario facundo = new Usuario("Juan", "Sanchez", "pedritosanchez@gmail.com","Falsa 2021", new Documento(new Dni(), "12345678"), LocalDate.of(2000, 1, 1),
      Arrays.asList(new Contacto("Juana", "Sanchez" , 121212, "juana@gmail.com")));

  ServicioJavaMail servicioJavaMail = mock(ServicioJavaMail.class);

  @BeforeEach
  public void init(){
    RepositorioCaracteristicasIdeales.instance().vaciarRepo();
  }

  @Test
  void laCaracteristicaSeCargoCorrectamente() {

  CaracteristicaIdeal alturaEnCentimetrosentimetros = new CaracteristicaIdeal
      ("altura en centimetros", true,
          new ValidadorEntreValores(30, 80));

  RepositorioCaracteristicasIdeales.instance().agregarCaracteristicaIdeal(alturaEnCentimetrosentimetros);

    Assertions.assertTrue(RepositorioCaracteristicasIdeales.
        instance().getCaracteristicasIdeales().contains(alturaEnCentimetrosentimetros));
  }

  @Test
  void noSePuedeCargarUnaCaracteristicaConCamposFaltantes() {

    Assertions.assertThrows(ExcepcionCaracteristica.class, () -> new CaracteristicaIdeal
        (null, null, null));
  }

  @Test
  void elValorParaLaCaracteristicaIngresadaNoCumpleConLasEspecificaciones() {
    CaracteristicaIdeal altura_en_centimetros = new CaracteristicaIdeal
        ("altura en centimetros", true,
            new ValidadorEntreValores(80, 30));
    Map<String, String> caracteristicasSensibles = new HashMap<String, String>();
    caracteristicasSensibles.put("altura_en_centimetros", "9");
    Assertions.assertThrows(ExcepcionCaracteristica.class, () ->altura_en_centimetros.validarCaracteristicaSensible(caracteristicasSensibles));
  }

  @Test
  void elValorParaLaCaracteristicaIngresadaEsCorrecto() {
    CaracteristicaIdeal esta_castrado = new CaracteristicaIdeal
        ("castrado", true,
            new ValidadorBooleano());

    RepositorioCaracteristicasIdeales.instance().agregarCaracteristicaIdeal(esta_castrado);
    HashMap<String, String> caracteristicas = new HashMap<String, String>();
    caracteristicas.put("castrado", "true");


    MascotaConChapita paquito = new MascotaConChapita("paco","paquito", 2, true,
        "fachero", Arrays.asList(Paths.get("cualquiera")), caracteristicas, Especie.PERRO, facundo, servicioJavaMail);

    Assertions.assertEquals(paquito.getLaCaracteristica("castrado"), "true");
  }

  @Test
  void laMascotaDebeTenerTodasLasCaracteristicasObligatorias() {
    CaracteristicaIdeal estaCastrado = new CaracteristicaIdeal
        ("castrado", true,
            new ValidadorBooleano());

    CaracteristicaIdeal largoDelPelo = new CaracteristicaIdeal
        ("largo del pelo", true,
            new ValidadorEntrePosibilidades(Arrays.asList("mediano", "corto")));

    //TODO rompe por el repo
    RepositorioCaracteristicasIdeales.instance().agregarCaracteristicaIdeal(estaCastrado);
    RepositorioCaracteristicasIdeales.instance().agregarCaracteristicaIdeal(largoDelPelo);

    HashMap<String, String> caracteristicas = new HashMap<String, String>();
    caracteristicas.put("largo del pelo", "mediano");

    //caracteristicas.put("castrado", false);

    Assertions.assertThrows(ExcepcionCaracteristica.class, () ->new MascotaConChapita
        ("paco","paquito", 2, true, "fachero",
            Arrays.asList(Paths.get("cualquiera")), caracteristicas, Especie.PERRO, facundo, servicioJavaMail));
  }

  @Test
  void noEsNecesarioIngresarUnaCaracteristicaOpcionalDeLaMascota() {
    CaracteristicaIdeal estCastrado = new CaracteristicaIdeal
        ("castrado", false,
            new ValidadorBooleano());

    CaracteristicaIdeal largoDelPelo = new CaracteristicaIdeal
        ("largo del pelo", true,
            new ValidadorEntrePosibilidades(Arrays.asList("mediano", "corto")));

    RepositorioCaracteristicasIdeales.instance().agregarCaracteristicaIdeal(estCastrado);
    RepositorioCaracteristicasIdeales.instance().agregarCaracteristicaIdeal(largoDelPelo);
    HashMap<String, String> caracteristicas = new HashMap<String, String>();
    caracteristicas.put("largo del pelo", "mediano");


    MascotaConChapita paquito = new MascotaConChapita("paco","paquito", 2, true,
        "fachero", Arrays.asList(Paths.get("cualquiera")), caracteristicas, Especie.PERRO, facundo, servicioJavaMail);


    Assertions.assertFalse(paquito.esUnaDeSusCaracteristicas("castrado"));
  }

  @Test
  void noSePuedeIngresarUnaCaracteristicaSuperandoLaCantidadDeCaracteresIndicada() {
    CaracteristicaIdeal gustosComida = new CaracteristicaIdeal
        ("gustos comida", false,
            new ValidadorCantidadCaracteres(10));

    RepositorioCaracteristicasIdeales.instance().agregarCaracteristicaIdeal(gustosComida);

    HashMap<String, String> caracteristicas = new HashMap<String, String>();
    caracteristicas.put("gustos comida", "Le gusta la carne y la comida de gato aunque sea un perro");

    Assertions.assertThrows(ExcepcionCaracteristica.class, () -> new MascotaConChapita("paco","paquito", 2, true,
        "fachero", Arrays.asList(Paths.get("cualquiera")), caracteristicas, Especie.PERRO, facundo, servicioJavaMail));

  }
}
