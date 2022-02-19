package com.example.petuniverse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.petuniverse.Interfaces.RetrofitInstance.PetfinderApi
import com.example.petuniverse.data.AccessToken
import com.example.petuniverse.data.TokenRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    val GRANT_TYPE = "client_credentials"
    val CLIENT_ID = "1PM6F4qoYIlDKWXh5IJjaak3NvJ4QzE1IJywYK8qoVl7IA9HRI"
    val CLIENT_SECRET = "F6VPL7ZIjQGmbeHMoRuSc5KnJ9zH14nwRT8FM0lg"
    private lateinit var searchEditText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchEditText = findViewById(R.id.search_edittext)
        searchEditText.setOnClickListener {
            val tokenRequest = TokenRequest(GRANT_TYPE,CLIENT_ID,CLIENT_SECRET)
            val call: Call<AccessToken?>? = PetfinderApi.getAccessToken(tokenRequest)
            call?.enqueue(object :Callback<AccessToken?>{
                override fun onResponse(
                    call: Call<AccessToken?>,
                    response: Response<AccessToken?>
                ) {
                    searchEditText.setText(response.body()?.expires_in.toString())
                }

                override fun onFailure(call: Call<AccessToken?>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}