import java.util.ArrayList;
import java.util.List;

public class WinMediaInfo {
    static {
        System.loadLibrary("resources/WinMediaInfoBridge");
    }

    public static native String[] getMediaSessions();

    public static List<MediaSession> getMediaSessionList(String[] mediaSessions) {
        List<MediaSession> mediaSessionList = new ArrayList<>();

        for (String mediaSession : mediaSessions) {
            String[] mediaSessionParts = mediaSession.split("\\|", -1);
            mediaSessionList.add(new MediaSession(
                    mediaSessionParts[0],
                    mediaSessionParts[1],
                    mediaSessionParts[2],
                    Integer.parseInt(mediaSessionParts[3]),
                    formatTime(Integer.parseInt(mediaSessionParts[4])),
                    formatTime(Integer.parseInt(mediaSessionParts[5]))
            ));
        }

        return mediaSessionList;
    }

    private static String formatTime(int seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;

        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, secs);
        } else {
            return String.format("%d:%02d", minutes, secs);
        }
    }
}
