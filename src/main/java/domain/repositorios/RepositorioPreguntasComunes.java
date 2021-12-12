package domain.repositorios;

import domain.asociacion.PreguntaSobreMascota;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import java.util.ArrayList;
import java.util.List;

public class RepositorioPreguntasComunes implements WithGlobalEntityManager {

  private final List<PreguntaSobreMascota> preguntas;

  private static final RepositorioPreguntasComunes INSTANCE = new RepositorioPreguntasComunes();

  public static RepositorioPreguntasComunes instance(){
    return INSTANCE;
  }

  public RepositorioPreguntasComunes() {
    this.preguntas = new ArrayList<PreguntaSobreMascota>();
  }

  public List<PreguntaSobreMascota> getPreguntas() {
    return entityManager()
        .createQuery("from PreguntaSobreMascota")
        .getResultList();
  }

  public void agregarPregunta(PreguntaSobreMascota pregunta){

    entityManager().persist(pregunta);

  }

  public void eliminarPregunta(PreguntaSobreMascota pregunta){
    entityManager().remove(pregunta);
  }
}
