package model;

import domain.repositorios.RepositorioCuentas;
import domain.usuario.Cuenta;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class InicioController extends Encabezado {

  RepositorioCuentas repositorioCuentas = RepositorioCuentas.instance();

  public ModelAndView getHome(Request request, Response response) {
    Map<String, Object> modelo = new HashMap<>();
    prepararEncabezado(modelo, request, response);
    Boolean esAdministrador = false;
    if(seLogueo(request)){
      esAdministrador = repositorioCuentas.buscarPorId(request.session().attribute("user_id")).getEsAdministrador();
    }
    modelo.put("esAdministrador", esAdministrador);
    return new ModelAndView(modelo, "home.html.hbs");
  }

}
