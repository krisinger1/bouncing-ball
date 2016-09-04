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

	class BallEventHandler implements EventHandler<ActionEvent>{

		double initialE;
		Vector2D v;
        double vX;
        double vY;
        double aY = .9;
        double e;
        double friction = .99;
        Ball circle;
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
				circle = (Ball)c;
                vX=circle.getVX();
                vY=circle.getVY();
                e=circle.getE();
                for (int i=indexc+1;i<group.size();i++){
                //for (int i=index+1;i<circleGroup.getChildren().size();i++){
				//for (Node b:circleGroup.getChildren()){
                	Node b=circleGroup.getChildren().get(i);
					int indexb=circleGroup.getChildren().indexOf(b);

					otherBall= (Ball)b;
					if (!circle.equals(otherBall)){
						System.out.println(indexc+" vs "+indexb);

						double distance= circle.distanceTo(otherBall);
						if (distance<(circle.getRadius()+otherBall.getRadius())){
							System.out.println(indexc+" vs "+indexb);
							// movingToX=(circle.getLayoutX()-otherBall.getLayoutX())*(circle.getVX()-otherBall.getVX())<=0;
							//boolean movingToY=(circle.getLayoutY()-otherBall.getLayoutY())*(circle.getVY()-otherBall.getVY())<=0;
							boolean movingTogether=((otherBall.getPosition().subtract(circle.getPosition())).dot(otherBall.getVelocity().subtract(circle.getVelocity()))<=0);
							//boolean movingTogether=(circle.getLayoutX()-otherBall.getLayoutX())*(circle.getVX()-otherBall.getVX())+(circle.getLayoutY()-otherBall.getLayoutY())*(circle.getVY()-otherBall.getVY())<=0;
								if(movingTogether){
							//they bounce off each other
								//conserve energy
								double totalVx=circle.getVX()+otherBall.getVX();
								double totalVy=circle.getVY()+otherBall.getVY();
								double cmVx=totalVx/2;
								double cmVy=totalVy/2;
								//double KEx = e*circle.getVX()*circle.getVX()+otherBall.getE()*otherBall.getVX()*otherBall.getVX();
								double KEx = (e+otherBall.getE()-1)*(circle.getVX()*circle.getVX()+otherBall.getVX()*otherBall.getVX())+2*(2-e-otherBall.getE())*circle.getVX()*otherBall.getVX();
								double KEy = (e+otherBall.getE()-1)*(circle.getVY()*circle.getVY()+otherBall.getVY()*otherBall.getVY())+2*(2-e-otherBall.getE())*circle.getVY()*otherBall.getVY();

								//double KEy = e*circle.getVY()*circle.getVY()+otherBall.getVY()*otherBall.getE()*otherBall.getVY();
								double finalVx1= cmVx+.5*Math.sqrt(2*KEx-totalVx*totalVx);
								double finalVx2= totalVx-finalVx1;//cmVx-.5*Math.sqrt(2*KEx-totalVx*totalVx);
								double finalVy1= cmVy+.5*Math.sqrt(2*KEy-totalVy*totalVy);
								double finalVy2= totalVy-finalVy1;//cmVy-.5*Math.sqrt(2*KEy-totalVy*totalVy);
								System.out.println("X: "+totalVx+" "+circle.getVX()+" "+otherBall.getVX()+" "+finalVx1+" "+finalVx2);
								System.out.println("Y: "+totalVy+" "+circle.getVY()+" "+otherBall.getVY()+" "+finalVy1+" "+finalVy2);

								if (Math.abs(finalVx1)>Math.abs(finalVx2)){
									if (Math.abs(circle.getVX())>Math.abs(otherBall.getVX())){

										circle.setVX(finalVx2);
										otherBall.setVX(finalVx1);
									}
									else {
										circle.setVX(finalVx1);
										otherBall.setVX(finalVx2);
									}
								}
								else if (Math.abs(finalVx1)<=Math.abs(finalVx2)){
									if (Math.abs(circle.getVX())>Math.abs(otherBall.getVX())){

										circle.setVX(finalVx1);
										otherBall.setVX(finalVx2);
									}
									else {
										circle.setVX(finalVx2);
										otherBall.setVX(finalVx1);
									}
								}

								if (Math.abs(finalVy1)>Math.abs(finalVy2)){
									if (Math.abs(circle.getVY())>Math.abs(otherBall.getVY())){

										circle.setVY(finalVy2);
										otherBall.setVY(finalVy1);
									}
									else {
										circle.setVY(finalVy1);
										otherBall.setVY(finalVy2);
									}
								}
								else if (Math.abs(finalVy1)<=Math.abs(finalVy2)){
									if (Math.abs(circle.getVY())>Math.abs(otherBall.getVY())){
										circle.setVY(finalVy1);
										otherBall.setVY(finalVy2);
									}
									else {
										circle.setVY(finalVy2);
										otherBall.setVY(finalVy1);
									}
								}
								if ((circle.getLayoutY()>=450 || otherBall.getLayoutY()>=450)&&!(circle.getLayoutY()>=450 && otherBall.getLayoutY()>=450)){
									double temp = circle.getVY();
									circle.setVY(-1*otherBall.getVY());
									otherBall.setVY(-1*temp);
								}
							}
						}
					}
				}
			}
			for (Node c: circleGroup.getChildren()){
				circle = (Ball)c;
				v= circle.getVelocity();
				vY = v.y;
				vX= v.x;
				//vX=circle.getVX();
                //vY=circle.getVY();
                e=circle.getE();
                final Bounds bounds = canvas.getBoundsInLocal();
                //System.out.println(" vY "+circle.getVY());
                double currentY = circle.getLayoutY();
                double currentX = circle.getLayoutX();

                initialE = .5*vY*vY+aY*(450-currentY);
                //initialE = .5*v.mag()*v.mag()+aY*(450-currentY);
				//initialE=circle.calculateEnergy(450-currentY, aY);
//				if (initialE<.00001)
//					try {
//						stop();
//					} catch (Exception e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
                System.out.println(.5*vY*vY+aY*(450-currentY)+"   "+initialE);
                //if (circle.getLayoutY()>=450)circle.setLayoutY(450-circle.getRadius());
            	//if (circle.getLayoutY()>bounds.getMaxY() - 5*circle.getRadius()) circle.setLayoutY(bounds.getMaxY() - 5*circle.getRadius());
//            	circle.setLayoutY(circle.getLayoutY() + vY);
//                circle.setLayoutX(circle.getLayoutX() + vX);


//                final boolean atRightBorder = circle.getLayoutX() >= 800;//(bounds.getMaxX() - circle.getRadius());
//                final boolean atLeftBorder = circle.getLayoutX() <= 0;//(bounds.getMinX() + circle.getRadius());
//                final boolean atBottomBorder = circle.getLayoutY() >= 450;//(bounds.getMaxY() - 5*circle.getRadius());
//                final boolean atTopBorder = circle.getLayoutY() <= 0;//(bounds.getMinY() + circle.getRadius());

                final boolean atRightBorder = currentX+vX >= 800;//(bounds.getMaxX() - circle.getRadius());
                final boolean atLeftBorder = currentX+vX <= 0;//(bounds.getMinX() + circle.getRadius());
                final boolean atBottomBorder = currentY+vY >= 450;//(bounds.getMaxY() - 5*circle.getRadius());
                final boolean atTopBorder = currentY+vY <= 0;//(bounds.getMinY() + circle.getRadius());



                if (atBottomBorder) {
                	if (Math.abs(vY)>0){	// if still bouncing, not rolling along bottom
                		//double timePreBounce=(450-currentY)/vY;
                		double timePreBounce = (Math.sqrt(2*initialE)-vY)/aY;
                		double timePostBounce=1-timePreBounce;

                		double prebounceVY= Math.sqrt(2*initialE); //vY+timePreBounce*aY;
                		double postBounceVY = -1*e*Math.abs(prebounceVY);
                		//circle.setVY(-1*e*prebounceVY + (currentY+vY-450)/vY*aY);
                		//if (2*aY+postBounceVY>0) {
                		double postBounceE = .5*postBounceVY*postBounceVY;
                		if (initialE<.1){
                			circle.setVY(0);
                			circle.setLayoutY(450);
                		}
                		else {
                			circle.setVY(postBounceVY+aY*timePostBounce);
                			//circle.setVY(postBounceVY+aY*timePostBounce);
                			circle.setLayoutY(450-(postBounceE-.5*circle.getVY()*circle.getVY())/aY);
                			//circle.setLayoutY(450+postBounceVY*timePostBounce);
                		}
                		//vX*=friction;

                	}


                }
                else {
                	if (Math.abs(vY)>=0) circle.setVY(vY+aY);
                	//circle.setLayoutY(currentY+vY);
                	circle.setLayoutY(450-(initialE-.5*circle.getVY()*circle.getVY())/aY);
                }

                if (atRightBorder){
                	double bounceVX = -1*e*vX;
                	double timePreBounce = (800-currentX)/vX;
                	double timePostBounce = 1-timePreBounce;
                	//circle.setLayoutX(800*2-currentX-vX);
                	circle.setLayoutX(800+bounceVX*timePostBounce);
                	circle.setVX(bounceVX);
                }
                else if (atLeftBorder){
                	double bounceVX = Math.abs(e*vX);
                	double timePreBounce = (currentX-0)/vX;
                	double timePostBounce = 1-timePreBounce;
                	circle.setLayoutX(0+bounceVX*timePostBounce);
                	circle.setVX(bounceVX);
                }
                else {
                	//if (Math.abs(vY)==0) vX*=friction;
                	if (atBottomBorder) {
                		vX*=friction;
                		circle.setVX(vX);
                	}

                	circle.setLayoutX(currentX+vX);

                }
            }
		}
    }

    //public static Circle circle;
    //public static Circle circle2;
    public static Pane canvas;
    public static Line ground;
    public static Ball ball1;
    public static Ball ball2;
    public static Ball ball3,ball4;


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
        ball2.setE(.9);
        ball2.setV(0, -5);
       // ball2.setVX(0);
        //ball2.setVY(-5);
        ball3 = new Ball(10,Color.GREENYELLOW);
        ball3.relocate(200,100);
        ball3.setE(.9);
        ball4 = new Ball(10,Color.MEDIUMORCHID);
        ball4.relocate(300,100);
        ball4.setE(.9);
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


        final Timeline loop = new Timeline(new KeyFrame(Duration.millis(70),new BallEventHandler(ballGroup)));

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



    public static void main(final String[] args) {
        launch(args);
    }

}