package domain.testUsuario;

import domain.AbstractPersistenceTest;
import domain.excepciones.exepcionesCuenta.ExcepcionContacto;
import domain.excepciones.exepcionesCuenta.ExcepcionUsuario;
import domain.mascota.CaracteristicaIdeal;
import domain.mascota.Especie;
import domain.mascota.Mascota;
import domain.mascota.MascotaConChapita;
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

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class TestUsuario extends AbstractPersistenceTest {

  ValidadorListaConstrasenias validadorListaConstrasenias =  new ValidadorListaConstrasenias();
  ValidadorLongitud validadorLongitud = new ValidadorLongitud();
  ValidadorNombreContrasenia validadorNombreContrasenia = new ValidadorNombreContrasenia();
  //ValidadorContrasenias validadorContrasenias = new ValidadorContrasenias(Arrays.asList(validadorListaConstrasenias, validadorLongitud, validadorNombreContrasenia));

  {
    ValidadorContrasenias.instance().agregarValidadores(Arrays.asList(validadorListaConstrasenias, validadorLongitud, validadorNombreContrasenia));
  }

  ValidadorEntrePosibilidades validadorEntrePosibilidadesPelo = new ValidadorEntrePosibilidades(Arrays.asList("largo", "corto"));
  ValidadorEntreValores validadorEntreValores = new ValidadorEntreValores(3,0);

  CaracteristicaIdeal temperamento = new CaracteristicaIdeal("temperamento", false, validadorEntreValores);
  CaracteristicaIdeal pelo = new CaracteristicaIdeal("", false, validadorEntrePosibilidadesPelo);

  HashMap<String, String> caracteristicas = new HashMap<String, String>(2);

  ServicioJavaMail servicioJavaMail = mock(ServicioJavaMail.class);

  Usuario usuarioSanchez = new Usuario("Juan", "Sanchez", "pedritosanchez@gmail.com","Falsa 2021", new Documento(new Dni(), "12345678"), LocalDate.of(2000, 1, 1),
      Arrays.asList(new Contacto("Juana", "Sanchez" , 121212, "juana@gmail.com")));

  Mascota mascotaManolo = new MascotaConChapita("Manolo", "Manito", 7, true, "Manolo es un perro muy bueno, nunca muerde", Arrays.asList(Paths.get("cualquiera")), caracteristicas, Especie.PERRO, usuarioSanchez, servicioJavaMail);

  Cuenta cuentaSanchez = new Cuenta("bysanchezrex777OmegaLOL", "paquitoelmejor", usuarioSanchez);


  @BeforeEach
  public void init(){
    caracteristicas.put("temperamento", "2");
    caracteristicas.put("pelo", "corto");
  }

  @Test
  public void usuarioNoPuedeTenerAtributosNulos() {
    ExcepcionUsuario e = Assertions.assertThrows(ExcepcionUsuario.class, () -> new Usuario(null, "Sanchez", "pedritosanchez@gmail.com", "Falsa 2021", new Documento(new Dni(), "12345678"), LocalDate.of(2000, 1, 1), Arrays.asList(new Contacto("Juana", "Sanchez" , 121212, "juana@gmail.com"))));
    assertEquals("Debe especificar el nombre del usuario", e.getMessage());
  }

  @Test
  public void datoContactoNoPuedeTenerTelefonoNegativo() {

    ExcepcionContacto e = Assertions.assertThrows(ExcepcionContacto.class, () -> new Contacto("Pablo", "Escobar", -1231, "pablitoelmasbonito@pablo.com.ar"));
    assertEquals("El telefono ingresado no debe ser negativo", e.getMessage());
  }

  @Test
  public void datoContactoNoPuedeTenerAtributoNegativo() {

    ExcepcionContacto e = Assertions.assertThrows(ExcepcionContacto.class, () -> new Contacto(null, "Escobar", 1231, "pablitoelmasbonito@pablo.com.ar"));
    assertEquals("Debe ingresar el nombre del contacto", e.getMessage());
  }

}
