package domain.mascota;

import domain.excepciones.expecionesMascota.ExcepcionMascota;
import domain.persistence.EntidadPersistente;
import domain.repositorios.RepositorioCaracteristicasIdeales;
import domain.services.serviceHogares.ServicioHogares;
import domain.services.serviceMail.ServicioNotificacion;
import domain.usuario.Usuario;

import javax.persistence.*;
import java.util.Locale;
import java.util.Map;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Tipo")
public abstract class Mascota extends EntidadPersistente {

  @Column
  public Boolean esMacho;

  @ElementCollection
  @CollectionTable(name = "caracteristicas_mascota",
      joinColumns = {@JoinColumn(name = "mascota_id", referencedColumnName = "id")})
  @MapKeyColumn(name = "nombre_caracteristica")
  @Column(name = "valor_caracteristica")
  private Map<String, String> caracteristicas;
  //antes era un object pero lo cambiamos a un string y q el controlador se encargue de convertir el
  //object en string, es decir un numero pasaria a ser un string, lo mismo para un booleano

  @Enumerated
  private Especie especie;

  @Transient
  public ServicioNotificacion servicioNotificacion;

  public Mascota(Boolean esMacho, Map<String, String> caracteristicas,
                 Especie especie, ServicioNotificacion servicioNotificacion) {

    this.servicioNotificacion = servicioNotificacion;
    this.validarSexo(esMacho);
    this.validarCaracteristicas(caracteristicas);
    this.validarEspecie(especie);

    this.esMacho = esMacho;
    this.caracteristicas = caracteristicas;
    this.especie = especie;
  }

  public Mascota() {

  }

  public void serRescatado(Rescate rescate, ServicioHogares servicioHogares){

    rescate.setMascotaEncontrada(this);
    gestionarRescate(rescate, servicioHogares);
  }


  abstract void gestionarRescate(Rescate rescate, ServicioHogares servicioHogares);

  public Object getLaCaracteristica(String keyCaracteristica){
    return caracteristicas.get(keyCaracteristica);
  }

  public boolean esUnaDeSusCaracteristicas(String keyCaracteristica){
    return caracteristicas.containsKey(keyCaracteristica) && getLaCaracteristica(keyCaracteristica) != null;
  }

  // VALIDACIONES Y GETTERS

  private void validarSexo(Boolean sexoAnimal) {
    if (sexoAnimal == null) {
      throw new ExcepcionMascota("Debe especificar el sexo de la mascota");
    }
  }

  private void validarCaracteristicas(Map<String, String> caracteristicas) {
    if (caracteristicas == null) {
      throw new ExcepcionMascota("Debe especificar las caracteristicas de la mascota");
    }

    //Repositorio<CaracteristicaIdeal> repo = new Repositorio<CaracteristicaIdeal>()
    RepositorioCaracteristicasIdeales.instance().validar(caracteristicas);
  }

  private void validarEspecie(Especie especie) {
    if (especie == null) {
      throw new ExcepcionMascota("Debe especificar la especie de la mascota");
    }
  }

  public Boolean isMacho() {
    return esMacho;
  }

  public Map<String, String> getCaracteristicas() {
    return caracteristicas;
  }

  public Especie getEspecie() {
    return especie;
  }

  public String getTamanio(){return (String) caracteristicas.get("tamanio");}

  public String getNombreEspecie() {
    return especie.toString().toLowerCase(Locale.ROOT);
  }

  protected abstract Usuario getDuenio();


}