package sokoban;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;


public class GameFrame extends JFrame {
    private static final String IMAGE_PATH = "resources/";
    private static final String MAP_PATH = "map/";
    private static final int MAX_LEVEL = 9;
    private int px, py;
    private int[][] map ;
    //最多四个箱子，箱子坐标
    private int[][] boxPositions;
    private int currentLevel;

    public GameFrame(int level) {
        this.currentLevel = level;

        loadMap(currentLevel); // 加载地图

        initFrame(); // 初始化窗口

        initMoveAction(); // 添加键盘监听

        initImage(); // 初始化图片和菜单

        this.setVisible(true);
    }
    private void movePlayer(int dx, int dy) {
        int newX  = px + dx, newY = py + dy;
        //检查新位置是否为墙
        if (map[newX][newY] == 1) {
            return; //不能移动
        }
        //检查是否有箱子
        for (int b = 0; b < boxPositions.length; b++) {
            if (newX == boxPositions[b][0] && newY == boxPositions[b][1]) {
                //计算箱子的新位置
                int boxNewX = boxPositions[b][0] + dx;
                int boxNewY = boxPositions[b][1] + dy;
                //检查箱子新位置是否为墙或其他箱子
                if (map[boxNewX][boxNewY] == 1) {
                    return; //箱子不能移动
                }
                for (int bb = 0; bb < boxPositions.length; bb++) {
                    if (bb != b && boxNewX == boxPositions[bb][0] && boxNewY == boxPositions[bb][1]) {
                        return; //箱子不能移动
                    }
                }
                //移动箱子
                boxPositions[b][0] = boxNewX;
                boxPositions[b][1] = boxNewY;
                break;
            }
        }
        //移动玩家
        px = newX;
        py = newY;
        this.getContentPane().removeAll();//删除所有组件
        initImage();//重新绘制图片和菜单
        this.repaint();//刷新界面
        checkWin();

    }
    private void checkWin() {
        //检查所有箱子是否都在目标位置
        for (int b = 0; b < boxPositions.length; b++) {
            int x = boxPositions[b][0];
            int y = boxPositions[b][1];
            if (map[x][y] != 9) {
                return; //有箱子不在目标位置，未获胜
            }
        }

        //所有箱子都在目标位置，获胜
        if(currentLevel < MAX_LEVEL){
            JDialog levelDialog = new JDialog(this);
            levelDialog.setSize(300, 120);
            levelDialog.setLayout(null);//使用绝对布局
            levelDialog.setLocationRelativeTo(this);
            levelDialog.setUndecorated(true);//删除标题栏

            JLabel messageLabel = new JLabel("恭喜你，过关了！", SwingConstants.CENTER);
            messageLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
            messageLabel.setBounds(50, 20, 200, 30); // 调整位置
            levelDialog.add(messageLabel);

            JButton nextLevelButton = new JButton("下一关");
            nextLevelButton.setBounds(50, 70, 80, 30);
            styleButton.applyStyle(nextLevelButton);
            nextLevelButton.addActionListener(e -> {
                levelDialog.dispose();
                this.dispose();
                new GameFrame(currentLevel + 1);
            });
            levelDialog.add(nextLevelButton);

            JButton exitButton = new JButton("主菜单");
            exitButton.setBounds(170, 70, 80, 30);
            styleButton.applyStyle(exitButton);
            exitButton.addActionListener(e -> {
                levelDialog.dispose();
                this.dispose();
                new MainMenu();
            });
            levelDialog.add(exitButton);

            levelDialog.setVisible(true);
        }
        else{
            JDialog finishDialog = new JDialog(this);
            finishDialog.setSize(300, 120);
            finishDialog.setLayout(null);
            finishDialog.setLocationRelativeTo(this);
            finishDialog.setUndecorated(true);

            JLabel messageLabel = new JLabel("恭喜完成最后关卡！", SwingConstants.CENTER);
            messageLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
            messageLabel.setBounds(20, 20, 240, 30); // 调整位置
            finishDialog.add(messageLabel);

            JButton mainMenuButton = new JButton("返回主菜单");
            mainMenuButton.setBounds(40, 70, 100, 30);
            styleButton.applyStyle(mainMenuButton);
            mainMenuButton.addActionListener(e -> { //e 代表事件对象ActionEvent -> 事件源
                finishDialog.dispose();
                this.dispose();
                new MainMenu();
            });
            finishDialog.add(mainMenuButton);

            JButton quitGameButton = new JButton("退出游戏");
            quitGameButton.setBounds(160, 70, 100, 30);
            styleButton.applyStyle(quitGameButton);
            quitGameButton.addActionListener(e -> { 
                finishDialog.dispose();
                System.exit(0);
            });
            finishDialog.add(quitGameButton);

            finishDialog.setVisible(true);
        }
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
        //第一行为人物坐标，第二行为箱子个数num_box，2~num_box+1行为箱子坐标，剩下为地图

        String resourcePath = MAP_PATH + level + ".txt";

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(resourcePath))))
        {
            String line;
            //人物
            if ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");//按空格分割
                px = Integer.parseInt(parts[0]);
                py = Integer.parseInt(parts[1]);
            }
            //箱子个数
            int numBox = 0;
            if((line = reader.readLine()) != null) {
                numBox = Integer.parseInt(line.trim());
            }
            //箱子坐标
            boxPositions = new int[numBox][2];
            for (int b = 0; b < numBox; b++) {
                if ((line = reader.readLine()) != null) {
                    String[] parts = line.trim().split("\\s+");
                    boxPositions[b][0] = Integer.parseInt(parts[0]);
                    boxPositions[b][1] = Integer.parseInt(parts[1]);
                }
            }
            
            map = new int[6][8];
            int i = 0;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                String[] numbers = line.split("\\s+");
                for (int j = 0; j < numbers.length; j++) {
                    map[i][j] = Integer.parseInt(numbers[j]);//parseInt 将字符串转换为整数
                }
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int tileSize = 100; // 每个格子的大小（像素）
        this.setSize(8 * tileSize, 6 * tileSize + 30); // 设置窗口大小
        this.setLocationRelativeTo(null); // 窗口居中显示
    }

    private void initMenuButton() {
        JButton menuButton = new JButton("菜单");
        styleButton.applyStyle(menuButton);
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

        JDialog menuDialog = new JDialog(this, "菜单", true);
        menuDialog.setSize(300, 150);
        menuDialog.setLayout(null);//使用绝对布局
        menuDialog.setLocationRelativeTo(this);//居中显示
        menuDialog.setUndecorated(true);//删除标题栏

        JButton backButton = new JButton("返回游戏");
        backButton.setBounds(100, 20, 100, 30);
        menuDialog.add(backButton);

        JButton restartButton = new JButton("重新开始");
        restartButton.setBounds(100, 60, 100, 30);
        menuDialog.add(restartButton);
        
        JButton exitButton = new JButton("主菜单");
        exitButton.setBounds(100, 100, 100, 30);
        menuDialog.add(exitButton);

        styleButton.applyStyle(backButton);
        styleButton.applyStyle(restartButton);
        styleButton.applyStyle(exitButton);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //回到游戏
                GameFrame.this.setEnabled(true);
                GameFrame.this.requestFocus();
                menuDialog.dispose();
            }
        });
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuDialog.dispose();
                GameFrame.this.dispose();//关闭菜单
                new GameFrame(currentLevel);//重新开始当前关卡
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
        this.setResizable(false);//窗口大小不可改变
        this.setLayout(null);//绝对布局

        // 设置背景图片
        java.net.URL BG_URL = getClass().getClassLoader().getResource("resources/background_game.png");
        if (BG_URL != null) {
            JLabel background = new JLabel(new ImageIcon(BG_URL));
            background.setBounds(0, 0, 800, 600);
            this.setContentPane(background);
        }
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
                    case 9:
                        imageName = "target";
                        break;
                }
                if (i == px && j == py) {
                    imageName = "player";
                } 
                else {
                    for (int b = 0; b < boxPositions.length; b++) {
                        if (i == boxPositions[b][0] && j == boxPositions[b][1]) {
                            imageName = "box";
                            if(map[i][j] == 9){
                                imageName = "box_correct";
                            }
                            break;
                        }
                    }
                }
                if(imageName.equals("place")){
                    continue;//空地不绘制图片
                }
                imageName += ".png";
                java.net.URL imgURL = getClass().getClassLoader().getResource(IMAGE_PATH + imageName);
                JLabel label = new JLabel(new ImageIcon(imgURL));
                label.setBounds(j * 100, i * 100, 100, 100);
                this.add(label);
            }
        }
    }
}
