package domain.repositorios;

import domain.excepciones.excepcionesCaracteristica.ExcepcionCaracteristica;
import domain.mascota.CaracteristicaIdeal;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RepositorioCaracteristicasIdeales implements WithGlobalEntityManager {
  //List<CaracteristicaIdeal> caracteristicasIdeales;

  private static final RepositorioCaracteristicasIdeales INSTANCE = new RepositorioCaracteristicasIdeales();

  public static RepositorioCaracteristicasIdeales instance(){
    return INSTANCE;
  }

  private RepositorioCaracteristicasIdeales(){
    //caracteristicasIdeales = new ArrayList<>();
  }


  public void agregarCaracteristicaIdeal(CaracteristicaIdeal caracteristicaIdeal) {

    validarCaracteristicaIdeal(caracteristicaIdeal);
    entityManager().persist(caracteristicaIdeal);

  }

  private void validarCaracteristicaIdeal(CaracteristicaIdeal caracteristicaIdeal) {

    if(entityManager().contains(caracteristicaIdeal)) {
      throw new ExcepcionCaracteristica("La caracteristica ya fue registrada");
    }
  }


  public List<CaracteristicaIdeal> getCaracteristicasIdeales() {
    return entityManager()
        .createQuery("from CaracteristicaIdeal")
        .getResultList();
  }

  public void vaciarRepo() {

    //caracteristicasIdeales = new ArrayList<CaracteristicaIdeal>();
  }

  public void validar(Map<String, String>  caracteristicas){
    this.getCaracteristicasIdeales().forEach(caracteristicaIdeal ->
        caracteristicaIdeal.validarCaracteristicaSensible(caracteristicas));
  }

  public void eliminarCaracteristica(CaracteristicaIdeal caracteristicaIdeal){
        entityManager().remove(caracteristicaIdeal);
  }

  public CaracteristicaIdeal buscarPorId(Integer id) {

    return getCaracteristicasIdeales().stream().
        filter(u -> u.getId() == id).findFirst().get();
  }

}
