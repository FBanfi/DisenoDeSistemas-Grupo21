package domain.asociacion;

import domain.persistence.EntidadPersistente;
import domain.publicaciones.Publicacion;
import domain.publicaciones.PublicacionAdopcion;
import domain.publicaciones.PublicacionInteresadoAdopcion;
import domain.publicaciones.PublicacionMascotaPerdida;
import domain.repositorios.Repositorio;
import domain.services.serviceHogares.Ubicacion;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Asociacion extends EntidadPersistente {

  @Embedded
  private Ubicacion ubicacion;

  @OneToMany(cascade = {CascadeType.ALL})
  @JoinColumn( name = "asociacion_id")
  private List<PublicacionAdopcion> publicacionesAdopcion;

  @OneToMany(cascade = {CascadeType.ALL})
  @JoinColumn( name = "asociacion_id")
  private List<PublicacionInteresadoAdopcion> publicacionesInteresadoAdopcion;

  @OneToMany(cascade = {CascadeType.ALL})
  @JoinColumn( name = "asociacion_id")
  private List<PublicacionMascotaPerdida> publicacionesMascotaPerdida;

  @ElementCollection
  private List<PreguntaSobreMascota> preguntasParticulares;

  public Asociacion(Ubicacion ubicacion) {
    this.ubicacion = ubicacion;
    this.publicacionesMascotaPerdida = new ArrayList<PublicacionMascotaPerdida>();
    this.publicacionesInteresadoAdopcion = new ArrayList<PublicacionInteresadoAdopcion>();
    this.publicacionesAdopcion = new ArrayList<PublicacionAdopcion>();
    this.preguntasParticulares = new ArrayList<PreguntaSobreMascota>();
  }

  public Asociacion() {

  }

  public void agregarPregunta(PreguntaSobreMascota nuevaPregunta) {
    preguntasParticulares.add(nuevaPregunta);
  }

  public void eliminarPregunta(PreguntaSobreMascota preguntaSobreMascota) {
    preguntasParticulares.remove(preguntaSobreMascota);
  }

  public boolean tieneEstaPregunta(PreguntaSobreMascota pregunta){
    return this.preguntas().contains(pregunta);
  }

  public void registrarPublicacionAdopcion(PublicacionAdopcion publicacionAdopcion){
    this.publicacionesAdopcion.add(publicacionAdopcion);
  }

  public void registrarPublicacionInteresadoAdopcion(PublicacionInteresadoAdopcion publicacionInteresadoAdopcion){
    this.publicacionesInteresadoAdopcion.add(publicacionInteresadoAdopcion);
  }

  public void registrarPublicacionMascotaPerdida(PublicacionMascotaPerdida publicacionMascotaPerdida){
    this.publicacionesMascotaPerdida.add(publicacionMascotaPerdida);
  }

  public List<PreguntaSobreMascota> preguntas(){
    List<PreguntaSobreMascota> totalPreguntas = new ArrayList<PreguntaSobreMascota>();
    totalPreguntas.addAll(this.preguntasParticulares);
    Repositorio repoPreguntasComunes = new Repositorio("PreguntaSobreMascota");
    totalPreguntas.addAll(repoPreguntasComunes.todos());
    return totalPreguntas;
  }

  public void recomendacionSemanal() {
    List<PublicacionInteresadoAdopcion> publicacionesInteresados = this.getPublicacionInteresadosEnAdopcion();
    List<PublicacionAdopcion> publicacionesAdopcion = this.getPublicacionAdopcion();

    publicacionesInteresados.forEach(publicacion -> publicacion.notificarRecomendacion(publicacionesAdopcion));
  }

  public List<PublicacionInteresadoAdopcion> getPublicacionInteresadosEnAdopcion(){
    return publicacionesInteresadoAdopcion;
  }

  public List<PublicacionAdopcion> getPublicacionAdopcion(){
    return publicacionesAdopcion;
  }

  public double distanciaA(Ubicacion lugar) {
    return Math.sqrt(Math.pow((ubicacion.getLat()- lugar.getLat()), 2) + Math.pow((ubicacion.getLong()- lugar.getLong()), 2));
  }

  public List<Publicacion> getPublicaciones() {
    List<Publicacion> publicaciones = new ArrayList<Publicacion>();
    publicaciones.addAll(publicacionesAdopcion);
    publicaciones.addAll(publicacionesInteresadoAdopcion);
    publicaciones.addAll(publicacionesMascotaPerdida);
    return publicaciones;
  }

  public void setPreguntasParticulares(List<PreguntaSobreMascota> preguntasParticulares) {
    this.preguntasParticulares = preguntasParticulares;
  }
}
