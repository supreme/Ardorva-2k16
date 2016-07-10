package org.hyperion;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.RS2Server;
import org.hyperion.rs2.model.World;
import org.hyperion.util.Language;
import org.hyperion.util.Logger;
import org.hyperion.util.Logger.Level;

/**
 * A class to start both the file and game servers.
 * @author Graham Edgecombe
 *
 */
public class Server {
	
	/**
	 * The protocol version.
	 */
	public static final int VERSION = 464;
	
	/**
	 * The cycle time.
	 */
	public static final int CYCLE_TIME = 600;
	
	/**
	 * The entry point of the application.
	 * @param args The command line arguments.
	 */
	public static void main(String[] args) {
		Logger.log(Level.CORE, "Starting " + Constants.SERVER_NAME + "...");
		World.getWorld(); // this starts off background loading
		try {
			new RS2Server().bind(RS2Server.PORT).start();
		} catch(Exception ex) {
			System.err.println("Error starting Hyperion" + Language.NEW_LINE + ex);
			System.exit(1);
		}
	}

}
