package net.nandgr.eth.report.beans;

public class Edge {

    private int from;
    private int to;
    private String arrows = "to";
    private boolean physics = false;
    private Smooth smooth = new Smooth();

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public String getArrows() {
        return arrows;
    }

    public void setArrows(String arrows) {
        this.arrows = arrows;
    }

    public boolean isPhysics() {
        return physics;
    }

    public void setPhysics(boolean physics) {
        this.physics = physics;
    }

    public Smooth getSmooth() {
        return smooth;
    }

    public void setSmooth(Smooth smooth) {
        this.smooth = smooth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (from != edge.from) return false;
        if (to != edge.to) return false;
        return arrows != null ? arrows.equals(edge.arrows) : edge.arrows == null;
    }

    @Override
    public int hashCode() {
        int result = from;
        result = 31 * result + to;
        result = 31 * result + (arrows != null ? arrows.hashCode() : 0);
        return result;
    }
}
