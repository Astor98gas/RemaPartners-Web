package com.arsansys.RemaPartners.services.retrofit;

import java.util.List;

import com.arsansys.RemaPartners.models.entities.UserEntity;
import com.arsansys.RemaPartners.models.firebase.Note;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitService {

    final String BASE_URL = "http://localhost:8080/"; // URL de la API REST

    // Métodos para realizar las peticiones a la API REST

    @GET(BASE_URL + "getUsers")
    Call<List<UserEntity>> getClientes();

    @POST(BASE_URL + "sendNotification")
    Call<Void> sendNotification(Note note);

}
