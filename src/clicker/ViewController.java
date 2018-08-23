package clicker;

import java.awt.AWTException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseMotionListener;

public class ViewController implements Initializable, NativeMouseMotionListener, NativeKeyListener, ClickCompleteEvent {

    private static final int ENTER_KEY_CODE = 28;
    private static final int SPACE_KEY_CODE = 57;
    private static final int ESCAPE_KEY_CODE = 1;

    @FXML
    private Label lblCursorPosition;
    @FXML
    private TextField tfTotalClicks;
    @FXML
    private ProgressBar pbClicks;

    private final ClickManager clickManager;
    private int mouseX, mouseY;

    public ViewController() throws AWTException {
        this.clickManager = new ClickManager(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        GlobalScreen.addNativeMouseMotionListener(this);
        GlobalScreen.addNativeKeyListener(this);

        tfTotalClicks.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,5}")) {
                    tfTotalClicks.setText(oldValue);
                }
            }
        });
    }

    @Override
    public void nativeMouseMoved(NativeMouseEvent nme) {
        mouseX = nme.getX();
        mouseY = nme.getY();

        Platform.runLater(() -> lblCursorPosition.setText(String.format("%d, %d!", mouseX, mouseY)));
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nke) {
        switch (nke.getKeyCode()) {
            case SPACE_KEY_CODE:
                startClicking();
                break;
        }
    }

    @Override
    public void nativeMouseDragged(NativeMouseEvent nme) {
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nke) {
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nke) {
    }

    @Override
    public void onClickComplete(int progression, int total) {
        pbClicks.setProgress((double) progression / (double) total);
    }

    private void startClicking() {
        tfTotalClicks.setDisable(true);
        int totalClicks = safeParseInteger(tfTotalClicks.getText());
        clickManager.fireClicksAt(mouseX, mouseY, totalClicks);
        tfTotalClicks.setDisable(false);
    }

    private int safeParseInteger(String text) {
        try {
            return Integer.parseInt(text);
        } catch (RuntimeException e) {
            return 0;
        }
    }
}
