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

  public String getTime() {
    int hours = time / 3600;
            int minutes = (time % 3600) / 60;
            int seconds = time % 60;

            // 형식 지정
            StringBuilder formattedTime = new StringBuilder();
            if (hours > 0) {
                formattedTime.append(hours).append("시간 ");
            }
            if (minutes > 0) {
                formattedTime.append(minutes).append("분 ");
            }
            if (seconds > 0 || formattedTime.length() == 0) { // 초를 항상 표시 (시간과 분이 둘 다 0인 경우)
                formattedTime.append(seconds).append("초");
            }
            return formattedTime.toString();
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
