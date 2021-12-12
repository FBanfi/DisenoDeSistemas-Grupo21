package domain.NotificacionesSemanales;

import domain.asociacion.Asociacion;
import domain.repositorios.RepositorioAsociaciones;
import domain.services.serviceMail.ServicioJavaMail;
import java.util.Timer;
import java.util.TimerTask;

public class Notificaciones {

  public static void main(String[] args) {

    // Asi seria con planificacion interna de eventos
    TimerTask task = new TimerTask() {
      public void run() {
        RepositorioAsociaciones.instance().getAsociaciones().forEach(Asociacion::recomendacionSemanal);
        //System.out.println("hola");

      }
    };

    Timer timer = new Timer("Timer");

    long interval = 604800000L; //Para una semana
    //long interval = 5000L; // Para 5 segundos
    timer.schedule(task, 0L, interval);

  }
}