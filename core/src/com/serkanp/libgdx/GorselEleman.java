package com.serkanp.libgdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class GorselEleman extends Actor{
	Texture resim;
	public GorselEleman(Texture resim) {
		super();
		this.resim = resim;
		setSize(640,480);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		Color color = getColor();
		batch.draw(resim, this.getX(),this.getY(), this.getOriginX(), this.getOriginY(), this.getWidth(),
				this.getHeight(), this.getScaleX(), this.getScaleY(), this.getRotation(),0, 0, resim.getWidth(), resim.getHeight(),
				false,false);
		batch.setColor(color.a,color.g, color.b, color.a * 1.0F);
		
	}
}
