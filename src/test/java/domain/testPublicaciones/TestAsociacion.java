package domain.testPublicaciones;

import domain.AbstractPersistenceTest;
import domain.asociacion.Asociacion;
import domain.asociacion.PreguntaSobreMascota;
import domain.repositorios.Repositorio;
import domain.services.serviceHogares.Ubicacion;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestAsociacion extends AbstractPersistenceTest {


  Asociacion asociacion1 = new Asociacion(new Ubicacion("utn frba medrano", 140.0, 222.0));
  Asociacion asociacion2 = new Asociacion(new Ubicacion("utn frba campus", 50.0, 77.0));

  PreguntaSobreMascota pregunta2 = new PreguntaSobreMascota("la mascota es juguetona?",
      "prefiere una mascota juguetona?", "esJuguetona");

  PreguntaSobreMascota pregunta1 = new PreguntaSobreMascota("la mascota muerde?",
      "esta de acuerdo con que la mascota ocasionalmente lo muerda?", "muerde");


  @Test
  void unaAsociacionPuedeDefinirSusPropiasPreguntas() {
    asociacion1.agregarPregunta(pregunta1);
    Assertions.assertTrue(asociacion1.tieneEstaPregunta(pregunta1));
    Assertions.assertFalse(asociacion2.tieneEstaPregunta(pregunta1));
  }

  @Test
  void unaAsociacionPuedeEliminarAlgunaDeSusPreguntas() {
    asociacion1.eliminarPregunta(pregunta1);
    Assertions.assertFalse(asociacion1.tieneEstaPregunta(pregunta1));
  }

  @Test
  void hayPreguntasQueSeDefinenParaTodasLasAsociaciones() {
    Repositorio repoPreguntasComunes = new Repositorio("PreguntaComunSobreMascota");
    repoPreguntasComunes.agregar(pregunta2);

    //RepositorioPreguntasComunes.instance().agregarPregunta(pregunta2);

    Assertions.assertTrue(asociacion1.tieneEstaPregunta(pregunta2));
    Assertions.assertTrue(asociacion2.tieneEstaPregunta(pregunta2));
  }
}
