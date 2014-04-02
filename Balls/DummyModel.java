package Balls;
import java.awt.geom.Ellipse2D;
import java.util.LinkedList;
import java.util.List;

public class DummyModel implements IBouncingBallsModel {

	private final double areaWidth;
	private final double areaHeight;

	private double x, y, vx, vy, r;

	public DummyModel(double width, double height) {
		this.areaWidth = width;
		this.areaHeight = height;
		x = 1;
		y = 1;
		vx = 2;
		vy = 10;
		r = 0.6; // TODO Make sure tiny balls don't spawn
                r = r > 1 ? 1 : r;
	}

	@Override
	public void tick(double deltaT) { //haha
		if (x < r || x > areaWidth - r) {
			vx *= -1;
		}
		if (y < r || y > areaHeight - r) {
			vy *= -1;
		}
                
                vy -= 0.1;
		x += vx * deltaT;
		y += vy * deltaT;
	}

	@Override
	public List<Ellipse2D> getBalls() {
		List<Ellipse2D> myBalls = new LinkedList<Ellipse2D>();
		myBalls.add(new Ellipse2D.Double(x - r, y - r, 2 * r, 2 * r));
		return myBalls;
	}
}