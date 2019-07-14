package ninja.cooperstuff.pokemon.entity;

import ninja.cooperstuff.engine.events.KeyListener;
import ninja.cooperstuff.engine.util.Keys;
import ninja.cooperstuff.engine.util.Vector;
import ninja.cooperstuff.pokemon.monster.Monster;
import ninja.cooperstuff.pokemon.util.Direction;
import ninja.cooperstuff.pokemon.util.Stats;
import ninja.cooperstuff.pokemon.world.World;

import java.awt.*;

public class Pokemon extends Entity {
	public Monster monster;
	protected Stats valuesEffort = new Stats();
	protected Stats valuesIndividual = new Stats();
	protected Stats stats;
	protected int walkCycle = 0;
	public Direction facing = Direction.DOWN;
	protected boolean moving = false;

	public Pokemon(World world, Monster monster) {
		super(world);
		this.setMonster(monster);
	}

	public void setMonster(Monster monster) {
		this.monster = monster;
		this.shadow.scale = this.monster.getShadowSize();
	}

	@Override
	public void update() {
		this.shadow.scale = this.monster.getShadowSize();
		if ((!this.moving && this.frame % (2 * this.monster.getAnimationSpeed()) == 0) || (this.moving && this.frame % this.monster.getAnimationSpeed() == 0)) {
			this.walkCycle = 1 - walkCycle;
		}
		this.moving = false;
	}

	@Override
	public void render(Graphics2D screen) {
		super.render(screen);
		Vector offset = this.monster.getSpriteOffset(this.facing);
		int x = (int) (offset.x - 32);
		int y = (int) (offset.y - this.walkCycle * this.monster.getBobHeight(this.facing) - 60);
		screen.drawImage(this.monster.spriteLayout.get(this.facing)[this.walkCycle], x, y, 64, 64, null);
		Vector col1 = this.monster.getCollisionCorner1();
		Vector col2 = this.monster.getCollisionCorner2();
		if (Player.collisionMode || KeyListener.isKeyHeld(Keys.SPACE)) screen.drawRect((int) col1.x, (int) col1.y, (int) (col2.x - col1.x), (int) (col2.y - col1.y));
	}
}
