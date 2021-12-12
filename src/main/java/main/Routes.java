package main;

import domain.asociacion.Asociacion;
import domain.mascota.CaracteristicaIdeal;
import domain.repositorios.RepositorioAsociaciones;
import domain.repositorios.RepositorioCaracteristicasIdeales;
import domain.services.serviceHogares.*;
import domain.services.serviceMail.ServicioJavaMail;
import model.*;
import org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class Routes implements WithGlobalEntityManager, TransactionalOps {

  public static void main(String[] args) {
    System.out.println("Corriendo bootstrap...");
    //new Bootstrap().run();
    HandlebarsTemplateEngine engine = new HandlebarsTemplateEngine();
    MascotaController mascotaController = new MascotaController();
    RescateController rescateController = new RescateController();
    InicioController inicioController = new InicioController();
    SesionController sesionController = new SesionController();
    CaracteristicasController caracteristicasController = new CaracteristicasController();

    System.out.println("Iniciando servidor...");
    Spark.port(Heroku.getHerokuAssignedPort());
    Spark.staticFileLocation("/public");

    Spark.get("/home", inicioController::getHome, engine);

    Spark.get("/login", sesionController::getLogin, engine);
    Spark.post("/login", sesionController::iniciarSesion, engine);

    Spark.get("/signup", sesionController::getSignup, engine);
    Spark.post("/signup", sesionController::registrarCuenta, engine);

    Spark.get("/logout", sesionController::cerrarSesion);

    Spark.get("/mascotas/nueva", mascotaController::getFormularioMascota, engine);
    Spark.post("/mascotas/nueva", mascotaController::registrarMascota, engine);
    Spark.get("/mascotas/encontrada", rescateController::mostrarOpcionesRescate, engine);

    Spark.get("/mascotas", mascotaController::getMascotas, engine);

    Spark.get("/rescate/sinChapita", rescateController::getFormularioRescate, engine);
    Spark.post("/rescate", rescateController::encontrarMascota, engine);
    Spark.get("/rescate/conChapita", rescateController::getScannerQR, engine);

    Spark.get("/caracteristicas", caracteristicasController::getTablaCaracteristicas, engine);
    Spark.post("/caracteristicas", caracteristicasController::crearCaracteristica, engine);
    Spark.post("/caracteristicas/borrar/:id", caracteristicasController::eliminarCaracteristica);
    Spark.get("/caracteristicas/nueva", caracteristicasController::getFormularioCaracteristica, engine);

    Spark.after((request,response)->{
      PerThreadEntityManagers.getEntityManager();
      PerThreadEntityManagers.closeEntityManager();
    });

    System.out.println("Servidor iniciado!");

    TimerTask task = new TimerTask() {
      public void run() {
        //RepositorioAsociaciones.instance().enviarRecomendacionesSemanales();
        //System.out.println("hola");
        //new ServicioJavaMail().enviarEmail("scarnezis@gmail.com", "Prueba routes", "Hola");
      }
    };

    Timer timer = new Timer("Timer");

    //long interval = 604800000L; //Para una semana
    long interval = 600000L;
    //long interval = 5000L; // Para 5 segundos
    timer.schedule(task, 0L, interval);

  }
}
