package com.company;

import javax.swing.tree.DefaultMutableTreeNode;

public class Folder {

    private DefaultMutableTreeNode newNode;
    private boolean isFile;

    public Folder(String name) {
        newNode = new DefaultMutableTreeNode(name, true);
        isFile = false;
    }

    public DefaultMutableTreeNode getFolder() {
        return newNode;
    }

}
