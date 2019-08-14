package ninja.cooperstuff.pokemon.client;

import ninja.cooperstuff.debug.Debug;
import ninja.cooperstuff.engine.Game;
import ninja.cooperstuff.engine.events.KeyListener;
import ninja.cooperstuff.engine.util.IntVector;
import ninja.cooperstuff.engine.util.Keys;
import ninja.cooperstuff.pokemon.entity.Player;
import ninja.cooperstuff.pokemon.monster.Monster;
import ninja.cooperstuff.pokemon.move.Move;
import ninja.cooperstuff.pokemon.sound.Sound;
import ninja.cooperstuff.pokemon.util.Constants;
import ninja.cooperstuff.pokemon.world.World;

import java.awt.*;
import java.util.ArrayList;

public class PokemonGame extends Game {
	public World world = new World(this);
	public Player player;

	private int generateCounter = 0;
	private int spawnCounter = 0;
	private int respawnCounter = 0;
	private boolean showDetails = true;

	public int pokeIndex = 0;

	public PokemonGame() {
		super();
		this.setSize(240 * 4 + 16, 160 * 4 + 39);
		this.setResizable(false);
		this.setTitle("Pokemon");
	}

	@Override
	public void update() {
		Sound.update();
		if (KeyListener.isKeyTyped(Keys.BRACKET_LEFT)) this.pokeIndex = (this.pokeIndex + 150) % 151;
		if (KeyListener.isKeyTyped(Keys.BRACKET_RIGHT)) this.pokeIndex = (this.pokeIndex + 152) % 151;
		if (KeyListener.isKeyTyped(Keys.BRACKET_LEFT) || KeyListener.isKeyTyped(Keys.BRACKET_RIGHT)) this.player.setMonster(Monster.ids.get(this.pokeIndex + 1));
		this.world.showDetails = this.showDetails;
		IntVector pos = this.player.transform.position.getTile();
		if (this.generateCounter == 0) {
			this.world.generate(pos.x, pos.y);
			this.generateCounter = 2;
		}
		if (this.spawnCounter == 0) {
			this.spawnCounter = (this.world.trySpawnPokemon(pos.x, pos.y) == null ? 2 : 100) + 10 * this.world.pokemon.size();
		}
		if (this.respawnCounter == 1 && this.player.getHealth() == 0) this.close();
		this.generateCounter--;
		this.spawnCounter--;
		if (this.respawnCounter > 0) this.respawnCounter--;
		super.update();
	}

	public void run() throws InterruptedException {
		Debug.level = 1;
		ninja.cooperstuff.pokemon.init.Init.preInit();
		ninja.cooperstuff.pokemon.init.Init.init();
		ninja.cooperstuff.pokemon.init.Init.postInit();
		this.stopLoading();

		this.player = this.instantiate(new Player(this.world, Monster.ids.get(this.pokeIndex + 1)));
		this.world.pokemon.add(this.player);

		while (this.running) {
			this.update();
			this.repaint();
			Thread.sleep((long) (1000.0 / 60.0));
		}
	}

	public void respawn() {
		if (this.respawnCounter == 0) this.respawnCounter = 121;
	}

