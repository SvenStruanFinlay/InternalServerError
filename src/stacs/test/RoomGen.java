package stacs.test;

import java.util.Random;

import stacs.logic.entity.EnemyEntity;
import stacs.logic.entity.Entity;
import stacs.logic.item.ItemApple;
import stacs.logic.item.ItemSaber;
import stacs.logic.item.ItemTreasure;
import stacs.logic.room.Room;
import stacs.logic.room.Square;
import stacs.logic.room.SquarePlate;
import stacs.logic.room.TerrainType;
import stacs.server.ServerWorld;

public class RoomGen {
    public static void generateWorld(ServerWorld world) {

        Room room = new Room("room", world, 7, 6);
        Room room2 = new Room("room2", world, 5, 4);
        Room room3 = new Room("room3", world, 4, 4);
        Room room4 = new Room("room4", world, 4, 9);
        Room roomIce = new Room("roomIce", world, 13, 12);
        Room roomIce2 = new Room("roomIce2", world, 12, 10);
        Room roomBoss1 = new Room("roomBoss1", world, 9, 5);
        Room roomI1 = new Room("roomI1", world, 8, 7);
        Room roomI2 = new Room("roomI2", world, 5, 5);
        Room roomI3 = new Room("roomI3", world, 5, 6);
        Room roomBoss2 = new Room("roomBoss2", world, 3, 2);
        Room roomMaze = new Room("roomMaze", world, 10, 8);
        Room roomBoss3 = new Room("roomBoss3", world, 7, 3);

        world.roomMap.put("room", room);
        world.roomMap.put("room2", room2);
        world.roomMap.put("room3", room3);
        world.roomMap.put("room4", room4);
        world.roomMap.put("roomIce", roomIce);
        world.roomMap.put("roomIce2", roomIce2);
        world.roomMap.put("roomBoss1", roomBoss1);
        world.roomMap.put("roomI1", roomI1);
        world.roomMap.put("roomI2", roomI2);
        world.roomMap.put("roomI3", roomI3);
        world.roomMap.put("roomMaze", roomMaze);
        world.roomMap.put("roomBoss2", roomBoss2);
        world.roomMap.put("roomBoss3", roomBoss3);

        Random rand = new Random(3);
        for (Room r : world.roomMap.values()) {

            System.out.println("\n" + r.id + "\n");

            if (!r.id.contains("Ice")) {

                for (int x = 0; x < r.w; x++) {
                    System.out.println("x: " + x + "\n ..." + r.w);
                    for (int y = 0; y < r.h; y++) {
                        System.out.println("y: " + y);
                        r.squares[x][y] = new Square(x, y, rand.nextInt(10) + 1,
                                rand.nextBoolean() ? TerrainType.dirt : TerrainType.stone, r);
                    }
                }
            } else {
                for (int x = 0; x < r.w; x++) {
                    for (int y = 0; y < r.h; y++) {
                        r.squares[x][y] = new Square(x, y, rand.nextInt(10) + 1, TerrainType.water, r);
                    }
                }
            }
        }

        world.addEntity(new EnemyEntity(""), room.squares[1][1]);
        world.addEntity(new EnemyEntity(""), room2.squares[1][1]);
        world.addEntity(new EnemyEntity(""), room3.squares[1][1]);
        world.addEntity(new EnemyEntity(""), room4.squares[1][1]);

        world.addEntity(new EnemyEntity(""), roomBoss1.squares[1][1]);
        world.addEntity(new EnemyEntity(""), roomBoss1.squares[2][2]);

        world.addEntity(new EnemyEntity(""), roomBoss2.squares[1][1]);
        world.addEntity(new EnemyEntity(""), roomBoss2.squares[0][0]);

        world.addEntity(new EnemyEntity(""), roomBoss3.squares[1][1]);
        world.addEntity(new EnemyEntity(""), roomBoss3.squares[0][0]);

        world.addEntity(new EnemyEntity(""), roomI1.squares[1][1]);
        world.addEntity(new EnemyEntity(""), roomI2.squares[1][1]);
        world.addEntity(new EnemyEntity(""), roomI3.squares[1][1]);

        String map =    "        \n" +
                        "#1##### \n" +
                        "#2##### \n" +
                        "#  3#EW \n" +
                        "#   ### \n" +
                        "#  4###W\n" +
                        "#     # \n";

        String[] rows = map.split("\n");
        for(int i = 0; i < rows.length; i++){
            char[] chars = rows[i].toCharArray();
            for(int j = 0; j < chars.length; j++){
                char c = chars[j];
                if(c == '#')
                    roomMaze.squares[i][j].terrainType = TerrainType.wall;
                else if(c == 'W')
                    roomMaze.squares[i][j].terrainType = TerrainType.trapWall;
                else if(c == '1'){
                     SquarePlate ns = new SquarePlate(i, j, 10, TerrainType.stone, roomMaze);
                     ns.dest = roomMaze.squares[1][7];
                     ns.newType = TerrainType.stone;
                    roomMaze.squares[i][j] = ns;
                } else if(c == '2'){
                     SquarePlate ns = new SquarePlate(i, j, 10, TerrainType.stone, roomMaze);
                     ns.dest = roomMaze.squares[1][7];
                     ns.newType = TerrainType.trapWall;
                    roomMaze.squares[i][j] = ns;
                }else if(c == '3'){
                     SquarePlate ns = new SquarePlate(i, j, 10, TerrainType.stone, roomMaze);
                     ns.dest = roomMaze.squares[3][6];
                     ns.newType = TerrainType.stone;
                    roomMaze.squares[i][j] = ns;
                } else if (c == '4'){
                     SquarePlate ns = new SquarePlate(i, j, 10, TerrainType.stone, roomMaze);
                     ns.dest = roomMaze.squares[5][7];
                     ns.newType = TerrainType.stone;
                    roomMaze.squares[i][j] = ns;
                } else if (c == 'E'){
                    world.addEntity(new EnemyEntity(""), roomMaze.squares[i][j]);
                }
                
            }
        }

        room2.squares[4][2].items.add(new ItemApple());
        roomIce2.squares[8][3].items.add(new ItemApple());
        roomMaze.squares[3][2].items.add(new ItemApple());

        roomI1.squares[1][1].items.add(new ItemTreasure());
        roomI2.squares[1][1].items.add(new ItemTreasure());
        roomI3.squares[1][1].items.add(new ItemTreasure());

        room3.squares[2][3].items.add(new ItemSaber());
        roomI1.squares[2][3].items.add(new ItemSaber());
        roomI3.squares[2][3].items.add(new ItemSaber());
        roomMaze.squares[2][3].items.add(new ItemSaber());

        room.squares[0][2].teleport = room2.squares[4][2];
        room.squares[3][0].teleport = room3.squares[1][3];
        room2.squares[0][1].teleport = roomIce.squares[12][4];
        room2.squares[2][0].teleport = room4.squares[1][8];
        roomIce.squares[12][4].teleport = room2.squares[0][1];
        roomIce.squares[0][6].teleport = roomI1.squares[7][2];
        roomIce.squares[3][11].teleport = roomBoss1.squares[4][0];
        roomI1.squares[7][2].teleport = roomIce.squares[0][6];
        roomBoss1.squares[4][0].teleport = roomIce.squares[3][11];

        room4.squares[1][8].teleport = room2.squares[2][0];
        room4.squares[3][3].teleport = room3.squares[0][2];
        room4.squares[0][4].teleport = roomMaze.squares[9][3];

        roomMaze.squares[0][2].teleport = roomBoss3.squares[6][1];
        roomMaze.squares[4][0].teleport = roomI3.squares[1][5];
        roomMaze.squares[9][3].teleport = room4.squares[0][4];

        roomBoss3.squares[6][1].teleport = roomMaze.squares[0][2];
        roomI3.squares[1][5].teleport = roomMaze.squares[4][0];

        room3.squares[1][0].teleport = roomIce2.squares[7][9];
        room3.squares[1][3].teleport = room.squares[3][0];

        roomIce2.squares[7][9].teleport = room3.squares[1][0];
        roomIce2.squares[11][4].teleport = roomBoss2.squares[0][1];
        roomIce2.squares[6][0].teleport = roomI2.squares[2][4];
        roomI2.squares[2][4].teleport = roomIce2.squares[5][0];
        roomBoss2.squares[0][1].teleport = roomIce2.squares[11][3];

        room2.squares[4][2].teleport = room.squares[0][2];
        room3.squares[0][2].teleport = room2.squares[3][3];
    }
}
