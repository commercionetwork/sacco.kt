package network.commercio.sacco

fun Any.readResource(resource: String): String {
    return javaClass.classLoader?.getResource(resource)?.readText() ?: ""
}