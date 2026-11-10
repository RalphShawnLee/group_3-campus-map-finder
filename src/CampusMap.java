import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CampusMap {
    final String[] places = new String[100];
    final boolean[][] connected = new boolean[100][100];
    int placeCount;
    int roadCount;

    int findOrAdd(String name) throws IOException {
        for (int i = 0; i < placeCount; i++) {
            if (places[i].equals(name)) return i;
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

    public static void main(String[] args) {
        if (args.length > 1) {
            System.err.println("Usage: java CampusMap.java [map-file]");
            System.exit(1);
        }
        try {
            load(Path.of(args.length == 0 ? "campus_map.txt" : args[0])).print();
        } catch (IOException | IllegalArgumentException | SecurityException e) {
            System.err.println("Cannot load map: " + e.getMessage());
            System.exit(1);
        }
    }
}
