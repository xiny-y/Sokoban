package sokoban;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class styleButton {
    public static void applyStyle(JButton button) {
        button.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 绘制浅色圆角矩形背景
                GradientPaint gradient = new GradientPaint(0, 0, new Color(173, 216, 230), 0, c.getHeight(), new Color(224, 255, 255));
                g2.setPaint(gradient); // 浅色渐变
                g2.fill(new RoundRectangle2D.Float(0, 0, c.getWidth(), c.getHeight(), 20, 20)); // 圆角矩形

                super.paint(g, c);
            }
        });
        button.setOpaque(false); // 设置按钮背景透明
        button.setBorderPainted(false); // 禁用边框绘制
    }
}
