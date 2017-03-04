package stacs.client;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import stacs.logic.entity.Entity;
import stacs.logic.room.Room;
import stacs.logic.room.Square;
import stacs.test.RoomGen;

public class ClientMain extends Canvas {

    private BufferStrategy buffers = null;
    private Room room = RoomGen.generateRoom();
    private int tick =0;
    
    private int mx;
    private int my;
    
    private Square highlight = null;
    
    private double scale = 50;
    private double transx = 500;
    private double transy = 100;

    public ClientMain() {
        
        this.addMouseMotionListener(new MouseMotionListener() {
            
            @Override
            public void mouseMoved(MouseEvent e) {
                mx = e.getX();
                my = e.getY();
            }
            
            @Override
            public void mouseDragged(MouseEvent e) {
                mx = e.getX();
                my = e.getY();
            }
        });
        
        this.setPreferredSize(new Dimension(1080, 720));

        JFrame window = new JFrame("Game");
        window.add(this);
        window.pack();
        window.setVisible(true);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.createBufferStrategy(2);
        buffers = this.getBufferStrategy();
    }

    @Override
    public void paint(Graphics g) {
        /* Ignore system painting */
    }

    @Override
    public void repaint() {
        /* Ignore system painting */
    }

    public boolean drawQuad(double x1, double y1, double z1,
                              double x2, double y2, double z2,
                              double x3, double y3, double z3,
                              double x4, double y4, double z4,
                              Graphics2D g, Color col, int mx, int my) {


        int[] x = {
                DrawingUtils.transformX(x1, y1, z1, scale, transx),
                DrawingUtils.transformX(x2, y2, z2, scale, transx),
                DrawingUtils.transformX(x3, y3, z3, scale, transx),
                DrawingUtils.transformX(x4, y4, z4, scale, transx) };
        int[] y = {
                DrawingUtils.transformY(x1, y1, z1, scale, transy),
                DrawingUtils.transformY(x2, y2, z2, scale, transy),
                DrawingUtils.transformY(x3, y3, z3, scale, transy),
                DrawingUtils.transformY(x4, y4, z4, scale, transy) };

        Polygon p = new Polygon(x, y, x.length);
        g.setColor(col);
        g.fillPolygon(p);
        return p.contains(mx, my);
    }
    
    public boolean drawFace(double x,  double y,  double z,
                              double dx, double dy, double dz,
                              Graphics2D g, Color col, int mx, int my){
        if(dy == 0){
        return drawQuad(x, y, z,
                x + dx, y, z,
                x + dx, y, z + dz,
                x, y, z + dz,
                g, col, mx, my);
        } else {
            return drawQuad(x, y, z,
                x, y + dy, z,
                x + dx, y + dy, z + dz,
                x + dx, y, z + dz,
                g, col, mx, my);
        }
    }
    
    public boolean drawCube(double x, double y, double z, double w, double h, double d, Graphics2D g, Color col, int mx, int my){
        return 
                                       drawFace(x + w, y + h, z + d,-w,-h,0, g, col.darker().darker(), mx, my) | 
                                       drawFace(x + w, y + h, z + d,0,-h,-d, g, col.darker(), mx, my) |
                                       drawFace(x,y,z,w,0,d, g, col, mx, my);
    }
    
    public void render() {
        tick++;
        Graphics2D g = (Graphics2D) buffers.getDrawGraphics();
        //g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        g.setColor(Color.black);
        g.fillRect(0, 0, w, h);
        
        Square lasthl = highlight;
        
        int depth = 0;
        while(depth <= room.w + room.h - 2){
        for(int x = Math.min(depth, room.w - 1); x >= 0; x--){
            int y = depth - x;
            if(y >= room.h)
                break;
            
            Square s = room.squares[x][y];
            double hh = 1.0 * s.height / 10;
            hh /= 1.5;
            if(drawCube(x, 1 - hh, y, 1, hh, 1, g, s.terrainType.c, mx, my)) {
                highlight = s;
            }

            if(s.teleport != null){
                double dh = 2;
                Color doorCol = Color.orange;
                
                double xx = x;
                double yy = y;
                
                double dx = 1;
                double dy = 1;
                
                if(x == 0){
                    dx = 0.2;
                }else if(x == w){
                    dx = 0.2;
                    xx += 1 - dx;
                } else if(y == 0){
                    dy = 0.2;
                } else if (y == h){
                    dy = 0.2;
                    yy += 1 - dy;
                } else {
                    int tt = (tick % 200);
                    if(tt > 100)
                        tt = 200 - tt;
                    doorCol = new Color(0, 255, 0, tt );
                }
                
                drawCube(xx, 1 - hh - dh, yy, dx, dh, dy, g, doorCol, -1, -1);
            }
            
            if(lasthl == s){
                Color high = new Color(255, 0, 0, 100);
                drawCube(x, 1 - hh, y, 1, hh, 1, g, high, -1, -1);    
            }
            
            for(Entity e : s.entities){
                Image img = e.getSprite(tick);
                if(img != null){
                    
                    int xx = DrawingUtils.transformX(x, 1 - hh, y, scale, transx);
                    int yy = DrawingUtils.transformY(x, 1 - hh, y, scale, transy);
                    
                    g.drawImage(img, xx, yy, null);
                }
            }
        }
        depth++;
        }
        

        buffers.show();
    }

    public void run() {
        int frametimeMs = 1000 / 60;
        
        
        while (true) {
            long lastFrameTime = System.currentTimeMillis();
            
            render();
            try {
                long slp = lastFrameTime + frametimeMs - System.currentTimeMillis();
                if(slp > 0) {
                    Thread.sleep(slp);
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ClientMain main = new ClientMain();
        main.run();
    }
}
