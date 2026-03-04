import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class Logs {
    static final StringProperty logsSP = new SimpleStringProperty("");
    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");


    static void log(String logStr) {
        log.info(logStr);
        logsSP.set(logsSP.get() + " "
                + LocalTime.now().format(formatter) + " "
                + logStr + "\n");
    }
}
