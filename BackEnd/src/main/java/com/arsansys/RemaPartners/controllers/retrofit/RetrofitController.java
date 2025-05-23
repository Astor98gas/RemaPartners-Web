package com.arsansys.RemaPartners.controllers.retrofit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Utilidad para obtener instancias de Retrofit configuradas.
 */
public class RetrofitController {

    /**
     * Obtiene un cliente Retrofit configurado con la URL base proporcionada.
     *
     * @param baseUrl URL base para las peticiones.
     * @return Instancia de Retrofit configurada.
     */
    public static Retrofit getRetrofitClient(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(new OkHttpClient.Builder().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
