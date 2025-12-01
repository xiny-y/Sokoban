package sokoban;// GameMenu.java

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 游戏主菜单界面
 */
public class MainMenu extends JFrame {
    private JButton startButton;
    private JButton levelSelectButton;
    private JButton exitButton;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public MainMenu() {


        initializeComponents();//初始化组件
        setupLayout();
        setupEventHandlers();


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("SOKOBAN V1.0");
        setSize(800, 600);
        setResizable(false);//窗口大小不可改变
        setLocationRelativeTo(null);//窗口居中显示
        setVisible(true);
    }

    private void initializeComponents() {
        startButton = new JButton("开始游戏");
        levelSelectButton = new JButton("选择关卡");
        exitButton = new JButton("退出游戏");

        // 设置按钮样式
        Font buttonFont = new Font("微软雅黑", Font.PLAIN, 18);
        startButton.setFont(buttonFont);
        levelSelectButton.setFont(buttonFont);
        exitButton.setFont(buttonFont);

        startButton.setPreferredSize(new Dimension(200, 50));
        levelSelectButton.setPreferredSize(new Dimension(200, 50));
        exitButton.setPreferredSize(new Dimension(200, 50));
    }

    private void setupLayout() {
        setLayout(new BorderLayout());// 设置布局为 BorderLayout

        //设置图片背景
//        java.net.URL BG_URL = getClass().getClassLoader().getResource("resources/" + "background.png");
//        JLabel background = null;
//        if (BG_URL != null) {
//            background = new JLabel(new ImageIcon(BG_URL));
//        }
//        background.setBounds(0, 0, 800, 600);
//        background.setLayout(null);// 设置布局为 null
//        setContentPane(background);//设置内容面板


        // 标题面板
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("推   箱   子");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 36));// Font.BOLD
        titlePanel.add(titleLabel);
        titlePanel.setOpaque(false); // 设置标题面板为透明
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        add(titlePanel, BorderLayout.NORTH);

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 0, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 150, 50, 150));
        buttonPanel.setOpaque(false); // 设置按钮面板为透明
        buttonPanel.add(startButton);
        buttonPanel.add(levelSelectButton);
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.CENTER);

//        // 底部面板
//        JPanel bottomPanel = new JPanel();
//        JLabel copyrightLabel = new JLabel("cloudy");
//        copyrightLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
//        bottomPanel.add(copyrightLabel);
//        add(bottomPanel, BorderLayout.SOUTH);

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
    }


    private void showLevelSelection() {
        LevelSelectionDialog levelDialog = new LevelSelectionDialog(this);
        levelDialog.setVisible(true);
    }


}
