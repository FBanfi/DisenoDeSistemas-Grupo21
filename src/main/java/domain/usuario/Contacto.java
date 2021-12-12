package domain.usuario;
import domain.excepciones.exepcionesCuenta.ExcepcionContacto;
import domain.persistence.EntidadPersistente;

import javax.persistence.*;

@Embeddable
public class Contacto {

  @Column
  private String nombre;

  @Column
  private String apellido;

  @Column
  private Integer telefono;

  @Column
  private String email;

  public Contacto(){

  }

  public Contacto(String nombre, String apellido, Integer telefono, String email) {

    validarCreacion(nombre, apellido, telefono, email);
    this.nombre = nombre;
    this.apellido = apellido;
    this.telefono = telefono;
    this.email = email;
  }

  private void validarCreacion(String nombre, String apellido, Integer telefono, String email) {

    validarNombreContacto(nombre);
    validarApellidoContacto(apellido);
    validarTelefonoContacto(telefono);
    validarEmailContacto(email);
  }


  public void validarNombreContacto(String nombreContacto) {

    if (nombreContacto == null) {

      throw new ExcepcionContacto("Debe ingresar el nombre del contacto");
    }

  }

  public void validarApellidoContacto(String apellidoContacto) {

    if (apellidoContacto == null) {

      throw new ExcepcionContacto("Debe ingresar el apellido del contacto");
    }

  }

  public void validarTelefonoContacto(Integer telefono) {

    if (telefono < 0) {

      throw new ExcepcionContacto("El telefono ingresado no debe ser negativo");
    }

  }

  public void validarEmailContacto(String email) {

    if (email == null /*|| !this.emailCumpleConFormato(email)*/) {

      throw new ExcepcionContacto("Debe ingresar el mail correctamente");
    }

  }

  public boolean emailCumpleConFormato(String email) {

    return (email.endsWith(".com") || email.endsWith(".com.ar")) && email.contains("@"); //Se podria separar los metodos en otra para lograr una mejor extensibilidad
  }

  public String getNombre() {

    return nombre;
  }

  public String getApellido() {

    return apellido;
  }

  public Integer getTelefono() {

    return telefono;
  }

  public String getEmail() {

    return email;
  }
}