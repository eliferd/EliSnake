package fr.eliferd.game.scenes;

import fr.eliferd.engine.renderer.Render;
import fr.eliferd.game.World;
import fr.eliferd.game.entities.FoodEntity;
import fr.eliferd.game.entities.SnakeHeadEntity;
import org.joml.Vector2f;

import java.util.Random;


public class GameScene extends AbstractScene {
    private final Render _render = new Render();
    private Random _rng = new Random();

    @Override
    public void init() {
        // Init rendering
        this._render.init();

        // Init entities
        this.initEntities();
    }

    @Override
    public void update(float dt) {
        World.getInstance().getLoadedEntityList().forEach(entity -> entity.update(dt));
        World.getInstance().flushPendingEntities();
        this._render.render(dt);
    }

    private void initEntities() {
        SnakeHeadEntity headSnakeEntity = new SnakeHeadEntity(new Vector2f(0f, 0f));
        World.getInstance().addEntity(headSnakeEntity);

        // TODO : replace this by a more "natural" way to spawn food. Only for tests at the moment.
        for(int i = 0; i < 3; i++) {
            FoodEntity food = new FoodEntity(new Vector2f(60f * ((i+1) * this._rng.nextInt(3)), 80f * ((i+1) * this._rng.nextInt(3))));
            World.getInstance().addEntity(food);
        }
    }
}
