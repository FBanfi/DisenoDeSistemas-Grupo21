package domain.repositorios;

import domain.excepciones.excepcionesRegistro.ExcepcionRegistro;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import java.util.List;

public class Repositorio<E> implements WithGlobalEntityManager {

  private final String nombreTabla;

  public Repositorio(String nombreTabla) {
    this.nombreTabla = nombreTabla;
  }

  public void agregar(E objeto) {
    if(this.contiene(objeto)){
      throw new ExcepcionRegistro("Ya fue registrado");
    }
   // EntityManagerHelper.beginTransaction();
    entityManager().persist(objeto);
   // EntityManagerHelper.commit();
  }

  public boolean contiene(E objeto) {
    return entityManager().contains(objeto);
  }

  public int cantidad() {
    return todos().size();
  }

  public List<E> todos() {
    return entityManager()
        .createQuery("from " + nombreTabla)
        .getResultList();
  }

  public E getUltimo() {
    return todos().get(this.cantidad()-1);
  }

  public E buscar(Long id) {

    List lista = entityManager()
        .createQuery("from " + nombreTabla + " where id = :id_buscado")
        .setParameter("id_buscado", id)
        .getResultList();

    if(lista.isEmpty()){
      throw new ExcepcionRegistro("No se encontro al objeto de id " + id);
    }
    else{
      return  (E) lista.get(0);
    }
  }

}
