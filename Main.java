package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Main
implements MouseListener, MouseMotionListener, KeyListener {
    public JPanel panel;
    public JPanel lowerPanel;
    public JFrame container = new JFrame("Fractle");
    public JFrame introContainer = new JFrame("Fractle");
    public JPanel introPanel = (JPanel)introContainer.getContentPane();
    public static final int NUM_TRIES = 256;
    public double nr;
    public double ni;
    public double X;
    public double Y;
    public byte zooms;
    public double xLoc = 0.0;
    public double yLoc = 0.0;
    public double size = 720.0;
    public BufferedImage bi;
    public double resolution = 1440.0;
    public static final int DEAFAULT_RES = 720;
    private int[] colorOne = new int[3];
    private int[] colorTwo = new int[3];
    private JButton startButton = new JButton("Start");
    private JTextField sizeFeild = new JTextField("720");
    private JLabel sizeLabel = new JLabel("Size");
    private boolean isJulietMode = false;
    private double realJ;
    private double imaginaryJ;
    private boolean isJulietDisplayed = false;
    private JLayeredPane pane = new JLayeredPane();
    private ImageIcon bg;
    private JLabel backroundIcon;
    private JLabel lbl;

    public static void main(String[] args) {
        new main.Main();
    }

    public void startPicture() {
        X = 720.0;
        Y = 720.0;
        panel = new JPanel();
        panel.setPreferredSize(new Dimension(740, 740));
        panel.setLayout(null);
        panel.setOpaque(true);
        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);
        panel.setLayout(new BoxLayout(panel, 3));
        container.addKeyListener(this);
        pane.setPreferredSize(new Dimension(720, 720));
        pane.setBorder(BorderFactory.createTitledBorder("Picture"));
        panel.add(pane);
        container.setContentPane(panel);
        container.pack();
        container.setVisible(true);
        colorOne[0] = 0;
        colorOne[1] = 0;
        colorOne[2] = 255;
        colorTwo[0] = 255;
        colorTwo[1] = 0;
        colorTwo[2] = 0;
        BufferedImage image = new BufferedImage(72, 72, 6);
        Graphics2D gr = (Graphics2D)image.getGraphics();
        gr.setColor(new Color(255, 255, 255, 0));
        gr.setColor(new Color(0, 0, 0, 255));
        gr.drawRect(1, 1, 70, 70);
        lbl = new JLabel(new ImageIcon(image.getScaledInstance(144, 144, 4)));
        lbl.setOpaque(false);
        lbl.setSize(new Dimension(144, 144));
        pane.add((Component)lbl, new Integer(1));
        display(0.0, 0.0, 720.0);
    }

    public Main() {
        introPanel.setPreferredSize(new Dimension(300, 300));
        introPanel.setLayout(null);
        startButton.setBounds(new Rectangle(120, 260, 60, 20));
        introPanel.add(startButton);
        sizeFeild.setBounds(new Rectangle(120, 160, 60, 20));
        introPanel.add(sizeFeild);
        sizeLabel.setLocation(new Point(150 - sizeLabel.getWidth() / 2, 130));
        introPanel.add(sizeLabel);
        System.out.print(150 - sizeLabel.getWidth());
        introContainer.pack();
        introContainer.setVisible(true);
        startPicture();
    }

    public void display(double x, double y, double size) {
        bi = new BufferedImage((int)resolution, (int)resolution, 5);
        Graphics2D g = (Graphics2D)bi.getGraphics();
        double smallSize = size / X * 2.0;
        double smallX = x / X * 2.0 - 1.5;
        double smallY = y / Y * 2.0 - 1.0;
        System.out.print(isJulietMode);
        System.out.println(isJulietDisplayed);
        double i = 0.0;
        while (i < X * (resolution / 720.0)) {
            double j = 0.0;
            while (j < Y * (resolution / 720.0)) {
                double real = i / X * smallSize / (resolution / 720.0) + smallX;
                double imaginary = j / Y * smallSize / (resolution / 720.0) + smallY;
                nr = real;
                ni = imaginary;
                boolean works = true;
                int k = 0;
                while (k < 256) {
                    if (Math.sqrt(nr * nr + ni * ni) > 2.0) {
                        works = false;
                        g.setColor(gradiant(k));
                        g.drawRect((int)i, (int)j, 1, 1);
                        break;
                    }
                    if (isJulietMode) {
                        isJulietMode = false;
                        isJulietDisplayed = true;
                    }
                    if (!isJulietDisplayed) {
                        multiply(real, imaginary);
                    } else {
                        multiply(realJ, imaginaryJ);
                    }
                    ++k;
                }
                if (works) {
                    g.setColor(Color.BLACK);
                    g.drawRect((int)i, (int)j, 1, 1);
                }
                j += 1.0;
            }
            i += 1.0;
        }
        if (isJulietMode) {
            isJulietDisplayed = true;
        }
        bg = new ImageIcon(bi.getScaledInstance(720, 720, 4));
        backroundIcon = new JLabel(bg);
        backroundIcon.setBounds(new Rectangle(10, 10, 720, 720));
        backroundIcon.setOpaque(true);
        pane.removeAll();
        pane.add((Component)lbl, new Integer(1));
        pane.add((Component)backroundIcon, new Integer(0));
        panel.add(pane);
        System.out.print("Test");
    }

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.print(true);
        if (e.getKeyChar() == 'r') {
            size = 720.0;
            xLoc = 0.0;
            yLoc = 0.0;
            display(0.0, 0.0, 720.0);
        } else if (e.getKeyChar() == 's') {
            File file = new File("Snapshot " + System.currentTimeMillis() + ".png");
            try {
                ImageIO.write((RenderedImage)bi, "png", file);
                System.out.print("saved");
            }
            catch (IOException i) {
                i.printStackTrace();
            }
        } else if (e.getKeyChar() == 'j') {
            isJulietMode = !isJulietMode;
        }
    }

    public Color gradiant(int k) {
        if (k < 51) {
            return new Color(255 - k * 5, 0, 255);
        }
        if (k < 102) {
        	k -= 51;
            return new Color(0, k * 5, 255 - k * 5);
        }
        if (k < 153) {
        	k -= 102;
            return new Color(k * 5, 255, 0);
        }
        if (k < 204) {
        	k -= 153;
            return new Color(255, 255 - k * 5 / 2, 0);
        }
        return new Color(255, 128 - (k - 204) * 5 / 2, 0);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        double pixelSize;
        if (e.getY() > 720) {
            return;
        }
        double x = e.getX();
        double y = e.getY();
        zooms = (byte)(zooms + 1);
        if (!e.isShiftDown()) {
            x = (x - 72.0) / 720.0 * size + xLoc;
            y = (y - 72.0) / 720.0 * size + yLoc;
            pixelSize = size / 5.0;
        } else {
            x = (x - 1800.0) / 720.0 * size + xLoc;
            y = (y - 1800.0) / 720.0 * size + yLoc;
            pixelSize = size * 5.0;
        }
        display(x, y, pixelSize);
        size = pixelSize;
        xLoc = x;
        yLoc = y;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!isJulietMode) {
            e.getY();
            lbl.setLocation(e.getX() - 72, e.getY() - 72);
        } else {
            double x = (double)e.getX() / X * size + xLoc;
            double y = (double)e.getY() / Y * size + yLoc;
            x = x / 720.0 * 2.0 - 1.5;
            y = y / 720.0 * 2.0 - 1.0;
            realJ = x;
            imaginaryJ = y;
            nr = x;
            ni = y;
            double pnr = nr;
            double pni = ni;
            Color clr = new Color(255, 0, 0, 18);
            BufferedImage img = new BufferedImage((int)resolution, (int)resolution, 5);
            Graphics2D g = (Graphics2D)img.getGraphics();
            g.drawImage(bi.getScaledInstance((int)resolution, (int)resolution, 4), 0, 0, null);
            g.setColor(clr);
            g.setColor(Color.GREEN);
            int i = 0;
            while (i < 256) {
                if (Math.sqrt(nr * nr + ni * ni) > 2.0) break;
                pnr = nr;
                pni = ni;
                multiply(x, y);
                g.drawLine((int)convertToScreen(pnr, true), (int)convertToScreen(pni, false), (int)convertToScreen(nr, true), (int)convertToScreen(ni, false));
                ++i;
            }
            lowerPanel.getGraphics().drawImage(img.getScaledInstance(720, 720, 4), 0, 22, null);
        }
    }

    public double convertToScreen(double d, boolean real) {
        if (real) {
            return (d + 1.5) / 2.0 * resolution;
        }
        return (d + 1.0) / 2.0 * resolution;
    }

    public void multiply(double rl, double im) {
        double real = 0.0;
        double imaginary = 0.0;
        int i = 0;
        while (i < 1) {
            real += nr * nr;
            real += 0.0 - ni * ni;
            imaginary += 2.0 * (nr * ni);
            ++i;
        }
        nr = real += rl;
        ni = imaginary += im;
    }

    @Override
    public void keyPressed(KeyEvent arg0) {
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
    }

    @Override
    public void mouseDragged(MouseEvent arg0) {
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }
}
