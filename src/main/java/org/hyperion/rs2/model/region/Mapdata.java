package org.hyperion.rs2.model.region;

import org.hyperion.util.Logger;
import org.hyperion.util.Logger.Level;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Mapdata {
    /**
     * Each list contains the region and 4 pieces of data.
     */
    public static Map[] mapLists = new Map[26000];

    /**
     * Constructs a new MapData class.
     */
    public Mapdata() {
        try {
            loadMapAreaData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Load the map data into memory for faster load time.
     *
     * @throws FileNotFoundException
     */
    public void loadMapAreaData() throws Exception {
        int count = 0;
        for (int i = 0; i < 26000; i++) {
            try {
                File file = new File("./data/world/xtea/" + i + ".txt");
                if (file.exists()) {
                    Map list = mapLists[i] = new Map();
                    BufferedReader in = new BufferedReader(new FileReader("./data/world/xtea/" + i + ".txt"));
                    String str;
                    int regionId = 0;
                    while ((str = in.readLine()) != null) {
                        if (!str.equals("")) {
                            list.data[regionId++] = Integer.parseInt(str.trim());
                            count++;
                        }
                    }
                    in.close();
                    in = null;
                }
                file = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Logger.log(Level.CORE, "Loaded " + count + " XTEA values...");
    }

    /**
     * Returns the four pieces of map data from a region.
     *
     * @param myRegion The region to get data from.
     * @return Returns the four mapdata.
     */
    public static int[] getData(int myRegion) {
        if (mapLists[myRegion] == null || mapLists[myRegion].data[0] == 0) {
            Logger.log(Level.INFO, "Missing map data: " + myRegion);
            return new int[4];
        }
        return mapLists[myRegion].data;
    }
}