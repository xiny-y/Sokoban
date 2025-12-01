package sokoban;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MoveAction extends JPanel {
    public MoveAction(){
        initMoveAction();
    }
    private void initMoveAction(){
        //捕获键盘按键
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_W:
                        //向上移动
                        break;
                    case KeyEvent.VK_DOWN:
                    case KeyEvent.VK_S:
                        //向下移动
                        break;
                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_A:
                        //向左移动
                        break;
                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_D:
                        //向右移动
                        break;
                }
            }
        });
    }
}
