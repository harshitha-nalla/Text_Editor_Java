import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class TextEditor {
    JTextArea textArea;

    public void start() {
        MyFrame mainFrame = new MyFrame(1000, 500, "Pratt_Text_Editor", JFrame.EXIT_ON_CLOSE, false, Color.white);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setLayout(new FlowLayout());

        ImageIcon appIcon = new ImageIcon("icons8-text-editor-64.png");
        textArea = new JTextArea();
        textArea.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        textArea.setBackground(mainFrame.getBackground());
        textArea.setForeground(Color.BLACK);
        textArea.setBackground(Color.white);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        MenuBar menu = new MenuBar(textArea);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(980, 480));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        mainFrame.setIconImage(appIcon.getImage());
        mainFrame.setJMenuBar(menu.menuBar);
        mainFrame.add(scrollPane);
        mainFrame.setVisible(true);
    }
}

class MyFrame extends JFrame {
    MyFrame(int width, int height, String Title, int defaultCloseOperation, boolean Resizable, Color color) {
        this.setSize(new Dimension(width, height));
        this.setDefaultCloseOperation(defaultCloseOperation);
        this.setResizable(Resizable);
        this.setTitle(Title);
        this.setBackground(color);
    }
}

class MenuBar {
    JMenuBar menuBar;

    MenuBar(JTextArea textArea) {
        menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem openItem = new JMenuItem("Open");
        openItem.setMnemonic(KeyEvent.VK_O);
        openItem.addActionListener(e -> openFunc(textArea));

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.setMnemonic(KeyEvent.VK_S);
        saveItem.addActionListener(e -> saveFunc(textArea));

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setMnemonic(KeyEvent.VK_E);
        exitItem.addActionListener(e -> exitFunc());

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);

        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);

        JMenuItem fontItem = new JMenuItem("Font");
        fontItem.setMnemonic(KeyEvent.VK_F);
        fontItem.addActionListener(e -> fontFunc(textArea));

        JMenuItem colorItem = new JMenuItem("Color");
        colorItem.setMnemonic(KeyEvent.VK_C);
        colorItem.addActionListener(e -> colorFunc(textArea));

        editMenu.add(fontItem);
        editMenu.add(colorItem);

        menuBar.add(editMenu);
    }

    public void openFunc(JTextArea textArea) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("C:\\"));
        int response = fileChooser.showOpenDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            textArea.setText("");
            File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            Scanner fileIn = null;
            try {
                fileIn = new Scanner(file);
                if (file.isFile()) {
                    while (fileIn.hasNextLine()) {
                        textArea.append(fileIn.nextLine() + "\n");
                    }
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } finally {
                if (fileIn != null) {
                    fileIn.close();
                }
            }
        }
    }

    public void saveFunc(JTextArea textArea) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("C:\\"));
        int response = fileChooser.showSaveDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            File file;
            PrintWriter fileOut = null;
            file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            try {
                fileOut = new PrintWriter(file);
                fileOut.println(textArea.getText());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } finally {
                if (fileOut != null) {
                    fileOut.close();
                }
            }
        }
    }

    public void exitFunc() {
        System.exit(0);
    }

    public void fontFunc(JTextArea textArea) {
        MyFrame newFrame = new MyFrame(500, 400, "Font", JFrame.DISPOSE_ON_CLOSE, false, Color.WHITE);
        newFrame.setLayout(null);

        JPanel lPanel = new JPanel();
        lPanel.setLayout(null);
        lPanel.setBounds(0, 0, 250, 400);
        JPanel rPanel = new JPanel();
        rPanel.setLayout(null);
        rPanel.setBounds(250, 0, 250, 400);

        JLabel fontLabel = new JLabel("Font Type: ");
        fontLabel.setBounds(30, 40, 125, 20);
        fontLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel fontStyleLabel = new JLabel("Font Style: ");
        fontStyleLabel.setBounds(30, 140, 125, 20);
        fontStyleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel fontSizeLabel = new JLabel("Font Size: ");
        fontSizeLabel.setBounds(30, 240, 125, 20);
        fontSizeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        String[] fontFamily = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        String[] fontType = {"Plain", "Bold", "Italic"};
        JSpinner fontSize = new JSpinner();

        fontSize.setValue(textArea.getFont().getSize());
        JComboBox<String> FFamily = new JComboBox<>(fontFamily);
        JComboBox<String> FType = new JComboBox<>(fontType);
        FFamily.setSelectedItem(textArea.getFont().getFamily());
        FType.setSelectedIndex(textArea.getFont().getStyle());

        FFamily.setBounds(0, 40, 200, 20);
        FType.setBounds(0, 140, 200, 20);
        fontSize.setBounds(75, 240, 50, 20);

        JButton save = new JButton("Save");
        save.setFont(new Font("Arial", Font.PLAIN, 12));
        save.setBounds(20, 310, 70, 20);
        save.setFocusable(false);
        save.addActionListener(e -> {
            textArea.setFont(new Font((String) FFamily.getSelectedItem(), FType.getSelectedIndex(), (int) fontSize.getValue()));
            newFrame.dispose();
        });

        JButton cancel = new JButton("Cancel");
        cancel.setFont(new Font("Arial", Font.PLAIN, 12));
        cancel.setBounds(130, 310, 80, 20);
        cancel.setFocusable(false);
        cancel.addActionListener(e -> {
            newFrame.dispose();
        });

        lPanel.add(fontLabel);
        lPanel.add(fontStyleLabel);
        lPanel.add(fontSizeLabel);
        lPanel.add(save);

        rPanel.add(FFamily);
        rPanel.add(FType);
        rPanel.add(fontSize);
        rPanel.add(cancel);

        newFrame.add(lPanel);
        newFrame.add(rPanel);

        newFrame.setVisible(true);
    }

    public void colorFunc(JTextArea textArea) {
        Color color = JColorChooser.showDialog(null, "Pick a font Color", textArea.getForeground());
        textArea.setForeground(color);
    }
}

