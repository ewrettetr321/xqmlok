package quarkninja.mode.xqmode;

import java.awt.BorderLayout;
import java.awt.CheckboxMenuItem;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.Box;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import processing.app.Base;
import processing.app.EditorState;
import processing.app.Mode;
import processing.app.syntax.JEditTextArea;
import processing.app.syntax.PdeTextAreaDefaults;
import processing.mode.java.JavaEditor;

/**
 * Editor for XQMode
 * 
 * @author Manindra Moharana
 * 
 */
public class XQEditor extends JavaEditor {

	XQMode xqmode;
	protected Thread errorCheckerThread = null;
	protected ErrorCheckerService errorCheckerService;
	protected ErrorBar errorBar;
	public JCheckBoxMenuItem problemWindowMenuCB;

	protected XQEditor(Base base, String path, EditorState state,
			final Mode mode) {
		super(base, path, state, mode);
		final XQEditor thisEditor = this;

		xqmode = (XQMode) mode;
		errorBar = new ErrorBar(thisEditor, textarea.getMinimumSize().height);
		// Starts it too! Error bar should be ready beforehand
		initializeErrorChecker();
		errorBar.errorCheckerService = errorCheckerService;
		ta.setECS(errorCheckerService);

		// - Start
		JPanel textAndError = new JPanel();
		Box box = (Box) textarea.getParent();
		box.remove(2); // Remove textArea from it's container, i.e Box
		textAndError.setLayout(new BorderLayout());
		textAndError.add(errorBar, BorderLayout.EAST);
		textarea.setBounds(0, 0, errorBar.getX() - 1, textarea.getHeight());
		textAndError.add(textarea);
		box.add(textAndError);
		// - End

		// textarea.setBounds(errorBar.getX() + errorBar.getWidth(),
		// errorBar.getY(), textarea.getWidth(), textarea.getHeight());

		// for (int i = 0; i < consolePanel.getComponentCount(); i++) {
		// System.out.println("Console: " + consolePanel.getComponent(i));
		// }
		// consolePanel.remove(1);
		// JTable table = new JTable(new String[][] { { "Problems", "Line no" },
		// { "Missing semicolon", "12" },
		// { "Extra  )", "15" },{ "Missing semicolon", "34" } },
		// new String[] { "A", "B" });
		// consolePanel.add(table);

	}

	XQTextArea ta;

	protected JEditTextArea createTextArea() {
		System.out.println("overriding creation of text area");
		ta = new XQTextArea(new PdeTextAreaDefaults(mode));

		return ta;
	}

	public JMenu buildModeMenu() {

		// Enable Error Checker - CB
		// Show/Hide Problem Window - CB

		JMenu menu = new JMenu("XQMode");
		JCheckBoxMenuItem item;

		item = new JCheckBoxMenuItem("Error Checker Enabled");
		item.setSelected(true);
		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				errorCheckerService.pauseThread = !((JCheckBoxMenuItem) e
						.getSource()).isSelected();
				if (errorCheckerService.pauseThread)
					System.out.println(getSketch().getName()
							+ " - Error Checker paused.");
				else
					System.out.println(getSketch().getName()
							+ " - Error Checker resumed.");
			}
		});
		menu.add(item);

		problemWindowMenuCB = new JCheckBoxMenuItem("Show Problem Window");
		problemWindowMenuCB.setSelected(true);
		problemWindowMenuCB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (errorCheckerService.errorWindow == null)
					return;
				errorCheckerService.errorWindow
						.setVisible(((JCheckBoxMenuItem) e.getSource())
								.isSelected());
			}
		});
		menu.add(problemWindowMenuCB);

		return menu;
	}

	/**
	 * Initializes and starts Syntax Checker Service
	 */
	private void initializeErrorChecker() {
		if (errorCheckerThread == null) {
			errorCheckerService = new ErrorCheckerService(this, errorBar);
			errorCheckerService.problemWindowMenuCB = this.problemWindowMenuCB;
			errorCheckerThread = new Thread(errorCheckerService);
			try {
				errorCheckerThread.start();
			} catch (Exception e) {
				System.err
						.println("Syntax Checker Service not initialized [XQEditor]: "
								+ e);
				// e.printStackTrace();
			}
			// System.out.println("Syntax Checker Service initialized.");
		}

	}

}
