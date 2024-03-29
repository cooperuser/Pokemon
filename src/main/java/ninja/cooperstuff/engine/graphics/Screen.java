package ninja.cooperstuff.engine.graphics;

import ninja.cooperstuff.engine.Game;
import ninja.cooperstuff.engine.components.GameObject;

import javax.swing.*;
import java.awt.*;
import java.util.ConcurrentModificationException;

public class Screen extends JComponent {
	Game game;

	public Screen(Game game) {
		this.game = game;
	}

	@Override
	public void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		if (this.game.loading) {
			this.game.loadingScreen(g);
		} else {
			this.game.camera.toGameCoords(g);
			this.game.render(g);
			try {
				for (GameObject gameObject : this.game.gameObjects) {
					g.translate(gameObject.transform.position.x, gameObject.transform.position.y);
					g.rotate(gameObject.transform.rotation);
					g.scale(gameObject.transform.scale.x, gameObject.transform.scale.y);
					gameObject.render(g);
					g.scale(1 / gameObject.transform.scale.x, 1 / gameObject.transform.scale.y);
					g.rotate(-gameObject.transform.rotation);
					g.translate(-gameObject.transform.position.x, -gameObject.transform.position.y);
				}
			} catch (ConcurrentModificationException ignored) {}
			this.game.camera.toScreenCoords(g);
		}
	}
}
