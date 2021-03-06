package com.au.Stark.VectorDefence;

import com.badlogic.gdx.math.Intersector;

public class CollisionHandler {
	
	//Intersector intersector = new Intersector();
	
	public void CollisionHandler() {
		
	}
	
	public void initCollisionHandler() {
		
	}
	
	// check for bullets v enemies, bullets v player, enemies v player
	public void checkForCollisions(BulletHandler bullets, EnemyHandler enemies) {
		// temp collisions.
		if(!bullets.bullets.isEmpty() && !enemies.enemiesShip.isEmpty()) {
			for (int b = 0; b < bullets.getAmmountOfBullets(); b++) {
				for (int e = 0; e < enemies.getAmmountOfEnemies(); e++) {
					// if collide, damage enemy, remove bullet.
					// ship type 0 = normal red, type 1 = power up yellow, type 2 = blue can fire back!
					// bullet type 0 = players, bullet type 1 = enemies																	// should not matter since powerups will not have shields?	or this could be interesting? w	 && (enemies.enemiesShip.get(e).type != 1 && enemies.enemiesShip.get(e).type != 3)
					if (enemies.enemiesShip.get(e).shieldSystem.isShieldEnabled()) {	
						//if(bullets.bullets.get(b).bullet.intersects(enemies.enemiesShip.get(e).shieldSystem.shield)) {
						if (bullets.bullets.get(b).getBulletType() == 0) {
							//if (bullets.bullets.get(b).bullet.intersects(enemies.enemiesShip.get(e).shieldSystem.shield)) {
							if (Intersector.overlaps(enemies.enemiesShip.get(e).shieldSystem.shield, bullets.bullets.get(b).bullet)) {
								enemies.enemiesShip.get(e).shieldSystem.dammageShield(bullets.bullets.get(b).getBulletDammage());
								bullets.bullets.get(b).needsToRemove = true;
								//System.out.println("ENEMY SHIELD DAMMAGED!");
							}
						}
					}
					//if (bullets.bullets.get(b).bullet.intersects(enemies.enemiesShip.get(e).ship) && bullets.bullets.get(b).getBulletType() == 0 && (enemies.enemiesShip.get(e).type != 1 && enemies.enemiesShip.get(e).type != 3)) {
					if (Intersector.overlaps(enemies.enemiesShip.get(e).ship, bullets.bullets.get(b).bullet) && bullets.bullets.get(b).getBulletType() == 0 && (enemies.enemiesShip.get(e).type != 1 && enemies.enemiesShip.get(e).type != 3)) {	
						enemies.enemiesShip.get(e).processDammage(bullets.bullets.get(b).getBulletDammage());
						if(enemies.enemiesShip.get(e).getHealthCur() <= 0) {
							enemies.enemiesShip.get(e).deathFromPlayer = true;
						}
						bullets.bullets.get(b).needsToRemove = true;
					}
					else if (Intersector.overlaps(bullets.bullets.get(b).bullet, enemies.enemiesShip.get(e).ship) && bullets.bullets.get(b).getBulletType() == 1 && (enemies.enemiesShip.get(e).type != 1 && enemies.enemiesShip.get(e).type != 3)) {
						bullets.bullets.get(b).needsToRemove = true;
					}
				}
			}
		}
	}
	public void checkForCollisions(BulletHandler bullets, Ship player) {
		if(!bullets.bullets.isEmpty() && !player.needsToRemove) {
			for (int b = 0; b < bullets.getAmmountOfBullets(); b++) {
				// if collide, damage player, remove bullet.
				// if bullet hits players shield, and bullet is enemies, and shield is active, damage the shield.
				if(Intersector.overlaps(player.shieldSystem.shield, bullets.bullets.get(b).bullet) && bullets.bullets.get(b).getBulletType() == 1 && player.shieldSystem.isShieldEnabled()) {
					player.shieldSystem.dammageShield(bullets.bullets.get(b).getBulletDammage());
					bullets.bullets.get(b).needsToRemove = true;
					//player.shieldSystem.removeShield = true;
					//player.shieldSystem.shield.setRadius(0);
				}
				// else if the bullet hits the player and is the enemies, damage the player
				if (Intersector.overlaps(player.ship, bullets.bullets.get(b).bullet) && bullets.bullets.get(b).getBulletType() == 1) {
					player.processDammage(bullets.bullets.get(b).getBulletDammage());
					bullets.bullets.get(b).needsToRemove = true;
				}
			}
		}
	}
	public void checkForCollisions(EnemyHandler enemies, Ship player) {
		if(!enemies.enemiesShip.isEmpty() && !player.needsToRemove) {
			for (int e = 0; e < enemies.getAmmountOfEnemies(); e++) {
				// if collide, damage player, remove ship.
				// type 0 = normal red, type 1 = power up yellow, type 2 = blue can fire back!
				if (Intersector.overlaps(player.ship, enemies.enemiesShip.get(e).ship) && (enemies.enemiesShip.get(e).type != 1 && enemies.enemiesShip.get(e).type != 3)) {
					player.processDammage(enemies.enemiesShip.get(e).getShipCollisionDammage());
					enemies.enemiesShip.get(e).collideWithPlayer = true;
					enemies.enemiesShip.get(e).deathFromPlayer = true;
					enemies.enemiesShip.get(e).needsToRemove = true;
				}
				else if(Intersector.overlaps(enemies.enemiesShip.get(e).ship, player.ship) && (enemies.enemiesShip.get(e).type == 1 || enemies.enemiesShip.get(e).type == 3)) {
					player.proccessPowerup(player, enemies.enemiesShip.get(e).type);
					enemies.enemiesShip.get(e).collideWithPlayer = true;
					enemies.enemiesShip.get(e).deathFromPlayer = true;
					enemies.enemiesShip.get(e).needsToRemove = true;
					
					// temp bullet leveling stuff.
					if (enemies.enemiesShip.get(e).type == 1) {
						player.setWeaponEXP(player.getWeaponEXP() + 1);
						if (player.getWeaponEXP() >= 5) {
							player.setWeaponLevel(player.getWeaponLevel() + 1);
							player.setWeaponEXP(0);
						}
					}
					
					// temp shield leveling stuff.
					/*if (enemies.enemiesShip.get(e).type == 3) {
						player.setWeaponEXP(player.getWeaponEXP() + 1);
						if (player.getWeaponEXP() >= 3) {
							player.setWeaponLevel(player.getWeaponLevel() + 1);
							player.setWeaponEXP(0);
						}
					}*/
				}
			}
		}
	}
	
