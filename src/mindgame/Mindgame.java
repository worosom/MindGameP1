package mindgame;

import processing.core.PApplet;
import themidibus.MidiBus;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletPhysics2D;
import toxi.physics2d.behaviors.GravityBehavior;

@SuppressWarnings("serial")
public class Mindgame extends PApplet {

	public static void main(String[] args) {
		PApplet.main("mindgame.Mindgame");
	}

	VerletPhysics2D physics;
	Ball ball;

	MidiAction midi;

	public void setup() {
		size(600, 400);
		frameRate(60);
		MidiBus.list();

		physics = new VerletPhysics2D();
		physics.setDrag(.001f);
		ball = new Ball(this, width / 2.f, height / 2.f);
		physics.addParticle(ball);
		physics.addBehavior(new GravityBehavior(new Vec2D(0.f, .3f)));
		midi = new MidiAction(this);
		midi.ball = ball;
	}

	public void draw() {
		physics.update();
		midi.update();
		background(0);
		if (ball.y + ball.radius - 1 > height
				&& ball.getVelocity().magnitude() < 3.f || ball.y < -height * 2
				|| Math.abs(ball.getVelocity().x) > Ball.maxVelocity)
			createBall();
		ball.draw();
	}

	public void mousePressed() {
		createBall();
	}

	public void createBall() {
		physics.removeParticle(ball);
		ball = new Ball(this, width / 2, height / 2);
		physics.addParticle(ball);
	}
}
