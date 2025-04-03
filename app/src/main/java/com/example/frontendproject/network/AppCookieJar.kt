package com.example.frontendproject.network

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

object AppCookieJar : CookieJar {
    private val cookieStore: MutableMap<String, List<Cookie>> = mutableMapOf()

    override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
        cookieStore[url.host()] = cookies
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieStore[url.host()] ?: listOf()
    }
}
