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
		vx = 7;
		vy = 4;
		r = Math.random(); // TODO Fix tiny balls
                r = r > 1 ? 1 : r;
	}

	@Override
	public void tick(double deltaT) {
		if (x < r || x > areaWidth - r) {
			vx *= -1;
		}
		if (y < r || y > areaHeight - r) {
			vy *= -1;
		}
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