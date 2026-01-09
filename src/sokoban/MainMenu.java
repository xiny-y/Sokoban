package sokoban;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

//游戏主菜单界面
public class MainMenu extends JFrame {
    private JButton startButton;
    private JButton levelSelectButton;
    private JButton exitButton;
    private JButton uploadCharacterButton; 

    public MainMenu() {
        initComponents();//初始化组件
        setupLayout();//设置布局
        setupEventHandlers();//设置事件处理

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("SOKOBAN V1.0");
        setSize(800, 600);
        setResizable(false);//窗口大小不可改变
        setLocationRelativeTo(null);//窗口居中显示
        setVisible(true);
    }

    private void initComponents() {
        startButton = new JButton("开始游戏");
        levelSelectButton = new JButton("选择关卡");
        exitButton = new JButton("退出游戏");
        uploadCharacterButton = new JButton("自定义角色");
        uploadCharacterButton.setFont(new Font("微软雅黑", Font.PLAIN, 10));

        // 设置按钮样式
        Font buttonFont = new Font("微软雅黑", Font.PLAIN, 24);
        startButton.setFont(buttonFont);
        levelSelectButton.setFont(buttonFont);
        exitButton.setFont(buttonFont);

        // 调整按钮大小
        startButton.setPreferredSize(new Dimension(300, 70));
        levelSelectButton.setPreferredSize(new Dimension(300, 70));
        exitButton.setPreferredSize(new Dimension(300, 70));

        // 自定义按钮外观
        styleButton.applyStyle(startButton);
        styleButton.applyStyle(levelSelectButton);
        styleButton.applyStyle(exitButton);
        styleButton.applyStyle(uploadCharacterButton);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());// 设置布局为 BorderLayout

