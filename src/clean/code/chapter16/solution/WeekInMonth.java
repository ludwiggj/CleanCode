package clean.code.chapter16.solution;

public enum WeekInMonth {
  FIRST(1), SECOND(2), THIRD(3), FOURTH(4), LAST(0);
  private final int index;

  WeekInMonth(int index) {
    this.index = index;
  }

  public int toInt() {
    return index;
  }
}