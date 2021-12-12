package domain.usuario;

import domain.excepciones.exepcionesCuenta.ExcepcionContrasenia;
import domain.persistence.EntidadPersistente;
import domain.validacionContrasenia.ValidadorContrasenias;

import javax.persistence.*;

@Entity
public class Cuenta extends EntidadPersistente {

  @Column
  private String nombreUsuario;

  @Column
  private String contrasenia;

  @OneToOne(cascade = {CascadeType.ALL})
  @JoinColumn(name = "usuario_id")
  private Usuario usuario;

  @Column
  private Boolean esAdministrador;

  public Cuenta(String nombreUsuario, String contrasenia, Usuario usuario) {

    this.validarCreacion(contrasenia, nombreUsuario);
    this.validarSeguridad(contrasenia, nombreUsuario);
    this.nombreUsuario = nombreUsuario;
    this.contrasenia = contrasenia;
    this.usuario = usuario;
    this.esAdministrador = false;
  }

  public Cuenta() {

  }

  private void validarCreacion(String contrasenia, String nombreUsuario) {

    this.validarContrasenia(contrasenia);
    this.validarNombreUsuario(nombreUsuario);
  }

  public void validarSeguridad(String contrasenia, String nombreUsuario) {

    ValidadorContrasenias.instance().validar(contrasenia, nombreUsuario);
  }

  public void validarNombreUsuario(String nombreUsuario) {

    if (nombreUsuario == null) {

      throw new ExcepcionContrasenia("Debe ingresar un usuario");
    }
  }

  public void validarContrasenia(String contrasenia) {

    if (contrasenia == null) {

      throw new ExcepcionContrasenia("Debe ingresar una contrasenia");
    }
  }

  public String getNombreUsuario() {

    return nombreUsuario;
  }

  public String getContrasenia() {

    return contrasenia;
  }

  public Usuario getUsuario() {

    return usuario;
  }

  public Boolean getEsAdministrador() {
    return esAdministrador;
  }

  public void hacerAdministrador() {
    this.esAdministrador = true;
  }
}

