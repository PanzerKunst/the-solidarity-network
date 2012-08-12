package controllers

import play.api.mvc.{Action, Controller}
import java.io.File

object FileController extends Controller {
  private val homeDir = System.getProperty("user.home")
  val profilePicturesDir = new File(homeDir + "/data/profile-pictures")

  def serveProfilePicture(userId: Int) = Action {
    implicit request =>
      Application.loggedInUser(session) match {
        case Some(loggedInUser) =>
          getProfilePicture(userId) match {
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
              deleteExistingProfilePicture(loggedInUser.id.get)

              val extension = picture.filename.substring(picture.filename.lastIndexOf(".")).toLowerCase

              picture.ref.moveTo(new File(profilePicturesDir.getPath + "/" + loggedInUser.id.get + extension))
              Ok("")
          }.getOrElse {
            NotFound
          }
        case None => Forbidden
      }
  }

  private def getProfilePicture(userId: Long) = {
    val matchingRegex = (userId + """\.(jpg|jpeg|png)""").r
    val matchingFiles = profilePicturesDir.listFiles.filter(f => matchingRegex.findFirstIn(f.getName).isDefined)

    if (matchingFiles.size > 1)
      throw new InconsistentDataException("Found more than 1 profile picture!")
    else if (matchingFiles.size == 0)
      None
    else
      Some(matchingFiles.head)
  }

  private def deleteExistingProfilePicture(userId: Long) {
    getProfilePicture(userId) match {
      case Some(file) => {
        deleteProfilePictureFile(file)
      }
      case None =>
    }
  }

  private def deleteProfilePictureFile(file: File) {
    val deletionSuccessful = if (file.exists())
      file.delete()
    else true

    if (!deletionSuccessful) throw new FileSystemException("Could not delete the profile picture file!")
  }
}
