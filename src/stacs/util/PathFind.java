package stacs.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import javafx.util.Pair;
import stacs.logic.room.Room;
import stacs.logic.room.Square;

public class PathFind {
    public static List<Square> findPath(Square start, Square end, Function<Square, Integer> costFunct, int maxCost) {
        assert start.room == end.room;
        Room room = start.room;
        Set<Square> visited = new HashSet<>();

        Map<Square, List<Square>> pathTo = new HashMap<>();
        PriorityQueue<Pair<Square, Pair<Integer, Integer>>> sq = new PriorityQueue<>((p1, p2) -> {
            return p1.getValue().getValue() - p2.getValue().getValue();
        });

        visited.add(start);
        pathTo.put(start, Arrays.asList(start));
        int tt = Math.abs(end.x - start.x) + Math.abs(end.y - start.y);
        sq.add(new Pair<>(start, new Pair<>(0, tt)));

        while (!sq.isEmpty()) {
            Pair<Square, Pair<Integer, Integer>> elem = sq.remove();

            List<Square> oldList = pathTo.get(elem.getKey());
            int x = elem.getKey().x;
            int y = elem.getKey().y;
            
            if(elem.getKey() == end)
                return oldList;

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0)
                        continue;
                    if(dx != 0 && dy != 0)
                        continue;

                    if (x + dx >= 0 && x + dx < room.w && y + dy >= 0 && y + dy < room.h) {
                        Square newSquare = room.squares[x + dx][y + dy];
                        if (!visited.contains(newSquare)) {
                            visited.add(newSquare);
                            int taxi = Math.abs(end.x - x - dx) + Math.abs(end.y - y - dy);

                            int newCost1 = elem.getValue().getKey() + costFunct.apply(newSquare);
                            int newCost2 = newCost1 + taxi;

                            List<Square> newList = new ArrayList<>();
                            newList.addAll(oldList);
                            newList.add(newSquare);
                            pathTo.put(newSquare, newList);

                            if (newCost1 <= maxCost) {
                                sq.add(new Pair<>(newSquare, new Pair<>(newCost1, newCost2)));
                            }

                        }
                    }
                }
            }
        }

        return null;
    }
}
