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
    public static Ball ball3,ball4;
    public final static double RIGHTSIDE = 800;
    public final static double LEFTSIDE = 0;
    public final static double FLOOR = 450;

	class BallEventHandler implements EventHandler<ActionEvent>{

		double initialE;
		Vector2D v1;
		Vector2D p1;
        double vX;
        double vY;
        Vector2D a = new Vector2D(0,.9);
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
                boolean hitFloor = firstBall.getPosition().y>FLOOR || firstBall.nextPosition().y>=FLOOR;
                if (hitRightWall || hitLeftWall || hitFloor){
                	if (hitFloor) bounceOffFloor(firstBall,FLOOR,a);
                	if (hitRightWall) bounceOffWall(firstBall,RIGHTSIDE);
                	else if (hitLeftWall) bounceOffWall(firstBall, LEFTSIDE);
                }
                else { //just move normally
                	firstBall.move();
                	firstBall.setVelocity(firstBall.getVelocity().add(a));

                }
			}

//                vX=circle.getVX();
//                vY=circle.getVY();
//                for (int i=indexc+1;i<group.size();i++){
//                //for (int i=index+1;i<circleGroup.getChildren().size();i++){
//				//for (Node b:circleGroup.getChildren()){
//                	Node b=circleGroup.getChildren().get(i);
//					int indexb=circleGroup.getChildren().indexOf(b);
//
//					otherBall= (Ball)b;
//					if (!circle.equals(otherBall)){
//						System.out.println(indexc+" vs "+indexb);
//
//						double distance= circle.distanceTo(otherBall);
//						if (distance<(circle.getRadius()+otherBall.getRadius())){
//							System.out.println(indexc+" vs "+indexb);
//							// movingToX=(circle.getLayoutX()-otherBall.getLayoutX())*(circle.getVX()-otherBall.getVX())<=0;
//							//boolean movingToY=(circle.getLayoutY()-otherBall.getLayoutY())*(circle.getVY()-otherBall.getVY())<=0;
//							boolean movingTogether=((otherBall.getPosition().subtract(circle.getPosition())).dot(otherBall.getVelocity().subtract(circle.getVelocity()))<=0);
//							//boolean movingTogether=(circle.getLayoutX()-otherBall.getLayoutX())*(circle.getVX()-otherBall.getVX())+(circle.getLayoutY()-otherBall.getLayoutY())*(circle.getVY()-otherBall.getVY())<=0;
//								if(movingTogether){
//							//they bounce off each other
//								//conserve energy
//								double totalVx=circle.getVX()+otherBall.getVX();
//								double totalVy=circle.getVY()+otherBall.getVY();
//								double cmVx=totalVx/2;
//								double cmVy=totalVy/2;
//								//double KEx = e*circle.getVX()*circle.getVX()+otherBall.getE()*otherBall.getVX()*otherBall.getVX();
//								double KEx = (e+otherBall.getE()-1)*(circle.getVX()*circle.getVX()+otherBall.getVX()*otherBall.getVX())+2*(2-e-otherBall.getE())*circle.getVX()*otherBall.getVX();
//								double KEy = (e+otherBall.getE()-1)*(circle.getVY()*circle.getVY()+otherBall.getVY()*otherBall.getVY())+2*(2-e-otherBall.getE())*circle.getVY()*otherBall.getVY();
//
//								//double KEy = e*circle.getVY()*circle.getVY()+otherBall.getVY()*otherBall.getE()*otherBall.getVY();
//								double finalVx1= cmVx+.5*Math.sqrt(2*KEx-totalVx*totalVx);
//								double finalVx2= totalVx-finalVx1;//cmVx-.5*Math.sqrt(2*KEx-totalVx*totalVx);
//								double finalVy1= cmVy+.5*Math.sqrt(2*KEy-totalVy*totalVy);
//								double finalVy2= totalVy-finalVy1;//cmVy-.5*Math.sqrt(2*KEy-totalVy*totalVy);
//								System.out.println("X: "+totalVx+" "+circle.getVX()+" "+otherBall.getVX()+" "+finalVx1+" "+finalVx2);
//								System.out.println("Y: "+totalVy+" "+circle.getVY()+" "+otherBall.getVY()+" "+finalVy1+" "+finalVy2);
//
//								if (Math.abs(finalVx1)>Math.abs(finalVx2)){
//									if (Math.abs(circle.getVX())>Math.abs(otherBall.getVX())){
//
//										circle.setVX(finalVx2);
//										otherBall.setVX(finalVx1);
//									}
//									else {
//										circle.setVX(finalVx1);
//										otherBall.setVX(finalVx2);
//									}
//								}
//								else if (Math.abs(finalVx1)<=Math.abs(finalVx2)){
//									if (Math.abs(circle.getVX())>Math.abs(otherBall.getVX())){
//
//										circle.setVX(finalVx1);
//										otherBall.setVX(finalVx2);
//									}
//									else {
//										circle.setVX(finalVx2);
//										otherBall.setVX(finalVx1);
//									}
//								}
//
//								if (Math.abs(finalVy1)>Math.abs(finalVy2)){
//									if (Math.abs(circle.getVY())>Math.abs(otherBall.getVY())){
//
//										circle.setVY(finalVy2);
//										otherBall.setVY(finalVy1);
//									}
//									else {
//										circle.setVY(finalVy1);
//										otherBall.setVY(finalVy2);
//									}
//								}
//								else if (Math.abs(finalVy1)<=Math.abs(finalVy2)){
//									if (Math.abs(circle.getVY())>Math.abs(otherBall.getVY())){
//										circle.setVY(finalVy1);
//										otherBall.setVY(finalVy2);
//									}
//									else {
//										circle.setVY(finalVy2);
//										otherBall.setVY(finalVy1);
//									}
//								}
//								if ((circle.getLayoutY()>=450 || otherBall.getLayoutY()>=450)&&!(circle.getLayoutY()>=450 && otherBall.getLayoutY()>=450)){
//									double temp = circle.getVY();
//									circle.setVY(-1*otherBall.getVY());
//									otherBall.setVY(-1*temp);
//								}
//							}
//						}
//					}
//				}
//			}


