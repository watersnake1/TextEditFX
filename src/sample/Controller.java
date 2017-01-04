package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import sun.reflect.generics.tree.Tree;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

public class Controller {

    @FXML
    private TabPane tabbedPane;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TreeView FileTree;


    @FXML
    void exit(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void about(ActionEvent event) {
        JOptionPane.showMessageDialog(new JFrame(), "JavaFX TextEditor version 0.0.1");
    }

    /**
     * Add a new tab with an empty textfield
     * @param event
     */
    @FXML
    void addTab(ActionEvent event) {
        //tabbedPane.getTabs().add(new Tab("Untitled"));
        TextArea textArea = new TextArea();
        tabbedPane.getTabs().add(new Tab("untitled", textArea));
    }

    /**
     * Clear the text in the selected text field
     */
    @FXML
    void clear() {
        TextArea t = (TextArea) getCurrentTabContent(tabbedPane);
        t.clear();
    }

    /**
     * Save a file with the text in the selected editorpane
     */
    @FXML
    void save() {
        FileChooser f = new FileChooser();
        File target = f.showSaveDialog(new Stage());
        try {
            FileWriter outBound = new FileWriter(target);
            TextArea t = (TextArea) getCurrentTabContent(tabbedPane);
            outBound.write(t.getText());
            outBound.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getCurrentTab(tabbedPane).setText(target.getName());
    }

    /**
     * Open a file from the filesystem and set its text as the text of the editor
     */
    @FXML
    void open() {
        FileChooser f = new FileChooser();
        File target = f.showOpenDialog(new Stage());
        FileReader inBound = null;
        try {
            inBound = new FileReader(target);
        }
        catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(new JFrame(), "File Not Found");
        } catch (IOException e) {
            e.printStackTrace();
        }
        char[] buffer = new char[100000000];
        int n = 0;
        try {
            n = inBound.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String text = new String(buffer, 0, n);
        TextArea t = (TextArea) getCurrentTabContent(tabbedPane);
        t.setText(text);
        Tab tab = getCurrentTab(tabbedPane);
        tab.setText(target.getName());
    }

    /**
     * return the contents of the currently selected tab in a tabpane.
     * cast the node to whatever it needs to be.
     * @param tabbedPane
     * @return
     */
    Node getCurrentTabContent(TabPane tabbedPane) {
       return tabbedPane.getSelectionModel().getSelectedItem().getContent();
    }

    /**
     * Get the currently selected tab object
     * @param tabbedPane
     * @return
     */
    Tab getCurrentTab(TabPane tabbedPane) {
        return tabbedPane.getSelectionModel().getSelectedItem();
    }

    void configureFileTree(TreeView tree) {


    }

    //remains to be seen if this code makes any sense with javafx (it probably does not)
    TreeItem addNodes(TreeItem top, File dir) {
        String currentPath = dir.getPath();
        TreeItem currentDirectory = new TreeItem(currentPath);
        if (top != null) {
            top.getChildren().add(currentDirectory);
        }
        Vector ol = new Vector();
        String[] tmp = dir.list();
        for (int i = 0; i < tmp.length; i++)
            ol.addElement(tmp[i]);
        Collections.sort(ol, String.CASE_INSENSITIVE_ORDER);
        File f;
        Vector files = new Vector();
        for (int i = 0; i < ol.size(); i++) {
            String thisObject = (String) ol.elementAt(i);
            String newPath;

            if (currentPath.equals(".")) {
                newPath = currentPath;
            } else {
                newPath = new String(currentPath + File.separator + thisObject + "/");
            }
            if ((f = new File(newPath)).isDirectory()) {
                addNodes(currentDirectory, f);
            } else {
                files.addElement(thisObject);
            }
        }
        for (int fnum = 0; fnum < files.size(); fnum++) {
            currentDirectory.getChildren().add(new TreeItem(files.elementAt(fnum)));
        }
        return currentDirectory;

    }
}
