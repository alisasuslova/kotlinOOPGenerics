package ru.netology

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class WallServiceTest {

    @Before
    fun clearBeforeTest() {
        WallService.clear()
    }

    @Test
    fun add() {

        val result = WallService.add(
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
        assertEquals(1, result.postId)
    }

    @Test
    fun ubdateTrue() {

        val firstPost = WallService.add(
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

        val result = WallService.ubdate(
            Post(
                postId = 1,
                page = 1,
                listOnTheLeft = "Документация",
                fieldId = 1,
                fieldName = "NEW text",
                fieldTypeDescription = "Текст записи."
            )
        )
        assertTrue(result)
    }

    @Test
    fun ubdateFalse() {

        val result = WallService.ubdate(
            Post(
                postId = 100,
                page = 1,
                listOnTheLeft = "Документация",
                fieldId = 0,
                fieldName = "Id",
                fieldTypeDescription = "NEW",
                likes = Likes(10)
            )
        )
        assertFalse(result)
    }

    @Test
    fun createCommentTrue() {

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

        val testComment = Comments(1, 1051, 160124, "Первый комментарий")

        var result = WallService.createComment(1, testComment)

        assertEquals(testComment, result)
    }


    @Test(expected = PostNotFoundException::class)
    fun shouldThrow() {
        // здесь код с вызовом функции, которая должна выкинуть PostNotFoundException
        val result = WallService.createComment(4, Comments(0, 1051, 160124, "Первый комментарий"))
    }
}