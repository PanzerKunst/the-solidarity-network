package controllers

import play.api.mvc.{Action, Controller}
import java.io.File

object FileController extends Controller {
  def profilePicture(userId: Int) = Action {

    val homeDir = System.getProperty("user.home")

    val profilePicturesDir = new File(homeDir + "/data/profile-pictures")

    profilePicturesDir.mkdirs()

    val matchingRegex = (userId + """\.png""").r

    val matchingFiles = profilePicturesDir.listFiles.filter(f => matchingRegex.findFirstIn(f.getName).isDefined)

    if (matchingFiles.size > 1)
      throw new InconsistentDataException
    else if (matchingFiles.size == 0)
      NotFound
    else
      Ok.sendFile(
        content = matchingFiles.head,
        inline = true
      )
  }
}
