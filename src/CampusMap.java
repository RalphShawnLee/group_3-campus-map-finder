import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


//CampusMap stores the campus as a graph. Each place is a node, and each
//road is an undirected edge between two places. The map holds at most
//100 places.

public class CampusMap {
    // Place names, stored in the order they first appear in the map file.
    final String[] places = new String[100];
    // connected[a][b] is true when a road joins place a and place b.
    final boolean[][] connected = new boolean[100][100];
    int placeCount;
    int roadCount;

    // Returns the index of a place, ignoring case, or -1 if it is not found.
    int find(String name) {
        for (int i = 0; i < placeCount; i++) {
            if (places[i].equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }

    // Returns the index of a place. A new place is added to the list when it
    // is not yet known. The method throws when the 100 place limit is full.
    int findOrAdd(String name) throws IOException {
        int index = find(name);
        if (index != -1) {
            return index;
        }
        if (placeCount == places.length) {
            throw new IOException("Map exceeds the limit of 100 places.");
        }
        places[placeCount] = name;
        return placeCount++;
    }


//      Builds a map from the text file. Each line must hold exactly two place
//      names separated by spaces, and the pair describes one road. The method
//      throws an IOException for a bad line or an empty file.
    static CampusMap load(Path path) throws IOException {
        CampusMap map = new CampusMap();
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                // Splitting on whitespace separates the two place names.
                String[] names = line.strip().split("\\s+");
                if (names.length != 2 || names[0].isEmpty()) {
                    throw new IOException("Line " + lineNumber
                            + ": expected exactly two place names.");
                }
                int a = map.findOrAdd(names[0]);
                int b = map.findOrAdd(names[1]);
                // A repeated road is counted once, since the file may list it
                // twice.
                if (!map.connected[a][b]) {
                    map.roadCount++;
                }
                // Roads work in both directions, so both cells are set.
                map.connected[a][b] = true;
                map.connected[b][a] = true;
            }
        }
        if (map.placeCount == 0) {
            throw new IOException("Map file is empty.");
        }
        return map;
    }

    // Prints the map summary, then the neighbours of every place.
    void print() {
        IO.println("Map loaded successfully.");
        IO.println("Places: " + placeCount);
        IO.println("Roads: " + roadCount);
        for (int i = 0; i < placeCount; i++) {
            printNeighbours(i);
        }
    }

    // Prints one place followed by the places that connect to it.
    void printNeighbours(int i) {
        IO.print(places[i] + " -> ");
        String separator = "";
        for (int j = 0; j < placeCount; j++) {
            if (connected[i][j]) {
                IO.print(separator + places[j]);
                separator = ", ";
            }
        }
        IO.println();
    }
}
