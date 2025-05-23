package com.arsansys.RemaPartners.services.retrofit;

import java.util.List;

import com.arsansys.RemaPartners.models.entities.UserEntity;
import com.arsansys.RemaPartners.models.firebase.Note;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Interfaz para definir los métodos de comunicación con la API REST usando
 * Retrofit.
 */
public interface RetrofitService {

    /**
     * URL base de la API REST.
     */
    final String BASE_URL = "http://localhost:8080/"; // URL de la API REST

    /**
     * Obtiene la lista de usuarios (clientes) desde la API REST.
     *
     * @return Llamada Retrofit que retorna una lista de entidades UserEntity.
     */
    @GET(BASE_URL + "getUsers")
    Call<List<UserEntity>> getClientes();

    /**
     * Envía una notificación a través de la API REST.
     *
     * @param note Objeto Note que contiene la información de la notificación.
     * @return Llamada Retrofit que retorna void.
     */
    @POST(BASE_URL + "sendNotification")
    Call<Void> sendNotification(Note note);

}
