package com.proficiency.assingment.api
import android.app.Application
import com.proficiency.assingment.util.Constants
import com.proficiency.assingment.util.Constants.Companion.BASE_URL
import com.proficiency.assingment.util.Utils
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class RetrofitInstance() {

    companion object {
        private lateinit var application: Application
        fun setContext(application: Application) {
            this.application = application
        }

        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val httpCacheDirectory = File(application.getCacheDir(), Constants.CACHE_DIR)
            val cache = Cache(httpCacheDirectory, 10 * 1024 * 1024)
            val client = OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor { chain ->
                    var request = chain.request()
                    request = if (Utils.hasInternetConnection(application))
                        request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
                    else
                        request.newBuilder().header(
                            "Cache-Control",
                            "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                        ).build()
                    chain.proceed(request)
                }
                .build()
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

        }
        val api by lazy {
            retrofit.create(ApiInterface::class.java)
        }

    }
}