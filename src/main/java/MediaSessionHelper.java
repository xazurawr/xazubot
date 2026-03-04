import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class MediaSessionHelper {

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public interface MediaSessionsCallback {
        void onResult(String[] mediaSessions);
        void onError(String error);
    }

    public static void getMediaSessions(MediaSessionsCallback callback) {
        executor.submit(() -> {
            try {
                String[] mediaSessions = WinMediaInfo.getMediaSessions();
                callback.onResult(mediaSessions != null ? mediaSessions : new String[0]);
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }

    public static String[] getMediaSessionsSync() {
        try {
            Future<String[]> future = executor.submit(WinMediaInfo::getMediaSessions);
            return future.get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    public static void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
}