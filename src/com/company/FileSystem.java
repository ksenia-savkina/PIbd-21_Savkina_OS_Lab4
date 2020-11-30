package com.company;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.util.ArrayList;
import java.util.Enumeration;

public class FileSystem {

    private final Frame frame;
    private final DiskPartition diskPartition;
    final private String[] typesOfFile = { "jpg", "png", "docx", "txt", "avi", "mp4", "mp3" };

    public FileSystem (Frame frame, DiskPartition diskPartition) {
        this.frame = frame;
        this.diskPartition = diskPartition;
    }

    public void addFileOrCatalog(boolean isFile, JTree tree) {
        frame.setTitle("Input Dialog in Frame");
        frame.setVisible(true);
        frame.setResizable(false);
        if (isFile) {
            String m = JOptionPane.showInputDialog(frame, "Введите название файла");
            if (!m.isEmpty()) {
                DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                if (currentNode.getAllowsChildren()) {
                    String typeSelecting = (String) JOptionPane.showInputDialog(frame, "Выберите тип файла",
                            "Выбор типа", JOptionPane.QUESTION_MESSAGE, null, typesOfFile, typesOfFile[0]);
                    if (!typeSelecting.isEmpty()) {
                        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(m + "." + typeSelecting, false);
                        File file = new File(m,typeSelecting);
                        currentNode.add(file.getFile());
                        addToDisc(file.getFile());
                    } else
                        JOptionPane.showMessageDialog(frame, "Тип файла не выбран! Файл не создан.");
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "Нельзя создавать файл в файле, выберите папку! Файл не создан.");
                }
            } else
                JOptionPane.showMessageDialog(frame, "Вы не ввели название! Файл не создан.");
        } else {
            String m = JOptionPane.showInputDialog(frame, "Введите название папки");
            if (!m.isEmpty()) {
                DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                if (currentNode.getAllowsChildren()) {
                    Folder folder = new Folder(m);
                    currentNode.add(folder.getFolder());
                } else
                    JOptionPane.showMessageDialog(frame,
                            "Нельзя создавать папку в файле, выберите папку! Папка не создана.");
            } else
                JOptionPane.showMessageDialog(frame, "Вы не ввели название! Папка не создана.");
        }
        tree.updateUI();
    }

    public ArrayList<String> getFolders(DefaultMutableTreeNode root) {
        ArrayList<String> leafs = new ArrayList<>();
        String str = "";
        getFoldersDop(root, leafs, str);
        leafs.add(0, root.getUserObject().toString());
        return leafs;
    }

    private void getFoldersDop(DefaultMutableTreeNode parent, ArrayList<String> leafs, String str) {
        String[] checkStr = splitCharacters(parent.getUserObject().toString());
        if (checkStr[1].equals("")) {
            str += parent.getUserObject().toString() + " / ";
            Enumeration<TreeNode> children = parent.children();
            while (children.hasMoreElements()) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) children.nextElement();
                if (!node.isLeaf()) {
                    leafs.add(str + node.getUserObject().toString());
                    getFoldersDop(node, leafs, str);
                }
            }
        }
    }

    private int countChildren(DefaultMutableTreeNode parent, String name) {
        Enumeration<TreeNode> children = parent.children();
        int k = 0;
        while (children.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) children.nextElement();
            StringBuilder str = new StringBuilder(node.getUserObject().toString());
            String strDop = str.delete(str.length() - 1, str.length()).toString();
            if (node.getUserObject().toString().equals(name) || strDop.equals(name + " Копия ")) {
                k++;
            }
        }
        return k;
    }

    private String[] splitCharacters(String str) {
        StringBuilder sb = new StringBuilder(str);
        String[] nameAndType = new String[2];
        String tempType = "";
        for (String type : typesOfFile) {
            if (sb.indexOf(type) != -1) {
                tempType = type;
                sb.delete(sb.indexOf(type), sb.indexOf(type) + type.length());
            }
        }
        nameAndType[0] = sb.toString();
        nameAndType[1] = tempType;
        return nameAndType;
    }

    public void copyNode(DefaultMutableTreeNode currentNode, DefaultMutableTreeNode selectedFolder) {
        String[] checkStr = splitCharacters(currentNode.getUserObject().toString());
        DefaultMutableTreeNode newNode = (DefaultMutableTreeNode) currentNode.clone();
        if (countChildren(selectedFolder, currentNode.getUserObject().toString()) != 0) {
            newNode = new DefaultMutableTreeNode(currentNode.getUserObject().toString() + " Копия "
                    + countChildren(selectedFolder, currentNode.getUserObject().toString()));
        }
        selectedFolder.add(newNode);
        if (!checkStr[1].equals("")) {
            addToDisc(newNode);
        } else {
            Enumeration<TreeNode> children = currentNode.children();
            while (children.hasMoreElements()) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) children.nextElement();
                copyNode(node, newNode);
            }
        }
    }

    public DefaultMutableTreeNode chooseFolder(DefaultMutableTreeNode root) {
        ArrayList<String> folderName = getFolders(root);
        String folderSelecting = (String) JOptionPane.showInputDialog(frame,
                "Выберите папку для копирования/перемещения", "Выбор папки", JOptionPane.QUESTION_MESSAGE, null,
                folderName.toArray(new String[folderName.size()]), folderName.get(0));
        DefaultMutableTreeNode selectedFolder = findFolder(folderSelecting, root);
        return selectedFolder;
    }

    private DefaultMutableTreeNode findFolder(String parentName, DefaultMutableTreeNode parent) {
        if(parent.getUserObject().toString().equals(parentName)) {
            return parent;
        }
        String[] foldersName = parentName.split(" / ");
        int k = 1;
        Enumeration<TreeNode> children = parent.children();
        DefaultMutableTreeNode node = new DefaultMutableTreeNode();
        for (int i = 1; i < foldersName.length; ) {
            while (children.hasMoreElements()) {
                node = (DefaultMutableTreeNode) children.nextElement();
                if ( node.getUserObject().toString().equals(foldersName[i])) {
                    k++;
                    if (k == foldersName.length) {
                        return node;
                    } else {
                        children = node.children();
                        i++;
                    }
                }
            }
        }
        return null;
    }

    private ArrayList<DefaultMutableTreeNode> getFiles(DefaultMutableTreeNode root) {
        ArrayList<DefaultMutableTreeNode> leafs = new ArrayList<>();
        getFilesDop(root, leafs);
        return leafs;
    }

    private void getFilesDop(DefaultMutableTreeNode parent, ArrayList<DefaultMutableTreeNode> leafs) {
        Enumeration<TreeNode> children = parent.children();
        while (children.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) children.nextElement();
            if (node.isLeaf()) {
                leafs.add(node);
            } else {
                getFilesDop(node, leafs);
            }
        }
    }

    public void checkAdding(DefaultMutableTreeNode root) {
        ArrayList<DefaultMutableTreeNode> arrCheck = getFiles(root);
        for (DefaultMutableTreeNode node : arrCheck) {
            addToDisc(node);
        }
    }

    private void addToDisc(DefaultMutableTreeNode node) {
        int size = 0;
        String[] checkStr = splitCharacters(node.getUserObject().toString());
        if (!checkStr[1].equals("")) {
            switch (checkStr[1]) {
                case "jpg":
                    size = 7;
                    break;
                case "png":
                    size = 6;
                    break;
                case "docx":
                    size = 3;
                    break;
                case "txt":
                    size = 1;
                    break;
                case "avi":
                    size = 15;
                    break;
                case "mp4":
                    size = 10;
                    break;
                case "mp3":
                    size = 5;
                    break;
            }
            diskPartition.addSector(size, node, frame);
        } else {
            Enumeration<TreeNode> children = node.children();
            while (children.hasMoreElements()) {
                DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) children.nextElement();
                addToDisc(currentNode);
            }
        }
    }

    public void setFoldersAndFilesRed(DefaultMutableTreeNode currentNode) {
        String[] checkStr = splitCharacters(currentNode.getUserObject().toString());
        if (!checkStr[1].equals("")) {
            diskPartition.setSectorRed(currentNode);
        } else {
            Enumeration<TreeNode> children = currentNode.children();
            while (children.hasMoreElements()) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) children.nextElement();
                setFoldersAndFilesRed(node);
            }
        }
    }

    public void transfer(DefaultMutableTreeNode currentNode) {
        String[] checkStr = splitCharacters(currentNode.getUserObject().toString());
        if (!checkStr[1].equals("")) {
            diskPartition.transferSector(currentNode);
        } else {
            Enumeration<TreeNode> children = currentNode.children();
            while (children.hasMoreElements()) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) children.nextElement();
                transfer(node);
            }
        }
    }

    public void delete(DefaultMutableTreeNode currentNode) {
        String[] checkStr = splitCharacters(currentNode.getUserObject().toString());
        if (!checkStr[1].equals("")) {
            diskPartition.deleteSector(currentNode);
        } else {
            Enumeration<TreeNode> children = currentNode.children();
            while (children.hasMoreElements()) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) children.nextElement();
                delete(node);
            }
        }
    }
}
