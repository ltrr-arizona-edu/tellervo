package edu.cornell.dendro.corina.browser;

import edu.cornell.dendro.corina.Sample;

import java.util.List;
import java.util.ArrayList;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.enterprisedt.net.ftp.*;

/*
  general plan:
  -- implements Source
  -- opens connection on-demand; closes it if it's been idle for a few minutes
  -- have a "state" string: "connecting", "listing files", etc., so the user knows what's going on
  -- cache aggressively, perhaps in some place like ~/.corina/itrdb-cache/
  -- (future: have a way to download the whole thing?  how big is it?)
  -- (future: allow uploading to itrdb)
  -- (future: need some way to overlay a site database on this mess --
      "hmm, i have no sites near ZKB.  show me what the ITRDB has near there.")

  plan B:
  -- make an "FTP" class which is like FTPClient but with intelligence
  -- FTP takes care of the state (connecting, listing, etc.)
  -- opens connection on-demand; closes if it's been idle for a few minutes
  -- (caching, too)
  -- methods: list files in folder, load sample, save sample?

  -- separate class "ITRDB" which uses FTP
  -- implements DataSource
  -- 
*/

public class ITRDB {

    private static String ITRDB_HOST = "ftp.ngdc.noaa.gov";
    private static String ITRDB_DIR = "/paleo/treering/";

    private FTPClient f;

    public  List getFolders(String folder) throws Exception {
	// LIST ("full") = human-readable, NLST ("not-full"?) = machine-readable
	String s[] = f.dir(ITRDB_DIR + folder, true);
	List folders = new ArrayList();
	for (int i=1; i<s.length; i++) { // (skip "total" line)
	    boolean isFolder = (s[i].charAt(0) == 'd');
	    boolean isLink = (s[i].charAt(0) == 'l');

	    // crop metadata
	    String name = s[i].substring(45);

	    // if link, crop target
	    name = cropLink(name);

	    // add to list
	    if (isFolder)
		folders.add(name);
	}

	return folders;
    }

    public List getFiles(String folder) throws Exception {
	String s[] = f.dir(ITRDB_DIR + folder, true);
	List files = new ArrayList();
	for (int i=1; i<s.length; i++) { // (skip "total" line)
	    boolean isFolder = (s[i].charAt(0) == 'd');
	    boolean isLink = (s[i].charAt(0) == 'l');

	    // crop metadata
	    String name = s[i].substring(45);

	    // if link, crop target
	    name = cropLink(name);

	    // add to list
	    if (!isFolder)
		files.add(name);
	}

	return files;
    }

    // given "a -> b", return just "a"; if no "->", just return the original.
    private String cropLink(String file) {
	// if link (a -> b), crop target
	if (file.indexOf(' ') != -1)
	    return file.substring(0, file.indexOf(' '));
	return file;
    }

    // for example, "measurements/europe/turk001.rwl"
    public Sample getSample(String filename) throws Exception {
	// create temp file
	File local = File.createTempFile("corina", ".itrdb");

	// copy remote -> local
	f.get(local.getPath(), ITRDB_DIR + filename);

	// load local temp file
	Sample s = new Sample(local.getPath());

	// delete local temp file
	local.delete();

	// return sample
	return s;
    }

    public ITRDB() throws Exception {
	String anonymous = "ftp";
        String user = System.getProperty("user.name");
	String password;
	try {
	    password = user + "@" + InetAddress.getLocalHost();
	} catch (UnknownHostException uhe) {
            // i'm not sure why this can happen (especially if i'm
            // connected to the internet), but if it does, there's an
            // easy fix.
	    password = user + "@corina.sf.net";
	}

	f = new FTPClient(ITRDB_HOST);
	f.login(anonymous, password);

	//	f.setConnectMode(FTPConnectMode.PASV);
	f.setType(FTPTransferType.BINARY); // either this, or i'll need to teach tucson how to deal with more ickiness
    }

    public static void main(String args[]) throws Exception {
	ITRDB itrdb = new ITRDB();

	// list folders in measurements/
	/*
	List l = itrdb.getFolders("measurements");
	for (int i=0; i<l.size(); i++)
	    System.out.println("folder: " + l.get(i));
	*/

	// list files in measurements/europe/turk*
	List l2 = itrdb.getFiles("measurements/europe/");
	for (int i=0; i<l2.size(); i++)
	    ; // System.out.println("file: " + l2.get(i));

	// make a list of only "turk*" files
	List turk = new ArrayList();
	for (int i=0; i<l2.size(); i++)
	    if (((String) l2.get(i)).startsWith("yugo"))
		turk.add(l2.get(i));

	// load and print summaries of all turk* files
	for (int i=1; i<turk.size(); i++) {
	    try {
		Sample s = itrdb.getSample("measurements/europe/" + turk.get(i));
		System.out.println(turk.get(i) + " = " + s);
	    } catch (Exception e) {
		System.out.println(turk.get(i) + " = ***");
	    }
	}
    }
}
