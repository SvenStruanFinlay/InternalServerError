package stacs.client;

import java.util.Random;

import stacs.main.PlayerEntity;
import stacs.main.Room;
import stacs.main.Square;
import stacs.main.TerrainType;

public class RoomGen {
    public static Room generateRoom() {
        Room room = new Room(1, null, 10, 10);
        
        Random rand = new Random(3);
        
        for(int x = 0; x < 10; x++){
            for(int y = 0; y < 10; y++){ 
                room.squares[x][y] = new Square(x, y, rand.nextInt(10) + 1, rand.nextBoolean() ? TerrainType.dirt : TerrainType.stone, room);
                
                if(x == 3 && y == 2) {
                    room.squares[x][y].entities.add(new PlayerEntity());
                }
            }
        }
        
        return room;
    }
}
