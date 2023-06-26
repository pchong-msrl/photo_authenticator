package com.android.example.cameraxapp

import android.content.ContentValues
import android.provider.MediaStore
import org.junit.Test
import org.junit.Assert.*
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivityTest {

    @Test
    fun generateContentValuesTest() {

        var FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        // Arrange
        val mainActivity = MainActivity()
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val expectedContentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/CameraX App")
        }
        // Act
        val resultContentValues = mainActivity.generateContentValues(name)

        // Assert
        assertEquals(expectedContentValues, resultContentValues)
    }


}
