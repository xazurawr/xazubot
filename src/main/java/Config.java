import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Config {
    static final File file = new File("resources/config.ini");
    static Ini ini = new Ini();

    static String nickname;
    static Map<String, Boolean> integrations = new HashMap<>();
    static boolean isMinToTray;
    static String botToken;
    static String botCommand;
    static String yandexToken;

    static Profile.Section channelSection;
    static Profile.Section integrationsSection;
    static Profile.Section settingsSection;
    static Profile.Section botSection;

    static StringProperty nicknameSP;

    public static void init() throws IOException {
        if (file.exists()) {
            ini.load(file);
        }
        String section = "channel";
        if (!ini.containsKey(section)) {
            channelSection = ini.add(section);
        } else {
            channelSection = ini.get(section);
        }
        if (!channelSection.containsKey("nickname")) {
            channelSection.put("nickname", "");
        }
        nickname = channelSection.get("nickname", String.class);
        nicknameSP = new SimpleStringProperty(nickname);

        section = "integrations";
        if (!ini.containsKey(section)) {
            integrationsSection = ini.add(section);
        } else {
            integrationsSection = ini.get(section);
        }
        for (String k : integrationsSection.keySet()) {
            integrations.put(k, integrationsSection.get(k, boolean.class));
        }

        section = "settings";
        if (!ini.containsKey(section)) {
            settingsSection = ini.add(section);
        } else {
            settingsSection = ini.get(section);
        }
        if (!settingsSection.containsKey("isMinToTray")) {
            settingsSection.put("isMinToTray", false);
        }
        isMinToTray = settingsSection.get("isMinToTray", boolean.class);

        section = "bot";
        if (!ini.containsKey(section)) {
            botSection = ini.add(section);
        } else {
            botSection = ini.get(section);
        }
        if (!botSection.containsKey("botToken")) {
            botSection.put("botToken", "past your bot token here");
        }
        if (!botSection.containsKey("botCommand")) {
            botSection.put("botCommand", "!media");
        }
        botToken = botSection.get("botToken", String.class);
        botCommand = botSection.get("botCommand", String.class);

        if (botSection.containsKey("yandexToken")) {
            yandexToken = botSection.get("yandexToken", String.class);
        } else {
            yandexToken = "";
        }

        ini.store(file);
    }

    public static void updateFile(String key) throws IOException {
        switch (key) {
            case "nickname": channelSection.put("nickname", nickname);
            case "integrations": {
                for (String k : integrations.keySet()) {
                    integrationsSection.put(k, integrations.get(k));
                }
            }
            case "isMinToTray": settingsSection.put("isMinToTray", isMinToTray);
            case "botCommand": botSection.put("botCommand", botCommand);
        }

        ini.store(file);
    }
}
