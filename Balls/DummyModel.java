package Balls;

import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DummyModel implements IBouncingBallsModel {

    private final double areaWidth;
    private final double areaHeight;
    private List<Ball> ballList;
//	private double x, y, vx, vy, r;

    public DummyModel(double width, double height) {
        this.areaWidth = width;
        this.areaHeight = height;
        ballList = new ArrayList<Ball>();

        ballList.add(new Ball(width, height, 1));
        ballList.add(new Ball(width, height, 0));
    }

    @Override
    public void tick(double deltaT) {
        boolean i = false;
        double deltaX = ballList.get(1).getX() - ballList.get(0).getX();
        double deltaY = ballList.get(1).getY() - ballList.get(0).getY();
        double distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

        if (distance <= ballList.get(1).getR() + ballList.get(0).getR() && !(ballList.get(0).getC() || ballList.get(1).getC())) {
            i = true;
            ballList.get(0).setC(true);
            ballList.get(1).setC(true);
        } else if (distance > ballList.get(1).getR() + ballList.get(0).getR() && (ballList.get(0).getC() || ballList.get(1).getC())) {
            ballList.get(0).setC(false);
            ballList.get(1).setC(false);
        }
        for (Ball b : ballList) {
            b.tick(deltaT, i);
        }
        if (i) {
            Collide(ballList.get(0), ballList.get(1));
        }
    }

    private void Collide(Ball b1, Ball b2) {
        double deltaX = b1.getX() - b2.getX();
        double deltaY = b1.getY() - b2.getY();

        double angle = Math.atan2(deltaY, deltaX);

        double[] polarb1 = rectToPolar(b1.getVX(), b1.getVY());
        double[] polarb2 = rectToPolar(b2.getVX(), b2.getVY());

        polarb1[1] -= angle;
        polarb2[1] -= angle;

        double rotb1[] = polarToRect(polarb1[0], polarb1[1]);
        double rotb2[] = polarToRect(polarb2[0], polarb2[1]);
        
        double I = b1.getM() * rotb1[0] + b2.getM() * rotb2[0];
        double R = -(rotb1[0] - rotb2[0]);
        double v2 = (I - b1.getM() * R) / (b1.getM() + b2.getM());
        double v1 = R + (I - b2.getM() * R) / (b1.getM() + b2.getM());
        
        double current = 0,starting = 0;
        for(Ball b : ballList) {
            current += b.getE();
            starting += b.getStartingE();
        }
        
        v1 *= Math.abs(-starting+current)/current;
        v2 *= Math.abs(-starting+current)/current;
        
        polarb1 = rectToPolar(v1, rotb1[1]);
        polarb2 = rectToPolar(v2, rotb2[1]);

        polarb1[1] += angle;
        polarb2[1] += angle;
        
        rotb1 = polarToRect(polarb1[0], polarb1[1]);
        rotb2 = polarToRect(polarb2[0], polarb2[1]);
        
        b1.setV(rotb1[0], rotb1[1]);
        b2.setV(rotb2[0], rotb2[1]);
    }

    private double[] polarToRect(double r, double phi) {
        double[] x = new double[2];
        x[0] = Math.cos(phi) * r;
        x[1] = Math.sin(phi) * r;
        return x;
    }

    private double[] rectToPolar(double x, double y) {
        double[] r = new double[2];
        r[0] = Math.sqrt(x * x + y * y);
        r[1] = Math.atan2(y, x);
        return r;
    }

    @Override
    public List<Ellipse2D> getBalls() {
        List<Ellipse2D> myBalls = new LinkedList<Ellipse2D>();
        for (Ball b : ballList) {
            myBalls.add(new Ellipse2D.Double(b.getX() - b.getR(),
                    b.getY() - b.getR(),
                    2 * b.getR(),
                    2 * b.getR()));
        }
        return myBalls;
    }

    private class Ball {

        private final double areaWidth;
        private final double areaHeight;
        private double x, y, vx, vy, r, m;
        private boolean c;
        
        private double startingE;
        
        public Ball(double width, double height, int n) {
            this.areaWidth = width;
            this.areaHeight = height;
            
            m = n % 2 == 0 ? 50 : 5;
            
            x = n % 2 == 0 ? 1 : areaWidth - 1;
            y = n % 2 == 0 ? 2 : 2;
            
            vx = n % 2 == 0 ? 2 : 5; 
            vy = n % 2 == 0 ? 7 : 9;
            
            r = n % 2 == 0 ? 0.8 : 0.5;
            
            c = false;
            
            startingE = getE();
            
        }

        public void tick(double deltaT, boolean collide) {
            if (x < r || x > areaWidth - r) {
                vx *= -1;
            }
            if (y <= r && vy <= 0 || y >= areaHeight - r) {
                
                vy *= -1;
            }
            System.out.println(getE());
            vy -= 0.2;
            x += vx * deltaT;
            y += vy * deltaT;
        }

        public double getX() {
            return x;
        }
        
        public double getM() {
            return m;
        }
        
        public double getStartingE(){
            return startingE;
        }
        
        public double getE() {
            return Math.sqrt(vy*vy+vx*vx)*m/2 + getPotensialE();
        }
        
        private double getPotensialE() {
            return (y-r)*0.2*getM();
        }
        
        public double getY() {
            return y;
        }

        public double getR() {
            return r;
        }

        public double getVX() {
            return vx;
        }

        public double getVY() {
            return vy;
        }

        public void setV(double vx, double vy) {
            this.vx = vx;
            this.vy = vy;
        }

        public void setC(boolean c) {
            this.c = c;
        }

        public boolean getC() {
            return c;
        }
    }
}
