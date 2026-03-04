import javafx.scene.control.CheckBox;

import java.io.IOException;

public record Integration(String appName, CheckBox isConnectedCB) {

    public Integration(String appName, CheckBox isConnectedCB) {
        this.appName = appName;

        this.isConnectedCB = isConnectedCB;
        this.isConnectedCB.setText(this.appName);
        this.isConnectedCB.setSelected(Config.integrations.getOrDefault(this.appName, false));

        this.isConnectedCB.setOnAction(actionEvent -> {
            Config.integrations.put(this.appName, this.isConnectedCB.isSelected());
            try {
                Config.updateFile("integrations");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public String getAppName() {
        return appName;
    }

    public CheckBox getIsConnectedCB() {
        return isConnectedCB;
    }

    public Integration getIntegration() {
        return this;
    }

    @Override
    public String toString() {
        return appName + ":" + isConnectedCB.isSelected();
    }
}