	public void drawPokemonInfo(Graphics2D screen) {
		int screenWidth = this.getWidth() - 16, screenHeight = this.getHeight() - 39;
		int width = 500, height = 80;
		screen.translate(0, screenHeight - height - Constants.userInterface.borderThickness);
		this.drawRect(screen, width, height);
		screen.scale(2, 2);
		screen.setColor(this.player.monster.type1.color);
		if (this.player.monster.type2 != null) {
			screen.fillOval(3, 8, 6, 3);
			screen.setColor(this.player.monster.type2.color);
			screen.fillOval(3, 11, 6, 3);
		} else screen.fillOval(3, 8, 6, 6);
		screen.setColor(Constants.userInterface.color.text);
		screen.drawString(this.player.monster.name, 10, 15);
		screen.drawString("Lv." + this.player.getLevel(), 200, 15);
		double percent = (double) this.player.getHealthAnimation() / (double) this.player.getHealthMax();
		int offsetX = 3, offsetY = height / 2 + 3, margin = 2;
		int barWidth = width - 6, barHeight = height / 2 - 6;
		screen.scale(0.5, 0.5);
		screen.setColor(Constants.healthBar.color.BORDER);
		screen.fillRect(offsetX, offsetY, barWidth, barHeight);
		screen.setColor(Constants.healthBar.getColor(percent));
		screen.fillRect(offsetX + margin, offsetY + margin, (int) Math.round((barWidth - 2 * margin) * percent), barHeight - 2 * margin);
		screen.setColor(Constants.healthBar.color.BACKGROUND);
		screen.fillRect(offsetX + margin + (int) Math.round((barWidth - 2 * margin) * percent), offsetY + margin, (int) Math.round((barWidth - 2 * margin) * (1 - percent)), barHeight - 2 * margin);
		screen.translate(0, height - screenHeight + Constants.userInterface.borderThickness);
	}

	public void drawMoveList(Graphics2D screen) {
		int screenWidth = this.getWidth() - 16, screenHeight = this.getHeight() - 39;
		int width = 400, height = 80;
		screen.translate(screenWidth - width - Constants.userInterface.borderThickness, screenHeight - height - Constants.userInterface.borderThickness);
		this.drawRect(screen, width, height);
		screen.drawRect((this.player.selected % 2 * width / 2) + 3, (this.player.selected / 2 * height / 2) + 3, width / 2 - 6, height / 2 - 6);
		ArrayList<Move> moves = this.player.getMoves();
		Move move1=null, move2=null, move3=null, move4=null;
		int size = moves.size();
		if (size > 0) move1 = moves.get(0);
		if (size > 1) move2 = moves.get(1);
		if (size > 2) move3 = moves.get(2);
		if (size > 3) move4 = moves.get(3);
		screen.scale(2, 2);
		if (move1 != null) {
			screen.setColor(move1.type.color);
			screen.fillOval(3, 8, 6, 6);
		}
		if (move2 != null) {
			screen.setColor(move2.type.color);
			screen.fillOval(width / 4 + 3, 8, 6, 6);
		}
		if (move3 != null) {
			screen.setColor(move3.type.color);
			screen.fillOval(3, height / 4 + 8, 6, 6);
		}
		if (move3 != null) {
			screen.setColor(move3.type.color);
			screen.fillOval(width / 4 + 3, height / 4 + 8, 6, 6);
		}
		screen.setColor(Constants.userInterface.color.text);
		if (move1 != null) screen.drawString(move1.name, 10, 15);
		if (move2 != null) screen.drawString(move2.name, width / 4 + 10, 15);
		if (move3 != null) screen.drawString(move3.name, 10, height / 4 + 15);
		if (move4 != null) screen.drawString(move4.name, width / 4 + 10, height / 4 + 15);
		screen.scale(0.5, 0.5);
		screen.translate(width - screenWidth + Constants.userInterface.borderThickness, height - screenHeight + Constants.userInterface.borderThickness);
	}

	@Override
	public void render(Graphics2D screen) {
		super.render(screen);
		world.render(screen);
		screen.setColor(Color.white);
		this.camera.toScreenCoords(screen);
		this.drawPokemonInfo(screen);
		this.drawMoveList(screen);
		this.camera.toGameCoords(screen);
		screen.fillRect(-1, -1, 2, 2);
	}

	private void drawRect(Graphics2D screen, int width, int height) {
		screen.setColor(Constants.userInterface.color.background);
		screen.fillRect(0, 0, width, height);
		screen.setStroke(new BasicStroke(Constants.userInterface.borderThickness));
		screen.setColor(Constants.userInterface.color.border);
		screen.drawRect(0, 0, width, height);
	}
}
