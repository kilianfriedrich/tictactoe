package gui.fxcontroller;

import gui.fxcontroller.lobby.Lobby;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.TicTacToeClient;

public class TicTacToeMatch {

    public Text turnField;
    protected TicTacToeClient serverConnection;
    protected String opponent, gameID;
    protected byte first;
    protected byte yourTurn;
    protected Lobby lobbyObject;

    public void opponentJoined(String opponentName, byte first) {

        this.first = first;
        
        this.opponent = opponentName;

        reset();

    }

    public void opponentPicked(int field) {

        state[field] = -1;

        Canvas affectedField = null;
        switch(field) {
            case 0: affectedField = field0; break;
            case 1: affectedField = field1; break;
            case 2: affectedField = field2; break;
            case 3: affectedField = field3; break;
            case 4: affectedField = field4; break;
            case 5: affectedField = field5; break;
            case 6: affectedField = field6; break;
            case 7: affectedField = field7; break;
            case 8: affectedField = field8; break;
        }

        if(affectedField == null)
            return;

        affectedField.getGraphicsContext2D().setStroke(Color.RED);
        affectedField.getGraphicsContext2D().setLineWidth(5);
        affectedField.getGraphicsContext2D().strokeOval(10D, 10D, 30D, 30D);
        
        if(state[0] == -1 && state[1] == -1 && state[2] == -1 ||
                state[3] == -1 && state[4] == -1 && state[5] == -1 ||
                state[6] == -1 && state[7] == -1 && state[8] == -1 ||
                state[0] == -1 && state[3] == -1 && state[6] == -1 ||
                state[1] == -1 && state[4] == -1 && state[7] == -1 ||
                state[2] == -1 && state[5] == -1 && state[8] == -1 ||
                state[0] == -1 && state[4] == -1 && state[8] == -1 ||
                state[2] == -1 && state[4] == -1 && state[6] == -1) {

            opponentScoreByte++;
            first = (byte) -first;
            reset();

        } else {
            
            boolean allSet = true;
            for(int i = 0; i < 9; i++)
                if(state[i] == 0)
                    allSet = false;
            if(allSet) {
                first = (byte) -first;
                reset();
            } else {
                yourTurn = 1;
                turnField.setText("It's your turn!");
            }
            
        }

    }

    public void opponentLeft() {

        System.out.print("_____________________LEFT");

        this.yourTurn = 0;
        this.opponent = null;

        initialize();

    }

    public TicTacToeMatch(Lobby lobbyObject, TicTacToeClient serverConnection, String gameID, String opponent, byte first) {

        this.serverConnection = serverConnection;
        this.lobbyObject = lobbyObject;
        this.gameID = gameID;
        this.opponent = opponent;

        this.yourTurn = first;
        this.first = first;

        serverConnection.setCurrentGame(this);

    }

    @FXML public Canvas field0, field1, field2, field3, field4, field5, field6, field7, field8;
    protected byte[] state = new byte[9];

    @FXML public Text opponentField;
    @FXML public Text gameidField;

    @FXML public Label youScore;
    protected byte youScoreByte = 0, opponentScoreByte = 0;
    @FXML public Label opponentScore;

    @FXML public void initialize() {

        if(opponent != null)
            opponentField.setText("Playing against " + opponent);
        else
            opponentField.setText("Waiting for opponent...");

        gameidField.setText("Game-ID: " + gameID);

        youScore.setText(Byte.toString(youScoreByte));
        opponentScore.setText(Byte.toString(opponentScoreByte));

        if(yourTurn == 1)
            turnField.setText("Your Turn!");
        else if(yourTurn == -1)
            turnField.setText("Opponent's turn!");
        else
            turnField.setText("");

    }

    @FXML public void clickOnField0() { processClick(field0, 0); }
    @FXML public void clickOnField1() { processClick(field1, 1); }
    @FXML public void clickOnField2() { processClick(field2, 2); }
    @FXML public void clickOnField3() { processClick(field3, 3); }
    @FXML public void clickOnField4() { processClick(field4, 4); }
    @FXML public void clickOnField5() { processClick(field5, 5); }
    @FXML public void clickOnField6() { processClick(field6, 6); }
    @FXML public void clickOnField7() { processClick(field7, 7); }
    @FXML public void clickOnField8() { processClick(field8, 8); }

    protected synchronized void processClick(Canvas canvas, int field) {

        if(yourTurn == 1 && state[field] == 0) {

            canvas.getGraphicsContext2D().setStroke(Color.GRAY);
            canvas.getGraphicsContext2D().setLineWidth(5);
            canvas.getGraphicsContext2D().strokeLine(10D, 10D, 40D, 40D);
            canvas.getGraphicsContext2D().strokeLine(40D, 10D, 10D, 40D);

            String response = serverConnection.pick(field);

            if(response.startsWith("ok")) {

                state[field] = 1;

                canvas.getGraphicsContext2D().setStroke(Color.RED);
                canvas.getGraphicsContext2D().setLineWidth(5);
                canvas.getGraphicsContext2D().strokeLine(10D, 10D, 40D, 40D);
                canvas.getGraphicsContext2D().strokeLine(40D, 10D, 10D, 40D);

            } else {

                canvas.getGraphicsContext2D().clearRect(0D, 0D, 50D, 50D);

            }

            if(state[0] == 1 && state[1] == 1 && state[2] == 1 ||
                    state[3] == 1 && state[4] == 1 && state[5] == 1 ||
                    state[6] == 1 && state[7] == 1 && state[8] == 1 ||
                    state[0] == 1 && state[3] == 1 && state[6] == 1 ||
                    state[1] == 1 && state[4] == 1 && state[7] == 1 ||
                    state[2] == 1 && state[5] == 1 && state[8] == 1 ||
                    state[0] == 1 && state[4] == 1 && state[8] == 1 ||
                    state[2] == 1 && state[4] == 1 && state[6] == 1) {

                youScoreByte++;
                first = (byte) -first;
                reset();

            } else {
                
                boolean allSet = true;
                for(int i = 0; i < 9; i++)
                    if(state[i] == 0)
                        allSet = false;
                
                if(allSet) {
                    first = (byte) -first;
                    reset();
                } else {
                    yourTurn = -1;
                    turnField.setText("Opponent's turn");
                }
                
            }

        }

    }
    
    protected synchronized void reset() {

        this.yourTurn = this.first;
        
        initialize();
        
        field0.getGraphicsContext2D().clearRect(0D, 0D, 50D, 50D);
        field1.getGraphicsContext2D().clearRect(0D, 0D, 50D, 50D);
        field2.getGraphicsContext2D().clearRect(0D, 0D, 50D, 50D);
        field3.getGraphicsContext2D().clearRect(0D, 0D, 50D, 50D);
        field4.getGraphicsContext2D().clearRect(0D, 0D, 50D, 50D);
        field5.getGraphicsContext2D().clearRect(0D, 0D, 50D, 50D);
        field6.getGraphicsContext2D().clearRect(0D, 0D, 50D, 50D);
        field7.getGraphicsContext2D().clearRect(0D, 0D, 50D, 50D);
        field8.getGraphicsContext2D().clearRect(0D, 0D, 50D, 50D);
        for(byte i = 0; i < 9; i++) state[i] = 0;
        
    }

    @FXML public void leave() {

        serverConnection.leave();

        ((Stage) gameidField.getScene().getWindow()).setScene(lobbyObject.getScene());

    }

}
