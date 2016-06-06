package main;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;

public class ComponentMouseSquare
extends JComponent {
    private static final long serialVersionUID = -5602991597436068087L;
    public static final int mouseSize = 144;
    private int x;
    private int y;
    private int lastX;
    private int lastY;

    public void setCoords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.BLACK);
        g.drawRect(this.x, this.y, 144, 144);
        g.setColor(new Color(255, 255, 255, 100));
        g.drawRect(this.lastX, this.lastY, 144, 144);
        this.lastX = this.x;
        this.lastY = this.y;
    }
}
