package es.ulpgc.eite.clean.mvp.sample.app;


public class Timetable {
    private int idHour;
    private String hour;

    public Timetable(int idHour, String hour) {
        this.idHour = idHour;
        this.hour = hour;
    }

    public Timetable(){}

    public int getIdHour() {
        return idHour;
    }

    public void setIdHour(int idHour) {
        this.idHour = idHour;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

}