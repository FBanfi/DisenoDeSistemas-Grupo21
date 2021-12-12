package domain.services.serviceHogares;

import domain.excepciones.excepcionesRegistro.ExcepcionRegistro;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.persistence.Entity;
import java.io.IOException;

public class ServicioHogares {

  private static ServicioHogares instancia = null;
  private static final String urlApi = "https://api.refugiosdds.com.ar/api/";
  private final Retrofit retrofit;
  private static final String token = "Bearer wVJi1lXTjEfQq3h9OWeIObbkfxYyoEGutjr7p6BbWS8S82Cx2T2bHhYQaUnx";

  public ServicioHogares() {
    this.retrofit = new Retrofit.Builder()
        .baseUrl(urlApi)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
  }

  public static ServicioHogares instancia(){
    if(instancia== null){
      instancia = new ServicioHogares();
    }
    return instancia;
  }

  public ListadoHogares listadoHogares() {

    try{
      ApiHogaresService apiHogaresService = this.retrofit.create(ApiHogaresService.class);
      Call<ListadoHogares> requestHogares = apiHogaresService.hogares(1, this.token); // TODO Ver como traer toda la lista de hogares
      Response<ListadoHogares> responseHogares = requestHogares.execute();
      return responseHogares.body();
    }catch (IOException e){
      throw new ExcepcionRegistro("Hubo un problema de conexion al servidor" );
    } catch (RuntimeException e){
      throw new ExcepcionRegistro("Hubo un problema inesperado o decodificando la respuesta" );
    }

  }



  public TokenAPI obtenerToken(Email email) {

    try{
      ApiHogaresService apiHogaresService = this.retrofit.create(ApiHogaresService.class);
      Call<TokenAPI> requestToken = apiHogaresService.usuarios(email);
      Response<TokenAPI> responseToken = requestToken.execute();
      return responseToken.body();
    } catch (IOException e){
        throw new ExcepcionRegistro("Hubo un problema de conexion al servidor" );
    } catch (RuntimeException e){
        throw new ExcepcionRegistro("Hubo un problema inesperado o decodificando la respuesta" );
    }
  }
}
