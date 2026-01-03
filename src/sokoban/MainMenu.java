package sokoban;// GameMenu.java

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.geom.RoundRectangle2D;
import java.awt.Color;
import java.awt.GradientPaint;

/**
 * 游戏主菜单界面
 */
public class MainMenu extends JFrame {
    private JButton startButton;
    private JButton levelSelectButton;
    private JButton exitButton;

    public MainMenu() {


        initializeComponents();//初始化组件
        setupLayout();
        setupEventHandlers();


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("SOKOBAN V1.0");
        setSize(800, 600);
        setResizable(false);//窗口大小不可改变
        setLocationRelativeTo(null);//窗口居中显示
        //setUndecorated(true);//去除边框
        setVisible(true);
    }

    private void initializeComponents() {
        startButton = new JButton("开始游戏");
        levelSelectButton = new JButton("选择关卡");
        exitButton = new JButton("退出游戏");

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
        JButton[] buttons = {startButton, levelSelectButton, exitButton};
        for (JButton button : buttons) {
            button.setUI(new BasicButtonUI() {
                @Override
                public void paint(Graphics g, JComponent c) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // 绘制浅色圆角矩形背景
                    GradientPaint gradient = new GradientPaint(0, 0, new Color(173, 216, 230), 0, c.getHeight(), new Color(224, 255, 255));
                    g2.setPaint(gradient);
                    g2.fill(new RoundRectangle2D.Float(0, 0, c.getWidth(), c.getHeight(), 30, 30));

                    // 绘制按钮文字
                    super.paint(g, c);
                }
            });
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setForeground(Color.DARK_GRAY);
        }
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
        layeredPane.add(background, JLayeredPane.DEFAULT_LAYER);

        // 标题面板
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("推   箱   子");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 54)); // 调整标题字体大小为 54
        titlePanel.add(titleLabel);
        titlePanel.setOpaque(false); // 设置标题面板为透明
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // 调整标题的边距，避免被遮挡
        titlePanel.setBounds(0, 0, 800, 120); // 增加标题面板的高度
        layeredPane.add(titlePanel, JLayeredPane.PALETTE_LAYER);

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null); // 使用绝对布局
        buttonPanel.setOpaque(false); // 设置按钮面板为透明

        // 设置按钮位置和大小
        startButton.setBounds(200, 50, 400, 90);
        levelSelectButton.setBounds(200, 160, 400, 90);
        exitButton.setBounds(200, 270, 400, 90);

        buttonPanel.add(startButton);
        buttonPanel.add(levelSelectButton);
        buttonPanel.add(exitButton);
        buttonPanel.setBounds(0, 150, 800, 400);
        layeredPane.add(buttonPanel, JLayeredPane.PALETTE_LAYER);

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
    }


    private void showLevelSelection() {
        LevelSelectionDialog levelDialog = new LevelSelectionDialog(this);
        levelDialog.setVisible(true);
    }


}
