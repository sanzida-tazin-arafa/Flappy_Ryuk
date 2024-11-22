package flappyryuk;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FlappyRyuk extends JPanel implements ActionListener, KeyListener{
    int boardWidth = 960;
    int boardHeight = 600;

    //images
    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;
    
    //Bird
    int RyukX=boardWidth/8;
    int RyukY=boardHeight/2;
    int RyukWidht=70;
    int RyukHeight=50;

    
    class Ryuk{
        int x=RyukX;
        int y=RyukY;
        int widht=RyukWidht;
        int height=RyukHeight;
        Image img;
        
        Ryuk(Image img){
            this.img=img;
        }
    }
    
    //pipes
    int pipeX=boardWidth;
    int pipeY=0;
    int pipeWidht=64;
    int pipeHeight=512;
    
    class Pipe{
        int x=pipeX;
        int y=pipeY;
        int widht=pipeWidht;
        int height=pipeHeight;
        Image img;
        boolean passed=false;
        
        Pipe(Image img){
            this.img= img;
        }
    }
    
    //gsmr logic
    Ryuk ryuk;
    int velocityX=-4;//moves pipe to the left side(simulates bird move to right)
    int velocityY=0;
    int gravity=1;
    
    ArrayList<Pipe>pipes;
    Random random=new Random();
    
    Timer gameloop;
    Timer placePipesTimer;
    boolean gameOver=false;
    double score=0;
    
    FlappyRyuk() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        // setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this);
        
        //load images
        backgroundImg = new ImageIcon(getClass().getResource("./ryukbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./ryuk.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
        
        //bird
        ryuk=new Ryuk(birdImg);
        pipes=new ArrayList<Pipe>();
        
        //place pipes timer
        placePipesTimer=new Timer(1500, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                placePipes();
            }
        });
        placePipesTimer.start();
        
        //game timer
        gameloop= new Timer(1000/60, this);//time unit is ms
        gameloop.start();
    }
    
    public void placePipes(){
        //(0-1)*(pipeHeight/2)-> (0-256)
        //0-128-(0-256)-->1/4 pipeHeight->3/4pipeHeight
        int randomPipeY=(int)(pipeY-pipeHeight/4-Math.random()*(pipeHeight/2));
        int openingSpace=boardHeight/3;
        
        Pipe topPipe=new Pipe(topPipeImg);
        topPipe.y=randomPipeY;
        pipes.add(topPipe);
        
        Pipe bottomPipe=new Pipe(bottomPipeImg);
        bottomPipe.y=topPipe.y+pipeHeight+openingSpace;
        pipes.add(bottomPipe);
    }
    
    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	draw(g);
    }

    public void draw(Graphics g) {
        //background
        g.drawImage(backgroundImg, 0, 0, this.boardWidth, this.boardHeight, null);
        
        //bird
        g.drawImage(ryuk.img, ryuk.x, ryuk.y, ryuk.widht, ryuk.height, null);
        
        //pipes
        for(int i=0; i<pipes.size();i++){
            Pipe pipe=pipes.get(i);
            g.drawImage(pipe.img,pipe.x,pipe.y,pipe.widht,pipe.height, null);
        }
        
        //score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        if(gameOver){
            g.drawString("GameOver: "+ String.valueOf((int)score)+"  \n"+"Press Space Key to Play Again",15, 35);
        }
        else{
            g.drawString(String.valueOf((int)score),15, 35);
        }
    }
    
    public void move(){
        //bird
        velocityY+=gravity;
        ryuk.y += velocityY;
        ryuk.y=Math.max(ryuk.y,0);
        
        //pipes
        for(int i=0; i<pipes.size();i++){
            Pipe pipe=pipes.get(i);
            pipe.x += velocityX;
            
            if(!pipe.passed && ryuk.x>pipe.x+pipe.widht){
                pipe.passed=true;
                score +=0.5;//0.5 because there are 2 pipes in every set. 0.5*2=10 for passing each set
            }
            
            if (collision(ryuk, pipe)){
                gameOver=true;
            }
        }
         
        if(ryuk.y>boardHeight){
            gameOver=true;//if the bird falls gameover
        }
    }
    
    public boolean collision(Ryuk a, Pipe b){
        return a.x< b.x+b.widht && //a's top left corner doesn't reach b's top right corner
               a.x+a.widht>b.x  && //a's top right corner passes b's top left corner
               a.y<b.y+b.height && //a's top left corner doesn't reach b's bpttom left corner
               a.y+a.height>b.y;   //a's bottom left corner passes b's top left corner
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            placePipesTimer.stop();
            gameloop.stop();
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_SPACE){
            velocityY=-9;
            if(gameOver){
                //restart the game by resetting the conditions
                ryuk.y=RyukY;
                velocityY=0;
                pipes.clear();
                score=0;
                gameOver=false;
                gameloop.start();
                placePipesTimer.start();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
    
}
