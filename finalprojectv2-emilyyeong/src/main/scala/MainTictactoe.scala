import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import scalafx.Includes._

object MainTictactoe extends JFXApp {
  val resource = getClass.getResource("welcome.fxml")
  val loader = new FXMLLoader(resource, NoDependencyResolver)
  loader.load()
  val root = loader.getRoot[javafx.scene.layout.StackPane]

  stage = new JFXApp.PrimaryStage {
    title = "Welcome to Tic-Tac-Toe"
    scene = new Scene(root)
  }
}