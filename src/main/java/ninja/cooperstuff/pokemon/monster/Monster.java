package ninja.cooperstuff.pokemon.monster;

import ninja.cooperstuff.engine.util.RandomGet;
import ninja.cooperstuff.engine.util.Vector;
import ninja.cooperstuff.pokemon.init.Types;
import ninja.cooperstuff.pokemon.move.Move;
import ninja.cooperstuff.pokemon.sound.Sound;
import ninja.cooperstuff.pokemon.sound.SoundClip;
import ninja.cooperstuff.pokemon.type.Type;
import ninja.cooperstuff.pokemon.util.Constants;
import ninja.cooperstuff.pokemon.util.Direction;
import ninja.cooperstuff.pokemon.util.Stats;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class Monster {
	public static HashSet<Monster> monsters = new HashSet<>();
	public static ArrayList<Monster> ids = new ArrayList<>();

	static {
		Monster.ids.add(null);
	}

	public String name;
	public Type type1;
	public Type type2;
	public Stats baseStats;
	private BufferedImage sprite;
	private BufferedImage spriteShiny;
	//public OggClip cry;
	private Sound cry;
	public SpriteLayout spriteLayout;
	public SpriteLayout spriteLayoutShiny;
	public HashMap<Direction, Vector> spriteOffset = new HashMap<>();
	public HashMap<Direction, Integer> bobHeight = new HashMap<>();
	public Vector collisionCorner1 = new Vector(-16, -32);
	public Vector collisionCorner2 = new Vector(16, 0);
	private double shadowSize = 1;
	private int animationSpeed = 15;

	public Monster(String name, Type type1, Type type2, int health, int attackPhysical, int attackSpecial, int defensePhysical, int defenseSpecial, int speed) {
		this.name = name;
		this.type1 = type1;
		this.type2 = type2;
		this.baseStats = new Stats(health, attackPhysical, attackSpecial, defensePhysical, defenseSpecial, speed);
		try {
			this.sprite = ImageIO.read(this.getClass().getResourceAsStream(String.format("/pokemon/sprites/%s.png", this.name.toLowerCase())));
			this.spriteShiny = ImageIO.read(this.getClass().getResourceAsStream(String.format("/pokemon/shiny/%s.png", this.name.toLowerCase())));
			this.cry = new Sound(String.format("/pokemon/cries/%s", this.name.toLowerCase()), Constants.audio.cryVolume);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.spriteLayout = new SpriteLayout(this.sprite);
		this.spriteLayoutShiny = new SpriteLayout(this.spriteShiny);
		Monster.monsters.add(this);
		Monster.ids.add(this);
	}

	public Monster setSpriteOffset(Direction dir, Vector vector) {
		this.spriteOffset.put(dir, vector);
		return this;
	}

	public Vector getSpriteOffset(Direction dir) {
		if (this.spriteOffset.containsKey(dir)) return this.spriteOffset.get(dir);
		return new Vector();
	}

	public Monster setCollisionCorner1(Vector vector) {
		this.collisionCorner1 = vector;
		return this;
	}

	public Vector getCollisionCorner1() {
		return collisionCorner1;
	}

	public Monster setCollisionCorner2(Vector vector) {
		this.collisionCorner2 = vector;
		return this;
	}

	public Vector getCollisionCorner2() {
		return collisionCorner2;
	}

	public Monster setShadowSize(double size) {
		this.shadowSize = size;
		return this;
	}

	public double getShadowSize() {
		return this.shadowSize;
	}

	public Monster setBobHeight(Direction dir, Integer integer) {
		this.bobHeight.put(dir, integer);
		return this;
	}

	public int getBobHeight(Direction dir) {
		if (this.bobHeight.containsKey(dir)) return this.bobHeight.get(dir);
		return 1;
	}

	public Monster setAnimationSpeed(int speed) {
		this.animationSpeed = speed;
		return this;
	}

	public int getAnimationSpeed() {
		return this.animationSpeed;
	}

	public BufferedImage getSprite(Direction dir, int frame) {
		return this.spriteLayout.get(dir, frame);
	}

	public BufferedImage getSprite(Direction dir, int frame, boolean shiny) {
		if (shiny) return this.spriteLayoutShiny.get(dir, frame);
		return this.spriteLayout.get(dir, frame);
	}

	public SoundClip startCry() {
		return this.cry.play();
	}

	public Move getRandomMove() {
		Move move = null;
		while (move == null) {
			double r = new Random().nextDouble();
			if (r < 0.2) move = RandomGet.get(Types.NORMAL.moves);
			else if (this.type2 != null && r < 0.6) move = RandomGet.get(this.type2.moves);
			else move = RandomGet.get(this.type1.moves);
		}
		return move;
	}

	public ArrayList<Move> getMoves() {
		ArrayList<Move> moves = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			Move move;
			int count = 15;
			do {
				move = this.getRandomMove();
				if (count == 0) return moves;
				count--;
			}
			while (moves.contains(move));
			moves.add(move);
		}
		return moves;
	}

	public static class SpriteLayout {
		private static int size = 32;

		public BufferedImage[] up = new BufferedImage[2];
		public BufferedImage[] down = new BufferedImage[2];
		public BufferedImage[] left = new BufferedImage[2];
		public BufferedImage[] right = new BufferedImage[2];

		public SpriteLayout(BufferedImage sprite) {
			this.up[0] = sprite.getSubimage(0, 0, size, size);
			this.up[1] = sprite.getSubimage(size, 0, size, size);
			this.down[0] = sprite.getSubimage(0, size, size, size);
			this.down[1] = sprite.getSubimage(size, size, size, size);
			this.left[0] = sprite.getSubimage(0, 2*size, size, size);
			this.left[1] = sprite.getSubimage(size, 2*size, size, size);
			this.right[0] = sprite.getSubimage(size, 3*size, size, size);
			this.right[1] = sprite.getSubimage(0, 3*size, size, size);
		}

		public BufferedImage[] get(Direction dir) {
			switch (dir) {
				case UP:
					return this.up;
				case DOWN:
					return this.down;
				case LEFT:
					return this.left;
				default:
					return this.right;
			}
		}

		public BufferedImage get(Direction dir, int index) {
			BufferedImage[] bi = this.get(dir);
			return bi[index % bi.length];
		}
	}
}