//			for (Node c1: circleGroup.getChildren()){
//				firstBall = (Ball)c1;
//				v1= firstBall.getVelocity();
//				vY = v1.y;
//				vX= v1.x;
//				//vX=circle.getVX();
//                //vY=circle.getVY();
//                e=firstBall.getE();
//                final Bounds bounds = canvas.getBoundsInLocal();
//                //System.out.println(" vY "+circle.getVY());
//                double currentY = firstBall.getLayoutY();
//                double currentX = firstBall.getLayoutX();
//
//                initialE = .5*vY*vY+aY*(450-currentY);
//
//                System.out.println(.5*vY*vY+aY*(450-currentY)+"   "+initialE);
//                //if (circle.getLayoutY()>=450)circle.setLayoutY(450-circle.getRadius());
//            	//if (circle.getLayoutY()>bounds.getMaxY() - 5*circle.getRadius()) circle.setLayoutY(bounds.getMaxY() - 5*circle.getRadius());
////            	circle.setLayoutY(circle.getLayoutY() + vY);
////                circle.setLayoutX(circle.getLayoutX() + vX);
//
//
////                final boolean atRightBorder = circle.getLayoutX() >= 800;//(bounds.getMaxX() - circle.getRadius());
////                final boolean atLeftBorder = circle.getLayoutX() <= 0;//(bounds.getMinX() + circle.getRadius());
////                final boolean atBottomBorder = circle.getLayoutY() >= 450;//(bounds.getMaxY() - 5*circle.getRadius());
////                final boolean atTopBorder = circle.getLayoutY() <= 0;//(bounds.getMinY() + circle.getRadius());
//
//                final boolean atRightBorder = currentX+vX >= 800;//(bounds.getMaxX() - circle.getRadius());
//                final boolean atLeftBorder = currentX+vX <= 0;//(bounds.getMinX() + circle.getRadius());
//                final boolean atBottomBorder = currentY+vY >= 450;//(bounds.getMaxY() - 5*circle.getRadius());
//                final boolean atTopBorder = currentY+vY <= 0;//(bounds.getMinY() + circle.getRadius());
//
//
//
//                if (atBottomBorder) {
//                	if (Math.abs(vY)>0){	// if still bouncing, not rolling along bottom
//                		//double timePreBounce=(450-currentY)/vY;
//                		double timePreBounce = (Math.sqrt(2*initialE)-vY)/aY;
//                		double timePostBounce=1-timePreBounce;
//
//                		double prebounceVY= Math.sqrt(2*initialE); //vY+timePreBounce*aY;
//                		double postBounceVY = -1*e*Math.abs(prebounceVY);
//                		//circle.setVY(-1*e*prebounceVY + (currentY+vY-450)/vY*aY);
//                		//if (2*aY+postBounceVY>0) {
//                		double postBounceE = .5*postBounceVY*postBounceVY;
//                		if (initialE<.1){
//                			firstBall.setVY(0);
//                			firstBall.setLayoutY(450);
//                		}
//                		else {
//                			firstBall.setVY(postBounceVY+aY*timePostBounce);
//                			//circle.setVY(postBounceVY+aY*timePostBounce);
//                			firstBall.setLayoutY(450-(postBounceE-.5*firstBall.getVY()*firstBall.getVY())/aY);
//                			//circle.setLayoutY(450+postBounceVY*timePostBounce);
//                		}
//                		//vX*=friction;
//
//                	}
//
//
//                }
//                else {
//                	if (Math.abs(vY)>=0) firstBall.setVY(vY+aY);
//                	//circle.setLayoutY(currentY+vY);
//                	firstBall.setLayoutY(450-(initialE-.5*firstBall.getVY()*firstBall.getVY())/aY);
//                }
//
//                if (atRightBorder){
//                	double bounceVX = -1*e*vX;
//                	double timePreBounce = (800-currentX)/vX;
//                	double timePostBounce = 1-timePreBounce;
//                	//circle.setLayoutX(800*2-currentX-vX);
//                	firstBall.setLayoutX(800+bounceVX*timePostBounce);
//                	firstBall.setVX(bounceVX);
//                }
//                else if (atLeftBorder){
//                	double bounceVX = Math.abs(e*vX);
//                	double timePreBounce = (currentX-0)/vX;
//                	double timePostBounce = 1-timePreBounce;
//                	firstBall.setLayoutX(0+bounceVX*timePostBounce);
//                	firstBall.setVX(bounceVX);
//                }
//                else {
//                	//if (Math.abs(vY)==0) vX*=friction;
//                	if (atBottomBorder) {
//                		vX*=friction;
//                		firstBall.setVX(vX);
//                	}
//
//                	firstBall.setLayoutX(currentX+vX);
//
//                }
//            }
		}
    }

    //public static Circle circle;
    //public static Circle circle2;



    @Override
    public void start(final Stage primaryStage) {

        canvas = new Pane();
        final Scene scene = new Scene(canvas, 800, 600);

        primaryStage.setTitle("Bouncing Balls");
        primaryStage.setScene(scene);
        primaryStage.show();

        ball1 = new Ball(10,Color.BLUEVIOLET);
        ball1.setE(.8);
        ball1.relocate(100,100);
        ball2 = new Ball(10,Color.ORANGERED);
        ball2.relocate(400,100);
        ball2.setE(.7);
        ball2.setV(0, -5);
       // ball2.setVX(0);
        //ball2.setVY(-5);
        ball3 = new Ball(10,Color.GREENYELLOW);
        ball3.relocate(200,100);
        ball3.setE(.6);
        ball4 = new Ball(10,Color.MEDIUMORCHID);
        ball4.relocate(300,100);
        ball4.setE(.5);
        Group ballGroup = new Group(ball1,ball2,ball3,ball4);
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
    	System.out.println("bounceOffBall");
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
    	b1.move(collisionTime);
    	b1Collide=b1.getPosition();
    	b2.move(collisionTime);
    	b2Collide=b2.getPosition();

    	System.out.println("distance "+b1.getPosition().subtract(b2.getPosition()).mag()+" \ntime "+collisionTime);
    	//b1Collide = b1Start.add(b1.getVelocity().multiply(collisionTime));
    	//b2Collide = b2Start.add(b2.getVelocity().multiply(collisionTime));

    	Vector2D collisionDirection = b2Collide.subtract(b1Collide);
    	Vector2D collisionDirectionUnit = collisionDirection.getUnitVector();
    	Vector2D b1CollisionVproj = b1.getVelocity().vProjOnto(collisionDirection);
    	double b1CollSprojSigned = b1CollisionVproj.dot(collisionDirectionUnit);
    	Vector2D b1CollisionVorthog = b1.getVelocity().subtract(b1CollisionVproj);

    	Vector2D b2CollisionVproj = b2.getVelocity().vProjOnto(collisionDirection);
    	double b2CollSprojSigned = b2CollisionVproj.dot(collisionDirectionUnit);
    	Vector2D b2CollisionVorthog = b2.getVelocity().subtract(b2CollisionVproj);

    	Vector2D b1Finalproj= b1CollisionVproj;
    	Vector2D b2Finalproj= b2CollisionVproj;


    	boolean movingTogether=(b2Start.subtract(b1Start).dot(b2.getVelocity().subtract(b1.getVelocity()))<=0);
    	System.out.println("movingTogether= "+movingTogether);
    	if (movingTogether){

    		double initialKE = b1CollSprojSigned*b1CollSprojSigned+b2CollSprojSigned*b2CollSprojSigned;
    		double velDiff = b2CollSprojSigned-b1CollSprojSigned;
    		double lostKE = (1-b1.getE())*(velDiff)*(velDiff)+(1-b2.getE())*(velDiff)*(velDiff);
    		//double lostKE = (b2CollisionVproj.mag()*(b2.getE())-(b1CollisionVproj.mag()*(b1.getE())))*(b2CollisionVproj.mag()*(b2.getE())-(b1CollisionVproj.mag()*(b1.getE())));//b2CollisionVproj.subtract(b1CollisionVproj).mag()*(b2.getE()-b1.getE());
    		double initialMomentum = b2CollSprojSigned+b1CollSprojSigned;
    		double totalCollVelocity = b2CollSprojSigned + b1CollSprojSigned;

    		double velAmag = .5*totalCollVelocity + .5*Math.sqrt(-1*totalCollVelocity*totalCollVelocity-2*lostKE+2*initialKE);
    		double velBmag = totalCollVelocity - velAmag;

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
			if ((b1Collide.y>=FLOOR ||b2Collide.y>=FLOOR)&&!(b1Collide.y>=FLOOR && b2Collide.y>=FLOOR)){
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
    	double vY =b.getVY();
    	double initialEy = .5*vY*vY + (floor-b.getPosition().y)*a.y;
    	double timePreBounce = (Math.sqrt(2*initialEy)-vY)/a.y;

    	b.move(timePreBounce);
    	b.setVelocity(b.getVelocity().add(a.multiply(timePreBounce)));

    	double bounceVy = b.getVY()*b.getE()*-1;
    	double postBounceEy = .5*bounceVy*bounceVy;
    	double vYfinal = bounceVy+a.y*(1-timePreBounce);
    	double yFinal= floor-(postBounceEy-.5*vYfinal*vYfinal)/a.y;
    	double xFinal = b.getPosition().x+ b.getVX()*(1-timePreBounce);


    	b.setVY(vYfinal);
     	b.setPosition(new Vector2D(xFinal,yFinal));

//      if (atBottomBorder) {
//    	if (Math.abs(vY)>0){	// if still bouncing, not rolling along bottom
//    		//double timePreBounce=(450-currentY)/vY;
//    		double timePreBounce = (Math.sqrt(2*initialE)-vY)/aY;
//    		double timePostBounce=1-timePreBounce;
//
//    		double prebounceVY= Math.sqrt(2*initialE); //vY+timePreBounce*aY;
//    		double postBounceVY = -1*e*Math.abs(prebounceVY);
//    		//circle.setVY(-1*e*prebounceVY + (currentY+vY-450)/vY*aY);
//    		//if (2*aY+postBounceVY>0) {
//    		double postBounceE = .5*postBounceVY*postBounceVY;
//    		if (initialE<.1){
//    			firstBall.setVY(0);
//    			firstBall.setLayoutY(450);
//    		}
//    		else {
//    			firstBall.setVY(postBounceVY+aY*timePostBounce);
//    			//circle.setVY(postBounceVY+aY*timePostBounce);
//    			firstBall.setLayoutY(450-(postBounceE-.5*firstBall.getVY()*firstBall.getVY())/aY);
//    			//circle.setLayoutY(450+postBounceVY*timePostBounce);
//    		}
//    		//vX*=friction;
//
//
    }

    public void bounceOffWall(Ball b, double wallLocation){
    	System.out.println("bounceOffWall");
    	double timePreBounce = (wallLocation-b.getPosition().x)/b.getVX();
    	double bounceVx = b.getVX()*b.getE()*(-1);
    	b.move(timePreBounce);
    	b.setVX(bounceVx);
    	b.move(1-timePreBounce);
    }

    public static void main(final String[] args) {
        launch(args);
    }

}