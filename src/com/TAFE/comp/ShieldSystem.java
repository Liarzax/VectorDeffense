package com.TAFE.comp;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

public class ShieldSystem {
	
	public boolean shieldEnabled = false;
	private int shieldMax = 2;
	private int shieldCur = shieldMax;
	
	// curX, curY, Range;
	Circle shield = null;
	private float shieldRange = 24f;
	public boolean removeShield;
	
	
	public ShieldSystem() {
		
	}
	
	public void initShieldSystem(float curX, float curY) {
		shieldEnabled = true;
		shield = new Circle(curX, curY, shieldRange);
	}
	
	public void updateShieldSystem(float curX, float curY) {
		if (shieldEnabled && shield != null) {
			// just give it 1hp for now?
			
			// update position with ship
			shield.setCenterX(curX);
			shield.setCenterY(curY);
		}
	}
	
	public void renderShieldSystem(Graphics g) {
		if (removeShield) {
			
		}
		else if (shieldEnabled && shield != null) {
			g.setColor(Color.cyan);
			g.draw(shield);
		}
	}
	
	public void dammageShield(int dammage) {
		shieldCur -= dammage;
		if (shieldCur < 0) {
			shieldEnabled = false;
			//shield = null;
		}
	}
	
	public void regenShield() {
		
	}

	public boolean isShieldEnabled() {
		return shieldEnabled;
	}

	public void setShieldEnabled(boolean shieldEnabled) {
		this.shieldEnabled = shieldEnabled;
	}

	public int getShieldMax() {
		return shieldMax;
	}

	public void setShieldMax(int shieldMax) {
		this.shieldMax = shieldMax;
	}

	public int getShieldCur() {
		return shieldCur;
	}

	public void setShieldCur(int shieldCur) {
		this.shieldCur = shieldCur;
	}
	
	

}