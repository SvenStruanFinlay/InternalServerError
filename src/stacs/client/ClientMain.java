package stacs.client;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferStrategy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.sun.xml.internal.ws.api.ha.HaInfo;
import com.sun.xml.internal.ws.api.message.Attachment;

import stacs.logic.entity.Entity;
import stacs.logic.entity.LivingEntity;
import stacs.logic.item.Item;
import stacs.logic.room.Room;
import stacs.logic.room.Square;
import stacs.logic.room.TerrainType;
import stacs.logic.turn.AttackAction;
import stacs.logic.turn.MoveAction;
import stacs.logic.turn.PickupItemAction;
import stacs.net.client.TcpClient;
import stacs.net.message.ChatMessage;
import stacs.test.RoomGen;
import stacs.util.PathFind;
import sun.dc.pr.PathFiller;

public class ClientMain extends Canvas {

    private BufferStrategy buffers = null;
    private Room room = null;
    private Room lastRoom = null;

    private int tick = 0;

    private Map<Integer, Square> lastPositionMap = new HashMap<>();
    private Map<Integer, Square> currentPositionMap = new HashMap<>();

    private double animProgress = 1;
    private double inProg = 0;
    private String msg = null;

    private int mx;
    private int my;

    private JTextArea area;
    private JTextField chatIn;

    private boolean needsTurn = false;

    private Square highlight = null;
    private Entity highlightEntity = null;
    private Item highlightItem = null;
    private Square highlightItemSquare = null;

    private List<Square> path = null;

    private LivingEntity myPlayer;

    private double scale = 100;
    private double transx = 500;
    private double transy = 100;

    TcpClient client;

    public void addMessage(String message) {
        area.append(message + "\n");
    }

    public synchronized void updateRoom(Room room, LivingEntity p, boolean update) {
        if (this.room == null || !this.room.id.equals(room.id)) {
            inProg = 0;
        }

        Map<Integer, Square> temp = lastPositionMap;
        lastPositionMap = currentPositionMap;
        currentPositionMap = temp;
        temp.clear();

        lastRoom = this.room;
        this.room = room;
        this.myPlayer = p;
        animProgress = 0;
        msg = null;

        if (update)
            needsTurn = true;
    }

