package ifmo.programming.lab5;

import java.util.Objects;

public abstract class Furnishings {
    private String name;

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    Furnishings (String name) {setName(name);}
    Furnishings () {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Furnishings that = (Furnishings) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Furnishings{" +
                "name='" + name + '\'' +
                '}';
    }
}
