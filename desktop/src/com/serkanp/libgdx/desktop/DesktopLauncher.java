package com.serkanp.libgdx.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.serkanp.libgdx.LibGdx;
import com.serkanp.libgdx.UcuncuBoyut;
import com.serkanp.libgdx.UcuncuBoyutYuzey;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new UcuncuBoyutYuzey(), config);
	//	new LwjglApplication(new LibGdx(), config);
	}
}
