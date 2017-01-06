package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import sun.plugin.javascript.navig.Anchor;
import sun.reflect.generics.tree.Tree;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;
import javafx.scene.paint.Color;

public class Controller {

    @FXML
    private TabPane tabbedPane;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private MenuItem closeItem;

    @FXML
    private SplitPane splitPane;

    @FXML
    private AnchorPane aPane;


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
        BorderStroke bs = new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3));
        Border b = new Border(bs);
        textArea.setBorder(b);
        tabbedPane.getTabs().add(new Tab("Untitled", textArea));
    }

    /**
     * Add a new tab with an empty textfield
     * @param event
     */
    @FXML
    void close(ActionEvent event) {
        tabbedPane.getTabs().remove(tabbedPane.getSelectionModel().getSelectedIndex());
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
     * Add in a file tree to the view
     */
    @FXML
    void setUpFileTree() {
        TreeView t = new TreeView(addNodes(new TreeItem("[files]"), new File(".")));
        t.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
        splitPane.getItems().set(0, t);
        splitPane.setDividerPosition(0, .25);
        //t.setMinSize(aPane.getWidth(), aPane.getMinHeight());
    }

    /**
     * Set up the file tree
     * @param currentTop
     * @param dir
     * @return
     */
    public TreeItem addNodes(TreeItem currentTop, File dir) {
        currentTop.setExpanded(true);
        //this only recognizes files in cwd.
        Node folderIcon = new ImageView(new Image(getClass().getResourceAsStream("folder@32x32.png")));
        String currentPath = dir.getPath();
        TreeItem<String> currentDir = new TreeItem<String>(currentPath);
        currentDir.setGraphic(folderIcon);
        if (!currentTop.getValue().equals("[files]")) {
            currentTop.getChildren().add(currentDir);
        }
        Vector ol = new Vector();
        String[] tmp = dir.list();
        for (int i = 0; i < tmp.length; i++) {
            ol.addElement(tmp[i]);
        }
        Collections.sort(ol, String.CASE_INSENSITIVE_ORDER);
        File f;
        Vector files = new Vector();
        for (int i = 0; i < ol.size(); i++) {
            String thisObject = (String) ol.elementAt(i);
            String newPath;
            if (currentPath.equals(".")) {
                newPath = thisObject;
            } else {
                newPath = new String(currentPath + File.separator + thisObject + File.separator);
            }
            if ((f = new File(newPath)).isDirectory() && !newPath.contains("git")) {
                addNodes(currentDir, f);
            } else {
                files.addElement(thisObject);
            }
        }
        TreeItem x;
        Node fileGraphic = new ImageView(new Image(getClass().getResourceAsStream("file-9@32x32.png")));
        for (int fnum = 0; fnum < files.size(); fnum++) {
            x = new TreeItem<String>((String) files.elementAt(fnum));
            //x.setGraphic(fileGraphic);
            currentDir.getChildren().add(x);
        }
        return currentDir;


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

    //consider creating a custom class to hold the  tree view which would extend from Treeview itself, so as to avoid issues
}
