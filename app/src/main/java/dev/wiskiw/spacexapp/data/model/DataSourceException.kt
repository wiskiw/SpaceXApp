package dev.wiskiw.spacexapp.data.model

class DataSourceException : Exception {

    constructor(message: String?) : super(message)
    constructor(cause: Throwable?) : super(cause)
}
