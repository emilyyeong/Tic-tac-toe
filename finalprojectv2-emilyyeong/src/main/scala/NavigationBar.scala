import scalafx.application.Platform

trait NavigationBar {
  def handleQuit(): Unit = {
    Platform.exit()
  }
}