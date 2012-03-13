import play.jobs.{Job, OnApplicationStart}
import play.Logger

@OnApplicationStart
class Bootstrap extends Job {
  override def doJob() {
    Logger.info("Bootstrapping complete")
  }
}
