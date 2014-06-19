package mindgame;

import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;

public class Ball extends VerletParticle2D{
	public static float maxVelocity = 30.f;
			
	Mindgame parent;
	MidiAction midi;
	float radius = 10.f;
	
	Ball(Mindgame _parent, float _x, float _y){
		super(_x, _y);
		parent = _parent;
		this.addVelocity(new Vec2D((float) (Math.random()*8.f), 0.f));
	}
	
	public void updateBall(){
		Vec2D v = this.getVelocity();
		if(y+radius - parent.height >= 0 && v.y > 0){
			this.addVelocity(v.scale(0.f, -2.f));
			parent.midi.ballHit(MidiAction.FLOOR);
		} if(x+radius - parent.width >= 0){
			this.addVelocity(v.scale(-2.f, Math.signum(v.y)));
			parent.midi.ballHit(MidiAction.PLAYER2);
		} else if(x-radius <= 0){
			this.addVelocity(v.scale(-2.f, Math.signum(v.y)));
			parent.midi.ballHit(MidiAction.PLAYER1);
		}

	}
	
	public void draw(){
		updateBall();
		parent.noStroke();
		parent.fill(255);
		parent.ellipse(x, y, radius, radius);
	}
}
