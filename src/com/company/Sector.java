package com.company;

import javax.swing.tree.DefaultMutableTreeNode;

public class Sector {

    private final int index;
    private int type;
    private final DefaultMutableTreeNode node;

    public Sector(int index, int type, DefaultMutableTreeNode node) {
        this.index = index;
        this.type = type;
        this.node = node;
    }

    public int getType() {
        return type;
    }

    public void setType(int value) {
        type = value;
    }

    public DefaultMutableTreeNode getNode() {
        return node;
    }

    public int getIndex() {
        return index;
    }
}