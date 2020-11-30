package com.company;

import javax.swing.tree.DefaultMutableTreeNode;

public class File {

    private DefaultMutableTreeNode newNode;
    private boolean isFile;

    public File(String name, String expansion) {
        newNode = new DefaultMutableTreeNode(name + "." + expansion, false);
        isFile = true;
    }

    public DefaultMutableTreeNode getFile() {
        return newNode;
    }

}
