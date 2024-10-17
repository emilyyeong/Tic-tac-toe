import scalafx.scene.control.{Button, Label}
import scalafxml.core.macros.sfxml
import scala.collection.mutable.Stack
import scalafx.scene.control.Alert
import scalafx.application.Platform
import scalafx.scene.control.Alert.AlertType
import scalafx.stage.{Window, Stage}
import scalafx.Includes._


@sfxml
class TictactoeController (private val statusLabel: Label,
                           private val button1: Button, private val button2: Button, private val button3: Button,
                           private val button4: Button, private val button5: Button, private val button6: Button,
                           private val button7: Button, private val button8: Button, private val button9: Button,
                           private val undoButton: Button, private val xScoreLabel: Label, private val oScoreLabel: Label
                          ) extends NavigationBar {
  private val buttons = Array(
    Array(button1, button2, button3),
    Array(button4, button5, button6),
    Array(button7, button8, button9)
  )

  private var currentPlayer = "X" //keeps track of player turn
  private var gameEnded = false //to indicate is game has ended or not
  private val moveHistory = Stack[(Int, Int)]() //track player moves to allow undo
  private var xScore = 0
  private var oScore = 0

  def handleButtonClick(event: scalafx.event.ActionEvent): Unit = {
    if (gameEnded) return

    val clickedButton = event.getSource.asInstanceOf[javafx.scene.control.Button]
    if (clickedButton.getText.isEmpty) { //if button that was clicked is empty
      clickedButton.setText(currentPlayer) //sets the button text to current player
      //to determine which buttons the players have clicked
      val (row, col) = buttons.zipWithIndex.flatMap { case (row, i) =>
        row.zipWithIndex.collect { case (button, j) if button == clickedButton => (i, j) }
      }.head
      moveHistory.push((row, col))
      undoButton.disable = false
      //checks if a player has won, if yes then game ends and updates score status
      if (checkWin()) {
        statusLabel.text = s"$currentPlayer wins!"
        gameEnded = true
        updateScore(currentPlayer)
      } else if (checkDraw()) { //if no player has won, check for a draw
        statusLabel.text = "It's a draw!"
        gameEnded = true
      } else { //if game has not ended, updates status label to show which player's turn it is
        currentPlayer = if (currentPlayer == "X") "O" else "X"
        statusLabel.text = s"$currentPlayer's turn"
      }
    }
  }

  def handleRestartButton(): Unit = {
    buttons.flatten.foreach(_.setText("")) //all buttons becomes empty, removes text
    currentPlayer = "X"
    gameEnded = false
    statusLabel.text = "X's turn"
    moveHistory.clear()
    undoButton.disable = true
  }

  //allows players to undo moves
  def handleUndoButton(): Unit = {
    if (moveHistory.nonEmpty && !gameEnded) {
      val (row, col) = moveHistory.pop()
      buttons(row)(col).setText("")
      currentPlayer = if (currentPlayer == "X") "O" else "X"
      statusLabel.text = s"$currentPlayer's turn"
      gameEnded = false
      undoButton.disable = moveHistory.isEmpty
    }
  }

  //combinations for a winning game
  private def checkWin(): Boolean = {
    val winningCombinations = Seq(
      (0, 0, 0, 1, 0, 2), (1, 0, 1, 1, 1, 2), (2, 0, 2, 1, 2, 2), // Rows
      (0, 0, 1, 0, 2, 0), (0, 1, 1, 1, 2, 1), (0, 2, 1, 2, 2, 2), // Columns
      (0, 0, 1, 1, 2, 2), (0, 2, 1, 1, 2, 0) // Diagonals
    )

    //checks if winning combinations exist on the board
    winningCombinations.exists { case (r1, c1, r2, c2, r3, c3) =>
      buttons(r1)(c1).getText == currentPlayer &&
        buttons(r2)(c2).getText == currentPlayer &&
        buttons(r3)(c3).getText == currentPlayer
    }
  }

  //determine if game is a draw, checks every button on the board is filled in
  private def checkDraw(): Boolean = {
    buttons.flatten.forall(_.getText.nonEmpty)
  }

  //track x and o score
  private def updateScore(winner: String): Unit = {
    if (winner == "X") {
      xScore += 1
      xScoreLabel.text = s"X: $xScore"
    } else {
      oScore += 1
      oScoreLabel.text = s"O: $oScore"
    }
  }

  //menu navigation
  def Quit(): Unit = {
    handleQuit()
  }

  def About(): Unit = {
    val window: Window = undoButton.getScene.getWindow
    new Alert(AlertType.Information) {
      initOwner(window)
      title = "About Tic-Tac-Toe"
      headerText = "Tic-Tac-Toe Game"
      contentText = "A win can be achieved when a player gets 3 of the same pattern (X or O) in a row (up, down, across, or diagonally). A draw occurs when the board is filled without any winning combinations."
    }.showAndWait()
  }

}

