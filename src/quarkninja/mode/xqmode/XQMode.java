package quarkninja.mode.xqmode;

import java.io.File;
import processing.app.Base;
import processing.app.Editor;
import processing.app.EditorState;
import processing.app.Mode;
import processing.mode.java.JavaMode;

/**
 * Mode Template for extending Java mode in Processing IDE 2.0a5 or later.
 * 
 */
public class XQMode extends JavaMode {

	public XQMode(Base base, File folder) {
		super(base, folder);
		System.out.println("Mode initialized.");
	}

	public Editor createEditor(Base base, String path, EditorState state) {
		return new XQEditor(base, path, state, this);
	}

	/**
	 * Called by PDE
	 */
	@Override
	public String getTitle() {
		return "TEH XQMode";
	}

	// @Override
	// public String getDefaultExtension() {
	// return "pde";
	// }
	//
	// @Override
	// public String[] getExtensions() {
	// return new String[] { "pde", "java" };
	// }
	//
	// @Override
	// public String[] getIgnorable() {
	// return new String[] {
	// "applet",
	// "application.macosx",
	// "application.windows",
	// "application.linux"
	// };
	// }

}
