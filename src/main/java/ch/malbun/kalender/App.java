package ch.malbun.kalender;

import ch.malbun.kalender.ui.CalenderView;
import ch.malbun.kalender.ui.enums.Months;
import ch.malbun.kalender.ui.exceptions.CalenderEntityOutOfBounceException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.AWTException;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class App extends Application {

  public static File homeDir = new File(System.getProperty("user.home"));
  public static Stage mainStage;
  public static CalenderView calenderView;
  public static File tempDir;

  @Override
  public void start(Stage stage) throws IOException, CalenderEntityOutOfBounceException, AWTException {
    mainStage = stage;

    tempDir = Files.createTempDirectory("calender").toFile();

    HBox root = new HBox();
    root.setStyle("-fx-background-color: gray");
    Scene scene = new Scene(root, 490, 445);
    scene.setFill(Color.GRAY);

    SystemTray systemTray = SystemTray.getSystemTray();
    Image image = Toolkit.getDefaultToolkit().createImage(App.class.getResource("checkbox.png"));
    TrayIcon trayIcon = new TrayIcon(image, "Test");
    trayIcon.setImageAutoSize(true);
    systemTray.add(trayIcon);
    trayIcon.addActionListener(e -> EventQueue.invokeLater(() -> Platform.runLater(stage::toFront)));


    Timer timer = new Timer();
    LocalDateTime current = LocalDateTime.now();
    LocalDateTime plusHour = current.plusHours(1);
    final LocalDateTime[] nextHour = {LocalDateTime.of(plusHour.getYear(), plusHour.getMonth(), plusHour.getDayOfMonth(), plusHour.getHour(), 0)};
    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        StringBuilder sb = new StringBuilder();
        try {
          calenderView
                  .getYear(nextHour[0].getYear())
                  .getMonth(Months.byValue(nextHour[0].getMonthValue()))
                  .getDay(nextHour[0].getDayOfMonth())
                  .getHour(nextHour[0].getHour())
                  .tasks.forEach(task -> sb.append(task.task).append("\n"));
        } catch (CalenderEntityOutOfBounceException e) {
          throw new RuntimeException(e);
        }

        trayIcon.displayMessage("Tasks", sb.toString(), TrayIcon.MessageType.INFO);

        nextHour[0] = nextHour[0].plusHours(1);
      }
    }, Date.from(nextHour[0].toInstant(OffsetDateTime.now().getOffset())), 3600000);

    calenderView = new CalenderView();
    calenderView.load();
    root.getChildren().add(calenderView);

    stage.setScene(scene);
    stage.getIcons().add(new javafx.scene.image.Image(App.class.getResourceAsStream("favicon.png")));
    stage.setTitle("Kalender");
    stage.setResizable(false);
    stage.setOnCloseRequest(e -> {
      try {
        calenderView.saveWithoutThread();
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
      Platform.exit();
      System.exit(0);
    });
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}
