package ifmo.programming.lab5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Room implements Comparable<Room> {
    private String wallcolor;
    private int width, height, length;
    private ArrayList<Thing> shelf;

    Room(int width, int height, int length) {
        this(width, height, length, "");
    }

    Room(int width, int height, int length, String wallcolor, Thing... things) {
        setWidth(width);
        setHeight(height);
        setLength(length);
        setWallcolor(wallcolor);
        shelf = new ArrayList<Thing>(Arrays.asList(things));

    }

    public ArrayList<Thing> getShelf() { return shelf; }

    String getWallcolor() { return wallcolor; }
    void setWallcolor(String wallcolor) {
        if (wallcolor == null) { throw new IllegalArgumentException("Устанавливаемый цвет не должен быть null"); }
        this.wallcolor = wallcolor;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getLength() { return length; }

    private void setWidth(int width) { this.width = width; }
    private void setHeight(int height) { this.height = height; }
    private void setLength(int length) { this.length = length; }


    @Override
    public int compareTo(Room o) {
        return getWidth()*getHeight()*getLength() - o.getWidth()*o.getHeight()*getLength();
    }

    @Override
    public String toString() {
        String roominfo = new String("Комната ");
        if (wallcolor.isEmpty()) {
            roominfo = roominfo.concat("с прозрачными стенами, ");
        }
        else {
            String color = "цвета " + wallcolor + ", ";
            roominfo = roominfo.concat(color);
        }
        roominfo = roominfo.concat("имеющая размеры: " + width + " x " + height + " x " + length + ", ");
        if (shelf.size() == 0) {roominfo = roominfo.concat("пустая.");}
        else {
            roominfo = roominfo.concat("содержащая " + shelf.size() + " предметов.");
        }
        return roominfo;

    }

    public static class Thing extends Furnishings {
        private int size;
        public void setSize (int size) {this.size = size;}
        public int getSize () {return size;}

        Thing(String name, int size) {
            this.setName(name);
            this.setSize(size);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return width == room.width &&
                height == room.height &&
                length == room.length &&
                Objects.equals(wallcolor, room.wallcolor) &&
                Objects.equals(shelf, room.shelf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wallcolor, width, height, length, shelf);
    }
}
