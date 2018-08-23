package clicker;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

public class ClickManager {
    
    private final ClickCompleteEvent clickCompleteEvent;
    private final Robot robot;

    public ClickManager(ClickCompleteEvent clickCompleteEvent) throws AWTException {
        this.clickCompleteEvent = clickCompleteEvent;
        this.robot = new Robot();
    }
    
    public void fireClicksAt(int x, int y, int clickCount) {
        for (int i = 0; i < clickCount; i++) {
            robot.mouseMove(x, y);
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);

            final int step = i + 1;
            Platform.runLater(() -> {
                clickCompleteEvent.onClickComplete(step, clickCount);
            });
            
            sleep();
        }
    }
    
    private void sleep() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException ex) {
            Logger.getLogger(ClickManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
