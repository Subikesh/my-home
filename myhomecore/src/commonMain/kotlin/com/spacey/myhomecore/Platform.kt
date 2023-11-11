package com.spacey.myhomecore

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform