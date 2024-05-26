package ch.malbun.kalender.ui.time;

import ch.malbun.kalender.App;
import ch.malbun.kalender.ui.Task;
import ch.malbun.kalender.ui.exceptions.CalenderEntityOutOfBounceException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.Serial;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

public class Day extends StackPane implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  public Month owner;
  public int day;
  public LocalDate date;
  public ArrayList<Hour> hours = new ArrayList<>();

  protected Day(Month owner, int day) {
    super();
    this.owner = owner;
    this.day = day;

    date = LocalDate.of(owner.owner.year, owner.month.getVal(), day);

    for (int i = 0; i < 24; i++) {
      Hour hour = new Hour(this, i);
      hours.add(i, hour);
    }

    Text text = new Text(String.valueOf(day));
    text.setFill(Color.BLACK);
    text.setFont(Font.font("Arial Black", FontWeight.BOLD, 20));

    AtomicInteger tasks = new AtomicInteger();
    hours.forEach(hour -> hour.tasks.forEach((Consumer<? super Task> & Serializable) task -> tasks.getAndIncrement()));

    double multiplied = tasks.get() * 0.15;
    double red = 1.0 / (multiplied + 1.0);

    Rectangle rectangle = new Rectangle(50, 50, Color.color(1, red, red));

    if (date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) text.setFill(Color.BLUE);
    if (date.isEqual(LocalDate.now())) text.setFill(Color.GREEN);

    setWidth(50);
    setHeight(50);

    getChildren().addAll(rectangle, text);

    setOnMouseClicked((EventHandler<? super MouseEvent> & Serializable) e -> {
      Stage stage = new Stage();
      stage.setWidth(450);

      VBox root = new VBox();
      root.setSpacing(5);
      Scene scene = new Scene(root);

      HBox addTask = new HBox();
      addTask.setAlignment(Pos.CENTER_LEFT);
      addTask.setSpacing(10);

      Button add = new Button("Hinzufügen");
      add.setFont(new Font(16));
      addTask.getChildren().add(add);

      Text stundeDesc = new Text("Stunde:");
      stundeDesc.setFont(new Font(16));
      addTask.getChildren().add(stundeDesc);

      MenuButton stunde = new MenuButton("0");
      List<MenuItem> menus = IntStream.rangeClosed(0, 23)
                  .mapToObj((IntFunction<MenuItem> & Serializable) i -> {
                        MenuItem item = new MenuItem(String.valueOf(i));
                        item.setOnAction(ev -> stunde.setText(String.valueOf(i)));
                        return item;
                      })
                  .toList();
      stunde.getItems().addAll(menus);
      addTask.getChildren().add(stunde);

      Text task = new Text("Task:");
      task.setFont(new Font(16));
      addTask.getChildren().add(task);

      TextField taskField = new TextField();
      addTask.getChildren().add(taskField);

      add.setOnAction((EventHandler<ActionEvent> & Serializable) ev -> {
        try {
          hours.get(Integer.parseInt(stunde.getText())).addTask(taskField.getText());
        } catch (NumberFormatException ex) {
          hours.get(0).addTask(taskField.getText());
        }
        stage.close();
      });

      root.getChildren().add(addTask);

      VBox taskBox = getVBoxWithTasks();
      root.getChildren().addAll(taskBox);

      stage.setAlwaysOnTop(true);
      stage.setScene(scene);
      stage.setTitle(toString());
      stage.setResizable(false);
      stage.initOwner(App.mainStage);
      stage.initModality(Modality.WINDOW_MODAL);
      stage.show();
    });

  }

  public Hour getHour(int hour) throws CalenderEntityOutOfBounceException {
    try {
      return hours.get(hour);
    } catch (IndexOutOfBoundsException e) {
      throw new CalenderEntityOutOfBounceException("Can't get hour: " + hour, e);
    }
  }

  private VBox getVBoxWithTasks() {
    VBox main = new VBox();
    ArrayList<Task> tasks = new ArrayList<>();
    hours.forEach((Consumer<? super Hour> & Serializable) hour -> tasks.addAll(hour.tasks));
    main.getChildren().addAll(tasks);
    main.setSpacing(2);
    return main;
  }

  @Override
  public String toString() {
    return owner.toString() + "." + day;
  }

  public void reload() {
    Text text = new Text(String.valueOf(day));
    text.setFill(Color.BLACK);
    text.setFont(Font.font("Arial Black", FontWeight.BOLD, 20));

    AtomicInteger tasks = new AtomicInteger();
    hours.forEach((Consumer<? super Hour> & Serializable) hour -> hour.tasks.forEach((Consumer<? super Task> & Serializable) task -> tasks.getAndIncrement()));

    double multiplied = tasks.get() * 0.15;
    double red = 1.0 / (multiplied + 1.0);

    Rectangle rectangle = new Rectangle(50, 50, Color.color(1, red, red));

    if (date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) text.setFill(Color.BLUE);
    if (date.isEqual(LocalDate.now())) text.setFill(Color.GREEN);

    setWidth(50);
    setHeight(50);

    getChildren().clear();
    getChildren().addAll(rectangle, text);
  }
}
