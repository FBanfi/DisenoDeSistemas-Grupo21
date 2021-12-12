package model;

import domain.excepciones.excepcionesCaracteristica.ExcepcionCaracteristica;
import domain.mascota.CaracteristicaIdeal;
import domain.repositorios.RepositorioCaracteristicasIdeales;
import domain.usuario.Contacto;
import domain.usuario.Cuenta;
import domain.usuario.Usuario;
import domain.usuario.documento.Dni;
import domain.usuario.documento.Documento;
import domain.validacionCaracteristicas.*;
import domain.validacionContrasenia.ValidadorContrasenias;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.time.LocalDate;
import java.util.*;


public class CaracteristicasController extends Encabezado{

  RepositorioCaracteristicasIdeales repositorioCaracteristicasIdeales = RepositorioCaracteristicasIdeales.instance();

  public ModelAndView getTablaCaracteristicas(Request request, Response response) {
    if (!seLogueo(request)) {
      response.redirect("/login?origin=/caracteristicas");
      return null;
    }

    Map<String, Object> modelo = new HashMap<>();
    List<CaracteristicaIdeal> caracteristicas = RepositorioCaracteristicasIdeales.instance().getCaracteristicasIdeales();
    modelo.put("caracteristicas", caracteristicas);
    prepararEncabezado(modelo, request, response);

    return new ModelAndView(modelo, "caracteristicas.html.hbs");
  }

  public ModelAndView getFormularioCaracteristica(Request request, Response response) {

    if (!seLogueo(request)) {
      response.redirect("/login?origin=/caracteristicas/nueva");
      return null;
    }
    Map<String, Object> modelo = new HashMap<>();
    prepararEncabezado(modelo, request, response);
    modelo.put("errorCaracteristica", false);

    return new ModelAndView(modelo, "formulario-caracteristicas.html.hbs");
  }

  public ModelAndView crearCaracteristica(Request request, Response response) {
    Map<String, Object> modelo = new HashMap<>();
    prepararEncabezado(modelo, request, response);

    try {
      String nombre = request.queryParams("nombre_caracteristica");
      Boolean obligatoriedad = Boolean.parseBoolean(request.queryParams("obligatoriedad"));
      String criterio = request.queryParams("validador");
      CriterioDeValidacion criterioDeValidacion = null;
      String posibilidades = null;

      if (criterio.equals("BOOLEANO")) {
        criterioDeValidacion = new ValidadorBooleano();
      }
      if (criterio.equals("POSIBILIDADES")) {
        posibilidades = request.queryParams("posibilidades");

        ArrayList<String> listaPosibilidades = new ArrayList<>(Arrays.asList(posibilidades.split(",")));

        criterioDeValidacion = new ValidadorEntrePosibilidades(listaPosibilidades);
      }
      if (criterio.equals("CARACTERES")) {
        Integer cantidadCaracteres = Integer.parseInt(request.queryParams("cantidadCaracteres"));
        criterioDeValidacion = new ValidadorCantidadCaracteres(cantidadCaracteres);

      }
      if (criterio.equals("VALORES")) {
        Integer valorMin = Integer.parseInt(request.queryParams("valorMin"));
        Integer valorMax = Integer.parseInt(request.queryParams("valorMax"));

        criterioDeValidacion = new ValidadorEntreValores(valorMax, valorMin);
      }

      CaracteristicaIdeal caracteristicaIdeal = new CaracteristicaIdeal(nombre, obligatoriedad, criterioDeValidacion);

      withTransaction(() -> {
        repositorioCaracteristicasIdeales.agregarCaracteristicaIdeal(caracteristicaIdeal);
      });

      response.redirect("/caracteristicas");
      return null;
    } catch (ExcepcionCaracteristica e) {
      modelo.put("errorCaracteristica", true);
      return new ModelAndView(modelo, "formulario-caracteristicas.html.hbs");
    }
  }

  public Void eliminarCaracteristica(Request request, Response response) {

    int id = Integer.parseInt( request.params("id"));
    withTransaction(()->{
             CaracteristicaIdeal caracteristicaIdeal = RepositorioCaracteristicasIdeales.instance().buscarPorId(id);
             RepositorioCaracteristicasIdeales.instance().eliminarCaracteristica(caracteristicaIdeal);
           });

    response.redirect("/caracteristicas");

    return null;
  }

}