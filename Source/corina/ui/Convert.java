package corina.ui;

import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.helpers.DefaultHandler;

import java.io.FileReader;

import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

public class Convert {

    // generate unique identifiers
    private static class Identifier {
        private static int num=0;
        public static String generate() { // return strings?  or identifiers?
            return "id" + num++;
        }
    }

    private static abstract class Component {
        String id = Identifier.generate(); // !!! (?)
        public abstract String emit();
    }
    private static class Menu extends Component {
        String text=""; // ??
        String mnemonic=null;
        String accelerator=null;
        List children=new ArrayList(); // sub-menus and menuitems go here!
        Menu(Attributes atts) {
            text = atts.getValue("title");
            mnemonic = atts.getValue("mnemonic"); // maybe null
            accelerator = atts.getValue("accelerator"); // maybe null
        }
        public String emit() {
            StringBuffer buf = new StringBuffer();
            buf.append("   javax.swing.JMenu " + id + " = new javax.swing.JMenu(\"" + text + "\");\n");
            if (mnemonic != null)
                buf.append("   " + id + ".setMnemonic('" + mnemonic.toUpperCase() + "');\n"); // am i SURE this is 1 char?  (no!)
            if (accelerator != null)
                buf.append("   " + id + ".setAccelerator(corina.ui.RuntimeUtils.getKeyStroke(\"" + accelerator + "\"));\n");
            // (is there some way to say "get-my-package" so i don't need to change this later?)

            // do children
            for (int i=0; i<children.size(); i++) {
                Component c = (Component) children.get(i);
                buf.append(c.emit());
            }
            for (int i=0; i<children.size(); i++) {
                Component c = (Component) children.get(i);
                if (c instanceof Separator) // these get added strangely
                    buf.append("   " + id + ".addSeparator();\n");
                else
                    buf.append("   " + id + ".add(" + c.id + ");\n");
            }

            return buf.toString();
        }
        // what about dependencies?  menu needs to be created before menu.add(menuitem), but
        // menuitems need to be created before that.
    }

    private static class Separator extends Component {
        public String emit() {
            return ""; // never gets called
        }
    }

    // THIS IS JUST A COPY OF MENU WITH S/MENU/MENUITEM/
    private static class MenuItem extends Component {
        String text=""; // ??
        String mnemonic=null;
        String accelerator=null;
        MenuItem(Attributes atts) {
            text = atts.getValue("title");
            mnemonic = atts.getValue("mnemonic"); // maybe null
            accelerator = atts.getValue("accelerator"); // maybe null
        }
        public String emit() {
            StringBuffer buf = new StringBuffer();
            buf.append("   javax.swing.JMenuItem " + id + " = new javax.swing.JMenuItem(\"" + text + "\");\n");
            if (mnemonic != null)
                buf.append("   " + id + ".setMnemonic('" + mnemonic.toUpperCase() + "');\n"); // am i SURE this is 1 char?  (no!)
            if (accelerator != null)
                buf.append("   " + id + ".setAccelerator(corina.ui.RuntimeUtils.getKeyStroke(\"" + accelerator + "\"));\n");
            // (some way to say "get-my-package" so i don't need to change this later?)
            return buf.toString();
        }
        // what about dependencies?  menu needs to be created before menu.add(menuitem), but
        // menuitems need to be created before that.
    }

    /*
     take menuitems, to start with.  what's a menuitem?
     -- text
     -- mnemonic (optional)
     -- accelerator (optional)
     -- class (optional)
     -- "id" (?)
     -- "is-separator" (?) -- or, separator, but only in a menu context (!)
     */

    // XML loader ------------------------------------------------------------
    private static class XMLUILoader extends DefaultHandler {
        // store state here, if desired
        private Stack menus = new Stack(); // just menus, for now, but it'll probably get generalized soon enough
        private StringBuffer data=new StringBuffer();
        public void startElement(String uri, String name, String qName, Attributes atts) {
            if (name.equals("ui")) {
                System.out.println("package " + atts.getValue("package") + ";\n");
                System.out.println("public class " + atts.getValue("class") + " {");
                System.out.println("  public static javax.swing.JComponent getComponent() {");

                return;
            }

            if (name.equals("menu")) {
                // BETTER: create structure in-memory, then have a method to dump it out whereever i want.
                // (or even better, each component dumps out the instructions to create itself -- tiling!)
                Menu m = new Menu(atts);
                menus.push(m);
//                System.out.println(m.emit());
                // --> need a factory to make Components from tags
            } else if (name.equals("menuitem")) {
                MenuItem m = new MenuItem(atts);
                ((Menu) menus.peek()).children.add(m); // ack!  how about just using real swing stuff with a factory?
//                System.out.println(m.emit());
            } else if (name.equals("separator")) {
                Separator s = new Separator();
                ((Menu) menus.peek()).children.add(s);
            }
//            if (name.equals("site"))
//                site = new Site();
//            else
//                state = name;
        }
        public void endElement(String uri, String name, String qName) {
            if (name.equals("menu")) {
                // ignore whitespace
//                String text = data.toString().trim();
//                if (text.length() == 0)
//                    return;

                // diamond in the rough
                Component m = (Component) menus.pop();
                System.out.println(m.emit());
                
                // something matched => reset data
                data.setLength(0);
            }
        }
        public void characters(char ch[], int start, int length) {
            // stringify
            data.append(new String(ch, start, length));
        }
    }
    // -----------------------------------------------------------------------------

    // load/parse XML file, output as .java file (same filename before extension)
    public static void process(String filename) {
        // [old comment:] make a dummy header, just to see if something will build
        // in reality: save to filename ~= s/.xmlui/.java/

        // create an interface (in its own file, yadayadayada) for these classes; that way,
        // i can do stuff at compile-time without resorting to reflection.  (uh, is that true?)
        
        try {
            // create XML reader
            XMLReader xr = XMLReaderFactory.createXMLReader();

            // set it up as a sitedb loader
            XMLUILoader loader = new XMLUILoader();
            xr.setContentHandler(loader);
            xr.setErrorHandler(loader);

            // load it
            FileReader r = new FileReader(filename);
            xr.parse(new InputSource(r));
        } catch (Exception e) { // SAXException or IOException
            System.out.println("e=" + e);
        }

        // dummy footer
        System.out.println("    return id0;"); // id0?  toplevel!
        System.out.println("  }");
        System.out.println("}");
    }

    // given an XML file, create a java source file; loop for each file given
    public static void main(String args[]) {
        for (int i=0; i<args.length; i++) {
            process(args[i]);
        }
    }
}
