package com.android.example.cameraxapp

import android.content.ContentValues
import android.provider.MediaStore
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mockito.*
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivityTest {

    @Test
    fun takePhoto() {
        // Arrange
        val imageCapture = mock(ImageCapture::class.java) // Assuming ImageCapture is a class you've defined.
        val mainActivity = MainActivity().apply {
            this.imageCapture = imageCapture
        }
        val name = SimpleDateFormat(MainActivity.FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val expectedContentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }

        // Act
        mainActivity.takePhoto()

        // Assert
        verify(imageCapture, times(1)).takePicture(any(), any(), any())
        // Assuming the `takePicture` method is called within your `takePhoto()` method.
        // Note: Make sure to customize the verify assertions based on your actual code.
        assertEquals(expectedContentValues, mainActivity.contentValues)
    }
}
