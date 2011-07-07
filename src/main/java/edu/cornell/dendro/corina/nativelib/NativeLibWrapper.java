/*
 * Copyright (C) 2011
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.cornell.dendro.corina.nativelib;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import javax.media.opengl.GLException;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class adapted from original JOGL library loader written by 
 * Romain Reuillon 
 * 
 * @author pwb48
 *
 */
public class NativeLibWrapper {

    private final static Logger log = LoggerFactory.getLogger(NativeLibWrapper.class);

    private static String[] allLibs = {"jogl_cg", "jogl_awt", "jogl", "gluegen-rt", "rxtxSerial"};

    //private NativeLibInfo nativeLibInfo;    //Compatible nativeLibInfo with OS/Arch
    public void init() throws GLException, Exception {
        NativeLibInfo nativeLibInfo = getNativLibInfo();
        String nativeArch = nativeLibInfo.getSubDirectoryPath();

        log.info("Native architecture: " + nativeArch);
        
        
        File libfolder;
        try {
            libfolder = extractLibDir(nativeLibInfo);
        } catch (IOException ex) {
            throw new GLException("Could not extract native libs from jar.",ex);
        }
        log.debug("Native library folder " + libfolder);
        loadNatives(libfolder, nativeLibInfo);
    }

    private File extractLibDir(NativeLibInfo nativeLibInfo) throws IOException {

        File dir = File.createTempFile("nativeLib", "");
        dir.delete();
        dir.mkdir();
        dir.deleteOnExit();

        for (String lib : allLibs) {
            String path = "lib/" + nativeLibInfo.getSubDirectoryPath() + '/' + nativeLibInfo.getNativeLibName(lib);
            
            //log.debug("Extracting " + path);
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);

            try {
                File dest = new File(dir, nativeLibInfo.getNativeLibName(lib));
                dest.deleteOnExit();
                OutputStream os = new FileOutputStream(dest);
                try {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead); // write
                    }
                } finally {
                    os.close();
                }
            } finally {
                is.close();
            }
        }

        return dir;

    }

    NativeLibInfo getNativLibInfo() throws Exception {
        String osName = System.getProperty("os.name");
        String osArch = System.getProperty("os.arch");

        for (int i = 0; i < allNativeLibInfo.length; i++) {
            NativeLibInfo info = allNativeLibInfo[i];
            if (info.matchesOSAndArch(osName, osArch)) {
                return info;
            }
        }
        throw new Exception("Init failed : Unsupported os / arch ( " + new Object[]{osName} + " / " + new Object[]{osArch} + "). Please check you're using a 32-bit JVM.");
    }

    public void loadNatives(final File nativeLibDir, final NativeLibInfo nativeLibInfo) throws GLException 
    {

             Exception throwable = null;

                try {
                    // disable JOGL and GlueGen runtime library loading from elsewhere
                    com.sun.opengl.impl.NativeLibLoader.disableLoading();
                    com.sun.gluegen.runtime.NativeLibLoader.disableLoading();
                    // Open GlueGen runtime library optimistically. Note that
                    // currently we do not need this on any platform except X11
                    // ones, because JOGL doesn't use the GlueGen NativeLibrary
                    // class anywhere except the DRIHack class, but if for
                    // example we add JOAL support then we will need this on
                    // every platform.
                    loadLibrary(nativeLibDir, "gluegen-rt", nativeLibInfo);
                    Class driHackClass = null;
                    if (nativeLibInfo.mayNeedDRIHack()) {
                        // Run the DRI hack
                        driHackClass = Class.forName("com.sun.opengl.impl.x11.DRIHack");
                        driHackClass.getMethod("begin", new Class[]{}).invoke(null, new Object[]{});

                    }
                    // Load core JOGL native library
                    loadLibrary(nativeLibDir, "jogl", nativeLibInfo);
                    if (nativeLibInfo.mayNeedDRIHack()) {
                        // End DRI hack
                        driHackClass.getMethod("end", new Class[]{}).invoke(null, new Object[]{});

                    }
                    if (!nativeLibInfo.isMacOS()) {
                        // borrowed from NativeLibLoader
                        // Must pre-load JAWT on all non-Mac platforms to
                        // ensure references from jogl_awt shared object
                        // will succeed since JAWT shared object isn't in
                        // default library path
                        try {
                            System.loadLibrary("jawt");
                        } catch (UnsatisfiedLinkError ex) {
                            // Accessibility technologies load JAWT themselves; safe to continue
                            // as long as JAWT is loaded by any loader
                            if (ex.getMessage().indexOf("already loaded") == -1) {
                                log.warn("Impossible to load JAWT");
                            }
                        }
                    }
                    // Load AWT-specific native code
                    loadLibrary(nativeLibDir, "jogl_awt", nativeLibInfo);
                    
                    //RXTX serial lib
                    loadLibrary(nativeLibDir, "rxtxSerial", nativeLibInfo);
                    
                    
                } catch (Exception t) {
                    throwable = t;
                }
    }
   

    public void loadLibrary(File installDir, String libName, NativeLibInfo nativeLibInfo) {
        String nativeLibName = nativeLibInfo.getNativeLibName(libName);
        try {
            System.load(new File(installDir, nativeLibName).getPath());
        } catch (UnsatisfiedLinkError ex) {
            // should be safe to continue as long as the native is loaded by any loader
            if (ex.getMessage().indexOf("already loaded") == -1) {
                log.warn("Unable to load " + new Object[]{nativeLibName} + "Impossible to load JAWT");
            }
        }
    }

    private static class NativeLibInfo {

        private String osName;
        private String osArch;
        private String osNameAndArchPair;
        private String nativePrefix;
        private String nativeSuffix;

        public NativeLibInfo(String osName, String osArch, String osNameAndArchPair, String nativePrefix, String nativeSuffix) {
            this.osName = osName;
            this.osArch = osArch;
            this.osNameAndArchPair = osNameAndArchPair;
            this.nativePrefix = nativePrefix;
            this.nativeSuffix = nativeSuffix;
        }

        public boolean matchesOSAndArch(String osName, String osArch) {
            if (osName.toLowerCase().startsWith(this.osName)) {
                if ((this.osArch == null)
                        || (osArch.toLowerCase().equals(this.osArch))) {
                    return true;
                }
            }
            return false;
        }

        public boolean matchesNativeLib(String fileName) {
            if (fileName.toLowerCase().endsWith(nativeSuffix)) {
                return true;
            }
            return false;
        }

        public String formatNativeJarName(String nativeJarPattern) {
            return MessageFormat.format(nativeJarPattern, new Object[]{osNameAndArchPair});
        }

        public String getNativeLibName(String baseName) {
            return nativePrefix + baseName + nativeSuffix;
        }

        public boolean isMacOS() {
            return (osName.equals("mac"));
        }

        public boolean mayNeedDRIHack() {
            return (!isMacOS() && !osName.equals("win"));
        }

        public String getSubDirectoryPath() {
            return osNameAndArchPair;
        }
    }
    private static final NativeLibInfo[] allNativeLibInfo = {
        new NativeLibInfo("win", "x86", "windows-i586", "", ".dll"),
        new NativeLibInfo("win", "amd64", "windows-amd64", "", ".dll"),
        new NativeLibInfo("win", "x86_64", "windows-amd64", "", ".dll"),
        new NativeLibInfo("mac", "ppc", "macosx-ppc", "lib", ".jnilib"),
        new NativeLibInfo("mac", "i386", "macosx-universal", "lib", ".jnilib"),
        new NativeLibInfo("mac", "x86_64", "macosx-universal", "lib", ".jnilib"),
        new NativeLibInfo("linux", "i386", "linux-i586", "lib", ".so"),
        new NativeLibInfo("linux", "x86", "linux-i586", "lib", ".so"),
        new NativeLibInfo("linux", "amd64", "linux-amd64", "lib", ".so"),
        new NativeLibInfo("linux", "x86_64", "linux-amd64", "lib", ".so"),
        new NativeLibInfo("sunos", "sparc", "solaris-sparc", "lib", ".so"),
        new NativeLibInfo("sunos", "sparcv9", "solaris-sparcv9", "lib", ".so"),
        new NativeLibInfo("sunos", "x86", "solaris-i586", "lib", ".so"),
        new NativeLibInfo("sunos", "amd64", "solaris-amd64", "lib", ".so"),
        new NativeLibInfo("sunos", "x86_64", "solaris-amd64", "lib", ".so")
    };
}
