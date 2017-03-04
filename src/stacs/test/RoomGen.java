package stacs.test;

import java.util.Random;

import stacs.logic.entity.PlayerEntity;
import stacs.logic.room.Room;
import stacs.logic.room.Square;
import stacs.logic.room.TerrainType;

public class RoomGen {
    public static Room generateRoom() {
        Room room = new Room(1, null, 10, 10);
        
        Random rand = new Random(3);
        
        for(int x = 0; x < 10; x++){
            for(int y = 0; y < 10; y++){ 
                room.squares[x][y] = new Square(x, y, rand.nextInt(10) + 1, rand.nextBoolean() ? TerrainType.dirt : TerrainType.stone, room);
                
                if(x == 9 && y == 3) {
                    room.squares[x][y].entities.add(new PlayerEntity());
                }
            }
        }
        
        room.squares[0][1].teleport = room.squares[2][3];
        room.squares[4][5].teleport = room.squares[2][3];
        
        return room;
    }
}
