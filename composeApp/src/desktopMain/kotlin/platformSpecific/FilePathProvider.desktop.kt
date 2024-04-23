package platformSpecific

import net.harawata.appdirs.AppDirs
import net.harawata.appdirs.AppDirsFactory

actual fun providePath(): String {
    val appDirs: AppDirs = AppDirsFactory.getInstance()
    return appDirs.getUserDataDir("GPTAssistant", null, null)
}
