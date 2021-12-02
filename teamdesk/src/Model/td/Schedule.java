package Model.td;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;

public class Schedule extends ArrayList<Schedule> {
    private LocalTime startTime1;
    private LocalTime endTime1;
    private LocalTime startTime2;
    private LocalTime endTime2;
    private boolean timeCourse1 = false;
    private String dayWeek;
    private boolean timeCourse2 = true;
    private String idMaquina;
    private int idSemana;

    public Schedule() {

    }

    public Schedule(LocalTime startTime1, LocalTime endTime1, boolean timeCourse1,
                    String dayWeek, LocalTime startTime2, LocalTime endTime2, boolean timeCourse2, String idMaquina, int idSemana) {
        this.startTime1 = startTime1;
        this.endTime1 = endTime1;
        this.timeCourse1 = timeCourse1;
        this.dayWeek = dayWeek;
        this.startTime2 = startTime2;
        this.endTime2 = endTime2;
        this.timeCourse2 = timeCourse2;
        this.idMaquina = idMaquina;
        this.idSemana = idSemana;
    }

    public LocalTime getStartTime1() {
        return startTime1;
    }

    public void setStartTime1(LocalTime startTime1) {
        this.startTime1 = startTime1;
    }

    public LocalTime getEndTime1() {
        return endTime1;
    }

    public void setEndTime1(LocalTime endTime1) {
        this.endTime1 = endTime1;
    }

    public LocalTime getStartTime2() {
        return startTime2;
    }

    public void setStartTime2(LocalTime startTime2) {
        this.startTime2 = startTime2;
    }

    public LocalTime getEndTime2() {
        return endTime2;
    }

    public void setEndTime2(LocalTime endTime2) {
        this.endTime2 = endTime2;
    }

    public boolean isTimeCourse1() {
        return timeCourse1;
    }

    public void setTimeCourse1(boolean timeCourse) {
        this.timeCourse1 = timeCourse1;
    }

    public boolean isTimeCourse2() {
        return timeCourse2;
    }

    public void setTimeCourse2(boolean timeCourse2) {
        this.timeCourse2 = timeCourse2;
    }

    public String getDayWeek() {
        return dayWeek;
    }

    public void setDayWeek(String dayWeek) {
        this.dayWeek = dayWeek;

    }

    public String getTimeCourseToString() {
        if (isTimeCourse1()) {
            return "Perido 1";
        }
        if (isTimeCourse2()){
            return "Periodo 2";
        }
        return null;
    }

    public String getIdMaquina() {
        return idMaquina;
    }

    public void setIdMaquina(String idMaquina) {
        this.idMaquina = idMaquina;
    }

    public int getIdSemana() {
        return idSemana;
    }

    public void setIdSemana(int idSemana) {
        this.idSemana = idSemana;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "startTime1=" + startTime1 +
                ", endTime1=" + endTime1 +
                ", startTime2=" + startTime2 +
                ", endTime2=" + endTime2 +
                ", timeCourse1=" + timeCourse1 +
                ", dayWeek='" + dayWeek + '\'' +
                ", timeCourse2=" + timeCourse2 +
                '}';
    }
}
