public record MediaSession(
        String appName,
        String artist,
        String title,
        int status,
        String currentPosition,
        String totalTime
) {
    @Override
    public String toString() {
        return appName + " : "
                + title + " - " + artist + " "
                + status + " (" + currentPosition + " / " + totalTime + ")";
    }
}
