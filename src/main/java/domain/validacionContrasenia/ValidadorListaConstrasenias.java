package domain.validacionContrasenia;

import domain.excepciones.exepcionesCuenta.ExcepcionContrasenia;
import domain.excepciones.exepcionesCuenta.ExcepcionListaContrasenia;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("LC")
public class ValidadorListaConstrasenias extends Validaciones {

  @Transient
  List<String> listaConstrasenias = leerArchivo("passwordlist.txt");

  public static List<String> leerArchivo(String archivo) {

    //List<String> lineas = Collections.emptyList();
    //TODO esto va a romper a futuro, hay que cambiar por classPath
    Path pathArchivo = Paths.get("src/main/java/domain/validacionContrasenia").toAbsolutePath().resolve(archivo);
    try {
      //lineas = Files.readAllLines(pathArchivo, StandardCharsets.UTF_8);
      return Files.readAllLines(pathArchivo, StandardCharsets.UTF_8);
    } catch (IOException e) {
      //e.printStackTrace();
      throw new ExcepcionContrasenia("contrasenia dentro de las contrasenias faciles");
    }
    //return lineas;
  }

  @Override
  public void validarAtributo(String contrasenia, String nombreUsuario) {

    if (this.listaConstrasenias.contains(contrasenia)) {

      throw new ExcepcionContrasenia("La contrasenia no es segura");
    }
  }
}