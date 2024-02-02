package ru.netology.nmedia.api

import com.google.firebase.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.netology.nmedia.dto.Post

private const val BASE_URL = "http://10.0.2.2:9999/api/slow/"


interface PostsApiService {
    @GET("posts")
    suspend fun getAll(): Response<List<Post>>
    @GET("posts/{id}/newer")
    suspend fun getNewerCount(@Path("id") id: Long): Response<List<Post>>
    @GET("posts/{id}")
    suspend fun getById(@Path("id") id: Long): Response<Post>

    @POST("posts")
    suspend fun save(@Body post: Post): Response<Post>

    @DELETE("posts/{id}")
    suspend fun removeById(@Path("id") id: Long): Response<Unit>

    @POST("posts/{id}/likes")
    suspend fun likeById(@Path("id") id: Long): Response<Post>

    @DELETE("posts/{id}/likes")
    suspend fun dislikeById(@Path("id") id: Long): Response<Post>
}


val logger = HttpLoggingInterceptor().apply {
    //   if (BuildConfig.DEBUG) {
    level = HttpLoggingInterceptor.Level.BODY
//    }

}
val client = OkHttpClient.Builder().addInterceptor(logger).build()

val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

object PostApi {
    val retrofitService by lazy {
        retrofit.create(PostsApiService::class.java)
    }
}