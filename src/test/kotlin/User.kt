open class User(private val username: String) {
    private val friendList: ArrayList<User> = ArrayList()

    open fun hello() {
        println("Hello!")
    }

    open fun add(x: Int, y: Int): Int = x + y
    open fun getName(): String = username
    open fun addFriend(friend: User): Boolean {
        friendList.add(friend)
        return friend.friendList.contains(this)
    }

    companion object {
        fun staticMethod(): String {
            println("STATIC")
            return "STATIC"
        }
    }
}