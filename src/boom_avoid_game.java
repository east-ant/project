import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//기본으로 깔리는 타일 그리드
class BG_grid extends JPanel{
    int Cell_size = 100; // 셀 크기
    // 기본틀
    public void paintGrid(Graphics g) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 5; y++) {
                g.setColor(Color.GRAY);
                g.fillRect(x * Cell_size, y * Cell_size, Cell_size, Cell_size);// 칸
                g.setColor(Color.BLACK);
                g.drawRect(x * Cell_size, y * Cell_size, Cell_size, Cell_size);// 선
            }
        }
    }
}
public class boom_avoid_game extends JFrame {
    int Player_posX=50, Player_posY=250,Player_Speed=3;
    boolean moveUP = false, moveDOWN = false, moveLEFT = false, moveRIGHT = false;
    int Cell_size = 100;
    boolean Boom_boolean =false;
    boolean stage1=true, stage2 = false, stage3=false;
    int[][] B_cycle = map.map1_boom0;
    int[][] B_cycle2 = map.map2_boom0;
    int[][] B_cycle3 = map.map3_boom0;
    int Delay_Sped = 750;

    //1
    int[][] First_stage_map = {
            {1,1,1,1,1,1,1,1},
            {1,1,1,1,1,1,1,1},
            {0,0,0,0,0,0,0,0},
            {1,1,1,1,1,1,1,1},
            {1,1,1,1,1,1,1,1},
    };
    //2
    int[][] Second_stage_map = {
            {1,1,0,1,1,1,1,1},
            {1,1,0,0,0,1,1,1},
            {0,0,0,1,0,0,0,0},
            {1,1,1,1,1,0,1,1},
            {1,1,1,1,1,0,1,1},
    };
    //3
    int[][] Third_stage_map = {
            {1,1,1,1,1,1,1,1},
            {1,1,0,0,0,0,1,1},
            {0,0,0,1,1,0,0,0},
            {1,1,0,0,0,0,1,1},
            {1,1,1,1,1,1,1,1},
    };

    int[][] map_cycle = First_stage_map;
    int[][] boom_cycle = B_cycle;

