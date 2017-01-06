package sample;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.io.File;
import java.util.Collections;
import java.util.Vector;

/**
 * Created by christian on 1/4/17.
 */
public class FileTree extends TreeView {

    public FileTree() {
        addNodes(null, new File("."));
    }

    /**
     * Set up the file tree
     * @param currentTop
     * @param dir
     * @return
     */
    public TreeItem addNodes(TreeItem currentTop, File dir) {
        String currentPath = dir.getPath();
        TreeItem<String> currentDir = new TreeItem<String>(currentPath);
        if (currentTop != null) {
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
            if ((f = new File(newPath)).isDirectory()) {
                addNodes(currentDir, f);
            } else {
                files.addElement(thisObject);
            }
        }
        for (int fnum = 0; fnum < files.size(); fnum++) {
            currentDir.getChildren().add(new TreeItem<String>((String) files.elementAt(fnum)));
        }
        return currentDir;
    }
}
