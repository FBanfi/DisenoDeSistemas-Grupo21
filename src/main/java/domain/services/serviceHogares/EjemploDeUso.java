package domain.services.serviceHogares;


import java.util.List;

public class EjemploDeUso {

  public static void main(String[] args) {
    ServicioHogares servicioHogares = ServicioHogares.instancia();

    ListadoHogares listadoHogares = servicioHogares.listadoHogares();

    List<HogarTransitorio> listaHogares = listadoHogares.getHogares();
    Double lat = listaHogares.get(1).ubicacion.getLat();
    Double longi = listaHogares.get(1).ubicacion.getLong();
    System.out.println(lat);
    System.out.println(longi);
    listaHogares.stream().forEach(h-> System.out.println(h.caracteristicas));


    /*Email email = new Email();
    email.setEmail("mailDePruebaaaaassd@gmail.com");
    TokenAPI tokenPrueba = servicioHogares.obtenerToken(email);
    System.out.println(tokenPrueba.bearer_token);
     */
  }

}
