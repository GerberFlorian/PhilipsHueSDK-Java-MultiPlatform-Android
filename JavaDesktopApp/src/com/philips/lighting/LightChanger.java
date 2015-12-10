package com.philips.lighting;

import java.util.List;
import java.util.Random;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResourcesCache;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

public class LightChanger implements Runnable {

	private volatile boolean shutdown;
	private PHHueSDK phHueSDK;
	private static final int MAX_HUE=65535;
	private static final long INTERVAL_TIME= 10000L;
	
	public LightChanger() {
		this.phHueSDK = PHHueSDK.getInstance();
	}
	
	@Override
	public void run() {
		
		  PHBridge bridge = phHueSDK.getSelectedBridge();
	        PHBridgeResourcesCache cache = bridge.getResourceCache();

	        List<PHLight> allLights = cache.getAllLights();
	        Random rand = new Random();
		
		while(!shutdown) {
	        for (PHLight light : allLights) {
	            PHLightState lightState = new PHLightState();
	            lightState.setHue(rand.nextInt(MAX_HUE));
	            bridge.updateLightState(light, lightState); // If no bridge response is required then use this simpler form.
	        }
	        
	        try {
				Thread.sleep(INTERVAL_TIME);
			} catch (InterruptedException e) {
				shutdown();
			}		
		}
	}
	
	public void shutdown() {
		shutdown = true;
	}

}
