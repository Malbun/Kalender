package ch.malbun.kalender.observableList;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ObservableList<T> implements List<T>, Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  private final ArrayList<T> mainList;
  private ObservableEvent<T> onEdit;

  public ObservableList() {
    mainList = new ArrayList<>();
  }

  public ObservableList(int size) {
    mainList = new ArrayList<>(size);
  }

  public void setOnElementAdd(ObservableEvent<T> elementAdd) {
    onEdit = elementAdd;
  }

  @Override
  public int size() {
    return mainList.size();
  }

  @Override
  public boolean isEmpty() {
    return mainList.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return mainList.contains(o);
  }

  @Override
  public Iterator<T> iterator() {
    return mainList.iterator();
  }

  @Override
  public Object[] toArray() {
    return mainList.toArray();
  }

  @Override
  public <T1> T1[] toArray(T1[] a) {
    return mainList.toArray(a);
  }

  @Override
  public boolean add(T t) {
    boolean result = mainList.add(t);
    onEdit.action();
    return result;
  }

  @Override
  public boolean remove(Object o) {
    boolean result = mainList.remove(o);
    onEdit.action();
    return result;
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return mainList.containsAll(c);
  }

  @Override
  public boolean addAll(Collection<? extends T> c) {
    boolean result = mainList.addAll(c);
    onEdit.action();
    return result;
  }

  @Override
  public boolean addAll(int index, Collection<? extends T> c) {
    boolean result = mainList.addAll(index, c);
    onEdit.action();
    return result;
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    boolean result = mainList.removeAll(c);
    onEdit.action();
    return result;
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    boolean result = mainList.retainAll(c);
    onEdit.action();
    return result;
  }

  @Override
  public void clear() {
    mainList.clear();
    onEdit.action();
  }

  @Override
  public T get(int index) {
    return mainList.get(index);
  }

  @Override
  public T set(int index, T element) {
    T result = mainList.set(index, element);
    onEdit.action();
    return result;
  }

  @Override
  public void add(int index, T element) {
    mainList.add(index, element);
    onEdit.action();
  }

  @Override
  public T remove(int index) {
    T result = mainList.remove(index);
    onEdit.action();
    return result;
  }

  @Override
  public int indexOf(Object o) {
    return mainList.indexOf(o);
  }

  @Override
  public int lastIndexOf(Object o) {
    return mainList.lastIndexOf(o);
  }

  @Override
  public ListIterator<T> listIterator() {
    return mainList.listIterator();
  }

  @Override
  public ListIterator<T> listIterator(int index) {
    return mainList.listIterator(index);
  }

  @Override
  public List<T> subList(int fromIndex, int toIndex) {
    return mainList.subList(fromIndex, toIndex);
  }
}
