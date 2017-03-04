package stacs.client;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import com.sun.org.apache.xalan.internal.xsltc.dom.UnionIterator;

import stacs.main.Room;
import stacs.main.Square;

public class ClientMain extends Canvas {

    private BufferStrategy buffers = null;
    private Room room = RoomGen.generateRoom();

    public ClientMain() {
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

    public void drawQuad(double x1, double y1, double z1,
                           double x2, double y2, double z2,
                           double x3, double y3, double z3,
                           double x4, double y4, double z4,
                           Graphics2D g, Color col) {
        double scale = 50;
        double transx = 500;
        double transy = 100;

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

        g.setColor(col);
        g.fillPolygon(x, y, x.length);
    }
    
    public void drawFace(double x,  double y,  double z,
                           double dx, double dy, double dz,
                           Graphics2D g, Color col){
        if(dy == 0){
        drawQuad(x, y, z,
                x + dx, y, z,
                x + dx, y, z + dz,
                x, y, z + dz,
                g, col);
        } else {
            drawQuad(x, y, z,
                x, y + dy, z,
                x + dx, y + dy, z + dz,
                x + dx, y, z + dz,
                g, col);
        }
    }
    
    public void drawCube(double x, double y, double z, double w, double h, double d, Graphics2D g, Color col){
                                       drawFace(x + w, y + h, z + d,-w,-h,0, g, col.darker().darker());
                                       drawFace(x + w, y + h, z + d,0,-h,-d, g, col.darker());
                                       drawFace(x,y,z,w,0,d, g, col);
    }

    public void render() {
        Graphics2D g = (Graphics2D) buffers.getDrawGraphics();

        int w = getWidth();
        int h = getHeight();

        g.setColor(Color.white);
        g.fillRect(0, 0, w, h);
        
        int depth = 0;
        while(depth <= room.w + room.h - 2){
        for(int x = Math.min(depth, room.w - 1); x >= 0; x--){
            int y = depth - x;
            if(y >= room.h)
                break;
            
            Square s = room.squares[x][y];
            double hh = 1.0 * s.height / 10;
            drawCube(x, 1 - hh, y, 1, hh, 1, g, s.terrainType.c);
        }
        depth++;
        }

        buffers.show();
    }

    public void run() {
        while (true) {

            render();
            try {
                Thread.sleep(200);
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
