package core

fun getDefaultValue(clazz: Class<*>): Any? {
    return when (clazz.simpleName) {
        "boolean", "Boolean" -> false
        "byte", "char", "short", "int", "long", "float", "double" -> 0
        "Byte", "Character", "Short", "Integer", "Long", "Float", "Double" -> 0
        else -> {
            return null
        }
    }
}