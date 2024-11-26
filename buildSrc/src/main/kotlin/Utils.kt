object Utils {

    val isInCI: Boolean
        get() = System.getenv()["CI"].equals("true", ignoreCase = true)

    val isOnLinux: Boolean
        get() = System.getProperty("os.name").contains("linux", ignoreCase = true)

    fun normally(todo: () -> Unit): Normally = Normally(todo)

    class Normally(private val normallyBlock: () -> Unit) {
        infix fun exceptOn(condition: () -> Boolean): Conditionally = Conditionally(normallyBlock, condition)
    }

    class Conditionally(private val normallyBlock: () -> Unit, private val condition: () -> Boolean) {
        infix fun where(exceptionalBlock: () -> Unit) =
            if (condition()) {
                exceptionalBlock()
            } else {
                normallyBlock()
            }
    }
}
