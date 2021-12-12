package domain.testUsuario;

import domain.AbstractPersistenceTest;
import domain.excepciones.exepcionesCuenta.ExcepcionContrasenia;
import domain.usuario.Contacto;
import domain.usuario.Cuenta;
import domain.usuario.Usuario;
import domain.usuario.documento.Dni;
import domain.usuario.documento.Documento;
import domain.validacionContrasenia.ValidadorContrasenias;
import domain.validacionContrasenia.ValidadorListaConstrasenias;
import domain.validacionContrasenia.ValidadorLongitud;
import domain.validacionContrasenia.ValidadorNombreContrasenia;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestSeguridad extends AbstractPersistenceTest {

  Usuario usuarioSanchez = new Usuario("Juan", "Sanchez", "pedritosanchez@gmail.com","Falsa 2021", new Documento(new Dni(), "12345678"), LocalDate.of(2000, 1, 1),
      Arrays.asList(new Contacto("Juana", "Sanchez" , 121212, "juana@gmail.com")));

  ValidadorListaConstrasenias validadorListaConstrasenias =  new ValidadorListaConstrasenias();
  ValidadorLongitud validadorLongitud = new ValidadorLongitud();
  ValidadorNombreContrasenia validadorNombreContrasenia = new ValidadorNombreContrasenia();
  //ValidadorContrasenias validadorContrasenias = new ValidadorContrasenias(Arrays.asList(validadorListaConstrasenias, validadorLongitud, validadorNombreContrasenia));

  {
    ValidadorContrasenias.instance().agregarValidadores(Arrays.asList(validadorListaConstrasenias, validadorLongitud, validadorNombreContrasenia));
  }


  @Test
  public void longitudDeContraseniaNoDebeSerMenorAOchoCaracteres() {

    ExcepcionContrasenia e = Assertions.assertThrows(ExcepcionContrasenia.class, () -> new Cuenta("juan","dfd", usuarioSanchez));
    assertEquals("La contrasenia debe tener al menos 8 caracteres", e.getMessage());
  }

  @Test
  public void contraseniaNoPuedeSerIgualANombreDeUsuario() {

    ExcepcionContrasenia e = Assertions.assertThrows(ExcepcionContrasenia.class, () -> new Cuenta("bysanchezrex777OmegaLOL","bysanchezrex777OmegaLOL", usuarioSanchez));
    assertEquals("La contrasenia no debe ser igual al nombre de usuario", e.getMessage());
  }

  @Test
  public void cuentaTieneContraseniaInsegura() {

    ExcepcionContrasenia e = Assertions.assertThrows(ExcepcionContrasenia.class, () -> new Cuenta("bysanchezrex777OmegaLOL","1234567890", usuarioSanchez));
    assertEquals("La contrasenia no es segura", e.getMessage());
  }

  @Test
  public void cuentaTieneContraseniaApta() throws IOException {

    new Cuenta("bysanchezrex777OmegaLOL","paquitoelmejor", usuarioSanchez);
  }

}
