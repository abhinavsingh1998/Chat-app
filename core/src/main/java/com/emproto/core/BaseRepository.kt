package com.emproto.core

import android.app.Application

abstract class BaseRepository{

    open var application: Application? = null

    open fun BaseRepository(application: Application?) {
        this.application = application
    }
}
