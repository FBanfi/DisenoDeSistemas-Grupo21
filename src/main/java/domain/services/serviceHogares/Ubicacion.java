package domain.services.serviceHogares;

import com.google.gson.annotations.SerializedName;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Ubicacion {

  @Column
  public String direccion;

  @Column
  public double lat;

  @Column
  @SerializedName(value = "long")
  public double longi;

  public Ubicacion(String direccion, double lat, double longi) {
    this.direccion = direccion;
    this.lat = lat;
    this.longi = longi;
  }

  public Ubicacion() {

  }


  public double getLat() {
    return lat;
  }

  public double getLong(){
    return longi;
  }
}
