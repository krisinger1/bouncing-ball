package bouncingball;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class Ball extends Circle{
	private Vector2D velocity;
	private Vector2D position;
	private double vX;
	private double vY;
	private double topY;
	private double bottomY;
	private double rightX;
	private double leftX;
	private double elasticity;
	String name;


	public Ball(double radius, Paint fill){
		super(radius,fill);
		velocity = new Vector2D(15,20);
		position = new Vector2D(getLayoutX(),getLayoutY());
		vX=25;
		vY=20;
//		topY=centerY+radius;
//		bottomY=centerY-radius;
//		rightX=centerX+radius;
//		leftX=centerX-radius;
		elasticity=.95;
		name=fill.toString();

	}

	public Vector2D getPosition(){
		position.x = getLayoutX();
		position.y = getLayoutY();
		return position;
	}

	public Vector2D getVelocity(){
		return velocity;
	}

	public void setVelocity(Vector2D v){
		velocity = v;
	}

//	public double getBottomY(){
//		bottomY=getLayoutY()+getRadius();
//		return bottomY;
//	}
//
//	public double getTopY(){
//		return topY;
//	}
//
//	public double getRightX(){
//		return rightX;
//	}
//
//	public double getLeftX(){
//		return leftX;
//	}

	public void setVX(double v){
		//vX=v;
		velocity.x=v;
	}

	public void setVY(double v){
		//vY=v;
		velocity.y=v;
	}

	public void setV(double vx, double vy){
		velocity.x=vx;
		velocity.y=vy;
		//vX=vx;
		//vY=vy;
	}

	public double getVX(){
		return velocity.x;
	}

	public double getVY(){
		return velocity.y;
	}

	public double getMagV(){
		return velocity.mag();
		//return Math.sqrt(vX*vX+vY*vY);
	}

	public void setE(double e){
		elasticity=e;
	}

	public double getE(){
		return elasticity;
	}

//	public double calculateEnergy(double h, double a){
//		//return .5*vY*vY + a*h;
//	}

	public double distanceTo(Ball ball){
		Vector2D p = getPosition();
		return ball.getPosition().subtract(p).mag();
		//return Math.sqrt((getLayoutX()-ball.getLayoutX())*(getLayoutX()-ball.getLayoutX())+(getLayoutY()-ball.getLayoutY())*(getLayoutY()-ball.getLayoutY()));
	}

}