	// process collisions.
	public void processCollisions(BulletHandler bullets, EnemyHandler enemies, Ship player, HUD hud) {
		// remove any ships or bullets
		//not to sure why this is spastic and i cant put it in the above collisions
		for (int s = 0; s < enemies.getAmmountOfEnemies(); s++) {
			if (enemies.enemiesShip.get(s).needsToRemove && enemies.enemiesShip.get(s).type == 0) {
				if (enemies.enemiesShip.get(s).deathFromPlayer == true) {
					hud.increasePoints();
				}
				enemies.enemiesShip.remove(s);
			}
			else if (enemies.enemiesShip.get(s).needsToRemove && (enemies.enemiesShip.get(s).type == 1 || enemies.enemiesShip.get(s).type == 3)) {
				if (enemies.enemiesShip.get(s).collideWithPlayer == true) {
					hud.displayPowerUpRecived(enemies.enemiesShip.get(s).type);
				}
				enemies.enemiesShip.remove(s);
			}
			else if (enemies.enemiesShip.get(s).needsToRemove && enemies.enemiesShip.get(s).type == 2) {
				if (enemies.enemiesShip.get(s).deathFromPlayer == true) {
					hud.increasePoints();
					hud.increasePoints();
				}
				enemies.enemiesShip.remove(s);
			}
			// Boss ship died
			else if (enemies.enemiesShip.get(s).needsToRemove && enemies.enemiesShip.get(s).type == 10) {
				if (enemies.enemiesShip.get(s).deathFromPlayer == true) {
					hud.increasePoints();
					hud.increasePoints();
				}
				enemies.enemiesShip.remove(s);
				enemies.setBossSpawned(false);
				System.out.println("Boss Destroyed!");
				enemies.setBossDestroyedTimes(enemies.getBossDestroyedTimes() + 1);
			}
		}
		
		for (int b = 0; b < bullets.getAmmountOfBullets(); b++) {
			if (bullets.bullets.get(b).needsToRemove) {
				bullets.bullets.remove(b);
			}
		}
		
		// ooh dear, player has died. Blegh. 
		if (player.needsToRemove) {
			player.shieldSystem.setShieldEnabled(false);
			hud.needToDisplayDeathMessage();
		}
	}

}
