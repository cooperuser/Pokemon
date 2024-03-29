package ninja.cooperstuff.pokemon.entity;

import ninja.cooperstuff.engine.components.GameObject;
import ninja.cooperstuff.engine.util.Vector;
import ninja.cooperstuff.pokemon.util.Constants;
import ninja.cooperstuff.pokemon.world.World;

import java.awt.*;

public abstract class Entity extends GameObject {
	public World world;
	public Shadow shadow = new Shadow(1);

	public Entity(World world) {
		this.world = world;
	}

	@Override
	public void render(Graphics2D screen) {
		this.shadow.render(screen);
	}

	public static class Shadow {
		public Color color = new Color(0, 0, 0, Constants.shadowOpacity);
		public Vector size = new Vector(32, 16);
		public double scale;

		public Shadow(double scale) {
			this.scale = scale;
		}

		public void render(Graphics2D screen) {
			Color color = screen.getColor();
			screen.setColor(this.color);
			int x = (int) (-this.size.x * this.scale / 2);
			int y = (int) (-this.size.y * this.scale / 2);
			screen.fillOval(x, y, (int) (this.size.x * this.scale), (int) (this.size.y * this.scale));
			screen.setColor(color);
		}
	}
}
