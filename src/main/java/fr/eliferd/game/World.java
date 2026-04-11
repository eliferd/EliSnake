package fr.eliferd.game;

import fr.eliferd.game.entities.AbstractEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class World {
    private static World _INSTANCE = null;
    private List<AbstractEntity> _loadedEntityList = new ArrayList<>();
    private List<AbstractEntity> _entityToAddList = new ArrayList<>();
    private List<AbstractEntity> _entityToRemoveList = new ArrayList<>();

    public void addEntity(AbstractEntity entity) {
        this._entityToAddList.add(entity);
    }

    public void removeEntity(AbstractEntity entity) {
        this._entityToRemoveList.add(entity);
    }

    /**
     * Workaround for ConcurrentModificationException when the list is being iteraded & edited in the same time.
     */
    public void flushPendingEntities() {
        if (this.hasPendingEntities()) {
            this._loadedEntityList.addAll(this._entityToAddList);
            this._loadedEntityList.removeAll(this._entityToRemoveList);
            this._entityToRemoveList.clear();
            this._entityToAddList.clear();
        }
    }

    public List<AbstractEntity> getLoadedEntityList() {
        return Collections.unmodifiableList(this._loadedEntityList);
    }

    private boolean hasPendingEntities() {
        return !this._entityToAddList.isEmpty() || !this._entityToRemoveList.isEmpty();
    }

    public static World getInstance() {
        if (World._INSTANCE == null) {
            World._INSTANCE = new World();
        }
        return World._INSTANCE;
    }
}
