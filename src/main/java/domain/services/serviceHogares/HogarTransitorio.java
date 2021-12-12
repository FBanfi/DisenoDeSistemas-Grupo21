package domain.services.serviceHogares;

import domain.excepciones.excepcionesHogar.ExcepcionHogar;
import domain.mascota.Mascota;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Entity
@Table
public class HogarTransitorio {

  @Id
  //@Column
  public String id;
  @Column
  public String nombre;
  @Column
  public Ubicacion ubicacion;
  @Column
  public String telefono;
  @Embedded
  public Admisiones admisiones;
  @Column
  public int capacidad;
  @Column
  public int lugaresDisponibles;
  @Column
  public boolean patio;
  @ElementCollection
  public List<String> caracteristicas;

  public HogarTransitorio(String id, String nombre, Ubicacion ubicacion, String telefono,
                          Admisiones admisiones, int capacidad, int lugaresDisponibles,
                          boolean patio, List<String> caracteristicas) {
    this.id = id;
    this.nombre = nombre;
    this.ubicacion = ubicacion;
    this.telefono = telefono;
    this.admisiones = admisiones;
    this.capacidad = capacidad;
    this.lugaresDisponibles = lugaresDisponibles;
    this.patio = patio;
    this.caracteristicas = caracteristicas;
  }

  public HogarTransitorio() {

  }

  public boolean esApto(Mascota mascota, Ubicacion lugar, Integer radio) {
    return admisiones.aceptaEspecie(mascota.getEspecie()) && aceptaTamanio(mascota.getTamanio())
        && tieneLugaresDisponibles() && estaDentroDelRadio(lugar,radio);
  }

  public boolean aceptaTamanio(String tamanio) {
    return patio || tamanio.equals("pequenio");
  }

  public boolean aceptaCaracteristicas(Map<String, String> caracteristicasMascota) {
    return caracteristicasMascota.values().containsAll(caracteristicas);
  }

  public boolean tieneLugaresDisponibles() {
    return 0 < lugaresDisponibles;
  }

  public boolean estaDentroDelRadio(Ubicacion centro, Integer radio){
    if(radio <= 0){
      throw new ExcepcionHogar("Debe agregar un radio mayor o igual a cero"); //esta excepcion esta de mas
    }
    if(Objects.isNull(radio)){
      return true;
    }
    return centro.lat - radio <= ubicacion.lat && ubicacion.lat <= centro.lat + radio
        &&  centro.longi - radio <= ubicacion.longi && ubicacion.longi <= centro.longi + radio;
  }

  public Admisiones getAdmisiones() {
    return admisiones;
  }

  public double getLatitud(){
    return ubicacion.getLat();
  }

  public double getLongitud(){
    return ubicacion.getLong();
  }
}
