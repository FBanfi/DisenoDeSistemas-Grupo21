package domain.validacionContrasenia;

import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

import java.util.List;

public class ValidadorContrasenias implements WithGlobalEntityManager, TransactionalOps {

  //private List<Validaciones> listaValidadores; // = Arrays.asList(new ValidadorLongitud(), new ValidadorNombreContrasenia(), new ValidadorListaConstrasenias());

  private static final ValidadorContrasenias INSTANCE = new ValidadorContrasenias();

  public static ValidadorContrasenias instance(){
    return INSTANCE;
  }


  public void agregarValidadores(List<Validaciones> listaValidadores){

    //this.listaValidadores = listaValidadores;
    listaValidadores.forEach(entityManager()::persist);
  }

  public List<Validaciones> getValidaciones(){
    return entityManager()
        .createQuery("from Validaciones")
        .getResultList();
  }

  public void validar(String contrasenia, String nombreUsuario) {

    this.getValidaciones().forEach(validador -> validador.validarAtributo(contrasenia, nombreUsuario));
  }

}
