package domain.services.serviceMail;

import domain.excepciones.excepcionesEmail.ExcepcionEmail;
import domain.mascota.Rescate;
import domain.publicaciones.PublicacionAdopcion;
import domain.publicaciones.PublicacionInteresadoAdopcion;
import domain.publicaciones.PublicacionMascotaPerdida;
import domain.usuario.Usuario;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;
import java.util.Properties;

public class ServicioJavaMail implements ServicioNotificacion{

  public ServicioJavaMail( ) {

  }

  @Override
  public void enviarNotificacionInteresado(Usuario usuario, PublicacionAdopcion publicacionAdopcion){
    String descripcion = this.armarDescripcionParaInteresadoEnAdoptar(publicacionAdopcion);
    this.enviarEmail(usuario.getEmail(), "Nuevo Interesado en tu mascota!", descripcion);
  }

  private String armarDescripcionParaInteresadoEnAdoptar(PublicacionAdopcion publicacionAdopcion) {
    //TODO alguna forma de la info del interesado en un string
    return null;
  }

  @Override
  public void enviarNotificacionRecomendacion(Usuario usuario, List<PublicacionAdopcion> publicacionesRecomendadas) {
    String descripcion = this.armarDescripcionParaRecomendacion(publicacionesRecomendadas);
    this.enviarEmail(usuario.getEmail(), "Hay una mascota que está esperando que la conozcas!", descripcion);
  }

  private String armarDescripcionParaRecomendacion(List<PublicacionAdopcion> publicacionesRecomendadas) {

    String mensaje = "Las recomendaciones de esta semana son: \n";
    int cantidadPublicaciones = publicacionesRecomendadas.size();
    for(int i = 0; i < cantidadPublicaciones; i++){
      PublicacionAdopcion publicacion = publicacionesRecomendadas.get(i);
      mensaje = mensaje + "Mascota recomendada:" + publicacion.mascotaEnAdopcion.getNombre() + " \n";
    }

    return mensaje;
    //TODO alguna forma de mandarle la lista de publicaciones en un string
  }

  @Override
  public void enviarNotificacionMascotaSinChapitaEncontrada(Usuario usuario, PublicacionMascotaPerdida publicacionMascotaPerdida){
    String descripcion = this.armarDescripcionParaMascotaSinChapitaEncontrada(publicacionMascotaPerdida);
    this.enviarEmail(usuario.getEmail(), "Alguien reclama a una mascota que encontraste" , descripcion);
  }

  private String armarDescripcionParaMascotaSinChapitaEncontrada(PublicacionMascotaPerdida publicacionMascotaPerdida) {
    return null;
    //TODO
  }

  @Override
  public void enviarNotificacionMascotaConChapitaRescatada(Usuario usuario, Rescate rescate){ //TODO alguna forma de mandarle datos del rescate al usuario
    String descripcion = this.armarDescripcionParaMascotaConChapitaEncontrada(rescate);
    this.enviarEmail(usuario.getEmail(), "Alguien rescato a tu mascota perdida", descripcion);
  }

  @Override
  public void enviarLinkDeBajaPublicacionInteresado(Usuario usuario, PublicacionInteresadoAdopcion publicacionInteresadoAdopcion) {
    String linkDePublicacion = this.obtenerLinkDePublicacion(publicacionInteresadoAdopcion);
    this.enviarEmail(usuario.getEmail(), "Link para dar de baja su publicacion para adoptar", linkDePublicacion);
  }

  private String obtenerLinkDePublicacion(PublicacionInteresadoAdopcion publicacionInteresadoAdopcion) {
    return null;
    //TODO
  }

  private String armarDescripcionParaMascotaConChapitaEncontrada(Rescate rescate) {
    return null;
  }

  public void enviarEmail(String direccionMail, String asunto, String textoMail) {
    Properties prop = new Properties();
    prop.put("mail.smtp.host", "smtp.gmail.com");
    prop.put("mail.smtp.port", "465");
    prop.put("mail.smtp.auth", "true");
    prop.put("mail.smtp.socketFactory.port", "465");
    prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

    String myAccountEmail = "juana@gmail.com"; //mail emisor
    String password = "juana!"; //constrasenia del mail emisor


    Session session = Session.getDefaultInstance(prop,
        new javax.mail.Authenticator() {
          protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(myAccountEmail,password);
        }
      });

    Message message = prepararMessage(session, myAccountEmail, direccionMail, asunto, textoMail);
    try {

      Transport.send(message);
      System.out.println("Done");
    }catch(Exception e){
      System.out.println(e.getMessage());
      throw new ExcepcionEmail("No se pudo enviar el mail");
    }
  }

  private static Message prepararMessage(Session session, String myAccountEmail, String direccionMail, String asunto, String textoMail) {

    try{
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(myAccountEmail));
      message.setRecipient(Message.RecipientType.TO, new InternetAddress(direccionMail));
      message.setSubject(asunto);
      message.setText(textoMail);
      return message;
    } catch(MessagingException e){
      throw new ExcepcionEmail("No se pudo preparar el mensaje del mail");
    }
  }
}
