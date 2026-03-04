import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class App extends Application {
    static Stage stage;

    static Scene mainWinScene;
    static Scene settingsWinScene;

    static final String title = "xazubot";

    static final int width = 300;
    static final int height = 400;

    private final Image appImg = new Image("resources/images/bot.png");

    @Override
    public void start(Stage stage) throws IOException, AWTException {
        Config.init();
        Bot.init();

        Application.setUserAgentStylesheet("resources/AtlantaFX-cupertino-dark.css");

        App.stage = stage;
        Platform.setImplicitExit(false);

        FXMLLoader mainWinFxmlLoader = new FXMLLoader(App.class.getResource("resources/xml/mainWinView.fxml"));
        mainWinScene = new Scene(mainWinFxmlLoader.load(), width, height);

        FXMLLoader settingsWinFxmlLoader = new FXMLLoader(App.class.getResource("resources/xml/settingsWinView.fxml"));
        settingsWinScene = new Scene(settingsWinFxmlLoader.load(), width, height);

        setMainWinScene();

        stage.setTitle(title);
        stage.getIcons().add(appImg);

        hideWinToTray(false);

        createTrayIcon();

        stage.show();
    }

    @Override
    public void stop() throws Exception {
        Bot.stop();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch();
    }

    static void setMainWinScene() {
        stage.setScene(mainWinScene);
    }

    static void setSettingsWinScene() {
        stage.setScene(settingsWinScene);
    }

    static void createTrayIcon() throws AWTException {
        java.awt.Image image = Toolkit.getDefaultToolkit().getImage(
                App.class.getResource("resources/images/bot.png"));

        MenuItem restoreItem = new MenuItem("Развернуть");
        restoreItem.addActionListener(event ->
                Platform.runLater(App::showWin)
        );

        MenuItem exitItem = new MenuItem("Выход");
        exitItem.addActionListener(event -> {
            Platform.exit();
        });

        PopupMenu popup = new PopupMenu();
        popup.add(restoreItem);
        popup.addSeparator();
        popup.add(exitItem);

        TrayIcon trayIcon = new TrayIcon(image, title, popup);
        trayIcon.setImageAutoSize(true);

        trayIcon.addActionListener(event ->
                Platform.runLater(App::showWin)
        );

        SystemTray.getSystemTray().add(trayIcon);
    }

    static void hideWinToTray(boolean isSelected) {
        if (isSelected) {
            stage.setOnCloseRequest(event -> {
                event.consume();
                stage.hide();
            });
        } else {
            stage.setOnCloseRequest(event -> {
                Platform.exit();
            });
        }
    }

    static void showWin() {
        stage.show();
        stage.toFront();
    }
}