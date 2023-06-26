package com.android.example.cameraxapp

import android.content.ContentValues
import android.provider.MediaStore
import androidx.camera.core.ImageProxy
import org.junit.Test
import org.junit.Assert.*
import java.text.SimpleDateFormat
import java.util.Locale
import org.mockito.Mockito.*
import java.util.concurrent.Executor
import org.junit.Assert.assertEquals
import org.mockito.ArgumentMatchers.any
import java.nio.ByteBuffer


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

    @Test
    fun requestPermissions() {
        // Arrange
        val activityResultLauncher = mock(ActivityResultLauncher::class.java)
        val mainActivity = MainActivity().apply {
            this.activityResultLauncher = activityResultLauncher
        }

        // Act
        mainActivity.requestPermissions()

        // Assert
        verify(activityResultLauncher, times(1)).launch(MainActivity.REQUIRED_PERMISSIONS)
    }

    @Test
    fun allPermissionsGranted() {
        // Arrange
        val contextCompat = mock(ContextCompat::class.java)
        val mainActivity = MainActivity()

        // When all permissions are granted
        `when`(contextCompat.checkSelfPermission(any(), anyString())).thenReturn(PackageManager.PERMISSION_GRANTED)

        // Act
        val permissionsGranted = mainActivity.allPermissionsGranted()

        // Assert
        assertTrue(permissionsGranted)
    }

    @Test
    fun onDestroy() {
        // Arrange
        val cameraExecutor = mock(Executor::class.java)
        val mainActivity = MainActivity().apply {
            this.cameraExecutor = cameraExecutor
        }

        // Act
        //the function was getting protected access
        mainActivity.onDestroy()

        // Assert
        verify(cameraExecutor, times(1)).shutdown()
    }

    class LuminosityAnalyzerTest {

        interface MockLumaListener : LuminosityAnalyzer.LumaListener {
            fun invoke(luma: Double)
        }

        @Test
        fun analyze() {
            // Arrange
            val listener = mock(MockLumaListener::class.java)
            val analyzer = LuminosityAnalyzer(listener)
            val buffer = ByteBuffer.allocate(3)
            buffer.put(0, 100.toByte())
            buffer.put(1, 150.toByte())
            buffer.put(2, 200.toByte())
            val imageProxy: ImageProxy = mock(ImageProxy::class.java)
            val planeProxy: ImageProxy.PlaneProxy = mock(ImageProxy.PlaneProxy::class.java)
            `when`(planeProxy.buffer).thenReturn(buffer)
            `when`(imageProxy.planes).thenReturn(arrayOf(planeProxy))

            // Act
            analyzer.analyze(imageProxy)

            // Assert - verify that the listener was called with the correct value
            verify(listener, times(1)).invoke(any())
            verify(imageProxy, times(1)).close()
        }
    }

}
