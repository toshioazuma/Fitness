package com.onrushers.app.file

class Downloader private constructor() {

    private object Holder {
        val downloader = Downloader()
    }

    companion object {
        val instance: Downloader by lazy { Holder.downloader }
    }

    private var accessToken: String = ""
    private var baseUrl: String = ""

    fun setTemplate(template: String) {
        baseUrl = template.substring(0.rangeTo(template.indexOf("?") - 1))
    }

    fun setAccessToken(token: String) {
        accessToken = token
        println("token= $token")
    }

    fun resourceUrl(id: Int): String {
        return resourceUrl(id.toString())
    }

    fun resourceUrl(id: String): String {
        return "$baseUrl?id=$id&access_token=$accessToken"
    }
}