package ninja.cooperstuff.pokemon.tile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ConnectedTile {
	protected String name;
	protected String path;
	protected BufferedImage sprite;
	protected boolean walkable;
	protected boolean walkableCenter;
	protected Tile[] tiles = new Tile[12];
	public final Tile tile;

	public ConnectedTile(String name, String path, boolean walkable, boolean walkableCenter) {
		this.name = name;
		this.path = path;
		this.walkable = walkable;
		this.walkableCenter = walkableCenter;
		try {
			this.sprite = ImageIO.read(this.getClass().getResourceAsStream(String.format("/pokemon/tiles/%s", this.path)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int size = 16;
		this.tile = new Tile(name + "_00", walkableCenter).setSprite(this.sprite.getSubimage(1 * size, 1 * size, size, size));
		this.tiles[0] = new Tile(name + "_01", walkable).setSprite(this.sprite.getSubimage(1 * size, 2 * size, size, size));
		this.tiles[1] = new Tile(name + "_02", walkable).setSprite(this.sprite.getSubimage(1 * size, 0 * size, size, size));
		this.tiles[2] = new Tile(name + "_03", walkable).setSprite(this.sprite.getSubimage(2 * size, 1 * size, size, size));
		this.tiles[3] = new Tile(name + "_04", walkable).setSprite(this.sprite.getSubimage(0 * size, 1 * size, size, size));
		this.tiles[4] = new Tile(name + "_05", walkable).setSprite(this.sprite.getSubimage(4 * size, 1 * size, size, size));
		this.tiles[5] = new Tile(name + "_06", walkable).setSprite(this.sprite.getSubimage(3 * size, 1 * size, size, size));
		this.tiles[6] = new Tile(name + "_07", walkable).setSprite(this.sprite.getSubimage(4 * size, 0 * size, size, size));
		this.tiles[7] = new Tile(name + "_08", walkable).setSprite(this.sprite.getSubimage(3 * size, 0 * size, size, size));
		this.tiles[8] = new Tile(name + "_09", walkable).setSprite(this.sprite.getSubimage(2 * size, 2 * size, size, size));
		this.tiles[9] = new Tile(name + "_10", walkable).setSprite(this.sprite.getSubimage(0 * size, 2 * size, size, size));
		this.tiles[10] = new Tile(name + "_11", walkable).setSprite(this.sprite.getSubimage(2 * size, 0 * size, size, size));
		this.tiles[11] = new Tile(name + "_12", walkable).setSprite(this.sprite.getSubimage(0 * size, 0 * size, size, size));
	}

	public Tile getTile(int id) {
		return this.tiles[id];
	}

	public boolean isCenter(Tile tile) {
		return tile.name.equals(String.format("%s_00", this.name));
	}

	public boolean equals(Tile tile) {
		int length = tile.name.length();
		return tile.name.substring(0, length - 3).equals(this.name);
	}
}
