package io.swapastack.dunetd.Towers;

public class IterableOverMap implements MapIterable{
    private int color = 0;
    private int length = Integer.MAX_VALUE;
    private int[] vorgaenger = {-1, -1};
    private int coordX, coordZ;

    @Override
    public int getPathLength() {
        return length;
    }

    @Override
    public void setPathLength(int length) {
        this.length = length;
    }

    @Override
    public int getPathColor() {
        return color;
    }

    @Override
    public void setPathColor(int color) {
        this.color = color;
    }

    public void setVorgaenger(int x, int z) {
        vorgaenger[0] = x;
        vorgaenger[1] = z;
    }

    public int[] getVorgaenger() {
        return vorgaenger;
    }

    public int[] getPosition() {
        int[] p = new int[2];
        p[0] = coordX;
        p[1] = coordZ;
        return p;
    }
    public void setPosition(int x, int z) {
        coordX = x;
        coordZ = z;
    }
}
