package us.bridgeses.minder_tasks.models;

/**
 * Basic model of a category. Immutable
 */
public class Category {

    private String name;
    private int color;

    public Category(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }
}