        // 创建一个分层面板
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(800, 600));

        // 设置图片背景
        java.net.URL BG_URL = getClass().getClassLoader().getResource("resources/" + "background.png");
        JLabel background = null;
        if (BG_URL != null) {
            background = new JLabel(new ImageIcon(BG_URL));
        }
        background.setBounds(0, 0, 800, 600);
        layeredPane.add(background, JLayeredPane.DEFAULT_LAYER);// 添加背景到默认层

        // 标题面板
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("推   箱   子");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 54)); // 调整标题字体大小为 54
        titlePanel.add(titleLabel);
        titlePanel.setOpaque(false); // 设置标题面板为透明
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // 调整标题的边距，避免被遮挡
        titlePanel.setBounds(0, 0, 800, 120); 
        layeredPane.add(titlePanel, JLayeredPane.PALETTE_LAYER);

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null); // 使用绝对布局
        buttonPanel.setOpaque(false); // 设置按钮面板为透明

        // 设置按钮位置和大小
        startButton.setBounds(200, 50, 400, 90);
        levelSelectButton.setBounds(200, 160, 400, 90);
        exitButton.setBounds(200, 270, 400, 90);
        uploadCharacterButton.setBounds(680, 520, 100, 30);

        buttonPanel.add(startButton);
        buttonPanel.add(levelSelectButton);
        buttonPanel.add(exitButton);
        buttonPanel.add(uploadCharacterButton); 
        buttonPanel.setBounds(0, 150, 800, 400);
        layeredPane.add(buttonPanel, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(uploadCharacterButton, JLayeredPane.PALETTE_LAYER); 

        add(layeredPane, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenu.this.dispose();
                new GameFrame(1);
            }
        });

        levelSelectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLevelSelection();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        uploadCharacterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uploadCharacterImage();
            }
        });
    }
    
    private void showLevelSelection() {
        LevelSelectionDialog levelDialog = new LevelSelectionDialog(this);
        levelDialog.setVisible(true);
    }

    private void uploadCharacterImage() {
        JDialog optionDialog = new JDialog(this, "选择操作", true);
        optionDialog.setSize(300, 150);
        optionDialog.setLayout(null);
        optionDialog.setLocationRelativeTo(this);
        optionDialog.setUndecorated(true);

        JLabel messageLabel = new JLabel("请选择操作", SwingConstants.CENTER);
        messageLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        messageLabel.setBounds(50, 20, 200, 30);
        optionDialog.add(messageLabel);

        JButton uploadButton = new JButton("上传图片");
        uploadButton.setBounds(15, 70, 130, 40);
        styleButton.applyStyle(uploadButton);
        uploadButton.addActionListener(e -> {
            optionDialog.dispose();
            performUploadImage();
        });
        optionDialog.add(uploadButton);

        JButton defaultButton = new JButton("使用默认角色");
        defaultButton.setBounds(155, 70, 130, 40); 
        styleButton.applyStyle(defaultButton);
        defaultButton.addActionListener(e -> {
            optionDialog.dispose();
            useDefaultCharacter();
        });
        optionDialog.add(defaultButton);

        optionDialog.setVisible(true);
    }

    private void performUploadImage() {
        FileDialog fileDialog = new FileDialog(this, "选择角色图片", FileDialog.LOAD);
        fileDialog.setVisible(true);
        String directory = fileDialog.getDirectory();
        String fileName = fileDialog.getFile();

        if (directory != null && fileName != null) {
            java.io.File file = new java.io.File(directory, fileName);
            try {
                java.nio.file.Path destination = java.nio.file.Paths.get("src/resources/player_custom.png");
                BufferedImage originalImage = ImageIO.read(file);
                int originalWidth = originalImage.getWidth();
                int originalHeight = originalImage.getHeight();
                int squareSize = Math.min(originalWidth, originalHeight);
                int x = (originalWidth - squareSize) / 2;
                int y = (originalHeight - squareSize) / 2;
                BufferedImage croppedImage = originalImage.getSubimage(x, y, squareSize, squareSize);

                int targetWidth = 100;
                int targetHeight = 100;
                BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = resizedImage.createGraphics();
                g2d.drawImage(croppedImage, 0, 0, targetWidth, targetHeight, null);
                g2d.dispose();
                ImageIO.write(resizedImage, "png", destination.toFile());

                showSuccessDialog("角色图片上传成功！");
            } catch (Exception ex) {
                showErrorDialog("上传失败: " + ex.getMessage());
            }
        }
    }

    private void useDefaultCharacter() {
        java.nio.file.Path destination = java.nio.file.Paths.get("src/resources/player_custom.png");
        try {
            java.nio.file.Files.deleteIfExists(destination);
            showSuccessDialog("已切换为默认角色！");
        } catch (Exception ex) {
            showSuccessDialog("已切使用默认角色！"); 
        }
    }

    private void showSuccessDialog(String message) {
        JDialog successDialog = new JDialog(this, "操作成功", true);
        successDialog.setSize(300, 120);
        successDialog.setLayout(null);
        successDialog.setLocationRelativeTo(this);
        successDialog.setUndecorated(true);

        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        messageLabel.setBounds(50, 20, 200, 30);
        successDialog.add(messageLabel);

        JButton okButton = new JButton("确定");
        okButton.setBounds(100, 70, 100, 30);
        styleButton.applyStyle(okButton);
        okButton.addActionListener(e -> successDialog.dispose());
        successDialog.add(okButton);

        successDialog.setVisible(true);
    }

    private void showErrorDialog(String message) {
        JDialog errorDialog = new JDialog(this, "操作失败", true);
        errorDialog.setSize(300, 120);
        errorDialog.setLayout(null);
        errorDialog.setLocationRelativeTo(this);
        errorDialog.setUndecorated(true);

        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        messageLabel.setBounds(50, 20, 200, 30);
        errorDialog.add(messageLabel);

        JButton okButton = new JButton("确定");
        okButton.setBounds(100, 70, 100, 30);
        styleButton.applyStyle(okButton);
        okButton.addActionListener(e -> errorDialog.dispose());
        errorDialog.add(okButton);

        errorDialog.setVisible(true);
    }
}
