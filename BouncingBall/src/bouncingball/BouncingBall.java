package bouncingball;


import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BouncingBall extends Application {

    public static Pane canvas;
    public static Line ground;
    public static Ball ball1;
    public static Ball ball2;
    public static Ball ball3,ball4,ball5,ball6;
    public final static double RIGHTSIDE = 800;
    public final static double LEFTSIDE = 0;
    public final static double FLOOR = 450;
    public static Vector2D a = new Vector2D(0,.9);


	class BallEventHandler implements EventHandler<ActionEvent>{

		double initialE;
		Vector2D v1;
		Vector2D p1;
        double vX;
        double vY;
        double e;
        double friction = .99;
        Ball firstBall;
        Ball otherBall;
        Group circleGroup;

        BallEventHandler(Group group){
        	circleGroup=group;

        }

		@Override
		public void handle(ActionEvent event) {
			ObservableList<Node> group = (ObservableList<Node>)circleGroup.getChildren();
			for (Node c: circleGroup.getChildren())
			//for (Node c:group)
			{
				int indexc=circleGroup.getChildren().indexOf(c);
				firstBall = (Ball)c;
                e=firstBall.getE();
                p1=firstBall.getPosition();
                v1 = firstBall.getVelocity();
                Vector2D nextP1 = firstBall.nextPosition();

                // check for bounces with other balls
                for (int i=indexc+1;i<group.size();i++){
                	Node b=circleGroup.getChildren().get(i);
                	otherBall=(Ball)b;
                	Vector2D p2 = otherBall.getPosition();
                	Vector2D v2 = otherBall.getVelocity();
                	Vector2D nextP2 = otherBall.nextPosition();
                	double distance = nextP2.subtract(nextP1).mag(); //TODO nextP1 or recalculate?
                	if (distance < firstBall.getRadius()+otherBall.getRadius()) bounceOffBall(firstBall,otherBall);
                }

                //check for bounces off walls and floor
                boolean hitRightWall = firstBall.getPosition().x>=RIGHTSIDE || firstBall.nextPosition().x>=RIGHTSIDE;
                boolean hitLeftWall = firstBall.getPosition().x<=LEFTSIDE || firstBall.nextPosition().x<=LEFTSIDE;
                boolean hitFloor = (firstBall.getPosition().y>=FLOOR || firstBall.nextPosition().y>=FLOOR);
                if (hitRightWall || hitLeftWall || hitFloor){
                	if (hitFloor) {
                		bounceOffFloor(firstBall,FLOOR,a);
                		//System.out.println("vy out of method "+firstBall.getVY());  
                		System.out.println("height out of method "+(FLOOR-firstBall.getLayoutY()));
         		
                	}
                	if (hitRightWall) bounceOffWall(firstBall,RIGHTSIDE);
                	else if (hitLeftWall) bounceOffWall(firstBall, LEFTSIDE);
                }
                else { //just move normally
                	if (firstBall.getPosition().y>=FLOOR && firstBall.getVelocity().y==0) a.y=0;
                	firstBall.move(a);
        			System.out.println("position "+firstBall.getPosition().toString());
                	firstBall.setVelocity(firstBall.getVelocity().add(a));
        			System.out.println("velocity "+firstBall.getVelocity().toString());

                }
    			System.out.println("vy out of method "+firstBall.getVY());
    			System.out.println("between height "+(FLOOR-firstBall.getLayoutY()));
			}
		}
    }

    @Override
    public void start(final Stage primaryStage) {

        canvas = new Pane();
        final Scene scene = new Scene(canvas, 800, 600);

        primaryStage.setTitle("Bouncing Balls");
        primaryStage.setScene(scene);
        primaryStage.show();

        ball1 = new Ball(10,Color.BLUEVIOLET);
        ball1.setE(.9);
        ball1.relocate(100,100);
        ball2 = new Ball(10,Color.ORANGERED);
        ball2.relocate(400,100);
        ball2.setE(.7);
        ball2.setV(5, -5);
       // ball2.setVX(0);
        //ball2.setVY(-5);
        ball3 = new Ball(10,Color.GREENYELLOW);
        ball3.relocate(200,100);
        ball3.setE(.8);
        ball3.setV(10, 0);
        ball4 = new Ball(10,Color.MEDIUMORCHID);
        ball4.relocate(300,100);
        ball4.setE(.5);
        ball4.setV(-5, 5);
        ball5 = new Ball(10,Color.CORNFLOWERBLUE);
        ball5.relocate(150,100);
        ball5.setE(.8);
        ball6 = new Ball(10,Color.ORANGE);
        ball6.relocate(250,100);
        ball6.setE(.8);
        Group ballGroup = new Group(ball1,ball2,ball3,ball4,ball5,ball6);
       // ballGroup.getChildren().addAll(ball1,ball2);

//        circle = new Circle(10, Color.BLUE);
//        circle.relocate(100, 100);
//        circle2 = new Circle(10, Color.RED);
//        circle2.relocate(200, 100);
//        Group circleGroup = new Group();
        ground = new Line(0,460,800,460);
        ground.setStroke(Color.BLACK);
//        circleGroup.getChildren().addAll(circle,circle2);

       // canvas.getChildren().addAll(circleGroup,ground);
        canvas.getChildren().addAll(ballGroup,ground);


        final Timeline loop = new Timeline(new KeyFrame(Duration.millis(50),new BallEventHandler(ballGroup)));

        //loop.setCycleCount(Timeline.INDEFINITE);
        loop.setCycleCount(1000);
        loop.play();
        scene.setOnKeyPressed(new EventHandler<KeyEvent>(){

			@Override
			public void handle(KeyEvent ke) {
				if (ke.getText().equals("q")) loop.stop();
				else if (loop.getStatus()==Status.RUNNING) loop.pause();
				else loop.play();

			}
        });
    }

    public void bounceOffBall(Ball b1, Ball b2){
    	System.out.println("****************************************");
    	System.out.println("*************bounceOffBall**************");
    	System.out.println("****    "+b1.name+" & "+b2.name+"    *****");
    	System.out.println("****************************************");
    	Vector2D b1Start = b1.getPosition();
    	Vector2D b2Start = b2.getPosition();
//    	Vector2D b1Next = b1.nextPosition();
//    	Vector2D b2Next = b2.nextPosition();
    	Vector2D b1Collide, b2Collide;
    	double collisionTime;
    	double initialDistance = b2Start.subtract(b1Start).mag();
//    	double nextDistance = b2Next.subtract(b1Next).mag();
    	double collisionDistance = b1.getRadius()+b2.getRadius();
//
    	if (initialDistance<collisionDistance) collisionTime=0;
    	else collisionTime=collideTime(b1,b2,1,0);
//    	collisionTime=1-Math.abs(collisionDistance-nextDistance)/(initialDistance); //1 minus approx post collision time
    	System.out.println("b1Collide y= "+b1.getPosition().y+"b2Collide y="+b2.getPosition().y);
    	b1.move(collisionTime,a);
    	b1Collide=b1.getPosition();
    	b2.move(collisionTime,a);
    	b2Collide=b2.getPosition();
    	if (b1Collide.y>FLOOR) b1Collide.y=FLOOR;
    	if (b2Collide.y>FLOOR) b2Collide.y=FLOOR;
    	System.out.println("b1Collide y= "+b1Collide.y+"b2Collide y="+b2Collide.y);
    	System.out.println("distance "+b1.getPosition().subtract(b2.getPosition()).mag()+" \ntime "+collisionTime);
    	//b1Collide = b1Start.add(b1.getVelocity().multiply(collisionTime));
    	//b2Collide = b2Start.add(b2.getVelocity().multiply(collisionTime));

    	Vector2D collisionDirection = b2Collide.subtract(b1Collide);
    	Vector2D collisionDirectionUnit = collisionDirection.getUnitVector();
    	Vector2D b1CollisionVproj = b1.getVelocity().vProjOnto(collisionDirection);
    	double b1CollSprojSigned = b1CollisionVproj.dot(collisionDirectionUnit);
    	Vector2D b1CollisionVorthog = b1.getVelocity().subtract(b1CollisionVproj);
    	System.out.println("b1CollSproj "+b1CollSprojSigned);


    	Vector2D b2CollisionVproj = b2.getVelocity().vProjOnto(collisionDirection);
    	double b2CollSprojSigned = b2CollisionVproj.dot(collisionDirectionUnit);
    	Vector2D b2CollisionVorthog = b2.getVelocity().subtract(b2CollisionVproj);

    	Vector2D b1Finalproj= b1CollisionVproj;
    	Vector2D b2Finalproj= b2CollisionVproj;

    	System.out.println("dot product "+b2Start.subtract(b1Start).dot(b2.getVelocity().subtract(b1.getVelocity())));
    	boolean movingTogether=(b2Start.subtract(b1Start).dot(b2.getVelocity().subtract(b1.getVelocity()))<=0);
    	System.out.println("movingTogether= "+movingTogether);
    	if (movingTogether){

    		double initialKE = b1CollSprojSigned*b1CollSprojSigned+b2CollSprojSigned*b2CollSprojSigned;
    		double velDiff = b2CollSprojSigned-b1CollSprojSigned;
    		double e1 = b1.getE();
    		double e2 = b2.getE();
    		double cr;
    		if (e1<e2) cr=.8*e1;
    		else cr=.8*e2;
    		double lostKE = (1-(e1*e1))*(velDiff)*(velDiff)+(1-(e2*e2))*(velDiff)*(velDiff);
    		//double lostKE = (b2CollisionVproj.mag()*(b2.getE())-(b1CollisionVproj.mag()*(b1.getE())))*(b2CollisionVproj.mag()*(b2.getE())-(b1CollisionVproj.mag()*(b1.getE())));//b2CollisionVproj.subtract(b1CollisionVproj).mag()*(b2.getE()-b1.getE());
    		double initialMomentum = b2CollSprojSigned+b1CollSprojSigned;
    		double totalCollVelocity = b2CollSprojSigned + b1CollSprojSigned;

    		//double velAmag = .5*totalCollVelocity + .5*Math.sqrt(-1*totalCollVelocity*totalCollVelocity-2*lostKE+2*initialKE);
    		//double velBmag = totalCollVelocity - velAmag;
    		double velAmag = (cr*(velDiff)+totalCollVelocity)/2;
    		double velBmag = (cr*(-1*velDiff)+totalCollVelocity)/2;

    		Vector2D velA = collisionDirectionUnit.multiply(velAmag);
    		Vector2D velB = collisionDirectionUnit.multiply(velBmag);

    		System.out.println(velAmag+"  "+velBmag+"  "+totalCollVelocity+"  "+lostKE+" "+initialKE);
    		System.out.println(Math.abs(velAmag)>Math.abs(velBmag));
    		System.out.println(Math.abs(velAmag)<=Math.abs(velBmag));


			if (Math.abs(velAmag)>Math.abs(velBmag)){
				if (Math.abs(b1CollSprojSigned)>Math.abs(b2CollSprojSigned)){
					b1Finalproj= velB;
					b2Finalproj = velA;

				}
				else {
					b1Finalproj= velA;
					b2Finalproj = velB;
				}
			}
			else if (Math.abs(velAmag)<=Math.abs(velBmag)){
	    		System.out.println(velA.toString()+" "+velB.toString());
	    		System.out.println(b1CollisionVorthog.toString()+" "+b2CollisionVorthog.toString());

				if (Math.abs(b1CollSprojSigned)>Math.abs(b2CollSprojSigned)){
					b1Finalproj= velA;
					b2Finalproj = velB;
				}
				else {
					b1Finalproj= velB;
					b2Finalproj = velA;
				}
			}
			b1.setVelocity(b1Finalproj.add(b1CollisionVorthog));
			b1.setPosition(b1Collide);
			b2.setVelocity(b2Finalproj.add(b2CollisionVorthog));
			b2.setPosition(b2Collide);
			if ((b1Collide.y>=FLOOR-2*b2.getRadius() ||b2Collide.y>=FLOOR-2*b1.getRadius())&&!(b1Collide.y>=FLOOR-2*b2.getRadius() && b2Collide.y>=FLOOR-2*b1.getRadius())){
				System.out.println("***swapping velocities******");
				Vector2D temp = b1.getVelocity();
				b1.setVelocity(b2.getVelocity());
				b2.setVelocity(temp);
			}
			//b1.setPosition(b1Collide.add(b1.getVelocity().multiply(1-collisionTime)));
			//b2.setPosition(b2Collide.add(b2.getVelocity().multiply(1-collisionTime)));

    	}
    }

    public double collideTime(Ball b1,Ball b2,double tMax, double tMin){

    	Vector2D b1Collide, b2Collide;
    	double tCollision = (tMax+tMin)/2;
    	double collisionDistance = b1.getRadius()+b2.getRadius();
    	Vector2D b1Min = b1.nextPosition(tMin);
    	Vector2D b2Min = b2.nextPosition(tMin);
    	double tMinDistApart=b2Min.subtract(b1Min).mag();
    	b1Collide=b1.nextPosition(tCollision);
    	b2Collide=b2.nextPosition(tCollision);
    	double nextDistanceApart = b2Collide.subtract(b1Collide).mag();

    	if (Math.abs(collisionDistance-nextDistanceApart)<.1) return tCollision;
    	else if (nextDistanceApart<collisionDistance){
    		return collideTime(b1,b2,tCollision,tMin);
    	}
    	else {
    		return collideTime(b1,b2,tMax,tCollision);
    	}
    }

    public void bounceOffFloor(Ball b,double floor,Vector2D a){
    	//TODO fix for when already past floor coming in to method  -Check bounce Off Wall too
    	System.out.println("******************************");
    	System.out.println("          bounceOffFloor      ");
    	System.out.println("          "+b.name+"     ");
    	System.out.println("******************************");

    	double vY =b.getVY();
    	System.out.println("VY = "+vY);
    	if (vY==0) {
    		b.setVX(.98*b.getVX());
    		//b.setPosition(new Vector2D(b.getLayoutX(),FLOOR));
    		b.setLayoutY(FLOOR);
    		System.out.println("** "+b.getLayoutY());
    		b.move(new Vector2D(0,0)); 
    		System.out.println("** "+b.getLayoutY());
    		return;
    		}
    	double height=floor-b.getLayoutY();
    	System.out.println("start height= "+(floor-b.getLayoutY()));

    	double prebounceVy = Math.sqrt(vY*vY+2*a.y*height);
    	double timePreBounce = (prebounceVy-vY)/a.y;
    	double bounceVy = prebounceVy*b.getE()*(-1);
    	//double initialEy = .5*vY*vY + Math.abs((floor-b.getPosition().y))*a.y;
    	//double timePreBounce = (Math.sqrt(2*initialEy)-vY)/a.y;
    	System.out.println("timeprebounce = "+timePreBounce);
    	System.out.println("1 timepostbounce = "+(1-timePreBounce));
    	b.move(timePreBounce,a);
    	System.out.println("bounce height= "+(floor-b.getLayoutY()));
    	b.setVY(bounceVy);
    	//b.setVelocity(b.getVelocity().add(a.multiply(timePreBounce)));

    	//double bounceVy = b.getVY()*b.getE()*-1;
    	double postBounceEy = .5*bounceVy*bounceVy;
    	double vYfinal = bounceVy+a.y*(1-timePreBounce);
    	System.out.println("bounceVy = "+bounceVy);
    	System.out.println("vyfinal = "+vYfinal);
    	double yFinal= floor-(postBounceEy-.5*vYfinal*vYfinal)/a.y;

    	if (vYfinal*bounceVy<0) { vYfinal=0; yFinal=floor;}
    	System.out.println("yfinal = "+yFinal);
    	double xFinal = b.getPosition().x+ b.getVX()*(1-timePreBounce);

    	b.setVY(vYfinal);
     	b.setPosition(new Vector2D(xFinal,yFinal));
    	System.out.println("2 timepostbounce = "+((vYfinal-bounceVy)/a.y));

    	System.out.println("vy end of method= "+b.getVY());

    }

    public void bounceOffWall(Ball b, double wallLocation){
    	System.out.println("bounceOffWall");
    	double timePreBounce = (wallLocation-b.getPosition().x)/b.getVX();
    	double bounceVx = b.getVX()*b.getE()*(-1);
    	b.move(timePreBounce,a);
    	b.setVX(bounceVx);
    	b.move(1-timePreBounce,a);
    }

    public static void main(final String[] args) {
        launch(args);
    }

}