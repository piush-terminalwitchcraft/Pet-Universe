package com.example.petuniverse.Interfaces

import com.example.petuniverse.data.AccessToken
import com.example.petuniverse.data.TokenRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface TodoInterface {

//    @GET("/todos")
//    suspend fun getTodos(@Header("Authorization") auth:String,):Response<List<TodoItem>>
//
//
//    // "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxUE02RjRxb1lJbERLV1hoNUlKamFhazNOdko0UXpFMUlKeXdZSzhxb1ZsN0lBOUhSSSIsImp0aSI6ImU3YjVmMWE5MWM1OTBmNzU2NmNlOGEwNjRhNDRhMmY1N2VmMzEzMWMyNjI0MmY3MWYyOTUxOTQ5YmY5YzNmMGExMmJlYTZhZWNmZjM5OTYzIiwiaWF0IjoxNjQ1MjEwNzg4LCJuYmYiOjE2NDUyMTA3ODgsImV4cCI6MTY0NTIxNDM4OCwic3ViIjoiIiwic2NvcGVzIjpbXX0.YOVI9IOKNMhpvpJ6V1zhAmhYWdAmpSbTrUZp28uF-bCKJW91l48wU0xXIx4fKADrkjjjNjkpNdz4LC422kUFh_yANegT_F4YSR-GRiSyJZqv8B3FLt-kcl3UnyzZJN6vPl5swcRMyomtnAvRKZzI6CsmEKwCtj3qdAVm-fCn4X14sisZgluqJMMj2lo5cG8cSHTUVBb9hBJ1GXXRwl62D3LK2qH6NTWL8npEEqTnvaervfUes1ttDochm6nufM0onYF7OGFtSromLv_ghd6ArBrart7ajJLU-eyUb1q7GRCGZG68bHo9zmgaKqqJSwbZqFNvtsvsEqO6mTvfmgsoBA"
//    //
//    @POST("/createtodos")
//    suspend fun postTodos(@Body todoItem: TodoItem):Response<TodoItem>

    @POST("oauth2/token")
    fun getAccessToken(@Body tokenRequest: TokenRequest?): Call<AccessToken?>?

//    @Header("Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxUE02RjRxb1lJbERLV1hoNUlKamFhazNOdko0UXpFMUlKeXdZSzhxb1ZsN0lBOUhSSSIsImp0aSI6Ijk0ZGExZjBmMzJlNWU1OWIxMWYyOTJjZGU5MzI4NmM2ZjRkMWE4YWNlYTk5MTQxZWVhODEwNzg0MmEyYzhiNzJkMjJiMzRkZjEzYWI5NWY3IiwiaWF0IjoxNjQ1MjQxNzY1LCJuYmYiOjE2NDUyNDE3NjUsImV4cCI6MTY0NTI0NTM2NSwic3ViIjoiIiwic2NvcGVzIjpbXX0.CI738dw0q4v-BuKsamuMalmW1BpFHy7xrTR4mB-AVrYBvRH7w5cmTj5aG5FohAmvkoGPE8PVCXesG7M7v1zfPLLH9r3b1iiqDwfgTKntPmXAVG2i31w6ZiKiKRXAMZ2l2-EiaVr3Uh0uYPRu0E8xTVeyrZTkHorwhTlRQb5Z-iejlt2F1lZdh3S7pl2NHEKNNkY7l17Xy_flN5H4H1xSnNVETp4WI2N73NVuRZlFwT7MaHlGAgvfoZsvz8iuJLP_VyvhmJaYx8ld7K3BNBvFrRnYi3JAbUO65IkulNEd2Lrz64QF_HCUQ94dI5yOmYZ3RE_HNUnUUxBLQIIAvOLqRQ")
//    @GET("")
//    fun getData()
}