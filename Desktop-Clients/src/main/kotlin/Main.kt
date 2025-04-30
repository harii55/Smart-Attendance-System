import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ui.AppContent

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Smart Attendance") {
        AppContent()
    }
}
