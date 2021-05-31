package src.entities;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import src.comp.DrawableGameComponent;
import src.utils.Position;
import java.util.Random;

public class GameBoard extends DrawableGameComponent {
    private List<DrawableGameComponent> components;
    public int nRows, nCols;
    private int tileSize, numVacantTiles;
    private int[] vacantList, vacantMap;
    private Random random;

    public GameBoard(PApplet sketch, int nRows, int nCols, int tileSize) {
        super(sketch);
        this.nRows = nRows;
        this.nCols = nCols;
        this.tileSize = tileSize;
        this.numVacantTiles = nRows * nCols;
        this.vacantList = new int[this.numVacantTiles];
        this.vacantMap = new int[this.numVacantTiles];
        this.components = new ArrayList<>();
        this.random = new Random();
        for (int i = 0, end = this.numVacantTiles; i < end; ++i) {
            this.vacantList[i] = i;
            this.vacantMap[i] = i;
        }
    }

    public void fillPosition(Position position) {
        fillTile(getTilePositionFromAbsPosition(position));
    }

    private void fillTile(int tile) {
        int positionInVacantList = vacantMap[tile];
        if (positionInVacantList == -1)
            return;
        if (positionInVacantList == numVacantTiles - 1) {
            --numVacantTiles;
            vacantMap[tile] = -1;
            return;
        }
        int lastVacantTile = vacantList[numVacantTiles - 1];
        vacantList[positionInVacantList] = lastVacantTile;
        vacantList[numVacantTiles - 1] = tile;
        vacantMap[lastVacantTile] = positionInVacantList;
        vacantMap[tile] = -1;
        --numVacantTiles;
    }

    public void vacatePosition(Position position) {
        vacateTile(getTilePositionFromAbsPosition(position));
    }

    private void vacateTile(int tile) {
        if (vacantMap[tile] != -1)
            return;
        vacantList[numVacantTiles] = tile;
        vacantMap[tile] = numVacantTiles;
        ++numVacantTiles;
    }

    public void addDrawableGameComponent(DrawableGameComponent component) {
        this.components.add(component);
    }

    public void reset() {
        this.components.clear();
    }

    public void update() {
        for (DrawableGameComponent component : components) {
            component.update();
        }
    }

    public void render() {
        for (DrawableGameComponent component : components) {
            component.render();
        }
    }

    // getters

    public int getNumVacantTiles() {
        return this.numVacantTiles;
    }

    public int getTilePositionFromAbsPosition(Position position) {
        int relX = position.x / this.tileSize;
        int relY = position.y / this.tileSize;
        // Row Major Order
        return relY * this.nCols + relX;
    }

    public Position getRandomVacantPosition() {
        int tile = vacantList[random.nextInt(this.numVacantTiles)];
        int relX = tile % this.nCols;
        int relY = tile / this.nCols;
        return new Position(relX * this.tileSize, relY * this.tileSize);
    }
}
