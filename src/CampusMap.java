import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CampusMap {
    final String[] places = new String[100];
    final boolean[][] connected = new boolean[100][100];
    int placeCount;
    int roadCount;

    int find(String name) {
        for (int i = 0; i < placeCount; i++) {
            if (places[i].equals(name)) {
                return i;
            }
        }
        return -1;
    }

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

    static CampusMap load(Path path) throws IOException {
        CampusMap map = new CampusMap();
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String[] names = line.strip().split("\\s+");
                if (names.length != 2 || names[0].isEmpty()) {
                    throw new IOException("Line " + lineNumber + ": expected exactly two place names.");
                }
                int a = map.findOrAdd(names[0]);
                int b = map.findOrAdd(names[1]);
                if (!map.connected[a][b]) {
                    map.roadCount++;
                }
                map.connected[a][b] = true;
                map.connected[b][a] = true;
            }
        }
        if (map.placeCount == 0) {
            throw new IOException("Map file is empty.");
        }
        return map;
    }

    void print() {
        IO.println("Map loaded successfully.");
        IO.println("Places: " + placeCount);
        IO.println("Roads: " + roadCount);
        for (int i = 0; i < placeCount; i++) {
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

    String[] sortedPlaces() {
        String[] sorted = new String[placeCount];
        for (int i = 0; i < placeCount; i++) {
            sorted[i] = places[i];
        }
        for (int end = placeCount - 1; end > 0; end--) {
            for (int i = 0; i < end; i++) {
                if (sorted[i].compareTo(sorted[i + 1]) > 0) {
                    String temporary = sorted[i];
                    sorted[i] = sorted[i + 1];
                    sorted[i + 1] = temporary;
                }
            }
        }
        return sorted;
    }

}
