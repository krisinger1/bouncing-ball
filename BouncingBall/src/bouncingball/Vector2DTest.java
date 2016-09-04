package bouncingball;

import junit.framework.TestCase;

public class Vector2DTest extends TestCase {

	Vector2D a,b,c,d;

	public void setup(){
		a=new Vector2D(3,4);
		b=new Vector2D(3,-4);
		c=new Vector2D(0,5);
		d=new Vector2D(4,0);
	}

	public void testVector2D() {
		setup();
		assertEquals(a.x,3.0);
		assertEquals(a.y,4.0);
	}

	public void testAdd() {
		setup();
		assertTrue((a.add(b)).equals(new Vector2D(6,0)));
	}

	public void testSubtract() {
		setup();
		assertTrue((a.subtract(b)).equals(new Vector2D(0,8)));
		assertTrue((b.subtract(a).equals(new Vector2D(0,-8))));
	}

	public void testDot() {
		setup();
		assertEquals(a.dot(b),-7.0);
		assertEquals(c.dot(d),0.0);
	}
	
	public void testGetUnitVector(){
		setup();
		Vector2D unit=a.getUnitVector();
		assertTrue(unit.equals(new Vector2D(3.0/5,4.0/5)));
		assertTrue(c.getUnitVector().equals(new Vector2D(0,1)));
	}

	public void testScProjOnto(){
		setup();
		assertEquals(a.scProjOnto(c),4.0);
		assertEquals(d.scProjOnto(a),12.0/5);
		
	}
	
	public void testVProjOnto(){
		setup();
		assertTrue(a.vProjOnto(c).equals(new Vector2D(0,4)));
	}
	
	public void testMag() {
		setup();
		assertEquals(a.mag(),5.0);
		assertEquals(b.mag(),5.0);
	}



}
