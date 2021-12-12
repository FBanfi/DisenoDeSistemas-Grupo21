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
import domain.usuario.Contacto;
import domain.usuario.Cuenta;
import domain.usuario.Usuario;
import domain.usuario.documento.Dni;
import domain.usuario.documento.Documento;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RescateController extends Encabezado{

  RepositorioMascotas repositorioMascotas = RepositorioMascotas.instance();
  RepositorioCuentas repositorioCuentas = RepositorioCuentas.instance();
  Repositorio repositorioRescate = new Repositorio("Rescate");
  Repositorio repositorioUsuario = new Repositorio("Usuario");

  public ModelAndView getFormularioRescate(Request request, Response response) {

    Map<String, Object> modelo = new HashMap<>();
    modelo.put("sesionSinIniciar",!seLogueo(request));
    prepararEncabezado(modelo, request, response);
    List<CaracteristicaIdeal> caracteristicas = RepositorioCaracteristicasIdeales.instance().getCaracteristicasIdeales();
    modelo.put("caracteristicas", caracteristicas);
    modelo.put("errorMascota", false);
    modelo.put("errorCaracteristica", false);

    return new ModelAndView(modelo, "formulario-rescate.html.hbs");
  }

  public ModelAndView mostrarOpcionesRescate(Request request, Response response) {
    Map<String, Object> modelo = new HashMap<>();
    prepararEncabezado(modelo, request, response);

    return new ModelAndView(modelo, "opciones-rescate.html.hbs");
  }

  public ModelAndView getScannerQR(Request request, Response response){
    response.redirect("/home");
    return null;//TODO esta puesto el home para probar
  }

  public ModelAndView encontrarMascota(Request request, Response response) {

    Usuario rescatista;
    MascotaSinChapita mascota;

    Map<String, Object> modelo = new HashMap<>();
    modelo.put("sesionSinIniciar",!seLogueo(request));
    prepararEncabezado(modelo, request, response);
    List<CaracteristicaIdeal> caracteristicas = RepositorioCaracteristicasIdeales.instance().getCaracteristicasIdeales();
    modelo.put("caracteristicas", caracteristicas);

    try {
      mascota = armarMascotaSinChapita(request);
    } catch (ExcepcionMascota e){
      System.out.println(e.getMessage());
      modelo.put("errorMascota", true);
      return new ModelAndView(modelo, "formulario-rescate.html.hbs");
    } catch (ExcepcionCaracteristica e){
      System.out.println(e.getMessage());
      modelo.put("errorCaracteristica", true);
      return new ModelAndView(modelo, "formulario-rescate.html.hbs");
    }

    if(seLogueo(request)){
      Cuenta cuenta = repositorioCuentas.buscarPorId(request.session().attribute("user_id"));
      rescatista = cuenta.getUsuario();

    }else {
      String direccion = request.queryParams("direccion_rescatista");
      LocalDate fechaNacimiento =  LocalDate.parse(request.queryParams("fecha_nacimiento_usuario"));
      Documento dni = new Documento(new Dni(), request.queryParams("documento_usuario"));

      rescatista = new Usuario(
          request.queryParams("nombre_rescatista"), request.queryParams("apellido_rescatista"),
          request.queryParams("email_rescatista"), direccion, dni, fechaNacimiento,
          Arrays.asList(new Contacto("Juana", "Sanchez" , 121212, "juana@gmail.com"))
      );
    }
    double latitud = Double.parseDouble(request.queryParams("latitud"));
    double longitud = Double.parseDouble(request.queryParams("longitud"));

    Ubicacion ubicacion = new Ubicacion(request.queryParams("ubicacion"), latitud,longitud);
    Rescate rescate = new Rescate(
        rescatista, Arrays.asList(Paths.get(request.queryParams("fotos"))),
        request.queryParams("descripcion"), ubicacion);
    rescate.setMascotaEncontrada(mascota);

    withTransaction(() ->{
      if(!seLogueo(request)){
        repositorioUsuario.agregar(rescatista);
      }
      repositorioMascotas.agregarMascota(mascota);
      repositorioRescate.agregar(rescate);
    });

    response.redirect("/home");
    return null;
  }

  // METODOS AUXILIARES


  private MascotaSinChapita armarMascotaSinChapita(Request request) {
    Boolean sexo = Boolean.parseBoolean(request.queryParams("sexo"));
    HashMap<String, String> caracteristicasMascota = armarCaracteristicas(request);
    Especie especie = Especie.valueOf(request.queryParams("especie"));

    return new MascotaSinChapita(sexo, caracteristicasMascota, especie, new ServicioJavaMail());
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
