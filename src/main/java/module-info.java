module ch.malbun.kalender {
  requires javafx.controls;
  requires org.json;
  requires java.desktop;

  opens ch.malbun.kalender to javafx.fxml;
  exports ch.malbun.kalender;
}