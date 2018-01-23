package com.z7dream.apm.base.utils.net

import android.text.TextUtils
import android.util.Log
import com.z7dream.apm.base.api.BaseNetConfig
import com.z7dream.apm.base.mvp.model.BaseEntity
import com.z7dream.apm.base.utils.exception.ApiException
import com.google.gson.Gson
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException
import java.util.concurrent.TimeUnit

/**
 * Created by Z7Dream on 2017/9/7 14:36.
 * Email:zhangxyfs@126.com
 */
open class OKHTTP {
    private var mHttpClient: OkHttpClient? = null;
    private var mRetrofit: Retrofit? = null;
    private var mBaseUrl: String? = null;

    val HTTP_CONNECTION_TIMEOUT = 15 * 1000


    /**
     * 初始化
     * @param headMap 请求时默认head信息
     * @param baseUrl 请求路径基础url（末尾不要加'/'）
     */
    fun init(headMap: HashMap<String, String>?, baseUrl: String?): OKHTTP {
        if (!TextUtils.equals(baseUrl, mBaseUrl)) {
            mHttpClient = newBuilder()
                    .addInterceptor(createCodeInterceptor())
                    .addNetworkInterceptor(createHeadInterceptor(headMap))//设置网络拦截器
                    .authenticator(createAuthenticator()).build()

            val mBuilder = Retrofit.Builder()
                    .client(mHttpClient);
            if (TextUtils.isEmpty(baseUrl))
                mBuilder.baseUrl(NetConfig.SERVER_ADD + "/");
            else
                mBuilder.baseUrl(baseUrl + "/")
            mRetrofit = mBuilder
                    .addConverterFactory(GsonConverterFactory.create())//json转换器
                    .addConverterFactory(ScalarsConverterFactory.create())//字符串转换器
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//RxJavaCallAdapterFactory
                    .build()
            mBaseUrl = baseUrl;
        }
        return this
    }

    //log 拦截器
    private fun createLogInterceptor(): HttpLoggingInterceptor {
        val logInterceptor = HttpLoggingInterceptor()
        if (DEBUG) {
            logInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        return logInterceptor
    }

    //添加head 的拦截器
    private fun createHeadInterceptor(headMap: HashMap<String, String>?) = Interceptor { chain ->
        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder()

        if (headMap != null && headMap.size > 0) {
            for (key: String in headMap.keys) {
                builder.addHeader(key, headMap.get(key))
                if (DEBUG) {
                    Log.d(TAG, key + "=" + headMap.get(key))
                }
            }
        }
        val authorised = builder.build()
        chain.proceed(authorised)
    }

    //code拦截器
    private fun createCodeInterceptor() = Interceptor { chain ->
        val response = chain.proceed(chain.request())
        val responseBody = response.body()
        val UTF8 = Charset.forName("UTF-8")
        if (DEBUG) {
            Log.d(TAG, response.request().url().toString() + " " + response.code())
        }
        var message = ""

        if (responseBody != null && response.code() != 200) {
            if (response.code() == 500) {
                message = "请求错误"
            } else if (response.code() == 404) {
                message = "请求地址错误"
            }
            httpCodeInterceptor(responseBody, UTF8, response, message)
        }
        response
    }

    //身份验证拦截器 如果得到401 Not Authorized未授权的错误
    private fun createAuthenticator() = Authenticator { _, response ->
        if (!response.isSuccessful) {
            throw ApiException(response.code().toString(), "请求错误")
        }
        if (DEBUG) Log.d(TAG, response.toString());
        null
    }

    /**
     * 初始化okhttp client
     */
    fun newBuilder(): OkHttpClient.Builder = OkHttpClient().newBuilder()
            .readTimeout(HTTP_CONNECTION_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
            .connectTimeout(HTTP_CONNECTION_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
            .writeTimeout(HTTP_CONNECTION_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(createLogInterceptor())//设置应用拦截器，主要用于设置公共参数，头信息，日志拦截等

    @Throws(IOException::class)
    private fun httpCodeInterceptor(responseBody: ResponseBody, UTF8: Charset, response: Response, msg1: String) {
        var msg = msg1
        val source = responseBody.source()
        source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
        val buffer = source.buffer()

        val charset: Charset?
        val contentType = responseBody.contentType()
        if (contentType != null) {
            try {
                charset = contentType.charset(UTF8)
            } catch (e: UnsupportedCharsetException) {
                throw ApiException(response.code().toString(), response.message())
            }

            if (responseBody.contentLength() != 0L) {
                val result = Gson().fromJson(buffer.clone().readString(charset!!), BaseEntity::class.java)
                if (result != null && !TextUtils.isEmpty(result.result)) {
                    msg = result.result!!
                }
                throw ApiException(response.code().toString(), msg)
            }
        }
        throw ApiException(response.code().toString(), response.message())
    }


    fun getRetrofit(): Retrofit {
        if (mRetrofit == null) {
            throw ExceptionInInitializerError("please call init() in your Application.")
        } else {
            return mRetrofit!!;
        }
    }

    fun setDebug(isDebug: Boolean): OKHTTP {
        DEBUG = isDebug;
        return this;
    }

    companion object {
        private val TAG = OKHTTP::class.java.simpleName//::class.java 反射
        private var DEBUG = BaseNetConfig.isLocal

        fun get(): OKHTTP = OKHTTPHolder.instance

        private object OKHTTPHolder {
            val instance = OKHTTP()
        }
    }


}