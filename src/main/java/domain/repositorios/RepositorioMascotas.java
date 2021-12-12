package domain.repositorios;


import domain.mascota.Mascota;
import domain.usuario.Usuario;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import java.util.List;

public class RepositorioMascotas implements WithGlobalEntityManager {


  private static final RepositorioMascotas INSTANCE = new RepositorioMascotas();

  public static RepositorioMascotas instance(){
    return INSTANCE;
  }

  private RepositorioMascotas(){

  }

  public void agregarMascota(Mascota mascota) {

    entityManager().persist(mascota);

  }

  public List<Mascota> getMascotas() {
    return entityManager()
        .createQuery("from Mascota")
        .getResultList();
  }

  public List<Mascota> getMascotasDeUnDuenio(Usuario duenio) {
    return entityManager()
        .createQuery("from Mascota where duenio_id = :idDuenio").
            setParameter("idDuenio", duenio.getId()).getResultList();
  }

  public Mascota buscarMascota(long id) {
    return entityManager().find(Mascota.class, id);
  }
}