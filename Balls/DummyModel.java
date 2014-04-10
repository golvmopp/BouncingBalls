package Balls;
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
		
		if(distance <= ballList.get(1).getR()+ballList.get(0).getR() && !(ballList.get(0).getC() || ballList.get(1).getC())) {
			i = true;
			ballList.get(0).setC(true);
			ballList.get(1).setC(true);
		} else if(distance > ballList.get(1).getR()+ballList.get(0).getR() && (ballList.get(0).getC() || ballList.get(1).getC())){
			ballList.get(0).setC(false);
			ballList.get(1).setC(false);
		}
		for(Ball b : ballList) {
			b.tick(deltaT,i);
		}
		if(i) 
			Collide(ballList.get(0), ballList.get(1));
	}
	
	
	private void Collide(Ball b1, Ball b2) {
//		System.out.println("sadf");
		double deltaX = b1.getX() - b2.getX();
		double deltaY = b1.getY() - b2.getY();
		
		double angle = Math.atan2(deltaY, deltaX);
		
		double[] polarb1 = rectToPolar(b1.getVX(), b1.getVY());
		double[] polarb2 = rectToPolar(b2.getVX(), b2.getVY());
		
//		System.out.println(""+polarb1[1]);
		
		polarb1[1] -= angle;
		polarb2[1] -= angle;
		
		double rotb1x = polarb1[0] * Math.cos(polarb1[1]);
		double rotb1y = polarb1[0] * Math.sin(polarb1[1]);
		double rotb2x = polarb2[0] * Math.cos(polarb2[1]);
		double rotb2y = polarb2[0] * Math.sin(polarb2[1]);
		
		double I = b1.getR()*rotb1x + b2.getR()*rotb2x;
		double R = -(rotb1x - rotb2x);
		double v1 = (I - b2.getR()* R)/ (b1.getR() + b2.getR());
		double v2 = R + (I-b2.getR()*R) / (b1.getR() + b2.getR());

		polarb1 = rectToPolar(v1, rotb1y);
		polarb2 = rectToPolar(v2, rotb2y);
		
		polarb1[1] += angle;
		polarb2[1] += angle;
		
//		polarb1[0] = v1;
//		polarb2[0] = v2;
		
		double rotb1[] = polarToRect(polarb1[0], polarb1[1]);
		double rotb2[] = polarToRect(polarb2[0], polarb2[1]);
		
		b1.setV(rotb1[0],rotb1[1]);
		b2.setV(rotb2[0],rotb2[1]);
		
		
 		//Energy stays the same
// 		double vxb1 = Math.pow(b1.getVX(), 2);
// 		double vyb1 = Math.pow(b1.getVY(), 2);
// 		double vb1 =(Math.sqrt(vyb1 + vxb1));
// 		double eb1 = b1.getR() * vb1;
// 		
// 		double vxb2 = Math.pow(b2.getVX(), 2);
// 		double vyb2 = Math.pow(b2.getVY(), 2);
// 		double vb2 =(Math.sqrt(vyb2 + vxb2));
// 		double eb2 = b2.getR() * vb2;
// 		
// 		double I = eb1 + eb2;
// 		double R = -(vb2-vb1);
// 		
// 		vb2 = (I+R*b1.getR()) /
// 				(b1.getR()+b2.getR());
// 		vb1 = vb2 - R;
//

 		//		double vx = b1.getVX() + b2.getVX();
//		double vy = b1.getVY() + b2.getVY();
//		double forceAngle = Math.acos(vx/vy);
	 } 
	
	private double[] polarToRect (double r, double phi) {
		double[] x = new double [2]; 
		x[0] = Math.cos(phi) * r;
		x[1] = Math.sin(phi) * r;
		return x;
	}
	
	private double[] rectToPolar (double x, double y) {
		double[] r = new double [2]; 
		r[0] = Math.sqrt(x*x+y*y);
		r[1] = Math.atan2(y, x);
		return r;
	}

	
	@Override
	public List<Ellipse2D> getBalls() {
		List<Ellipse2D> myBalls = new LinkedList<Ellipse2D>();
		for(Ball b : ballList) {
			myBalls.add(new Ellipse2D.Double(b.getX() -b.getR(), 
											b.getY() - b.getR(),
											2 * b.getR(),
											2 * b.getR()));
		}
		return myBalls;
	}
	
private class Ball {
		
		private final double areaWidth;
		private final double areaHeight;
		private double x,y,vx,vy,r;
		private boolean c;
		
		public Ball(double width, double height, int n) {
			this.areaWidth = width;
			this.areaHeight = height;
			
			x = n%2 == 0 ? 1 : areaWidth-1;
			y = n%2 == 0 ? 2 : 2;
			vx = 3;//5 + Math.random()*5;
			vy = 0.1;
			r = 0.8; // TODO Make sure tiny balls don't spawn
	                r = r > 1 ? 1 : r;
	        c = false;
		}
		
		public void tick(double deltaT, boolean collide){
			if (x < r || x > areaWidth - r) {
				vx *= -1;
			}
			if (y < r || y > areaHeight - r) {
				vy *= -1;
			}
			
//			if(collide) {
//				vx *= -1;
//			}
	                
	        vy -= 0.2;
			x += vx * deltaT;
			y += vy * deltaT;
		}

		public double getX(){
			return x;
		}
		
		public double getY(){
			return y;
		}
		
		public double getR(){
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
		
		public double Energi() {
			return vx*vy*vx*vy*r;
		}
		
		public void setC(boolean c){
			this.c = c;
		}
		
		public boolean getC(){
			return c;
		}
	}
}