open class User (private val username: String){
    fun hello() {
        println("Hello!")
    }
    fun add(x: Int, y: Int): Int = x + y
}