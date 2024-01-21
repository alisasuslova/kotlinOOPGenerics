package ru.netology

data class Likes(val likes: Int = 0)

data class Comments(
    var commentId: Int = 0,
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

data class Notes(
    var note_id: Int = 0,
    var title: String = "",
    var text: String = "",
    var data: Int,
    var comments: Array<Comments> = emptyArray(),
    var commentsCount: Int = 0, //кол-во комментариев
    var read_comments: Int = 0, //кол-во прочитанных комментариев
    var view_url: String = "",
    var privacy: Int, // Уровень доступа к заметке. Возможные значения: 0 - все пользователи, 1 -только друзья, 2 -  друзья и друзья друзей, 3 -только пользователь
    var comment_privacy: Int, // Уровень доступа к комментариям в заметке. Возможные значения: 0 - все пользователи, 1 -только друзья, 2 -  друзья и друзья друзей, 3 -только пользователь
    var privacy_view: String = "",
    var privacy_comment: String = ""
    /*var privacy_comment: Boolean = false,
    var user_id: Int = 0,
    var count: Int = 20, //до 100!
    var sort : Boolean = false // 0 по убыванию, 1 - по возрастанию*/
)

object WallService {
    private var posts = emptyArray<Post>() // массив для хранения постов
    private var lastPostId = 0

    private var comments = emptyArray<Comments>() //массив для хранения комментариев к посту
    private var lastCommentsId = 1

    private var notes = emptyArray<Notes>() //массив для хранения заметок
    private var lastNotesId = 0

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

    fun printNotes() {
        for (note in notes) {
            print(note)
            println(" ")
            println()
        }
    }

    fun clear() {
        posts = emptyArray()
        lastPostId = 0
    }

    fun clearNotes() {
        notes = emptyArray()
        lastNotesId = 0
    }

    fun createComment(postId: Int, comment: Comments): Comments {

        for ((index, post) in posts.withIndex()) {
            if (post.postId == postId) {
                comments += comment.copy(commentId = lastCommentsId++)
                posts[index] = post.copy(comments = post.comments + comments.last())
                return comments.last()
            }
        }
        throw PostNotFoundException("Поста с id $postId нет!")
    }

    //------------------------------------------------------------------------------
    fun addNote(note: Notes): Int {
        notes += note.copy(++lastNotesId) //добавляет заметку в массив
        if (notes.isNotEmpty() && notes.last().note_id == lastNotesId) {
            return notes.last().note_id
        }
        throw PostNotFoundException(
            "\n" +
                    "Произошла неизвестная ошибка. Заметка не была создана."
        )
    }

    fun notesCreateComment(note_id: Int, comment: Comments): Int {
        for ((index, note) in notes.withIndex()) {
            if (note.note_id == note_id) {
                comments += comment.copy(commentId = lastCommentsId++)
                notes[index] = note.copy(comments = note.comments + comments.last())
                return comments.last().commentId
            }
        }
        throw PostNotFoundException("Заметки с таким id $note_id нет!")
    }

    fun deleteNote(note_id: Int): Int {
        val arrList = notes.toMutableList()
        arrList.removeAt(note_id - 1)
        notes = arrList.toTypedArray()
        if (notes.size > 0) {
            return 1
        }
        throw IndexOutOfBoundsException("Note not found")
    }

    //ToDo
    fun notesDeleteComment(note_id: Int, comment_id: Int): Int {
        for (note in notes) {
            if (note.note_id == note_id) {
                for (comment in note.comments) {
                    if (comment.commentId == comment_id) {
                        val arrList = comments.toMutableList()
                        arrList.removeAt(comment_id) //????
                        comments = arrList.toTypedArray()
                        if (comments.size > 0) {
                            return 1
                        }
                    }
                }
            }
        }
        throw IndexOutOfBoundsException("Note not found")
    }

    fun editNote(note_id: Int,
                 title: String,
                 text: String,
                 privacy: Int,
                 comment_privacy: Int,
                 privacy_view: String,
                 privacy_comment: String): Int {

        for ((index, note) in notes.withIndex()) {
            if (note.note_id == note_id) {

                notes[index] = note.copy(title = title,
                    text = text,
                    privacy = privacy,
                    comment_privacy = comment_privacy,
                    privacy_view = privacy_view,
                    privacy_comment = privacy_comment
                )
                return 1
            }
        }
        throw IndexOutOfBoundsException("Заметка с id $note_id не найдена!")
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

            println(
                WallService.addNote(
                    Notes(
                        0,
                        "Заметка первая",
                        "Какой-то текст",
                        210124,
                        commentsCount = 0,
                        read_comments = 0,
                        view_url = "URL_1",
                        privacy = 2,
                        comment_privacy = 2
                    )
                )
            )

            WallService.printNotes()

            WallService.notesCreateComment(
                1,
                Comments(0, 0, 11111, "Комментарий №1")
            )
            WallService.notesCreateComment(
                1,
                Comments(0, 0, 22222, "Комментарий №2")
            )
            WallService.notesCreateComment(
                1,
                Comments(100500, 0, 22222, "Комментарий №3")
            )
            WallService.printNotes()


            println(
                WallService.addNote(
                    Notes(
                        1,
                        "Заметка вторая",
                        "Приветствие",
                        220124,
                        commentsCount = 1,
                        read_comments = 1,
                        view_url = "URL_2",
                        privacy = 3,
                        comment_privacy = 3
                    )
                )
            )

            println(
                WallService.notesCreateComment(
                    2,
                    Comments(0, 0, 3333, "Комментарий №1.1")
                )
            )
            println(
                WallService.notesCreateComment(
                    2,
                    Comments(0, 0, 44, "Комментарий №2.2")
                )
            )
            WallService.printNotes()
            println("------------------------------")
            WallService.deleteNote(1)
            //WallService.deleteNote(1) // Note not found
            WallService.printNotes()

            println(WallService.notesDeleteComment(2,6)) //не работает!!!
            WallService.printNotes()

            println(WallService.editNote(2, "Изменение заметки",  "ТЕСТОВЫЙ ТЕКСТ!!!!", 2, 2, "www", "qqq"))
            WallService.printNotes()


        }


