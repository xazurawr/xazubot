import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWinController implements Initializable {
    @FXML
    private BorderPane mainWinBP;

    @FXML
    private Button settingsBtn;

    @FXML
    private ToggleButton startTB;

    @FXML
    private Label nicknameLbl;

    @FXML
    private TextArea logsTA;

    @FXML
    private ScrollPane logsSP;

    private final Image settingsImg = new Image("resources/images/settings.png",
            32, 32, false, true);

    private final ImageView switchIV = new ImageView();

    static BooleanProperty isActiveBP;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mainWinBP.setOnMouseClicked(event -> mainWinBP.requestFocus());
        Platform.runLater(() -> mainWinBP.requestFocus());

        settingsBtn.setGraphic(new ImageView(settingsImg));

        switchIV.imageProperty().bind(Bot.switchIP);
        startTB.setGraphic(switchIV);
        startTB.disableProperty().bind(Bot.switchBP.not());

        nicknameLbl.textProperty().bind(Config.nicknameSP);

        logsTA.textProperty().bind(Logs.logsSP);

        logsSP.setContent(logsTA);

        isActiveBP = new SimpleBooleanProperty(startTB.isSelected());
    }

    public void startBtnAction(ActionEvent actionEvent) throws IOException {
        isActiveBP.set(startTB.isSelected());
        if (startTB.isSelected()) {
            Bot.joinChannel();
        } else {
            Bot.leaveChannel();
        }
    }

    public void settingsBtnAction(ActionEvent actionEvent) throws IOException {
        App.setSettingsWinScene();
    }
}