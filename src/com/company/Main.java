package com.company;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    private JFrame frame;
    private DiskPartition diskPartition;
    private FileSystem fileSystem;
    private JTree tree;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Main window = new Main();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Main() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        String[] sizeOfDiskPartition = { "1024", "2048", "4096", "8192", "16384" };

        String diskPartitionSizeSelecting = (String) JOptionPane.showInputDialog(frame,
                "Задайте размер дискового раздела", "Выбор размера", JOptionPane.QUESTION_MESSAGE, null,
                sizeOfDiskPartition, sizeOfDiskPartition[0]);

        String[] sizeOfDiskSector = { "2", "4", "6", "8", "10" };

        String diskSectorSizeSelecting = (String) JOptionPane.showInputDialog(frame, "Задайте размер сектора диска",
                "Выбор размера", JOptionPane.QUESTION_MESSAGE, null, sizeOfDiskSector, sizeOfDiskSector[0]);

        diskPartition = new DiskPartition(Integer.parseInt(diskPartitionSizeSelecting),
                Integer.parseInt(diskSectorSizeSelecting));

        frame = new JFrame();
        frame.setBackground(new Color(240, 240, 240));
        frame.setBounds(100, 100, 1014, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        PanelDisc panel = new PanelDisc(diskPartition);
        panel.setBackground(Color.WHITE);
        panel.setBounds(217, 10, 773, 325);
        frame.getContentPane().add(panel);

        fileSystem = new FileSystem(frame, diskPartition);

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("C:/");

        DefaultMutableTreeNode pictures = new DefaultMutableTreeNode("pictures");
        pictures.add(new DefaultMutableTreeNode("photo_dr.jpg"));
        pictures.add(new DefaultMutableTreeNode("screenshot1.png"));
        pictures.add(new DefaultMutableTreeNode("new_year.jpg"));

        DefaultMutableTreeNode documents = new DefaultMutableTreeNode("documents");
        documents.add(new DefaultMutableTreeNode("univer.docx"));
        documents.add(new DefaultMutableTreeNode("temp.txt"));
        documents.add(new DefaultMutableTreeNode("doklad.docx"));
        documents.add(new DefaultMutableTreeNode("homework.docx"));

        DefaultMutableTreeNode video = new DefaultMutableTreeNode("video");
        video.add(new DefaultMutableTreeNode("summer.avi"));
        video.add(new DefaultMutableTreeNode("lection.mp4"));

        DefaultMutableTreeNode music = new DefaultMutableTreeNode("music");
        music.add(new DefaultMutableTreeNode("471646.mp3"));
        music.add(new DefaultMutableTreeNode("new_track.mp3"));
        music.add(new DefaultMutableTreeNode("dkdghdr03gbf.mp3"));
        music.add(new DefaultMutableTreeNode("dark.mp3"));
        music.add(new DefaultMutableTreeNode("evening.mp3"));

        root.add(pictures);
        root.add(documents);
        root.add(video);
        root.add(music);

        tree = new JTree(root);
        tree.addTreeSelectionListener(e -> {
            diskPartition.setSectorGreen();
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            fileSystem.setFoldersAndFilesRed(currentNode);
            panel.repaint();
        });
        tree.setBounds(10, 10, 197, 443);
        frame.getContentPane().add(tree);

        fileSystem.checkAdding(root);

        JButton btnCreateFolder = new JButton("<html>Создать папку</html>");
        btnCreateFolder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                fileSystem.addFileOrCatalog(false, tree);
            }
        });
        btnCreateFolder.setBounds(221, 366, 125, 21);
        frame.getContentPane().add(btnCreateFolder);

        JButton btnCreateFile = new JButton("<html>Создать файл</html>");
        btnCreateFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                fileSystem.addFileOrCatalog(true, tree);
                panel.repaint();
            }
        });
        btnCreateFile.setBounds(363, 366, 125, 21);
        frame.getContentPane().add(btnCreateFile);

        JButton btnCopy = new JButton("Копировать");
        btnCopy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                DefaultMutableTreeNode selectedFolder = fileSystem.chooseFolder(root);
                fileSystem.copyNode(currentNode, selectedFolder);
                tree.updateUI();
                panel.repaint();
            }
        });
        btnCopy.setBounds(221, 399, 125, 21);
        frame.getContentPane().add(btnCopy);

        JButton btnTransfer = new JButton("Переместить");
        btnTransfer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                DefaultMutableTreeNode selectedFolder = fileSystem.chooseFolder(root);
                selectedFolder.add(currentNode);
                fileSystem.transfer(currentNode);
                panel.repaint();
                tree.updateUI();
            }
        });
        btnTransfer.setBounds(363, 397, 125, 21);
        frame.getContentPane().add(btnTransfer);

        JButton btnDelete = new JButton("Удалить");
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                currentNode.removeFromParent();
                fileSystem.delete(currentNode);
                tree.updateUI();
                panel.repaint();
            }
        });
        btnDelete.setBounds(292, 430, 125, 21);
        frame.getContentPane().add(btnDelete);
    }
}
