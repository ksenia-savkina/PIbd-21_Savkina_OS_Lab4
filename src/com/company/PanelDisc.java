package com.company;

import java.awt.Graphics;

import javax.swing.JPanel;

public class PanelDisc extends JPanel {
    private final DiskPartition disc;

    public PanelDisc(DiskPartition disc) {
        this.disc = disc;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        disc.draw(g, this.getWidth(), this.getHeight());
    }
}
