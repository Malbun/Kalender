package ch.malbun.kalender.ui.time;

import ch.malbun.kalender.App;
import ch.malbun.kalender.observableList.ObservableEvent;
import ch.malbun.kalender.observableList.ObservableList;
import ch.malbun.kalender.ui.Task;

import java.io.Serial;
import java.io.Serializable;

public class Hour implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  public Day owner;
  public int hour;
  public ObservableList<Task> tasks = new ObservableList<>();

  protected Hour(Day owner, int hour) {
    this.owner = owner;
    this.hour = hour;

    tasks.setOnElementAdd((ObservableEvent<Task> & Serializable) () -> {
      App.calenderView.getYear(App.calenderView.current.getYear()).reloadAll();
    });
  }

  public void addTask(String desc) {
    Task task = new Task(desc, this);
    tasks.add(task);
  }

  @Override
  public String toString() {
    return owner.toString() + ":" + hour;
  }
}
