package model;

import domain.excepciones.exepcionesCuenta.ExcepcionContrasenia;
import domain.excepciones.exepcionesCuenta.ExcepcionListaContrasenia;
import domain.excepciones.exepcionesCuenta.ExcepcionUsuario;
import domain.mascota.MascotaConChapita;
import domain.repositorios.Repositorio;
import domain.repositorios.RepositorioCuentas;
import domain.usuario.Contacto;
import domain.usuario.Cuenta;
import domain.usuario.Usuario;
import domain.usuario.documento.Dni;
import domain.usuario.documento.Documento;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;


public class SesionController extends Encabezado{

    RepositorioCuentas repositorioCuentas = RepositorioCuentas.instance();
    Repositorio repositorioUsuario = new Repositorio<Usuario>("Usuario");
    //Repositorio repositorioDocumento = new Repositorio<Documento>("Documento");

    public ModelAndView getLogin(Request request, Response response) {
      if (seLogueo(request)) {
        response.redirect("/home");
        return null;
      }

      Map<String, Object> modelo = new HashMap<>();
      prepararEncabezado(modelo, request, response);
      modelo.put("origen", request.queryParams("origin"));
      modelo.put("errorUsuarioYcontrasenia", false);

      return new ModelAndView(modelo, "formulario-login.html.hbs");
    }

    public ModelAndView iniciarSesion(Request request, Response response) {
      try {
        Cuenta cuenta = repositorioCuentas.buscarPorUsuarioYContrasenia(
            request.queryParams("username"), //son body params
            request.queryParams("password"));

        request.session().attribute("user_id", cuenta.getId());

        response.redirect(request.queryParams("origin"));
        return null;
      } catch (NoSuchElementException e) {
        Map<String, Object> modelo = new HashMap<>();
        prepararEncabezado(modelo, request, response);
        modelo.put("origen", request.queryParams("origin"));
        modelo.put("errorUsuarioYcontrasenia", true);
        System.out.println(e.getMessage());
        return new ModelAndView(modelo, "formulario-login.html.hbs");
      }
    }

  public Void cerrarSesion(Request request, Response response) {
    request.session().attribute("user_id", null);
    response.redirect("/home");
    return null;
  }

  public ModelAndView getSignup(Request request, Response response) {

    if (seLogueo(request)) {
      response.redirect("/home");
      return null;
    }

    Map<String, Object> modelo = new HashMap<>();
    modelo.put("errorCuentaExistente", false);
    modelo.put("errorUsuario", false);
    modelo.put("errorContrasenia", false);
    prepararEncabezado(modelo, request, response);

    return new ModelAndView(modelo, "formulario-singup.html.hbs");
  }

  public ModelAndView registrarCuenta(Request request, Response response) {

    Map<String, Object> modelo = new HashMap<>();
    prepararEncabezado(modelo, request, response);

    try {
      Cuenta cuenta = repositorioCuentas.buscarPorUsuarioYContrasenia(
          request.queryParams("nombre_cuenta"),
          request.queryParams("contrasenia_cuenta"));

      modelo.put("errorCuentaExistente", true);

      return new ModelAndView(modelo, "formulario-singup.html.hbs");

    } catch (NoSuchElementException e) {

      String direccion = request.queryParams("direccion_usuario");
      LocalDate fechaNacimiento = null;
      try {
        fechaNacimiento =  LocalDate.parse(request.queryParams("fecha_nacimiento_usuario"));
      }
      catch (DateTimeParseException a){
        modelo.put("errorUsuario", true);
        return new ModelAndView(modelo, "formulario-singup.html.hbs");
      }
      try {

        Documento dni = new Documento(new Dni(), request.queryParams("documento_usuario"));

        Usuario nuevoUsuario = new Usuario(
            request.queryParams("nombre_usuario"), request.queryParams("apellido_usuario"),
            request.queryParams("email_usuario"), direccion, dni, fechaNacimiento,
            Arrays.asList(new Contacto(request.queryParams("nombre_contacto"),
                request.queryParams("apellido_contacto"),
                Integer.parseInt(request.queryParams("telefono_contacto")),
                request.queryParams("email_contacto")))
        );

        Cuenta nuevaCuenta = new Cuenta(request.queryParams("nombre_cuenta"),
            request.queryParams("contrasenia_cuenta"), nuevoUsuario);

        withTransaction(() ->{
          repositorioUsuario.agregar(nuevoUsuario);
          repositorioCuentas.agregar(nuevaCuenta);
        });

        request.session().attribute("user_id", nuevaCuenta.getId());
        response.redirect("/home");

        return null;
      } catch (ExcepcionUsuario u){
        modelo.put("errorUsuario", true);
        return new ModelAndView(modelo, "formulario-singup.html.hbs");
      } catch (ExcepcionContrasenia c){
        modelo.put("errorContrasenia", true);
        return new ModelAndView(modelo, "formulario-singup.html.hbs");
      }
    }
  }
}
