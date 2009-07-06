package edu.cornell.dendro.corina.ui;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.lang.reflect.Method;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.KeyStroke;

import edu.cornell.dendro.corina.core.App;

// TODO: if on 1.4, use setDisplayedMnemonicIndex() so "Save &As..."
// underlines the correct 'A'

public class Builder {

	// (don't instantiate me)
	private Builder() {
	}

	// UNUSED!
	public final static String INDENT = "    "; // 4 spaces

	public static JMenu makeMenu(String key) {
		JMenu m = new JMenu();

		// TODO: set font only on java<1.4?
		// NOTE: now using UIDefaults customization through Appearance Panel
		// instead of corina.menubar.font property - cross this off PRIORITY list

		m.setText(I18n.getText(key));

		if (!App.platform.isMac()) {
			Integer mnemonic = I18n.getMnemonic(key);
			if (mnemonic != null)
				m.setMnemonic(mnemonic);
		}

		return m;
	}

	public static JMenuItem makeMenuItem(String key) {
		JMenuItem m = new JMenuItem("");

		// TODO: set font only on java<1.4?
		// NOTE: now using UIDefaults customization through Appearance Panel
		// instead of corina.menubar.font property - cross this off PRIORITY list

		m.setText(I18n.getText(key));

		if (!App.platform.isMac()) {
			Integer mnemonic = I18n.getMnemonic(key);
			if (mnemonic != null)
				m.setMnemonic(mnemonic);
		}

		KeyStroke keystroke = I18n.getKeyStroke(key);
		if (keystroke != null)
			m.setAccelerator(keystroke);

		return m;
	}

	public static JMenuItem makeMenuItem(String key, boolean enabled) {
		JMenuItem m = makeMenuItem(key);
		m.setEnabled(enabled);
		return m;
	}
	
	public static JMenuItem makeMenuItem(String key, boolean enabled, String iconfilename) {
		JMenuItem m = makeMenuItem(key, null, iconfilename);
		m.setEnabled(enabled);
		return m;
	}


	public static JMenuItem makeMenuItem(String key, String action) {
		JMenuItem m = makeMenuItem(key);
		addAction(m, action);
		return m;
	}
	
	public static JMenuItem makeMenuItem(String key, String action, String iconfilename){
					
		JMenuItem m = new JMenuItem(key, getIcon(iconfilename, "Icons"));
		m.setText(I18n.getText(key));

		if (!App.platform.isMac()) {
			Integer mnemonic = I18n.getMnemonic(key);
			if (mnemonic != null)
				m.setMnemonic(mnemonic);
		}

		KeyStroke keystroke = I18n.getKeyStroke(key);
		if (keystroke != null)
			m.setAccelerator(keystroke);
		
		if(action != null)
			addAction(m, action);
		
		return m;	
	}
	
	public static JCheckBoxMenuItem makeCheckBoxMenuItem(String key){
		return makeCheckBoxMenuItem(key, null);
		
	}

	public static JCheckBoxMenuItem makeCheckBoxMenuItem(String key, String iconfilename) {
		JCheckBoxMenuItem m = new JCheckBoxMenuItem("");

		// TODO: set font only on java<1.4?
		// NOTE: now using UIDefaults customization through Appearance Panel
		// instead of corina.menubar.font property - cross this off PRIORITY list

		m.setText(I18n.getText(key));

		if (!App.platform.isMac()) {
			Integer mnemonic = I18n.getMnemonic(key);
			if (mnemonic != null)
				m.setMnemonic(mnemonic);
		}

		KeyStroke keystroke = I18n.getKeyStroke(key);
		if (keystroke != null)
			m.setAccelerator(keystroke);
		
		if (iconfilename!=null)	m.setIcon(getIcon(iconfilename, "Icons"));

		return m;
	}

	public static JRadioButtonMenuItem makeRadioButtonMenuItem(String key) {
		JRadioButtonMenuItem m = new JRadioButtonMenuItem("");

		// TODO: set font only on java<1.4?
		// NOTE: now using UIDefaults customization through Appearance Panel
		// instead of corina.menubar.font property - cross this off PRIORITY list

		m.setText(I18n.getText(key));

		if (!App.platform.isMac()) {
			Integer mnemonic = I18n.getMnemonic(key);
			if (mnemonic != null)
				m.setMnemonic(mnemonic);
		}

		KeyStroke keystroke = I18n.getKeyStroke(key);
		if (keystroke != null)
			m.setAccelerator(keystroke);

		return m;
	}

	public static JButton makeButton(String key) {
		JButton b = new JButton();

		b.setText(I18n.getText(key));

		if (!App.platform.isMac()) {
			Integer mnemonic = I18n.getMnemonic(key);
			if (mnemonic != null)
				b.setMnemonic(mnemonic);
		}

		return b;
	}

