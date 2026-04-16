package fr.eliferd.game.scenes;

import fr.eliferd.engine.renderer.Render;
import fr.eliferd.game.World;
import fr.eliferd.game.entities.AbstractEntity;
import fr.eliferd.game.entities.FoodEntity;
import fr.eliferd.game.entities.SnakeHeadEntity;
import org.joml.Vector2f;

import java.util.List;
import java.util.Random;


public class GameScene extends AbstractScene {
    private final Render _render = new Render();
    private Random _rng = new Random();

    @Override
    public void init() {
        // Init rendering engine
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
        final List<AbstractEntity> entities = List.of(
                new SnakeHeadEntity(new Vector2f(60f, 60f)),
                new FoodEntity(new Vector2f(240f, 180f))
        );
        entities.forEach(World.getInstance()::addEntity);
    }

    @Override
    public void onDestroy() {
        World.getInstance().getLoadedEntityList().forEach(World.getInstance()::removeEntity);
    }
}
