import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class Bot {
    static OAuth2Credential credential;
    static TwitchClient twitchClient;
    static String botToken;

    static final Image switchOffImg = new Image("resources/images/switchOff.png",
            128, 128, false, true);
    static final Image switchOnImg = new Image("resources/images/switchOn.png",
            128, 128, false, true);
    static final Image switchLoadingImg = new Image("resources/images/switchLoading.png",
            128, 128, false, true);
    static final ObjectProperty<Image> switchIP = new SimpleObjectProperty<>(switchLoadingImg);

    static final BooleanProperty switchBP = new SimpleBooleanProperty(false);

    public static void init() {
        Logs.log("Инициализация клиента Twitch...");
        botToken = Config.botToken;
        credential = new OAuth2Credential("twitch", botToken);
        twitchClient = TwitchClientBuilder.builder()
                .withEnableChat(true).withChatAccount(credential).build();

        twitchClient.getEventManager().onEvent(ChannelJoinEvent.class, event -> {
            switch (twitchClient.getChat().getConnectionState()) {
                case CONNECTED -> {
                    Logs.log("Подключение к " + event.getChannel().getName() + " успешно.");
                    if (!event.getChannel().getName().equals(Config.nickname)) {
                        switchIP.set(switchOnImg);
                        switchBP.set(true);
                    } else {
                        switchIP.set(switchOffImg);
                    }
                }
                case CONNECTING -> Logs.log("Подключение к " + event.getChannel().getName() + "...");
                case DISCONNECTED -> Logs.log("Отключение от " + event.getChannel().getName() + " успешно.");
                case RECONNECTING -> Logs.log("Переподключение к " + event.getChannel().getName() + "...");
                case DISCONNECTING -> Logs.log("Отключение от " + event.getChannel().getName() + "...");
            }
        });

        twitchClient.getEventManager().onEvent(ChannelLeaveEvent.class, event -> {
            Logs.log("Отключение от " + event.getChannel().getName() + " успешно.");
            switchIP.set(switchOnImg);
        });

        twitchClient.getEventManager().onEvent(ChannelMessageEvent.class, event -> {
            if (event.getMessage().equals(Config.botCommand)) {
                StringBuilder message = new StringBuilder();

                MediaSessionHelper.getMediaSessions(new MediaSessionHelper.MediaSessionsCallback() {
                    @Override
                    public void onResult(String[] mediaSessions) {
                        List<MediaSession> mediaSessionList = WinMediaInfo.getMediaSessionList(mediaSessions);

                        for (MediaSession mediaSession : mediaSessionList) {
                            if (Config.integrations.containsKey(mediaSession.appName())
                                    && Config.integrations.get(mediaSession.appName())
                                    && mediaSession.status() == 2) {
                                message.append(mediaSession.title());
                                message.append(" - ");
                                message.append(mediaSession.artist());
                                message.append(" music ");
                            }
                        }

                        if (message.isEmpty()) {
                            message.append("Сейчас ничего не играет durak");
                        }

                        event.reply(event.getTwitchChat(), message.toString());
                    }

                    @Override
                    public void onError(String error) {
                        if (!error.equals("null")) {
                            Logs.log("Ошибка: " + error);
                        }
                    }
                });
            }
        });
    }

    static void joinChannel() {
        switchIP.set(switchLoadingImg);
        twitchClient.getChat().joinChannel(Config.nickname);
    }

    static void leaveChannel() {
        switchIP.set(switchLoadingImg);
        twitchClient.getChat().leaveChannel(Config.nickname);
        switchIP.set(switchOnImg);
    }

    public static void stop() {
        twitchClient.close();
    }
}

