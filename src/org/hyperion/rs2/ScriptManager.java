package org.hyperion.rs2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.hyperion.util.Language;

/**
 * Manages server scripts.
 * @author blakeman8192
 * 
 */
public class ScriptManager {
	
	/**
	 * The singleton of this class.
	 */
	private static final ScriptManager INSTANCE = new ScriptManager();
	
	/**
	 * Gets the ScriptManager singleton.
	 * @return The ScriptManager singleton.
	 */
	public static ScriptManager getScriptManager() {
		return INSTANCE;
	}

	/**
	 * The ScriptEngineManager.
	 */
	private ScriptEngineManager mgr;

	/**
	 * The JavaScript Engine.
	 */
	private ScriptEngine jsEngine;

	/**
	 * The logger for this manager.
	 */
	private final Logger logger = Logger.getLogger(this.toString());

	/**
	 * Creates the script manager.
	 */
	private ScriptManager() {
		mgr = new ScriptEngineManager();
		jsEngine = mgr.getEngineByName("JavaScript");
		System.out.println("Loading scripts...");
	}

	/**
	 * Invokes a JavaScript function.
	 * @param identifier The identifier of the function.
	 * @param args The function arguments.
	 */
	public void invoke(String identifier, Object... args) {
		Invocable invEngine = (Invocable) jsEngine;
		try {
			invEngine.invokeFunction(identifier, args);
		} catch (NoSuchMethodException ex) {
			System.err.println("No such method: " + identifier + Language.NEW_LINE + ex);
		} catch (ScriptException ex) {
			System.err.println("ScriptException thrown!" + Language.NEW_LINE + ex);
		}
	}

	/**
	 * Loads JavaScript files into the JavaScript ScriptEngine from the argued
	 * path.
	 * @param dirPath The path of the directory to load the JavaScript source files
	 * from.
	 */
	public void loadScripts(String dirPath) {
		File dir = new File(dirPath);
		if (dir.exists() && dir.isDirectory()) {
			File[] children = dir.listFiles();
			for (File child : children) {
				if (child.isFile() && child.getName().endsWith(".js"))
					try {
						jsEngine.eval(new InputStreamReader(
								new FileInputStream(child)));
					} catch (ScriptException ex) {
						System.err.println("Unable to load script!" + Language.NEW_LINE + ex);
					} catch (FileNotFoundException ex) {
						System.err.println("Unable to find script!" + Language.NEW_LINE + ex);
					}
				else if (child.isDirectory())
					loadScripts(child.getPath());
			}
		}
	}

}
