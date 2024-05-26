package ch.malbun.kalender.ui;

import ch.malbun.kalender.App;
import ch.malbun.kalender.ui.exceptions.CalenderEntityOutOfBounceException;
import ch.malbun.kalender.ui.time.Day;
import ch.malbun.kalender.ui.time.Month;
import ch.malbun.kalender.ui.time.Year;
import ch.malbun.kalender.ui.enums.Levels;
import ch.malbun.kalender.ui.enums.Months;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class CalenderView extends VBox {

  public LocalDate current = LocalDate.now();
  public Levels currentLevel = Levels.DAY;
  private GridPane content = new GridPane();
  private Text path;
  private ArrayList<Year> years = new ArrayList<>();

  public CalenderView() {
    super();
    BorderPane top = new BorderPane();
    BorderPane bottom = new BorderPane();

    setStyle("-fx-background-color: gray");
    top.setStyle("-fx-background-color: gray");
    content.setStyle("-fx-background-color: gray");
    bottom.setStyle("-fx-background-color: gray");

    for(int i = 0; i < 15; i++) {
      Year year = new Year(2024 + i);
      years.add(i, year);
    }

    path = new Text();
    path.setText(Month.getGermanMonthName(current.getMonthValue()) + " " + current.getYear());
    path.setFont(new Font(18));
    path.setUnderline(true);
    path.setOnMouseClicked((EventHandler<? super MouseEvent> & Serializable) e -> {
      if (currentLevel == Levels.DAY) currentLevel = Levels.MONTH;
      reload();
    });
    top.setCenter(path);

    Image upImage = new Image(String.valueOf(App.class.getResource("Arrow.png")), 50, 50, false, true);
    ImageView up = new ImageView(upImage);
    up.setRotate(-90);
    top.setLeft(up);
    up.setOnMouseClicked((EventHandler<? super MouseEvent> & Serializable) e -> {
      if (currentLevel == Levels.DAY) {
        if (current.getMonthValue() == 1) {
          if (current.getYear() != 2024) {
            current = LocalDate.of(current.getYear() - 1, 12, current.getDayOfMonth());
            reload();
          }
        } else {
          current = LocalDate.of(current.getYear(), current.getMonthValue() - 1, current.getDayOfMonth());
          reload();
        }
      } else {
        if (current.getYear() != 2024) {
          current = LocalDate.of(current.getYear() - 1, current.getMonthValue(), current.getDayOfMonth());
          reload();
        }
      }
    });

    ImageView down = new ImageView(upImage);
    down.setRotate(90);
    top.setRight(down);
    down.setOnMouseClicked((EventHandler<? super MouseEvent> & Serializable) e -> {
      if (currentLevel == Levels.DAY) {
        if (current.getMonthValue() == 12) {
          if (current.getYear() != 2034) {
            current = LocalDate.of(current.getYear() + 1, 1, current.getDayOfMonth());
            reload();
          }
        } else {
          current = LocalDate.of(current.getYear(), current.getMonthValue() + 1, current.getDayOfMonth());
          reload();
        }
      } else {
        if (current.getYear() != 2034) {
          current = LocalDate.of(current.getYear() + 1, current.getMonthValue(), current.getDayOfMonth());
          reload();
        }
      }
    });

    Text today = new Text("Aktueller Monat");
    today.setFont(new Font(18));
    today.setUnderline(true);
    bottom.setCenter(today);
    today.setOnMouseClicked(e -> {
      current = LocalDate.now();
      currentLevel = Levels.DAY;
      reload();
    });

    getChildren().add(top);
    getChildren().add(content);
    getChildren().add(bottom);

    content.setHgap(2);
    content.setVgap(2);
    reload();
  }

  public void reload() {
    switch (currentLevel) {
      case DAY -> {
        path.setText(Month.getGermanMonthName(current.getMonthValue()) + " " + current.getYear());
        content.getChildren().clear();

        Year year = getYear(current.getYear());
        Month month = year.getMonth(Months.byValue(current.getMonthValue()));
        month.reload();

        int row = 0;
        for (Day day : month.days) {
          int weekday = day.date.getDayOfWeek().getValue();
          content.add(day, weekday-1, row);
          if (weekday == 7) row++;
        }
      }

      case MONTH -> {
        path.setText(String.valueOf(current.getYear()));

        content.getChildren().clear();

        Year currentYear = getYear(current.getYear());

        content.add(currentYear.january, 0, 0);
        content.add(currentYear.february, 1, 0);
        content.add(currentYear.march, 2, 0);
        content.add(currentYear.april, 3, 0);
        content.add(currentYear.may, 0, 1);
        content.add(currentYear.june, 1, 1);
        content.add(currentYear.july, 2, 1);
        content.add(currentYear.august, 3, 1);
        content.add(currentYear.september, 0, 2);
        content.add(currentYear.october, 1, 2);
        content.add(currentYear.november, 2, 2);
        content.add(currentYear.december, 3, 2);
      }
    }
    save();
  }

  public void save() {
    Thread save = new Thread(() -> {
      File outFileTemp = new File(App.tempDir, "calender.cald");
      File outFile = new File(App.homeDir, "calender.caldz");

      JSONObject root = new JSONObject();

      years.forEach(year -> {
        for (Months months : Months.values()) {
          year.getMonth(months).days.forEach(day -> {
            day.hours.forEach(hour -> {
              hour.tasks.forEach(task -> {
                JSONObject tasks = new JSONObject();
                tasks.put("d", year.year + "." + months.getVal() + "." + day.day + "." + hour.hour);
                tasks.put("e", task.task);
                root.append("tasks", tasks);
              });
            });
          });
        }
      });

      try (FileOutputStream fos = new FileOutputStream(outFileTemp)) {
        fos.write(root.toString(2).getBytes());
        fos.flush();

      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      try {
        FileOutputStream fos = new FileOutputStream(outFile);
        ZipOutputStream zos = new ZipOutputStream(fos);
        FileInputStream fis = new FileInputStream(outFileTemp);

        ZipEntry zipEntry = new ZipEntry("1");
        zos.putNextEntry(zipEntry);

        byte[] bytes = new byte[8192];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
          zos.write(bytes, 0, length);
        }

        zos.close();
        fos.close();
        fis.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
    save.start();
  }

  public void saveWithoutThread() throws IOException {
    File outFileTemp = new File(App.tempDir, "calender.cald");
    File outFile = new File(App.homeDir, "calender.caldz");

    JSONObject root = new JSONObject();

    years.forEach(year -> {
      for (Months months : Months.values()) {
        year.getMonth(months).days.forEach(day -> {
          day.hours.forEach(hour -> {
            hour.tasks.forEach(task -> {
              JSONObject tasks = new JSONObject();
              tasks.put("d", year.year + "." + months.getVal() + "." + day.day + "." + hour.hour);
              tasks.put("e", task.task);
              root.append("tasks", tasks);
            });
          });
        });
      }
    });

    try (FileOutputStream fos = new FileOutputStream(outFileTemp)) {
      fos.write(root.toString(2).getBytes());
      fos.flush();

    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    FileOutputStream fos = new FileOutputStream(outFile);
    ZipOutputStream zos = new ZipOutputStream(fos);
    FileInputStream fis = new FileInputStream(outFileTemp);

    ZipEntry zipEntry = new ZipEntry("1");
    zos.putNextEntry(zipEntry);

    byte[] bytes = new byte[8192];
    int length;
    while((length = fis.read(bytes)) >= 0) {
      zos.write(bytes, 0, length);
    }

    zos.close();
    fos.close();
    fis.close();

  }

  public void load() throws IOException, CalenderEntityOutOfBounceException {
    File toLoad = new File(App.homeDir, "calender.caldz");
    if (toLoad.exists()) {
      ZipFile zipFile = new ZipFile(toLoad);
      BufferedReader bf = new BufferedReader(new InputStreamReader(zipFile.getInputStream(zipFile.getEntry("1"))));
      StringBuilder sb = new StringBuilder();
      bf.lines().forEach(sb::append);
      JSONObject root = new JSONObject(sb.toString());
      if (!root.isEmpty()) {
        JSONArray tasks = root.getJSONArray("tasks");
        for (int i = 0; i < tasks.length(); i++) {
          JSONObject task = tasks.getJSONObject(i);
          String d = task.getString("d");
          String e = task.getString("e");

          String[] dates = d.split("\\.");
          getYear(Integer.parseInt(dates[0])).getMonth(Months.byValue(Integer.parseInt(dates[1]))).getDay(Integer.parseInt(dates[2])).getHour(Integer.parseInt(dates[3])).addTask(e);
        }

      }
      zipFile.close();
    }

  }

  public Year getYear(int year) {
    return years.get(year - 2024);
  }

}
