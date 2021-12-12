package domain.repositorios;

import domain.asociacion.Asociacion;
import domain.mascota.CaracteristicaIdeal;
import domain.services.serviceHogares.Ubicacion;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

import java.util.ArrayList;
import java.util.List;

public class RepositorioAsociaciones implements WithGlobalEntityManager, TransactionalOps {

  private static final RepositorioAsociaciones INSTANCE = new RepositorioAsociaciones();

  public static RepositorioAsociaciones instance(){
    return INSTANCE;
  }

  public void agregarAsociacion(Asociacion asociacion) {

    entityManager().persist(asociacion);

  }


  public List<Asociacion> getAsociaciones() {
    return entityManager()
        .createQuery("from Asociacion")
        .getResultList();
  }


  public Asociacion getAsociacionMasCercanaA(Ubicacion lugar) {
    List<Asociacion> asociacionesPersistidas = getAsociaciones();
    Asociacion asociacionMasCerca = asociacionesPersistidas.get(0);
    for (Asociacion asociacion:asociacionesPersistidas){
      if (asociacionMasCerca.distanciaA(lugar) > asociacion.distanciaA(lugar)) {
        asociacionMasCerca = asociacion;
      }
    }
    return asociacionMasCerca;
  }

  public void enviarRecomendacionesSemanales(){
    withTransaction(()->{
      this.getAsociaciones().forEach(Asociacion::recomendacionSemanal);
    });
  }

  public void vaciarRepo() {

    //asociaciones = new ArrayList<Asociacion>();
  }
}
