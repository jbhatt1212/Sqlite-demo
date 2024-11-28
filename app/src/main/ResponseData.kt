data class ResponseData(
    val limit: Int,
    val posts: List<Post>,
    val skip: Int,
    val total: Int
)