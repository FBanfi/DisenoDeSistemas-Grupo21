package model;

import domain.excepciones.excepcionesCaracteristica.ExcepcionCaracteristica;
import domain.excepciones.expecionesMascota.ExcepcionMascota;
import domain.mascota.*;
import domain.repositorios.Repositorio;
import domain.repositorios.RepositorioCaracteristicasIdeales;
import domain.repositorios.RepositorioCuentas;
import domain.repositorios.RepositorioMascotas;
import domain.services.serviceHogares.Ubicacion;
import domain.services.serviceMail.ServicioJavaMail;
import domain.services.serviceMail.ServicioNotificacion;
import domain.usuario.Contacto;
import domain.usuario.Cuenta;
import domain.usuario.Usuario;
import domain.usuario.documento.Dni;
import domain.usuario.documento.Documento;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateEngine;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;


public class MascotaController extends Encabezado{

  RepositorioMascotas repositorioMascotas = RepositorioMascotas.instance();
  RepositorioCuentas repositorioCuentas = RepositorioCuentas.instance();

  public ModelAndView getFormularioMascota(Request request, Response response) {

    if (!seLogueo(request)) {
      response.redirect("/login?origin=/mascotas/nueva");
      return null;
    }

    Map<String, Object> modelo = new HashMap<>();
    List<CaracteristicaIdeal> caracteristicas = RepositorioCaracteristicasIdeales.instance().getCaracteristicasIdeales();
    modelo.put("caracteristicas", caracteristicas);
    modelo.put("errorMascota", false);
    modelo.put("errorCaracteristica", false);
    prepararEncabezado(modelo, request, response);

    return new ModelAndView(modelo, "formulario-mascota.html.hbs");
  }

  public ModelAndView registrarMascota(Request request, Response response) {

    Cuenta cuenta = repositorioCuentas.buscarPorId(request.session().attribute("user_id"));
    Map<String, Object> modelo = new HashMap<>();
    List<CaracteristicaIdeal> caracteristicas = RepositorioCaracteristicasIdeales.instance().getCaracteristicasIdeales();
    modelo.put("caracteristicas", caracteristicas);
    prepararEncabezado(modelo, request, response);

    try{
      MascotaConChapita mascota = armarMascotaConChapita(request, cuenta.getUsuario());
      withTransaction(() -> { repositorioMascotas.agregarMascota(mascota); });
      response.redirect("/home");
      return null;

    } catch (ExcepcionMascota e){
      System.out.println(e.getMessage());
      modelo.put("errorMascota", true);
      return new ModelAndView(modelo, "formulario-mascota.html.hbs");

    } catch (ExcepcionCaracteristica e){
      System.out.println(e.getMessage());
      modelo.put("errorCaracteristica", true);
      return new ModelAndView(modelo, "formulario-mascota.html.hbs");

    } catch (NumberFormatException n){
      modelo.put("errorMascota", true);
      return new ModelAndView(modelo, "formulario-mascota.html.hbs");
    }
  }

  public ModelAndView getMascotas(Request request, Response response) {
    if (!seLogueo(request)) {
      response.redirect("/login?origin=/mascotas");
      return null;
    }

    Cuenta cuenta = repositorioCuentas.buscarPorId(request.session().attribute("user_id"));
    Map<String, Object> modelo = new HashMap<>();
    List<Mascota> mascotas = RepositorioMascotas.instance().getMascotasDeUnDuenio(cuenta.getUsuario());
    modelo.put("mascotas", mascotas);
    prepararEncabezado(modelo, request, response);

    return new ModelAndView(modelo, "mascotas.html.hbs");
  }


  // METODOS AUXILIARES


  private MascotaConChapita armarMascotaConChapita(Request request, Usuario duenio){
    String nombreMascota = request.queryParams("nombre");
    String apodoMascota= request.queryParams("apodo");
    Integer edadMascota = Integer.parseInt(request.queryParams("edad"));
    System.out.println(request.queryParams("sexo"));
    Boolean sexo = Boolean.parseBoolean(request.queryParams("sexo"));
    String descripcion = request.queryParams("descripcion");
    List<Path> fotos = Arrays.asList(Paths.get("cualquiera"));
    HashMap<String, String> caracteristicasMascota = armarCaracteristicas(request);
    Especie especie = Especie.valueOf(request.queryParams("especie"));

    MascotaConChapita mascota = new MascotaConChapita(
        nombreMascota, apodoMascota, edadMascota, sexo, descripcion, fotos,
        caracteristicasMascota, especie, duenio, new ServicioJavaMail());

    mascota.setFoto(request.queryParams("foto"));

    return mascota;
  }

  private HashMap<String, String> armarCaracteristicas(Request request){
    List<CaracteristicaIdeal> caracteristicaIdeales = RepositorioCaracteristicasIdeales.instance().getCaracteristicasIdeales();
    HashMap<String, String> caracteristicasMascota = new HashMap<String, String>();
    String nombreCaracteristica;

    for(int i=0; i < caracteristicaIdeales.size(); i++){
      nombreCaracteristica = caracteristicaIdeales.get(i).getNombreCaracteristica();
      if(request.queryParams(nombreCaracteristica) != null){
        caracteristicasMascota.put(nombreCaracteristica, request.queryParams(nombreCaracteristica));
      }
    }

    return caracteristicasMascota;
  }
}