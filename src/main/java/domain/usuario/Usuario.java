package domain.usuario;

import domain.excepciones.exepcionesCuenta.ExcepcionUsuario;
import domain.persistence.EntidadPersistente;
import domain.services.serviceHogares.ServicioHogares;
import domain.usuario.documento.Documento;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Usuario")
public class Usuario extends EntidadPersistente {

  @Column
  private String nombre;

  @Column
  private String apellido;

  @Column
  private String email;

  @Column
  private String direccion;

  @Column(columnDefinition = "DATE")
  private LocalDate fechaNacimiento;

  @OneToOne(cascade = {CascadeType.ALL})
  private Documento documento;

  @ElementCollection
  private List<Contacto> contactos;

  @Transient  //???
  private ServicioHogares servicioHogares;

  public Usuario(String nombre, String apellido, String email, String direccion,
                 Documento documento, LocalDate fechaNacimiento, List<Contacto> datosContacto) {

    this.validarNombre(nombre);
    this.validarApellido(apellido);
    this.validarEmail(email);
    this.validarContacto(datosContacto);
    this.validarDireccion(direccion);
    this.validarDocumento(documento);
    this.validarFecha(fechaNacimiento);

    this.nombre = nombre;
    this.apellido = apellido;
    this.email = email;
    this.direccion = direccion;
    this.documento = documento;
    this.fechaNacimiento = fechaNacimiento;
    this.contactos = datosContacto;
    this.servicioHogares = new ServicioHogares();
  }

  public Usuario() {

  }


  // VALIDACIONES
  private void validarEmail(String email) {
    if (email == null) {
      throw new ExcepcionUsuario("Debe especificar el mail");
    }
  }

  private void validarNombre(String nombre) {
    if (nombre == null) {
      throw new ExcepcionUsuario("Debe especificar el nombre del usuario");
    }
  }

  private void validarApellido(String apellido) {
    if (apellido == null) {
      throw new ExcepcionUsuario("Debe especificar el apellido del usuario");
    }
  }

  private void validarDocumento(Documento tipoDoc) {
    if (tipoDoc == null) {
      throw new ExcepcionUsuario("Debe especificar el tipo de doc del usuario");
    }
  }

  private void validarContacto(List <Contacto> datosContacto) {
    if (datosContacto == null) {
      throw new ExcepcionUsuario("Debe especificar un contacto del usuario");
    }
  }

  private void validarDireccion(String direccion) {
    if (direccion == null) {
      throw new ExcepcionUsuario("Debe especificar la direccion del usuario");
    }
  }

  private void validarFecha(LocalDate fechaNacimiento) {
    if (fechaNacimiento == null) {
      throw new ExcepcionUsuario("Debe especificar una fecha de nacimiento");
    }
  }

  // GETTERS
  public String getNombre() {
    return nombre;
  }

  public String getApellido() {
    return apellido;
  }

  public String getEmail() {
    return email;
  }

  public String getDireccion() {
    return direccion;
  }

  public LocalDate getFechaNacimiento() {
    return fechaNacimiento;
  }

  public Documento getTipoDocumento() {
    return documento;
  }

  public List<Contacto> getContacto() {
    return contactos;
  }

}
