package domain.services.serviceHogares;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiHogaresService {

  @GET("hogares")
  Call<ListadoHogares> hogares(@Query("offset") int offset, @Header("Authorization") String token);

  @POST("usuarios")
  Call<TokenAPI> usuarios(@Body Email email);
}

//Bearer wVJi1lXTjEfQq3h9OWeIObbkfxYyoEGutjr7p6BbWS8S82Cx2T2bHhYQaUnx
