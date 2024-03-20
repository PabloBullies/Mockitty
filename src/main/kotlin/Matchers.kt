import core.matching.match


inline fun <reified T> any(): T = match { true }

inline fun <reified T> eq(value: T): T = match { it == value }

