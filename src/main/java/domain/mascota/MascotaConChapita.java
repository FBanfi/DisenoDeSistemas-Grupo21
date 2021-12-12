package domain.mascota;

import domain.excepciones.exepcionesCuenta.ExcepcionUsuario;
import domain.excepciones.expecionesMascota.ExcepcionMascota;
import domain.services.serviceHogares.ServicioHogares;
import domain.services.serviceMail.ServicioNotificacion;
import domain.usuario.Usuario;

import javax.persistence.*;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Entity
@DiscriminatorValue("C")
public class MascotaConChapita extends Mascota {

  @Column
  private String nombre;

  @Column
  private String apodo;

  @Column
  private Integer edad;

  @Column
  private String descripcion;

  //@OneToMany pero no funca
  @Transient
  private List<Path> fotos;

  @Column
  private String foto;

  @ManyToOne
  private Usuario duenio;

  public MascotaConChapita(String nombre,String apodo,Integer edad, Boolean esMacho,String descripcion,List<Path> fotos,
                           Map<String, String> caracteristicas, Especie especie, Usuario duenio,
                           ServicioNotificacion servicioNotificacion
                            ) {
    super(esMacho, caracteristicas, especie, servicioNotificacion);

    this.validarNombre(nombre);
    this.validarApodo(apodo);
    this.validarEdad(edad);
    this.validarDescripcion(descripcion);
    this.validarUsuario(duenio);
    this.validarFoto(fotos);

    this.nombre = nombre;
    this.apodo = apodo;
    this.edad = edad;
    this.descripcion = descripcion;
    this.fotos = fotos;
    this.duenio = duenio;
  }

  public MascotaConChapita() {

  }

  @Override
  void gestionarRescate(Rescate rescate, ServicioHogares servicioHogares ){
    servicioNotificacion.enviarNotificacionMascotaConChapitaRescatada(duenio, rescate);
  }

  // VALIDACIONES Y GETTERS

  public void validarFoto(List<Path> fotos) {
    if (fotos == null) {
      throw new ExcepcionMascota("No se ha ingresado ninguna foto de la mascota");
    }
  }

  public void validarUsuario(Usuario duenio) {
    if (duenio == null) {
      throw new ExcepcionMascota("Debe especificar el duenio de la mascota");
    }
  }

  public void validarDescripcion(String descripcion) {
    if (descripcion == null) {
      throw new ExcepcionMascota("Debe especificar una descripcion sobre la mascota");
    }
  }

  private void validarEdad(Integer edad) {
    if (edad == null || edad < 0)
      throw new ExcepcionMascota("Ingrese una edad valida");
  }

  private void validarNombre(String nombre) {
    if (nombre == null)
      throw new ExcepcionMascota("Debe especificar el nombre de la mascota");
  }

  private void validarApodo(String apodo) {
    if (apodo == null)
      throw new ExcepcionMascota("Debe especificar el apodo de la mascota");
  }

  public String getEmailDeDuenio(){
    if (this.getDuenio().getEmail() == null){
      throw new ExcepcionUsuario("El usuario no tiene registrado un mail");
    }
    return this.getDuenio().getEmail();
  }

  public Usuario getDuenio(){
    return duenio;
  }

  public String getNombre() {
    return nombre;
  }

  public String getApodo() {
    return apodo;
  }

  public Integer getEdad() {
    return edad;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setFoto(String foto) {
    this.foto = foto;
  }

  public String getFoto() {
    return foto;
  }
}