package ch.malbun.kalender.ui;

import ch.malbun.kalender.ui.time.Hour;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.Serial;
import java.io.Serializable;

public class Task extends HBox implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  public String task;
  public Hour owner;

  public Task(String task, Hour owner) {
    this.task = task;
    this.owner = owner;

    setAlignment(Pos.CENTER_LEFT);

    setSpacing(10);
    Button done = new Button("Erledigt");
    done.setFont(new Font(16));
    done.setOnAction((EventHandler<ActionEvent> & Serializable) e -> {
      this.owner.tasks.remove(this);
      done.setDisable(true);
    });
    getChildren().add(done);

    Text text = new Text(owner.hour + ": " + task);
    text.setFont(new Font(16));
    getChildren().add(text);
  }
}
