package ru.netology

data class Likes(val likes: Int = 0)

data class Comments(
    var id: Int = 0,
    var from_id: Int = 0,
    var date: Int = 0,
    var test: String = ""
)

class PostNotFoundException(message: String) : RuntimeException(message)

data class Post(
    var postId: Int,
    var dateOfPublished: String = "07/21/2020",
    var title: String = "Запись на стене | Разработчикам",
    var page: Int,
    var listOnTheLeft: String,
    var nameFieldOfTheList: String = "Запись на стене",
    var descriptionFieldOfTheList: String = "Объект, описывающий запись на стене пользователя или сообщества, содержит следующие поля:",
    var fieldId: Int, //id самой табличной записи, не поста
    var fieldName: String?,
    var fieldTypeDescription: String,
    var fieldType: String = "",
    var likes: Likes = Likes(),
    var attachment: Array<Attachment>? = emptyArray(),
    var comments: Array<Comments> = emptyArray()
)

object WallService {
    private var posts = emptyArray<Post>() // массив для хранения постов
    private var lastPostId = 0

    private var comments = emptyArray<Comments>() //массив для хранения комментариев к посту
    private var lastCommentsId = 1


    fun add(post: Post): Post {
        posts += post.copy(++lastPostId, likes = post.likes.copy()) //добавляет пост в массив
        return posts.last() // возращает последний пост
    }

    fun ubdate(newPost: Post): Boolean {
        for ((index, post) in posts.withIndex()) {
            if (post.postId == newPost.postId) {
                posts[index] = newPost.copy(likes = post.likes.copy())
                return true
            }
        }
        return false
    }

    fun printPosts() {
        for (post in posts) {
            print(post)
            println(" ")
            println()
        }

    }

    fun clear() {
        posts = emptyArray()
        lastPostId = 0
    }

    fun createComment(postId: Int, comment: Comments): Comments {

        for ((index, post) in posts.withIndex()) {
            if (post.postId == postId) {
                comments += comment.copy(id = lastCommentsId++)
                posts[index] = post.copy(comments = post.comments + comments.last())
                return comments.last()
            }
        }
        throw PostNotFoundException("Поста с id $postId нет!")
    }
}


interface Attachment {
    val type: String
}

data class Photo(
    var id: Int,
    var owner_id: Int,
    var photo_130: String,
    var photo_604: String
)

data class Video(
    var id: Int,
    var owner_id: Int,
    var title: String,
    var duration: Int
)

data class Audio(
    var id: Int,
    var owner_id: Int,
    var artist: String,
    var title: String
)

data class File(
    var id: Int,
    var owner_id: Int,
    var title: String,
    var size: Int
)

data class Gift(
    var id: Int,
    var thumb_256: String,
    var thumb_96: String,
    var thumb_48: String
)

class PhotoAttachment(val photo: Photo) : Attachment {
    override val type = "Photo"
}

class VideoAttachment(val video: Video) : Attachment {
    override val type = "Video"
}

class AudioAttachment(val audio: Audio) : Attachment {
    override val type = "Audio"
}

class FileAttachment(val file: File) : Attachment {
    override val type = "File"
}

class GiftAttachment(val gift: Gift) : Attachment {
    override val type = "Gift"
}


fun main() {

    WallService.add(
        Post(
            postId = 0,
            page = 1,
            listOnTheLeft = "Документация",
            fieldId = 0,
            fieldName = "Id",
            fieldTypeDescription = "Идентификатор записи.",
            likes = Likes(10)
        )
    )

    WallService.add(
        Post(
            postId = 0,
            page = 1,
            listOnTheLeft = "Документация",
            fieldId = 1,
            fieldName = "text",
            fieldTypeDescription = "Текст записи."
        )
    )

    println(
        WallService.ubdate(
            Post(
                postId = 1,
                page = 1,
                listOnTheLeft = "Документация",
                fieldId = 1,
                fieldName = "NEW text",
                fieldTypeDescription = "Текст записи."
            )
        )
    )

    //WallService.printPosts()

    WallService.add(
        Post(
            postId = 3,
            page = 1,
            listOnTheLeft = "Документация",
            fieldId = 1,
            fieldName = null,
            fieldTypeDescription = "Текст записи."
        )
    )
    //WallService.printPosts()

    WallService.add(
        Post(
            postId = 4,
            page = 1,
            listOnTheLeft = "Документация",
            fieldId = 1,
            fieldName = null,
            fieldTypeDescription = "Фото",
            attachment = arrayOf(
                PhotoAttachment(Photo(0, 555, "url_1", "url_1")),
                VideoAttachment(Video(1, 9847, "Просто видео", 60)),
                AudioAttachment(Audio(10, 1047, "Аудиозапись", "Happy new year")),
                FileAttachment(File(207, 6584, "Текстовый документ", 217)),
                GiftAttachment(Gift(2000, "url_1", "url_2", "url_3"))
            )
        )
    )

    WallService.createComment(4, Comments(0, 1051, 160124, "Первый комментарий"))
    WallService.createComment(4, Comments(0, 2541, 170124, "Второй комментарий"))

    WallService.printPosts()
}



