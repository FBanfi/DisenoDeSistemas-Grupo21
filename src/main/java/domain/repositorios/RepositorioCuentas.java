package domain.repositorios;


import domain.usuario.Cuenta;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import java.util.List;

public class RepositorioCuentas implements WithGlobalEntityManager {


  private static final RepositorioCuentas INSTANCE = new RepositorioCuentas();

  public static RepositorioCuentas instance(){
    return INSTANCE;
  }

  private RepositorioCuentas(){

  }

  public void agregar(Cuenta cuenta) {

    entityManager().persist(cuenta);

  }

  public List<Cuenta> getCuentas() {
    return entityManager()
        .createQuery("from Cuenta")
        .getResultList();
  }

  public Cuenta buscarPorUsuarioYContrasenia(String username, String password) {

    return getCuentas().stream()
        .filter(u -> u.getContrasenia().equals(password) && u.getNombreUsuario().equals(username)).findFirst().get();
  }

  public Cuenta buscarPorId(Integer id) {

    return getCuentas().stream().filter(u -> u.getId() == id).findFirst().get();
  }
}