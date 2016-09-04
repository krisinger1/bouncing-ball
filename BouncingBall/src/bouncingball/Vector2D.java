package bouncingball;

public class Vector2D {
	double x;
	double y;

	public Vector2D(double x,double y){
		this.x=x;
		this.y=y;
	}

	public Vector2D add(Vector2D v){
		return (new Vector2D(this.x+v.x, this.y+v.y));
	}

	public Vector2D subtract(Vector2D v){
		return (new Vector2D(this.x-v.x,this.y-v.y));
	}

	public double dot(Vector2D v){
		return this.x*v.x+this.y*v.y;
	}
	
	public Vector2D getUnitVector(){
		return new Vector2D(x/this.mag(),y/this.mag());
	}
	
	public double scProjOnto(Vector2D v){
		return dot(v)/v.mag();
	}
	
	public Vector2D vProjOnto(Vector2D v){
		return new Vector2D(scProjOnto(v)*v.getUnitVector().x,scProjOnto(v)*v.getUnitVector().y);
	}

	public double mag(){
		return Math.sqrt(this.x*this.x+this.y*this.y);
	}

	public boolean equals(Vector2D v){
		if (x==v.x && y==v.y) return true;
		else return false;
	}


}
