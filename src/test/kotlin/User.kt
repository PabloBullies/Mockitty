open class User(private val username: String) {
    private val friendList: ArrayList<User> = ArrayList()

    open fun hello() {
        println("Hello, $username!")
    }

    open fun add(x: Int, y: Int): Int = x + y
    open fun getName(): String = username
    open fun addFriend(friend: User): Boolean {
        friendList.add(friend)
        return friend.friendList.contains(this)
    }
    open fun readFriendList(){
        println("${getName()}'s FriendList: ")
        for (friend in friendList){
            println(friend.getName())
        }
    }

    companion object {
        fun staticMethod(): String {
            return "STATIC"
        }
        fun countAge(year: Int): Int{
            return 2024 - year
        }
    }
}