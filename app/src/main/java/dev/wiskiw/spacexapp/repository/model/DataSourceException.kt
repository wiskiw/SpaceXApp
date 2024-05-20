package dev.wiskiw.spacexapp.repository.model

class DataSourceException : Exception {

    constructor(message: String?) : super(message)
    constructor(cause: Throwable?) : super(cause)
}
