import page.codeberg.terratactician_expandoria.bots.*;
import page.codeberg.terratactician_expandoria.world.tiles.*;
import page.codeberg.terratactician_expandoria.world.tiles.Tile.TileType;
import page.codeberg.terratactician_expandoria.world.CubeCoordinate;
import page.codeberg.terratactician_expandoria.world.World;

import java.util.*;

public class MyBot extends ChallengeBot {

    @Override
    public int getMatrikel() {
        return 133742; // Replace with your Matrikelnummer
    }

    @Override
    public String getStudentName() {
        return "Obelix Gallier"; // Replace with your name
    }

    @Override
    public String getName() {
        return "Hinkelstein Crusher";
    }

    @Override
    public void executeTurn(World world, Controller controller) {
        // Step 1: Get remaining goals (e.g., wood, food, stone...)
        Map<Resource, Integer> goals = world.getGoals();

        // Step 2: Find all valid places to build
        List<CubeCoordinate> buildableTiles = world.getBuildableTiles();

        if (buildableTiles.isEmpty()) {
            return; // Skip if nothing can be placed
        }

        // Step 3: Define which tile type produces which resource (basic assumption)
        Map<TileType, Resource> tileToResource = Map.of(
            TileType.FOREST, Resource.WOOD,
            TileType.FARM, Resource.FOOD,
            TileType.MINE, Resource.METAL,
            TileType.QUARRY, Resource.STONE
        );

        // Step 4: Try placing the most-needed resource tile first
        for (CubeCoordinate coord : buildableTiles) {
            Tile tile = world.getTile(coord);
            if (tile.getType() != TileType.EMPTY) continue;

            // For each goal, try to place a matching tile
            for (Map.Entry<TileType, Resource> entry : tileToResource.entrySet()) {
                TileType type = entry.getKey();
                Resource res = entry.getValue();
                int needed = goals.getOrDefault(res, 0);

                if (needed > 0) {
                    boolean placed = controller.place(coord, type);
                    if (placed) {
                        controller.confirm();
                        return;
                    }
                }
            }
        }

        // Step 5: If nothing contributes to goals, place any basic tile to expand
        for (CubeCoordinate coord : buildableTiles) {
            if (world.getTile(coord).getType() == TileType.EMPTY) {
                boolean placed = controller.place(coord, TileType.FOREST);
                if (placed) {
                    controller.confirm();
                    return;
                }
            }
        }

        // Otherwise, skip turn
    }
}
