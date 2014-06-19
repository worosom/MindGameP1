package mindgame;

import themidibus.ControlChange;
import themidibus.MidiBus;
import themidibus.Note;

public class MidiAction {
	public static int PLAYER1 = 0;
	public static int PLAYER2 = 1;
	public static int FLOOR = 2;
	Mindgame parent;
	MidiBus midibus;
	Ball ball;
	
	Player[] player = new Player[2];
	
	ControlChange ballx = new ControlChange(4, 0, 0);
	ControlChange bally = new ControlChange(4, 1, 0);
	ControlChange ballvx = new ControlChange(4, 2, 0);
	ControlChange ballvy = new ControlChange(4, 3, 0);
	
	MidiAction(Mindgame _parent){
		parent = _parent;
		midibus = new MidiBus(this, -1, "IAC-Bus 1");
	}
	
	private void setup(){
		player[0].setPosition(0);
		player[1].setPosition(parent.width);
	}
	
	public void update(){
		ball = parent.ball;
		ballx.setValue((int) (ball.x / parent.width * 127));
		bally.setValue((int) (ball.y / parent.height * 127));
		float pitchbendp1 = (.5f/Ball.maxVelocity*ball.getVelocity().x+.5f);
		ballvx.setValue((int) (pitchbendp1*127));
		ballvy.setValue((int) (Math.abs(ball.getVelocity().y+10)*2.45f));
		midibus.sendControllerChange(ballx);
		//midibus.sendControllerChange(bally);
		midibus.sendControllerChange(ballvx);
		//midibus.sendControllerChange(ballvy);
	}
	
	public void ballHit(int what){
		
		midibus.sendNoteOn(10, 1, 127);
		midibus.sendNoteOff(10, 1, 127);
		switch(what){
		case 0:
			new MidiThread(new Note(0, 12, (int) (Math.abs(ball.getVelocity().x) * 3.f)));
			break;
		case 1:
			new MidiThread(new Note(0, 13, (int) (Math.abs(ball.getVelocity().x) * 3.f)));
			break;
		case 2:
			new MidiThread(new Note(0, 17, (int) (Math.abs(ball.getVelocity().y) * 3.f)));
			break;
		default:
			println("Invalid hit identifier: " + what);
		}
		
	}
	
	private void println(String what){
		System.out.println(what);
	}
	
	class MidiThread extends Thread{
		Note note;
		MidiThread(Note _note){
			note = _note;
			start();
		}
		
		public void run(){
			midibus.sendNoteOn(note);
			long time = (long) Math.abs(ball.getVelocity().x)*20;
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			midibus.sendNoteOff(note);
		}


	}
	
	class Player{
		float position;
		float doppler;
		float distball;
		
		Player(){
			doppler = 0;
			distball = 0;
		}
		
		void setPosition(float pos){
			position = pos;
		}
		
		void update(){
			distball = ball.x/parent.width;
		}
	}
}
