package domain.testRescate;

import domain.AbstractPersistenceTest;
import domain.mascota.*;
import domain.publicaciones.PublicacionMascotaPerdida;
import domain.services.serviceHogares.ServicioHogares;
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
import java.util.Arrays;
import java.util.HashMap;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class TestRescate extends AbstractPersistenceTest {

  Usuario usuario = new Usuario("Pedro", "Sanchez", "pedritosanchez@gmail.com" ,"Falsa 2021", new Documento(new Dni(), "12345678"), LocalDate.of(2000, 1, 1) ,
      Arrays.asList(new Contacto("Juana", "Sanchez" , 121212, "juana@gmail.com")));

  HashMap<String, String> caracteristicas = new HashMap<String, String>(2);

  ServicioJavaMail servicioJavaMail = mock(ServicioJavaMail.class);

  MascotaSinChapita pepe = new MascotaSinChapita(true, caracteristicas, Especie.PERRO, servicioJavaMail);

  Rescate nuevoRescate = new Rescate(usuario, Arrays.asList(Paths.get("cualquiera")), "Lo encontre yendo a trabajar, estaba asustado", new Ubicacion("Plaza de Mayo", -34.3333, +36.73217));


  @BeforeEach
  public void init(){
    caracteristicas.put("temperamento", "2");
    caracteristicas.put("pelo", "corto");
  }

  @Test
  void unRescateDeHoyEsUnRescateReciente() {
    Assertions.assertTrue(nuevoRescate.esFechaReciente());
  }

  @Test
  public void duenioEncuentraASuMascotaSinChapita() {
    Usuario duenio = mock(Usuario.class);
    ServicioJavaMail servicioJavaMail = mock(ServicioJavaMail.class);

    when(duenio.getNombre()).thenReturn("javito");
    when(duenio.getEmail()).thenReturn("javito@gmail.com");
    PublicacionMascotaPerdida publicacionMascotaPerdida = new PublicacionMascotaPerdida(usuario, servicioJavaMail, nuevoRescate, new ServicioHogares());
    publicacionMascotaPerdida.hacerVisible();
    publicacionMascotaPerdida.alguienEstaInteresado(duenio);

//    doNothing().when(emailService.enviarEmail(anyString(), anyString()));


    verify(servicioJavaMail, times(1)).enviarNotificacionMascotaSinChapitaEncontrada(any(),any());
  }

}
