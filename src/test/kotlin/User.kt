open class User (private val username: String){
    open fun hello() {
        println("Hello!")
    }
    open fun add(x: Int, y: Int): Int = x + y
    open fun getName(): String = username
}