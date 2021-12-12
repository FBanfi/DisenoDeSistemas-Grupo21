package main;

import domain.asociacion.Asociacion;
import domain.asociacion.PreguntaSobreMascota;
import domain.mascota.CaracteristicaIdeal;
import domain.mascota.Especie;
import domain.mascota.MascotaConChapita;
import domain.publicaciones.PublicacionAdopcion;
import domain.publicaciones.PublicacionInteresadoAdopcion;
import domain.publicaciones.RespuestaSobreMascota;
import domain.repositorios.*;
import domain.services.serviceHogares.Ubicacion;
import domain.services.serviceMail.ServicioJavaMail;
import domain.usuario.Contacto;
import domain.usuario.Cuenta;
import domain.usuario.Usuario;
import domain.usuario.documento.Dni;
import domain.usuario.documento.Documento;
import domain.validacionCaracteristicas.ValidadorBooleano;
import domain.validacionCaracteristicas.ValidadorEntreValores;
import domain.validacionContrasenia.ValidadorContrasenias;
import domain.validacionContrasenia.ValidadorListaConstrasenias;
import domain.validacionContrasenia.ValidadorLongitud;
import domain.validacionContrasenia.ValidadorNombreContrasenia;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;


public class Bootstrap implements WithGlobalEntityManager, TransactionalOps {

  Asociacion asociacion1 = new Asociacion(new Ubicacion("utn frba medrano", 140.0, 222.0));
  PreguntaSobreMascota preguntaPerro = new PreguntaSobreMascota("La mascota a adoptar es un perro?", "Desea adoptar un perro?");
  PreguntaSobreMascota preguntaGrande = new PreguntaSobreMascota("La mascota a adoptar es grande?", "Desea adoptar una mascota grande?");
  ServicioJavaMail servicioJavaMail = new ServicioJavaMail();

  Usuario usuario = new Usuario("Pepe", "Argento", "juana@gmail.com",
          "Falsa 2022", new Documento(new Dni(), "99345678"), LocalDate.of(2000, 10, 9),
          Arrays.asList(new Contacto("Juan", "Perez" , 121212, "JuanP@gmail.com")));

  Documento documento1 = new Documento(new Dni(), "12345678");
  Usuario usuarioSanchez = new Usuario("Juan", "Sanchez", "pedritosanchez@gmail.com","Falsa 2021", documento1, LocalDate.of(2000, 1, 1),
      Arrays.asList(new Contacto("Juana", "Sanchez" , 121212, "juana@gmail.com")));

  Map<String, String> caracteristicasSensiblesLola = new HashMap<String, String>();
  Map<String, String> caracteristicasSensiblesTimoteo = new HashMap<String, String>();

  {
    caracteristicasSensiblesLola.put("altura_en_centimetros", "50");
    caracteristicasSensiblesLola.put("castrado", "true");
    caracteristicasSensiblesTimoteo.put("altura_en_centimetros", "40");
    caracteristicasSensiblesTimoteo.put("castrado", "false");
  }

  MascotaConChapita lola = new MascotaConChapita("Lola","Loli", 2, false, "chiquita", Arrays.asList(Paths.get("cualquiera")),
      caracteristicasSensiblesLola, Especie.PERRO, usuario, servicioJavaMail);

  MascotaConChapita timoteo = new MascotaConChapita("timoteo","timo", 5, true, "gordito", Arrays.asList(Paths.get("cualquiera")),
      caracteristicasSensiblesTimoteo, Especie.PERRO, usuario, servicioJavaMail);


  PublicacionInteresadoAdopcion publicacionInteresadoAdopcion = this.unaPublicacionDeInteresEnAdoptar();
  PublicacionAdopcion publicacionAdopcion = this.adopcionPerroChico();
  PublicacionAdopcion publicacionAdopcion2 = this.adopcionMascotaGrande();

