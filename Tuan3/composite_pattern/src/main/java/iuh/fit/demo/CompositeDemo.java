package iuh.fit.demo;

import iuh.fit.filesystem.FileLeaf;
import iuh.fit.filesystem.Folder;
import iuh.fit.ui.Button;
import iuh.fit.ui.Panel;

public class CompositeDemo {
    public static void main(String[] args) {
        // FILE SYSTEM
        System.out.println("=== FILE SYSTEM ===");

        Folder root = new Folder("Root");
        Folder documents = new Folder("Documents");
        Folder images = new Folder("Images");

        root.add(new FileLeaf("readme.txt"));
        root.add(documents);
        root.add(images);

        documents.add(new FileLeaf("doc1.pdf"));
        documents.add(new FileLeaf("doc2.docx"));

        images.add(new FileLeaf("img1.png"));
        images.add(new FileLeaf("img2.jpg"));

        root.show("");

        // UI SYSTEM
        System.out.println("\n=== UI SYSTEM ===");

        Panel mainPanel = new Panel("Main Panel");
        Panel subPanel = new Panel("Sub Panel");

        mainPanel.add(new Button("Login"));
        mainPanel.add(new Button("Register"));
        mainPanel.add(subPanel);

        subPanel.add(new Button("Submit"));
        subPanel.add(new Button("Cancel"));

        mainPanel.render("");
    }
}
