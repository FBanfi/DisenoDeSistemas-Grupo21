package domain.mascota;

import domain.excepciones.excepcionesRescate.ExcepcionRescate;
import domain.persistence.EntidadPersistente;
import domain.services.serviceHogares.Ubicacion;
import domain.usuario.Usuario;

import javax.persistence.*;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table
public class Rescate extends EntidadPersistente {

  //Para saber los rescates recientes, pido todos los rescates y despues filtro los recientes

  @ManyToOne
  @JoinColumn(name = "rescatista_id")
  private Usuario rescatista;

  @Column
  private LocalDate fechaRescate;

  @Transient
  private List<Path> fotos;


  @Column
  private String descripcion;

  @Embedded
  private Ubicacion lugar;

  @ManyToOne(cascade = {CascadeType.ALL})
  @JoinColumn(name = "mascotaEncotrada_id")
  public Mascota mascotaEncontrada;

  public Rescate(Usuario rescatista,List<Path> fotos,
                 String descripcion, Ubicacion lugar) {

    validarUsuario(rescatista);
    validarLugar(lugar);
    validarDescripcion(descripcion);
    validarFoto(fotos);

    this.rescatista = rescatista;
    this.fechaRescate = LocalDate.now();
    this.fotos = fotos;
    this.descripcion = descripcion;
    this.lugar = lugar;

  }

  public Rescate() {

  }

  public Boolean esFechaReciente() {

    return LocalDate.now()
        .minusDays(10)
        .isBefore(this.fechaRescate);
  }

  public Mascota getMascotaEncontrada() {
    return this.mascotaEncontrada;
  }

  public Usuario getRescatista() {
    return rescatista;
  }

  public LocalDate getFechaRescate() {
    return fechaRescate;
  }

  public List<Path> getFotos() {
    return fotos;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public Ubicacion getLugar() {
    return lugar;
  }

  public void setMascotaEncontrada(Mascota mascota){
    this.mascotaEncontrada = mascota;
  }

  public void validarFoto(List<Path> fotos) {
    if (fotos == null) {
      throw new ExcepcionRescate("No se ha ingresado ninguna foto del rescate");
    }
  }

  public void validarUsuario(Usuario duenio) {
    if (duenio == null) {
      throw new ExcepcionRescate("El usuario no es valido");
    }
  }

  public void validarDescripcion(String descripcion) {
    if (descripcion == null) {
      throw new ExcepcionRescate("Debe especificar una descripcion sobre el rescate");
    }
  }

  public void validarLugar(Ubicacion lugarDeRescate){
    if(lugarDeRescate == null){
      throw new ExcepcionRescate("Ingrese el lugar donde se hizo el rescate");
    }
  }

}