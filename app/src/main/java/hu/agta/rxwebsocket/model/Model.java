package hu.agta.rxwebsocket.model;

public class Model {

    private int lcount;
    private int rcount;

    private String type;
    private String pend;
    private String inprog;
    private String app;

    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getLcount() {
        return lcount;
    }

    public void setLcount(int lcount) {
        this.lcount = lcount;
    }

    public int getRcount() {
        return rcount;
    }

    public void setRcount(int rcount) {
        this.rcount = rcount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPend() {
        return pend;
    }

    public void setPend(String pend) {
        this.pend = pend;
    }

    public String getInprog() {
        return inprog;
    }

    public void setInprog(String inprog) {
        this.inprog = inprog;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }
}
