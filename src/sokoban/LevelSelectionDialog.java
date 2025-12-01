package sokoban;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 关卡选择对话框
 */
public class LevelSelectionDialog extends JDialog {
    private JButton[] levelButtons;
    private JButton backButton;

    public LevelSelectionDialog(Frame parent) {
        super(parent, "选择关卡", true);
        initializeComponents();//初始化组件
        setupLayout();//设置布局
        setupEventHandlers();
        setSize(600, 400);
        setLocationRelativeTo(parent);
    }

    private void initializeComponents() {
        // 创建关卡按钮 (1-9关)
        levelButtons = new JButton[9];
        for (int i = 0; i < levelButtons.length; i++) {
            levelButtons[i] = new JButton("第 " + (i + 1) + " 关");
            levelButtons[i].setFont(new Font("微软雅黑", Font.PLAIN, 16));
            levelButtons[i].setPreferredSize(new Dimension(120, 50));
        }

        backButton = new JButton("返回");
        backButton.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        backButton.setPreferredSize(new Dimension(100, 40));
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // 关卡按钮网格
        JPanel levelPanel = new JPanel(new GridLayout(3, 3, 10, 10));
        levelPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        for (JButton button : levelButtons) {
            levelPanel.add(button);
        }

        add(levelPanel, BorderLayout.CENTER);

        // 底部按钮
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(backButton);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 20));
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void setupEventHandlers() {
        // 为每个关卡按钮添加事件监听器
        for (int i = 0; i < levelButtons.length; i++) {
            final int levelIndex = i + 1;
            levelButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectLevel(levelIndex);
                }
            });
        }
        // 返回按钮事件监听器
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void selectLevel(int level) {
        dispose();
        Window parent = SwingUtilities.getWindowAncestor(this);
        if (parent != null) {
            parent.dispose();
        }
        new GameFrame(level);
    }
}
