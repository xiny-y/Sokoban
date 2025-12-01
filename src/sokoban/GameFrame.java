package sokoban;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;

public class GameFrame extends JFrame {
    private static final String IMAGE_PATH = "resources/";
    private static final String MAP_PATH = "map/";
    private int px, py;
    private int[][] map ;
    public GameFrame(int level) {

        initFrame();//初始化窗口

        initMoveAction();//添加键盘监听

        loadMap(level);//加载地图

        initImage();//初始化图片和菜单

        this.setVisible(true);
    }
    private void movePlayer(int dx, int dy) {
        int newX  = px + dx, newY = py + dy;
        if (map[newX][newY] == 1) {
            return;
        }
        if(map[newX][newY] == 8){
            int boxX = newX + dx, boxY = newY + dy;
            if (map[boxX][boxY] == 1 || map[boxX][boxY] == 8) {
                return;
            }
            map[newX][newY] = 5;
            map[boxX][boxY] = 8;
            map[px][py] = 0;
            px = newX;
            py = newY;
        }
        else{
            map[newX][newY] = 5;
            map[px][py] = 0;
            px = newX;
            py = newY;
        }
        this.getContentPane().removeAll();//删除所有组件
        initImage();//重新绘制图片和菜单
        this.repaint();//刷新界面

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
                        movePlayer(-1, 0);
                        break;
                    case KeyEvent.VK_DOWN:
                    case KeyEvent.VK_S:
                        //向下移动
                        movePlayer(1, 0);
                        break;
                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_A:
                        //向左移动
                        movePlayer(0, -1);
                        break;
                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_D:
                        //向右移动
                        movePlayer(0, 1);
                        break;
                }
            }
        });
        this.setFocusable(true);//获取焦点
    }
    private void loadMap(int level) {
        map = new int[6][8];
        String resourcePath = MAP_PATH + level + ".txt";

        BufferedReader reader = null;
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath);
            if (is != null) {
                reader = new BufferedReader(new InputStreamReader(is));
            } else {
                // fallback: relative to working directory (用于调试或运行时工作目录包含 map 文件夹)
                String filePath = resourcePath;
                reader = new BufferedReader(new FileReader(filePath));
            }

            String line;
            int i = 0;
            while ((line = reader.readLine()) != null && i < map.length) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] numbers = line.split("\\s+");
                for (int j = 0; j < Math.min(numbers.length, map[i].length); j++) {
                    map[i][j] = Integer.parseInt(numbers[j]);
                    if (map[i][j] == 5) {
                        px = i;
                        py = j;
                    }
                }
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (reader != null) reader.close(); } catch (IOException ignored) {}
        }
    }

    private void initMenuButton() {
        JButton menuButton = new JButton("菜单");
        menuButton.setBounds(700, 15, 70, 30);
        //点击菜单弹出小界面
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //弹出小界面菜单
                MenuDialog();
            }
        });
        this.add(menuButton);

    }

    private void MenuDialog() {

        JDialog menuDialog = new JDialog(this, "菜单", true); // modal dialog
        menuDialog.setSize(300, 200);
        menuDialog.setLayout(null);//使用绝对布局
        menuDialog.setLocationRelativeTo(this);//居中显示
        menuDialog.setUndecorated(true);//删除标题栏

        JButton backButton = new JButton("返回游戏");
        backButton.setBounds(100, 50, 100, 30);
        menuDialog.add(backButton);
        JButton exitButton = new JButton("主菜单");
        exitButton.setBounds(100, 100, 100, 30);
        menuDialog.add(exitButton);



        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //回到游戏
                GameFrame.this.setEnabled(true);
                GameFrame.this.requestFocus();
                menuDialog.dispose();
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuDialog.dispose();
                GameFrame.this.dispose();//关闭菜单
                new MainMenu();
            }
        });

        menuDialog.setVisible(true);
    }

    private void initFrame() {
        this.setTitle("Sokoban V1.0");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//关闭窗口时退出程序
        this.setSize(800, 650);
        this.setLocationRelativeTo(null);//窗口居中显示
        this.setResizable(false);//窗口大小不可改变
        this.setLayout(null);//绝对布局
    }

    private void initImage() {

        initMenuButton();//添加菜单按钮

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                String imageName = "";
                switch (map[i][j]) {
                    case 0:
                        imageName = "place";
                        break;
                    case 1:
                        imageName = "wall";
                        break;
                    case 5:
                        imageName = "player";
                        break;
                    case 8:
                        imageName = "box";
                        break;
                    case 9:
                        imageName = "target";
                        break;
                }
                imageName += ".png";
                java.net.URL imgURL = getClass().getClassLoader().getResource(IMAGE_PATH + imageName);
                JLabel label = new JLabel(new ImageIcon(imgURL));
                label.setBounds(j * 100, i * 100, 100, 100);
                this.add(label);

//                JLabel label = new JLabel(new ImageIcon(IMAGE_PATH + imageName));
//                label.setBounds(j * 100, i * 100, 100, 100);
//                this.add(label);

            }
        }

    }
}
