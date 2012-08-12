import controllers.FileController
import play.api.{GlobalSettings, Application}

object Global extends GlobalSettings {
  override def onStart(app: Application) {
    FileController.profilePicturesDir.mkdirs()
  }
}