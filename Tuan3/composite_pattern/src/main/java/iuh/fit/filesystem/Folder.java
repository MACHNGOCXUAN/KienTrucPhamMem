package iuh.fit.filesystem;

import java.util.ArrayList;
import java.util.List;

public class Folder implements FileComponent{
    private String name;
    private List<FileComponent> children = new ArrayList<>();

    public Folder(String name) {
        this.name = name;
    }

    public void add(FileComponent component) {
        children.add(component);
    }

    public void remove(FileComponent component) {
        children.remove(component);
    }

    @Override
    public void show(String indent) {
        System.out.println(indent + "+ Folder: " + name);
        for (FileComponent c : children) {
            c.show(indent + "   ");
        }
    }
}