  public void run() {

    lola.setFoto("https://cdn.discordapp.com/attachments/834456555214078024/916393725654077490/Captura-de-pantalla-2020-07-24-a-las-17.png");
    timoteo.setFoto("https://cdn.discordapp.com/attachments/834456555214078024/916391973861068870/purina-conoce-8-razones-de-un-perro-enojado-para-que-no-las-hagas-0.png");

    asociacion1.registrarPublicacionInteresadoAdopcion(publicacionInteresadoAdopcion);
    publicacionInteresadoAdopcion.hacerVisible();
    asociacion1.registrarPublicacionAdopcion(publicacionAdopcion);
    publicacionAdopcion.hacerVisible();
    asociacion1.registrarPublicacionAdopcion(publicacionAdopcion2);
    publicacionAdopcion2.hacerVisible();

    Repositorio<Usuario> repositorioUsuario = new Repositorio<Usuario>("Usuario");
    Repositorio<Documento> repositorioDocumento = new Repositorio<Documento>("Documento");

    ValidadorListaConstrasenias validadorListaConstrasenias =  new ValidadorListaConstrasenias();
    ValidadorLongitud validadorLongitud = new ValidadorLongitud();
    ValidadorNombreContrasenia validadorNombreContrasenia = new ValidadorNombreContrasenia();

    CaracteristicaIdeal alturaEnCentimetrosentimetros = new CaracteristicaIdeal
        ("altura en centimetros", true,
            new ValidadorEntreValores(80, 30));

    CaracteristicaIdeal esta_castrado = new CaracteristicaIdeal
        ("castrado", true,
            new ValidadorBooleano());

    Cuenta cuenta1 = new Cuenta("Pepe", "contradificil1", usuario);
    cuenta1.hacerAdministrador();
    Cuenta cuenta2 = new Cuenta("Sanchez", "contradificil2", usuarioSanchez);

    withTransaction(() ->{

      ValidadorContrasenias.instance().agregarValidadores(Arrays.asList(validadorListaConstrasenias, validadorLongitud, validadorNombreContrasenia));

      RepositorioCaracteristicasIdeales.instance().agregarCaracteristicaIdeal(alturaEnCentimetrosentimetros);
      RepositorioCaracteristicasIdeales.instance().agregarCaracteristicaIdeal(esta_castrado);
      RepositorioAsociaciones.instance().agregarAsociacion(asociacion1);

      RepositorioMascotas.instance().agregarMascota(lola);
      RepositorioMascotas.instance().agregarMascota(timoteo);

      repositorioDocumento.agregar(documento1);
      repositorioUsuario.agregar(usuarioSanchez);
      repositorioUsuario.agregar(usuario);

      RepositorioCuentas.instance().agregar(cuenta1);
      RepositorioCuentas.instance().agregar(cuenta2);
    });
  }

  public PublicacionInteresadoAdopcion unaPublicacionDeInteresEnAdoptar(){

    List<RespuestaSobreMascota> preferenciasMascota = new ArrayList<RespuestaSobreMascota>();
    preferenciasMascota.add(new RespuestaSobreMascota(preguntaPerro, "true"));
    preferenciasMascota.add(new RespuestaSobreMascota(preguntaGrande, "false"));

    return new PublicacionInteresadoAdopcion(usuario, servicioJavaMail, preferenciasMascota);
  }

  public PublicacionAdopcion adopcionPerroChico(){

    List<RespuestaSobreMascota> respuestasMascota = new ArrayList<RespuestaSobreMascota>();
    respuestasMascota.add(new RespuestaSobreMascota(preguntaPerro, "true"));
    respuestasMascota.add(new RespuestaSobreMascota(preguntaGrande, "false"));

    HashMap<String, String> caracteristicas = new HashMap<String, String>();
    caracteristicas.put("altura en centimetros", "35");
    caracteristicas.put("castrado", "true");

    MascotaConChapita paquito = new MascotaConChapita("paco","paquito", 2, true, "fachero", Arrays.asList(Paths.get("cualquiera")),
        caracteristicas, Especie.PERRO, usuarioSanchez, servicioJavaMail);

    paquito.setFoto("https://t2.ea.ltmcdn.com/es/images/5/3/7/6_welsh_corgi_cardigan_y_pembroke_24735_5_600.jpg");

    return new PublicacionAdopcion(usuarioSanchez, servicioJavaMail, paquito, respuestasMascota);
  }

  public PublicacionAdopcion adopcionMascotaGrande(){

    HashMap<String, String> respuestas = new HashMap<String, String>();
    respuestas.put("grande", "true");
    List<RespuestaSobreMascota> respuestasMascota = new ArrayList<RespuestaSobreMascota>();
    respuestasMascota.add(new RespuestaSobreMascota(preguntaGrande, "true"));

    HashMap<String, String> caracteristicas = new HashMap<String, String>();
    caracteristicas.put("temperamento", "2");
    caracteristicas.put("pelo", "corto");

    MascotaConChapita pacote = new MascotaConChapita("pacote","pacote", 2, true, "fachero", Arrays.asList(Paths.get("cualquiera")),
        caracteristicas, Especie.PERRO, usuarioSanchez, servicioJavaMail);

    pacote.setFoto("https://media.discordapp.net/attachments/834456555214078024/916391973861068870/purina-conoce-8-razones-de-un-perro-enojado-para-que-no-las-hagas-0.png");

    return new PublicacionAdopcion(usuarioSanchez, servicioJavaMail, pacote,  respuestasMascota);
  }
}