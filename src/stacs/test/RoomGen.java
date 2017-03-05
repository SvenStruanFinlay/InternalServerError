package stacs.test;

import java.util.Random;

import stacs.logic.item.ItemSaber;
import stacs.logic.item.ItemTreasure;
import stacs.logic.room.Room;
import stacs.logic.room.Square;
import stacs.logic.room.TerrainType;
import stacs.server.ServerWorld;

public class RoomGen {
    public static void generateWorld(ServerWorld world) {

        Room room = new Room(1, null,  7,6);
        Room room2 = new Room(2, null,  5,4);
        Room room3 = new Room(3, null, 4, 4);
        Room room4 = new Room(4, null,  4,9);
        Room roomIce = new Room(5, null, 13,12);
        Room roomIce2 = new Room(6, null, 12,10);
        Room roomBoss1 = new Room(7, null, 9,5);
        Room roomI1 = new Room(8, null, 8, 7);
        Room roomI2 = new Room(9, null, 5, 5);
        Room roomI3 = new Room(10, null, 5, 6);
        Room roomBoss2 = new Room(11, null, 3, 2);
        Room roomMaze = new Room(12, null, 10, 7);
        Room roomBoss3 = new Room(13, null, 7, 3);


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
        for (Room r: world.roomMap.values()) {

            System.out.println("\n" + r.id +"\n");

            if (r.id != 5 && r.id != 6) {

                for (int x = 0; x < r.w; x++) {
                    System.out.println("x: "+x + "\n ..." + r.w);
                    for (int y = 0; y < r.h; y++) {
                        System.out.println("y: "+y);
                        r.squares[x][y] = new Square(x, y, rand.nextInt(10) + 1, rand.nextBoolean() ? TerrainType.dirt : TerrainType.stone, r);
                    }
                }
            }
            else {
                for (int x = 0; x < r.w; x++) {
                    for (int y = 0; y < r.h; y++) {
                        r.squares[x][y] = new Square(x, y, rand.nextInt(10) + 1, TerrainType.water, r);
                    }
                }
            }
        }

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
    }
}
