import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsWinController implements Initializable {

    @FXML
    private BorderPane settingsWinBP;

    @FXML
    private Button backBtn;

    @FXML
    private Label settingsLbl;

    @FXML
    private TextField nicknameTF;

    @FXML
    private TextField botCommandTF;

    @FXML
    private TableView<Integration> integrationsTV;

    @FXML
    private TableColumn<Integration, CheckBox> isConnectedCBColumn;

    @FXML
    private Button configIntegrationsBtn;

    @FXML
    private Button openConfigBtn;

    @FXML
    private CheckBox isMinToTrayCB;

    private final Image backImg = new Image("resources/images/back.png",
            48, 48, false, true);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        settingsWinBP.setOnMouseClicked(event -> settingsWinBP.requestFocus());
        Platform.runLater(() -> settingsWinBP.requestFocus());

        backBtn.setGraphic(new ImageView(backImg));

        nicknameTF.editableProperty().bind(MainWinController.isActiveBP.not());
        nicknameTF.setText(Config.nickname);
        nicknameTF.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                try {
                    nicknameTFAction();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        botCommandTF.editableProperty().bind(MainWinController.isActiveBP.not());
        botCommandTF.setText(Config.botCommand);
        botCommandTF.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                try {
                    botCommandTFAction();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        for (String k : Config.integrations.keySet()) {
            integrationsTV.getItems().add(new Integration(k, new CheckBox()));
        }

        isMinToTrayCB.setSelected(Config.isMinToTray);
    }

    public void backBtnAction(ActionEvent actionEvent) throws IOException {
        App.setMainWinScene();
    }

    public void isMinToTrayCBAction(ActionEvent actionEvent) {
        App.hideWinToTray(isMinToTrayCB.isSelected());
    }

    public void nicknameTFAction() throws IOException {
        Config.nickname = nicknameTF.getText();
        Config.nicknameSP.set(nicknameTF.getText());
        Config.updateFile("nickname");
    }

    public void nicknameTFAction(ActionEvent ActionEvent) throws IOException {
        Config.nickname = nicknameTF.getText();
        Config.nicknameSP.set(nicknameTF.getText());
        Config.updateFile("nickname");
    }

    public void botCommandTFAction() throws IOException {
        Config.botCommand = botCommandTF.getText();
        Config.updateFile("botCommand");
    }

    public void botCommandTFAction(ActionEvent actionEvent) throws IOException {
        Config.botCommand = botCommandTF.getText();
        Config.updateFile("botCommand");
    }

    public void configIntegrationsBtnAction(ActionEvent actionEvent) {
        integrationsTV.getItems().clear();

        for (String key : Config.integrations.keySet()) {
            integrationsTV.getItems().add(new Integration(key, new CheckBox()));
        }

        MediaSessionHelper.getMediaSessions(new MediaSessionHelper.MediaSessionsCallback() {
            @Override
            public void onResult(String[] mediaSessions) {
                for (MediaSession mediaSession : WinMediaInfo.getMediaSessionList(mediaSessions)) {
                    if (!Config.integrations.containsKey(mediaSession.appName())) {
                        integrationsTV.getItems().add(new Integration(mediaSession.appName(), new CheckBox()));
                    }
                }
            }

            @Override
            public void onError(String error) {
                if (!error.equals("null")) {
                    Logs.log("Ошибка: " + error);
                }
            }
        });
    }

    public void openConfigBtnAction(ActionEvent actionEvent) throws IOException {
        Desktop.getDesktop().open(Config.file);
    }
}
