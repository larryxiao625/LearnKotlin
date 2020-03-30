package com.example.core.http

import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.lang.reflect.Type

class HttpClient : OkHttpClient() {
    companion object {
        val INSTANCE by lazy {
            HttpClient()
        }
    }

    private val gson = Gson()

    private fun <T> convert(json: String?, type: Type): T {
        return gson.fromJson(json, type)
    }

    operator fun <T> get(path: String, type: Type, entityCallback: EntityCallback<T>) {
        val request = Request.Builder()
                .url("https://api.hencoder.com/$path")
                .build()
        val call = INSTANCE.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                entityCallback.onFailure("网络异常")
            }

            @Suppress("UNCHECKED_CAST")
            override fun onResponse(call: Call, response: Response) {
                val code = response.code()
                when (code) {
                    in 200..299 -> {
                        val body = response.body()
                        var json: String? = null
                        json = body?.string()
                        entityCallback.onSuccess(convert<Any>(json, type) as T)
                    }
                    in 400..499 -> entityCallback.onFailure("客户端错误")

                    in 500..599 -> entityCallback.onFailure("服务器错误")

                    else -> entityCallback.onFailure("未知错误")

                }
            }
        })
    }
}