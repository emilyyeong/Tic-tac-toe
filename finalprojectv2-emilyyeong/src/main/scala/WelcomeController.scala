import scalafx.scene.Scene
import scalafx.scene.image.Image
import scalafx.scene.image.ImageView
import scalafxml.core.macros.sfxml
import scalafx.stage.Stage
import scalafx.Includes._
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import scalafx.event.ActionEvent
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType

@sfxml
class WelcomeController (val welcomeImage: ImageView) extends NavigationBar {
  //set the image for the ImageView
  welcomeImage.image = new Image(getClass.getResourceAsStream("/tictac.png"))

  //adjust image opacity
  welcomeImage.opacity = 0.3

  def handleGetStarted(event: ActionEvent): Unit = {
    val resource = getClass.getResource("tictactoe.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val root = loader.getRoot[javafx.scene.layout.VBox]

    val gameStage = new Stage {
      title = "Tic-Tac-Toe"
      scene = new Scene(root)
    }

    gameStage.show()

    // Close the welcome stage
    event.source.asInstanceOf[javafx.scene.Node].getScene.getWindow.hide()
  }

  def Quit(): Unit = {
    handleQuit()
  }

  def About(): Unit = {
    val window = welcomeImage.getScene.getWindow
    new Alert(AlertType.Information) {
      initOwner(window)
      title = "About Tic-Tac-Toe"
      headerText = "Tic-Tac-Toe Game"
      contentText = "This is a Tic-Tac-Toe game created using ScalaFX. Enjoy your game!"
    }.showAndWait()
  }
}