    public ClientMain() {

        client = new TcpClient(this);
        client.start();

        String n = JOptionPane.showInputDialog("Enter player name");
        client.joinGame(n);

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

        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                turn: if (needsTurn) {
                    if (highlight != null && path != null) {
                        boolean teleport = false;
                        Square dest = highlight;
                        Square t = highlight.teleport;
                        if (t != null) {
                            int reply = JOptionPane.showConfirmDialog(null, "Take the door/telporter?", "Travel",
                                    JOptionPane.YES_NO_OPTION);
                            if (reply == JOptionPane.YES_OPTION) {
                                teleport = true;
                                dest = t;
                            }
                        }

                        MoveAction act = new MoveAction(dest, teleport);
                        client.sendAction(act);

                        needsTurn = false;
                    } else if (highlightEntity != null && highlightEntity != myPlayer) {
                        int dist = Math.abs(myPlayer.currentSquare.x - highlightEntity.currentSquare.x);
                        dist += Math.abs(myPlayer.currentSquare.y - highlightEntity.currentSquare.y);
                        if (dist > 1)
                            return;

                        AttackAction act = new AttackAction(highlightEntity);
                        client.sendAction(act);
                        needsTurn = false;
                    } else if (highlightItem != null) {
                        int dist = Math.abs(myPlayer.currentSquare.x - highlightItemSquare.x);
                        dist += Math.abs(myPlayer.currentSquare.y - highlightItemSquare.y);
                        if (dist > 1)
                            return;

                        PickupItemAction pick = new PickupItemAction(highlightItemSquare, highlightItem);
                        client.sendAction(pick);
                        needsTurn = false;
                    }
                }
            }
        });

        this.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                scale = scale + e.getPreciseWheelRotation() * 20;
                scale = Math.min(Math.max(scale, 50), 200);
            }

        });

        this.setPreferredSize(new Dimension(1080, 720));

        JFrame window = new JFrame("Game: " + n);

        JPanel chatpane = new JPanel();
        chatpane.setLayout(new BorderLayout());
        area = new JTextArea();
        area.setPreferredSize(new Dimension(0, 200));
        chatpane.add(area, BorderLayout.CENTER);
        chatIn = new JTextField();
        chatIn.addActionListener(e -> {
            client.sendChat(new ChatMessage(myPlayer.getDisplayName() + ": " + chatIn.getText()));
            chatIn.setText("");
        });
        chatpane.add(chatIn, BorderLayout.SOUTH);

        JPanel pane = new JPanel();
        pane.setLayout(new BorderLayout());
        pane.add(this, BorderLayout.CENTER);
        pane.add(chatpane, BorderLayout.SOUTH);
        window.add(pane);
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

    public boolean drawQuad(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3,
            double z3, double x4, double y4, double z4, Graphics2D g, Color col, int mx, int my) {

        int[] x = { DrawingUtils.transformX(x1, y1, z1, scale, transx),
                DrawingUtils.transformX(x2, y2, z2, scale, transx), DrawingUtils.transformX(x3, y3, z3, scale, transx),
                DrawingUtils.transformX(x4, y4, z4, scale, transx) };
        int[] y = { DrawingUtils.transformY(x1, y1, z1, scale, transy),
                DrawingUtils.transformY(x2, y2, z2, scale, transy), DrawingUtils.transformY(x3, y3, z3, scale, transy),
                DrawingUtils.transformY(x4, y4, z4, scale, transy) };

        Polygon p = new Polygon(x, y, x.length);
        g.setColor(col);
        g.fillPolygon(p);
        return p.contains(mx, my);
    }

    public boolean drawFace(double x, double y, double z, double dx, double dy, double dz, Graphics2D g, Color col,
            int mx, int my) {
        if (dy == 0) {
            return drawQuad(x, y, z, x + dx, y, z, x + dx, y, z + dz, x, y, z + dz, g, col, mx, my);
        } else {
            return drawQuad(x, y, z, x, y + dy, z, x + dx, y + dy, z + dz, x + dx, y, z + dz, g, col, mx, my);
        }
    }

    public boolean drawCube(double x, double y, double z, double w, double h, double d, Graphics2D g, Color col, int mx,
            int my) {
        return drawFace(x + w, y + h, z + d, -w, -h, 0, g, col.darker().darker(), mx, my)
                | drawFace(x + w, y + h, z + d, 0, -h, -d, g, col.darker(), mx, my)
                | drawFace(x, y, z, w, 0, d, g, col, mx, my);
    }

    public synchronized void render() {
        if (room == null)
            return;

        if (inProg > 1)
            inProg = 1;
        inProg += 1.0 / 60 / 2;

        double inProg = this.inProg * this.inProg * 3 - 2 * this.inProg * this.inProg * this.inProg;

        transx -= (myPlayer.getInterpolatedX(animProgress, scale, transx, lastPositionMap.get(myPlayer.id))
                - getWidth() / 2);
        transy -= (myPlayer.getInterpolatedY(animProgress, scale, transy, lastPositionMap.get(myPlayer.id))
                - getHeight() / 2);
        if (animProgress < 1)
            animProgress += 1.0 / 60;
        double animProgress = this.animProgress * this.animProgress * 3
                - 2 * this.animProgress * this.animProgress * this.animProgress;

        tick++;
        Graphics2D g = (Graphics2D) buffers.getDrawGraphics();

        int w = getWidth();
        int h = getHeight();

        if (room.rain)
            g.setColor(Color.blue);
        else
            g.setColor(Color.black);
        g.fillRect(0, 0, w, h);

        Square lasthl = highlight;
        Entity lasthe = highlightEntity;
        Item lasthi = highlightItem;

        Random rand = new Random();
        rand.setSeed(2);
        int depth = 0;
        while (depth <= room.w + room.h - 2) {
            for (int x = Math.min(depth, room.w - 1); x >= 0; x--) {
                int y = depth - x;
                if (y >= room.h)
                    break;

                Square s = room.squares[x][y];
                double hh = s.getH();

                double dhy = -(10 + rand.nextInt(20)) * (1 - inProg);

                if (drawCube(x, 1 - hh + dhy, y, 1, hh, 1, g, s.getCol(), mx, my)) {
                    highlight = s;
                    highlightEntity = null;
                    highlightItem = null;
                }

                if (s.teleport != null) {
                    double dh = 2;
                    Color doorCol = Color.orange;

                    double xx = x;
                    double yy = y;

                    double dx = 1;
                    double dy = 1;

                    if (x == 0) {
                        dx = 0.2;
                    } else if (x == room.w -1) {
                        dx = 0.2;
                        xx += 1 - dx;
                    } else if (y == 0) {
                        dy = 0.2;
                    } else if (y == room.h- 1) {
                        dy = 0.2;
                        yy += 1 - dy;
                    } else {
                        int tt = (tick % 200);
                        if (tt > 100)
                            tt = 200 - tt;
                        doorCol = new Color(0, 255, 0, tt);
                    }

                    drawCube(xx, 1 - hh - dh + dhy, yy, dx, dh, dy, g, doorCol, -1, -1);
                }

                if (lasthl == s && path == null) {
                    Color high = new Color(255, 0, 0, 100);
                    drawCube(x, 1 - hh + dhy, y, 1, hh, 1, g, high, -1, -1);
                } else if (path != null && path.contains(s)) {
                    Color high = new Color(0, 255, 0, 100);
                    drawCube(x, 1 - hh + dhy, y, 1, hh, 1, g, high, -1, -1);
                }
            }
            depth++;
        }

        depth = 0;
        while (depth <= room.w + room.h - 2) {
            for (int x = Math.min(depth, room.w - 1); x >= 0; x--) {
                int y = depth - x;
                if (y >= room.h)
                    break;

                Square s = room.squares[x][y];
                double hh = s.getH();
                double dhy = -(10 + rand.nextInt(20)) * (1 - inProg);

                for (Entity e : s.entities) {
                    currentPositionMap.put(e.id, e.currentSquare);
                    Square last = lastPositionMap.get(e.id);
                    Image img = e.getSprite(tick);
                    if (img != null) {

                        int xx = (int) e.getInterpolatedX(animProgress, scale, transx, last);
                        int yy = (int) e.getInterpolatedY(animProgress, scale, transy, last);

                        int width = (int) (scale / .8);
                        int height = (int) (scale / .8);
                        int dx = (int) (scale / 1.5);
                        int dy = (int) (scale / 1.2);

                        int offset = (int) (scale / 1.2);

                        g.drawImage(img, xx - dx, yy + (int) (dhy * scale) - dy, width, height, null);
                        if (lasthe == e) {
                            g.setColor(new Color(255, 0, 0, 100));
                            g.drawRect(xx - dx, yy + (int) (dhy * scale) - dy, width, height);
                        }

                        if (mx > xx - dx && mx <= xx - dx + width && my > yy + (int) (dhy * scale) - dy
                                && my <= yy + (int) (dhy * scale) - dy + height) {
                            highlightEntity = e;
                            highlightItem = null;
                            highlight = null;
                        }

                        g.setColor(Color.white);

                        if (e.getDisplayName() != null) {
                            g.drawString(e.getDisplayName(), xx - 13, yy - offset - 20 + (int) (dhy * scale));
                        }

                        if (e instanceof LivingEntity) {
                            LivingEntity liv = (LivingEntity) e;
                            g.drawString(liv.health + "/" + liv.maxHealth, xx - 13,
                                    yy - offset - 5 + (int) (dhy * scale));
                        }
                    }
                }

                for (Item e : s.items) {
                    Image img = e.getImage();
                    int xx = DrawingUtils.transformX(x, 1 - hh, y, scale, transx);
                    int yy = DrawingUtils.transformY(x, 1 - hh, y, scale, transy);

                    int width = (int) (scale / 2);
                    int height = (int) (scale / 1.5);
                    int dx = (int) (scale / 3.6);
                    int dy = (int) (scale / 4);

                    int offset = (int) (scale / 2.5);

                    g.drawImage(img, xx - dx, yy + (int) (dhy * scale) - dy, width, height, null);
                    if (lasthi == e) {
                        g.setColor(new Color(255, 0, 0, 100));
                        g.drawRect(xx - dx, yy + (int) (dhy * scale) - dy, width, height);
                    }

                    if (mx > xx - dx && mx <= xx - dx + width && my > yy + (int) (dhy * scale) - dy
                            && my <= yy + (int) (dhy * scale) - dy + height) {
                        highlightItem = e;
                        highlightItemSquare = s;
                        highlightEntity = null;
                        highlight = null;
                    }

                    g.setColor(Color.white);

                    if (e.getName() != null) {
                        g.drawString(e.getName(), xx - 13, yy - offset + (int) (dhy * scale));
                    }
                }
            }
            depth++;
        }

        if (highlight != null && highlight != lasthl) {
            path = PathFind.findPath(myPlayer.currentSquare, highlight, s -> {
                if(s.terrainType == TerrainType.water)
                    return s.room.freeze ? 1 : 10000;
                else
                    return 1;
            }, 5);
        }

        g.setColor(Color.white);
        String msg = this.msg;
        if (msg == null)
            msg = needsTurn ? "Make your move" : "waiting on others";
        g.drawString(msg, 100, 100);
        
        g.drawString(room.id, 100, 50);
        buffers.show();
    }

    public void run() {
        int frametimeMs = 1000 / 60;

        while (true) {
            long lastFrameTime = System.currentTimeMillis();

            render();
            try {
                long slp = lastFrameTime + frametimeMs - System.currentTimeMillis();
                if (slp > 0) {
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
