package mindgame;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.*;

public class SoundSystem extends Thread{
	public static int BUFFERSIZE = 1024;
	public static int FORMAT = AL10.AL_FORMAT_MONO16;
	
	public static int FMTSIZE = 16 / 4;
	public static int FREQ = 44100;
	private ALCdevice device;
	IntBuffer sampleCount;
	ByteBuffer buf = BufferUtils.createByteBuffer(FREQ*FMTSIZE);
	
	IntBuffer buffers = BufferUtils.createIntBuffer(1);
	IntBuffer sources = BufferUtils.createIntBuffer(1);
	
	SoundSystem() throws LWJGLException{
		AL.create("CoreAudio", 44100, 15, false);
		setup();
		start();
	}
	
	private void setup(){
		sampleCount = BufferUtils.createIntBuffer(1);
		ByteBuffer buf = BufferUtils.createByteBuffer(BUFFERSIZE*FMTSIZE);
		if (!ALC10.alcIsExtensionPresent(AL.getDevice(), "ALC_EXT_CAPTURE")) {
			throw new OpenALException("ALC_EXT_CAPTURE extension not available");
		}
		device = ALC11.alcCaptureOpenDevice("mdev", FREQ, FORMAT, BUFFERSIZE);
		
		ALC11.alcCaptureStart(device);
		buffers.position(0).limit(1);
		AL10.alGenBuffers(buffers);
		
		sources.position(0).limit(1);
		AL10.alGenSources(sources);
		
		AL10.alBufferData(buffers.get(0), FORMAT, buf, FREQ);
		AL10.alSourcei(sources.get(0), AL10.AL_BUFFER, buffers.get(0));
		AL10.alSourcei(buffers.get(0), AL10.AL_LOOPING, AL10.AL_FALSE);
		AL10.alSourcePlay(sources.get(0));
	}
	
	public void run(){
		System.out.println("Sound Thread started!");
		while(true){
			while(sampleCount.get(0) < BUFFERSIZE){
				ALC10.alcGetInteger(device, ALC11.ALC_CAPTURE_SAMPLES, sampleCount);
			}
			ALC11.alcCaptureSamples(device, buf, BUFFERSIZE);

		}
	}
}
