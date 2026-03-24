package iuh.fit.filesystem;

public class FileLeaf implements FileComponent{
    private String name;

    public FileLeaf(String name) {
        this.name = name;
    }

    @Override
    public void show(String indent) {
        System.out.println(indent + "- File: " + name);
    }
}
