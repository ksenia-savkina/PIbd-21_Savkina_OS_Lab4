package com.company;

import java.awt.*;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

class DiskPartition {

    private final Sector[] arrSectors;

    public DiskPartition(int discPartionSize, int diskSectorSize) {
        arrSectors = new Sector[discPartionSize / diskSectorSize];
    }

    public void draw(Graphics g, int width, int height) {
        int CELL_WIDTH = 15;
        int rowLength = width / CELL_WIDTH - 1;
        int CELL_HEIGHT = 15;
        int i = 0;
        int j = 0;
        for (Sector arrSector : arrSectors) {
            if (arrSector == null) {
                g.setColor(Color.gray);
            } else if (arrSector.getType() == 1) {
                g.setColor(Color.blue);
            } else if (arrSector.getType() == 2) {
                g.setColor(Color.green);
            } else if (arrSector.getType() == 3) {
                g.setColor(Color.red);
            }
            int top = i * CELL_HEIGHT;
            int left = j * CELL_WIDTH;
            g.fillRect(left, top, CELL_WIDTH, CELL_HEIGHT);
            g.setColor(Color.black);
            g.drawRect(left, top, CELL_WIDTH, CELL_HEIGHT);
            j++;
            if (j > rowLength) {
                i++;
                j = 0;
            }
        }
    }

    private int findSector(int size) {
        int k = 0;
        for (Sector arrSector : arrSectors) {
            if (arrSector == null) {
                k++;
            }
        }
        if (k >= size) {
            for (int i = 0; i < arrSectors.length; i++) {
                if (arrSectors[i] == null) {
                    return i;
                }
            }
        }
        return -100;
    }

    public void addSector(int size, DefaultMutableTreeNode node, Frame frame) {
        int i = findSector(size);
        if (i == -100)
            JOptionPane.showMessageDialog(frame, "Недостаточно места на диске!");
        else {
            Sector newSector = new Sector(i, 1, node);
            while (size != 0) {
                if (arrSectors[i] != null) {
                    i = findSector(size);
                }
                arrSectors[i] = newSector;
                size--;
                i++;
            }
        }
    }

    public void setSectorGreen() {
        for (Sector arrSector : arrSectors) {
            if (arrSector != null) {
                if (arrSector.getType() == 1 || arrSector.getType() == 3) {
                    arrSector.setType(2);
                }
            }
        }
    }

    public void deleteSector(DefaultMutableTreeNode node) {
        for (int i = 0; i < arrSectors.length; i++) {
            if (arrSectors[i] != null) {
                if (arrSectors[i].getNode().equals(node)) {
                    arrSectors[i] = null;
                }
            }
        }
    }

    public void transferSector(DefaultMutableTreeNode node) {
        int index = 0;
        int size = 0;
        for (Sector arrSector : arrSectors) {
            if (arrSector != null) {
                if (arrSector.getNode().equals(node)) {
                    index = arrSector.getIndex();
                    size++;
                }
            }
        }
        deleteSector(node);
        addSectorIndex(index, size, node);
    }

    private void addSectorIndex(int index, int size, DefaultMutableTreeNode node) {
        int i = index;
        Sector newSector = new Sector(i, 1, node);
        while (size != 0) {
            arrSectors[i] = newSector;
            size--;
            i++;
        }
    }

    public void setSectorRed(DefaultMutableTreeNode node) {
        for (Sector arrSector : arrSectors) {
            if (arrSector != null) {
                if (arrSector.getNode().equals(node)) {
                    arrSector.setType(3);
                }
            }
        }
    }
}