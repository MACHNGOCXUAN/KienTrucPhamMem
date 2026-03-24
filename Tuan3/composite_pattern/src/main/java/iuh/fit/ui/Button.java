package iuh.fit.ui;

public class Button implements UIComponent{
    private String name;

    public Button(String name) {
        this.name = name;
    }

    @Override
    public void render(String indent) {
        System.out.println(indent + "Button: " + name);
    }
}
