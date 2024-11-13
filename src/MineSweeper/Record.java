package MineSweeper;

import java.util.Scanner;

public class Record {
  private String name;
  private String level;
  private String mode;
  private int time;
  private int score;

  Record(String n, String l, String m, int t, int s) {
    name = n;
    level = l;
    mode = m;
    time = t;
    score = s;
  }

  public String getName() {
    return name;
  }

  public String getLevel() {
    return level;
  }

  public String getMode() {
    return mode;
  }

  public int getTime() {
    return time;
  }

  Record (Scanner sc) {
    name = sc.next();
    level = sc.next();
    mode = sc.next();
    time = sc.nextInt();
    score = sc.nextInt();
  }

  public int getScore() {
    return score;
  }

  public String toString() {
    return name + " " + level + " " + mode + " " + time + " " + score;
  }
}
