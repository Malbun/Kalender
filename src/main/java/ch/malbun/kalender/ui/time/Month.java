package ch.malbun.kalender.ui.time;

import ch.malbun.kalender.App;
import ch.malbun.kalender.ui.enums.Levels;
import ch.malbun.kalender.ui.enums.Months;
import ch.malbun.kalender.ui.exceptions.CalenderEntityOutOfBounceException;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Month extends StackPane implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  public ArrayList<Day> days = new ArrayList<>();
  public Year owner;
  public Months month;

  protected Month(Months months, Year owner) {
    this.owner = owner;
    month = months;

    String nameGerman = getGermanMonthName(months);

    Rectangle rectangle = new Rectangle(120, 120, Color.WHITE);
    setOnMouseClicked((EventHandler<? super MouseEvent> & Serializable) e -> {
      App.calenderView.current = LocalDate.of(App.calenderView.current.getYear(), months.getVal(), App.calenderView.current.getDayOfMonth());
      App.calenderView.currentLevel = Levels.DAY;
      App.calenderView.reload();
    });

    Text text = new Text(nameGerman);
    text.setFont(Font.font("Arial Black", FontWeight.NORMAL, 18));

    setWidth(120);
    setHeight(120);
    getChildren().addAll(rectangle, text);

    switch (months) {
      case JANUARY, MARCH, MAY, JULY, AUGUST, OCTOBER, DECEMBER:
        for (int i=0; i<31; i++) {
          Day day = new Day(this, i+1);
          days.add(i, day);
        }
        break;

      case APRIL, JUNE, SEPTEMBER, NOVEMBER:
        for (int i=0; i<30; i++) {
          Day day = new Day(this, i+1);
          days.add(i, day);
        }
        break;

      case FEBRUARY:
        if (owner.isLeapYear()) {
          for (int i=0; i<29; i++) {
            Day day = new Day(this, i+1);
            days.add(i, day);
          }
        } else {
          for (int i=0; i<28; i++) {
            Day day = new Day(this, i+1);
            days.add(i, day);
          }
        }
        break;
    }
  }

  public static String getGermanMonthName(Months months) {
    String nameGerman = "";

    switch (months) {
      case JANUARY -> nameGerman = "Januar";
      case FEBRUARY -> nameGerman = "Februar";
      case MARCH -> nameGerman = "März";
      case APRIL -> nameGerman = "April";
      case MAY -> nameGerman = "Mai";
      case JUNE -> nameGerman = "Juni";
      case JULY -> nameGerman = "Juli";
      case AUGUST -> nameGerman = "August";
      case SEPTEMBER -> nameGerman = "September";
      case OCTOBER -> nameGerman = "Oktober";
      case NOVEMBER -> nameGerman = "November";
      case DECEMBER -> nameGerman = "Dezember";
    }
    return nameGerman;
  }

  public static String getGermanMonthName(int month) {
    String nameGerman = "";

    switch (month) {
      case 1 -> nameGerman = "Januar";
      case 2 -> nameGerman = "Februar";
      case 3 -> nameGerman = "März";
      case 4 -> nameGerman = "April";
      case 5 -> nameGerman = "Mai";
      case 6 -> nameGerman = "Juni";
      case 7 -> nameGerman = "Juli";
      case 8 -> nameGerman = "August";
      case 9 -> nameGerman = "September";
      case 10 -> nameGerman = "Oktober";
      case 11 -> nameGerman = "November";
      case 12 -> nameGerman = "Dezember";
    }
    return nameGerman;
  }

  public Day getDay(int day) throws CalenderEntityOutOfBounceException {
    try {
      return days.get(day - 1);
    } catch (IndexOutOfBoundsException e) {
      throw new CalenderEntityOutOfBounceException("Can't get day: " + day, e);
    }
  }

  @Override
  public String toString() {
    return owner.toString() + "." + month.toString();
  }

  public void reload() {
    days.forEach(Day::reload);
  }

}
