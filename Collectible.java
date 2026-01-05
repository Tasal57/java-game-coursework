package game;

import city.cs.engine.*;

public class Collectible extends DynamicBody {
    private static final Shape collectibleShape = new CircleShape(1f);
    private static final BodyImage collectibleImage = new BodyImage("data/Coin.png", 2f);

    public Collectible(World world) {
        super(world);

        // Add a solid fixture for collision detection
        new SolidFixture(this, collectibleShape);
        addImage(collectibleImage);


    }
}