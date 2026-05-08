package com.joseantonio.norte.lopez.cuadragastosapp.auth

class TokenHolder {
    private val refreshing = java.util.concurrent.atomic.AtomicBoolean(false)

    fun isRefreshing(): Boolean = refreshing.get()

    fun startRefreshing(): Boolean {
        return refreshing.compareAndSet(false, true)
    }

    fun finishRefreshing() {
        refreshing.set(false)
    }
}