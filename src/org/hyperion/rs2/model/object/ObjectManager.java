package org.hyperion.rs2.model.object;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import org.hyperion.application.ConsoleMessage;
import org.hyperion.rs2.model.Location;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Loads objects from a JSON file and adds them to the world.
 * @author Stephen Andrews
 */
public class ObjectManager {

	/**
	 * A list of objects in the world.
	 */
	private GameObject[] objects;
	
	/**
	 * The location of the spawns file.
	 */
	private final static String OBJECT_FILE = "./data/world/objects/objects.json";
	
	/**
	 * Loads the custom objects.
	 */
	private void loadObjects() {
		try (BufferedReader reader = new BufferedReader(new FileReader(OBJECT_FILE))) {
            Gson gson = new GsonBuilder().create();
            objects = gson.fromJson(reader, GameObject[].class);
            ConsoleMessage.info("Loaded " + objects.length + " game objects...");
        } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Packs the custom objects into a format ready to be loaded.
	 */
	private void packRegionObjectSpawns() {
		for (GameObject obj : objects) {
			try {
			    DataOutputStream out = new DataOutputStream(new FileOutputStream("./data/world/objects/spawns/" + obj.getLocation().getRegionId() + ".os", true));
			    out.writeShort(obj.getId());
			    out.writeByte(obj.getType());
			    out.writeByte(obj.getFace());
			    out.writeByte(obj.getLocation().getZ());
			    out.writeShort(obj.getLocation().getX());
			    out.writeShort(obj.getLocation().getY());
			    out.writeByte(obj.getAction());
			    out.flush();
			    out.close();
			}
			catch (FileNotFoundException e) {
			    e.printStackTrace();
			}
			catch (IOException e) {
			    e.printStackTrace();
			}
		}
	}
	
	/**
	 * Prepares the objects in the world according to the spawn file.
	 * @param region
	 */
	public static void loadSpawns(int region) {
		File file = new File("data/world/objects/spawns/" + region + ".os");
		if (!file.exists())
			return;
		try {
			RandomAccessFile in = new RandomAccessFile(file, "r");
			FileChannel channel = in.getChannel();
			ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
			while (buffer.hasRemaining()) {
				int objectId = buffer.getShort() & 0xffff;
				int type = buffer.get() & 0xff;
				int rotation = buffer.get() & 0xff;
				int plane = buffer.get() & 0xff;
				int x = buffer.getShort() & 0xffff;
				int y = buffer.getShort() & 0xffff;
				boolean delete = buffer.get() == 1;
				Location loc = Location.create(x, y, plane);
				GameObject object = new GameObject(objectId, loc, rotation, type);
				if (!delete) {
					//World.spawnObject(object);
				} else {
					//World.removeObject(object);
				}
			}
			channel.close();
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}
