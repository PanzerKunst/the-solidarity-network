package controllers

import play.api.mvc.{Action, Controller}
import java.io.File

object FileController extends Controller {
  private val homeDir = System.getProperty("user.home")
  val profilePicturesDir = new File(homeDir + "/data/profile-pictures")

  def serveProfilePicture(userId: Int, isTemp: Boolean = false) = Action {
    implicit request =>
      Application.loggedInUser(session) match {
        case Some(loggedInUser) =>
          val isTemp = if (request.queryString.contains("isTemp"))
            request.queryString.get("isTemp").get.head == "true"
          else
            false

          getProfilePicture(userId, isTemp) match {
            case Some(file) => Ok.sendFile(
              content = file,
              inline = true
            )
            case None => NotFound
          }
        case None => Forbidden
      }
  }

  def uploadProfilePicture() = Action(parse.multipartFormData) {
    implicit request =>
      Application.loggedInUser(session) match {
        case Some(loggedInUser) =>
          request.body.file("image").map {
            picture =>
              deleteExistingTempProfilePicture(loggedInUser.id.get)

              val extension = picture.filename.substring(picture.filename.lastIndexOf(".")).toLowerCase

              picture.ref.moveTo(new File(profilePicturesDir.getPath + "/" + loggedInUser.id.get + ".tmp" + extension))
              Ok("")
          }.getOrElse {
            NotFound
          }
        case None => Forbidden
      }
  }

  def saveTempProfilePicture(userId: Long) {
    getProfilePicture(userId, true) match {
      case Some(tempFile) =>
        // Deleting actual profile pic
        getProfilePicture(userId, false) match {
          case Some(file) => deleteFile(file)
          case None =>
        }

        val fileName = tempFile.getName
        val extension = fileName.substring(fileName.lastIndexOf("."))
        if (!tempFile.renameTo(new File(profilePicturesDir.getPath + "/" + userId + extension)))
          throw new FileSystemException("Temporary profile pic couldn't be moved to non-temp file!")
      case None =>
    }
  }

  private def getProfilePicture(userId: Long, isTemp: Boolean) = {
    val matchingRegex = if (isTemp)
      (userId + """\.tmp\.(jpg|jpeg|png)""").r
    else
      (userId + """\.(jpg|jpeg|png)""").r
    val matchingFiles = profilePicturesDir.listFiles.filter(f => matchingRegex.findFirstIn(f.getName).isDefined)

    if (matchingFiles.size > 1)
      throw new InconsistentDataException("Found more than 1 profile picture!")
    else if (matchingFiles.size == 0)
      None
    else
      Some(matchingFiles.head)
  }

  private def deleteExistingTempProfilePicture(userId: Long) {
    getProfilePicture(userId, true) match {
      case Some(file) => deleteFile(file)
      case None =>
    }
  }

  private def deleteFile(file: File) {
    val deletionSuccessful = if (file.exists())
      file.delete()
    else true

    if (!deletionSuccessful) throw new FileSystemException("Could not delete file: " + file.getAbsolutePath)
  }
}
