class MockittyCore {
    private var createdObject = ArrayList<String>()
    fun <T> mock(classToMock: Class<T>): T {
        //TODO: mock
        return createdObject as T
    }

    fun every(block: Any) {
        //TODO: every
    }

    fun returns(block: () -> Any?) {
        //TODO: returns
        createdObject.add("1")
    }

}