    public boom_avoid_game() {
        setTitle("폭탄 피하기");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(815, 540);

        //액션리스너
        Timer timer = new Timer(10,new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(moveUP){Player_posY -=Player_Speed;}
                if(moveDOWN){Player_posY +=Player_Speed;}
                if(moveLEFT){Player_posX -=Player_Speed;}
                if(moveRIGHT){Player_posX +=Player_Speed;}
                Collision();
                repaint();
            }
        });timer.start();

        //폭탄 타이머
        Timer boomT = new Timer(Delay_Sped,new ActionListener() {
            int cycle = 0;
            public void actionPerformed(ActionEvent e) {
                if (stage1) {
                    switch (cycle) {
                        case 0:
                            boom_cycle = map.map1_boom0;boom_appear();break;
                        case 1:
                            boom_cycle = map.map1_boom1;boom_appear();break;
                        case 2:
                            boom_cycle = map.map1_boom2;boom_appear();break;
                        case 3:
                            boom_cycle = map.map1_boom3;boom_appear();break;
                    }
                    if (cycle == 3) cycle = 0;
                    else cycle += 1;
                } else if (stage2) {
                    switch (cycle) {
                        case 0:
                            boom_cycle = map.map2_boom0;boom_appear();break;
                        case 1:
                            boom_cycle = map.map2_boom1;boom_appear();break;
                        case 2:
                            boom_cycle = map.map2_boom2;boom_appear();break;
                        case 3:
                            boom_cycle = map.map2_boom3;boom_appear();break;
                    }
                    if (cycle == 3) cycle = 0;
                    else cycle += 1;
                } else if (stage3) {
                    switch (cycle) {
                        case 0:
                            boom_cycle = map.map3_boom0;boom_appear();break;
                        case 1:
                            boom_cycle = map.map3_boom1;boom_appear();break;
                        case 2:
                            boom_cycle = map.map3_boom2;boom_appear();break;
                        case 3:
                            boom_cycle = map.map3_boom3;boom_appear();break;
                        case 4:
                            boom_cycle = map.map3_boom4;boom_appear();break;

                    }
                    if (cycle == 4) cycle = 0;
                    else cycle += 1;
                }
            }
        }); boomT.start();

        setContentPane(new PlayPanel());
        addKeyListener(new MyListener());
        setVisible(true);
    }
    //방향키 리스너
    class MyListener extends KeyAdapter{
        public void keyPressed(KeyEvent e) {
            int keycode = e.getKeyCode();
            switch (keycode) {
                case KeyEvent.VK_UP:
                    moveUP =true;break;
                case KeyEvent.VK_DOWN:
                    moveDOWN =true;break;
                case KeyEvent.VK_LEFT:
                    moveLEFT =true;break;
                case KeyEvent.VK_RIGHT:
                    moveRIGHT =true;break;
            }
        }
        public void keyReleased(KeyEvent e) {
            int keycode = e.getKeyCode();
            switch (keycode) {
                case KeyEvent.VK_UP:
                    moveUP =false;break;
                case KeyEvent.VK_DOWN:
                    moveDOWN =false;break;
                case KeyEvent.VK_LEFT:
                    moveLEFT =false;break;
                case KeyEvent.VK_RIGHT:
                    moveRIGHT =false;break;
            }
        }
    }
    class PlayPanel extends BG_grid {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            //기본 칸나누기
            paintGrid(g);
            //골인지점
            g.setColor(Color.GREEN);
            g.fillRect(700, 200, Cell_size, Cell_size);
            //벽만들기
            for (int y = 0; y < 5; y++) {
                for (int x = 0; x < 8; x++) {
                    if (map_cycle[y][x] == 1) {
                        g.setColor(new Color(31, 61, 73));
                        g.fillRect(x * Cell_size, y * Cell_size, Cell_size, Cell_size);
                    }
                }
            }
            //폭탄만들기
            if(Boom_boolean){
                for (int y = 0; y < 5; y++) {
                    for (int x = 0; x < 8; x++) {
                        if (boom_cycle[y][x] == 1) {
                            g.setColor(Color.RED);
                            g.fillRect(x * Cell_size, y * Cell_size, Cell_size, Cell_size);
                        }
                    }
                }
            }
            //플레이어 물체
            g.setColor(Color.BLUE);
            g.fillRect(Player_posX, Player_posY, 30, 30);
        }
    }
    //충돌판정
    public void Collision(){
        Rectangle Goal_rect = new Rectangle(750,200,50,100);//골인지점
        Rectangle P_rect = new Rectangle(Player_posX,Player_posY,30,30);//플레이어

        if (Player_posX<0) Player_posX += Player_Speed;
        if (Player_posX>845) Player_posX -= Player_Speed;
        if (Player_posY<0) Player_posY += Player_Speed;
        if (Player_posY>570) Player_posY -= Player_Speed;

        //골인지점 in
        if(stage1){
            if(P_rect.intersects(Goal_rect)){
                Player_goal();
                stage1 = false;
                stage2 = true;
                boom_cycle = B_cycle2;
                map_cycle = Second_stage_map;

            }
        }
        else if(stage2){
            if(P_rect.intersects(Goal_rect)){
                Player_goal();
                stage2 = false;
                stage3 =true;
                boom_cycle = B_cycle3;
                map_cycle = Third_stage_map;
            }
        }
        else if(stage3){
            if(P_rect.intersects(Goal_rect)){
                Player_goal();
                JOptionPane.showMessageDialog(null,"성공!");
                System.exit(0);
            }
        }
        //벽처리
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 8; x++) {
                if (map_cycle[y][x] == 1) {
                    Rectangle Wall = new Rectangle(x * Cell_size,y * Cell_size,Cell_size,Cell_size);
                    if(P_rect.intersects(Wall)){
                        if (moveUP) Player_posY += Player_Speed;
                        if (moveDOWN) Player_posY -= Player_Speed;
                        if (moveLEFT) Player_posX += Player_Speed;
                        if (moveRIGHT) Player_posX -= Player_Speed;
                    }
                }
            }
        }
        //폭탄과 충돌
        if (!Boom_boolean) return;
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 8; x++) {
                if (boom_cycle[y][x] == 1) {
                    Rectangle B = new Rectangle(x * Cell_size,y * Cell_size,Cell_size,Cell_size);
                    if(P_rect.intersects(B)){
                        Player_posX = 50;
                        Player_posY = 250;
                    }
                }
            }
        }
    }
    //골인했을때
    public void Player_goal(){
        Player_posX = 50;
        Player_posY = 250;
        moveUP =false;
        moveDOWN =false;
        moveLEFT =false;
        moveRIGHT =false;
        Boom_boolean = false;
    }
    //폭탄 리스폰
    public void boom_appear(){
        Boom_boolean = true;

        Timer B_appear = new Timer(50, new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                Boom_boolean = false;

            }
        });
        B_appear.setRepeats(false);//타이머 한번만실행
        B_appear.start();
    }
    public static void main(String[] args) {
        new boom_avoid_game();
    }
}