	public static JLabel makeLabel(String key) {
		JLabel l = new JLabel();

		l.setText(I18n.getText(key));

		return l;
	}

	public static JRadioButton makeRadioButton(String key) {
		JRadioButton r = new JRadioButton();

		r.setText(I18n.getText(key));

		if (!App.platform.isMac()) {
			Integer mnemonic = I18n.getMnemonic(key);
			if (mnemonic != null)
				r.setMnemonic(mnemonic);
		}

		return r;
	}

	// i make icons from files in Images/ so often, i'll just make it a builder method.
	// use: Builder.getIcon("x.png") returns an Icon made from the file "Images/x.png".
	public static Icon getIcon(String name) {
		return getIcon(name, "Images");
	}
		
	public static Icon getIcon(String name, String packagename){
		
		java.net.URL url = cl.getResource("edu/cornell/dendro/corina_resources/" + packagename + "/" + name);
		if (url != null)
			return new ImageIcon(url);
		else
			return null;
	}

	// TODO: Cursor makeCursor(String name)
	// (yeah, it's pretty much the same as getIcon(), but don't tell anybody!)
	public static Image getImage(String name) {
		java.net.URL url = cl.getResource("edu/cornell/dendro/corina_resources/Images/" + name);
		if (url != null)
			return new ImageIcon(url).getImage();
		else
			return null;
	}

	// my classloador, for getting icons as resources.
	private static ClassLoader cl = edu.cornell.dendro.corina.ui.Builder.class.getClassLoader();

	// ----------------------------------------
	/*
	 REFACTOR!
	 
	 pattern:
	 
	 A.addActionListener(new AbstractAction() {
	 public void actionPerformed(ActionEvent e) {
	 new B();
	 }
	 });
	 
	 or:
	 
	 A.addActionListener(new AbstractAction() {
	 public void actionPerformed(ActionEvent e) {
	 B.c();
	 }
	 });
	 
	 -- of course, what i really want is simpler closures.
	 can i fake that easily?
	 
	 how about another param for Builder?
	 Builder.makeMenuItem("crossdate_kit", "new CrossdateKit();");
	 not perfect, but it would make my job easier in places, i think.
	 (same for buttons?)
	 */

	/**
	 * An easy way to add actions.  The action string is of the form
	 * <code>"new package.SomeClass();"</code> or
	 * <code>"package.SomeClass.staticMethod();</code>.  Be sure to
	 * fully-qualify the class name.
	 * @param button a JButton or JMenuItem
	 * @param action an action string */
	public static void addAction(AbstractButton button, String action) {
		// parse |action|
		action = action.trim();
		StringTokenizer tok = new StringTokenizer(action, " ();");
		String arg1 = tok.nextToken();

		// "new package.SomeClass();"
		if (arg1.equals("new")) {
			String arg2 = tok.nextToken();
			try {
				final Class c = Class.forName(arg2);

				button.addActionListener(new AbstractAction() {
					public void actionPerformed(ActionEvent e) {
						try {
							c.newInstance();
						} catch (Exception ex) {
							System.out
									.println("Builder.addAction(): can't instantiate "
											+ c);
							// FIXME?  (state what the action was, at least)
							ex.printStackTrace(System.out);
						}
					}
				});
			} catch (ClassNotFoundException cnfe) {
				throw new IllegalArgumentException("class '" + arg2
						+ "' not found");
			}
			return;
		}

		// "package.SomeClass.staticMethod();"
		int lastDot = action.lastIndexOf(".");
		if (lastDot == -1)
			throw new IllegalArgumentException(
					"no 'new' or '.' in action string");
		final String className = action.substring(0, lastDot);

		// the method name is everything after the last dot, for as long
		// as java-identifier chars are available.
		String methodName = action.substring(lastDot + 1);
		while (!Character.isJavaIdentifierPart(methodName.charAt(methodName
				.length() - 1)))
			methodName = methodName.substring(0, methodName.length() - 1);
		final String methodNameGlue = methodName;

		try {
			Class c = Class.forName(className);
			final Method m = c.getMethod(methodName, new Class[] {});

			button.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					try {
						m.invoke(null, new Object[] {});
						// IllegalAccess, InvocationTarget exceptions
					} catch (Exception ex) {
						ex.printStackTrace();
						throw new IllegalArgumentException(
								"Builder.addAction(): can't invoke "
										+ "requested method (" + methodNameGlue
										+ " in " + className + ")");
					}
				}
			});
		} catch (ClassNotFoundException cnfe) {
			throw new IllegalArgumentException(
					"Builder.addAction(): can't find requested class");
		} catch (NoSuchMethodException nsme) {
			throw new IllegalArgumentException(
					"Builder.addAction(): can't find requested method");
		}
	}
}
