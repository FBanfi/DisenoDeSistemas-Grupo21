package model;

import domain.repositorios.RepositorioCuentas;
import domain.usuario.Cuenta;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import spark.Request;
import spark.Response;

import java.util.Map;

public class Encabezado implements WithGlobalEntityManager, TransactionalOps {

  RepositorioCuentas repositorioCuentas = RepositorioCuentas.instance();

  public Map<String, Object> prepararEncabezado(Map<String, Object> modelo, Request request, Response response){
    modelo.put("sesionIniciada", request.session().attribute("user_id") != null);
    if(seLogueo(request)){
      Cuenta cuenta = repositorioCuentas.buscarPorId(request.session().attribute("user_id"));
      modelo.put("nombreCuenta", cuenta.getNombreUsuario());
    }
    return modelo;
  }

  public Boolean seLogueo(Request request){
    return request.session().attribute("user_id") != null;
  }
}
