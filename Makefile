#
# This file is part of Corina.
#
# Corina is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
#
# Corina is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with Corina; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
#
# Copyright 2001 Ken Harris <kbh7@cornell.edu>
#

# LEFT TO DO:
# - run -- automatic mac/non-mac chooser?
# - simple jikes/javac option
# --- 1.3/1.4 option?
# --- docbook auto local/remote?
# - bugs?
# - need "upload to server" rule, to make it easy to upload stuff
# - PERF: make jar, make jar calls compile rule again (!!)
# - add to stats: number of words in the manual?
# - add rule: spell-check manual, except for <list of words> -- to-text first?

all: default

help:
	@echo "Ways to build Corina:"
	@echo "   make run         Run Corina.jar"
	@echo "   make jar         Compile, prepare files, and link Corina.jar"
	@echo "   make prejar      Compile and prepare files for linking"
	@echo "   make compile     Compile only"
	@echo "The user manual:"
	@echo "   make html        Make HTML version of the manual"
	@echo "   make pdf         Make PDF version of the manual"
	@echo "   make rtf         Make RTF version of the manual (broken)"
	@echo "Testing and verification:"
	@echo "   make stats       Show number of lines, files of Java code"
	@echo "   make test        Run unit tests on Corina"
	@echo "   make findbugs    Show potential bugs (requires Java 1.4)"
	@echo "   make validate-l  Validate manual against DocBook DTD (local)"
	@echo "   make validate-r  Validate manual against DocBook DTD (remote)"
	@echo "Miscellaneous:"
	@echo "   make clean       Remove all compiled/processed files"
# rules i don't mention: default, timestamp, javadoc, web, html-1, fo, run-mac

SRC = Source
# why's build in src?  (because i can't think of a better name?)
BUILD = "Compiled classes"
API = Javadoc
LIB = Libraries
WEB = Webpage

MANUAL = Manual
MANUAL_DOCBOOK = $(MANUAL)/DocBook/
MANUAL_DOCBOOK_FILES = $(MANUAL_DOCBOOK)/*.docbook $(MANUAL_DOCBOOK)/*.chap $(MANUAL_DOCBOOK)/*.appdx $(MANUAL)/Images/*.png

JAVADOC_STUBS = $(SRC)/"JavaDoc package lists"

# JAVA_ROOT = /System/Library/Frameworks/JavaVM.framework/Versions
JAVA_ROOT = /usr/lib/j2sdk1.3/

VERSION = 1.4.1

#JAVA = $(JAVA_ROOT)/$(VERSION)/Commands/java
#JAVAC = $(JAVA_ROOT)/$(VERSION)/Commands/javac -nowarn
#JAVADOC = $(JAVA_ROOT)/$(VERSION)/Commands/javadoc
JAVA = $(JAVA_ROOT)/bin/java
JAVAC = $(JAVA_ROOT)/bin/javac
#JAVAC = /usr/bin/jikes-sun
JAVADOC = $(JAVA_ROOT)/bin/javadoc

RM = rm -rf

default: test jar

# make the "Timestamp" file -- this should always be run!
timestamp:
	date "+%Y %B %e  %l:%M %p (%Z)" > $(SRC)/Timestamp

# BUG: 1.3.x can't handle -source/-target!
JAVAC_ARGS = -O -g -deprecation
# -source 1.3 -target 1.3

# i want to use the "label: object" / "object: source" idiom here
# so i can say "make compile" elsewhere.  but it's not a 1-to-1
# relationship (like ??), and there's no 1 head output file
# (like index.html), so i have to pick one -- Year will always
# be around, so use that.
# BUG: this doesn't work!  (why not?)
compile: $(BUILD)/corina/Year.class
$(BUILD)/corina/Year.class: $(SRC)/corina/*.java $(SRC)/corina/*/*.java $(SRC)/corina/*/*/*.java
	$(RM) $(BUILD)
	mkdir -p $(BUILD)
	$(JAVAC) -sourcepath $(SRC) \
		-d $(BUILD) \
		$(JAVAC_ARGS) \
		-classpath $(LIB)/jython.jar:$(LIB)/junit.jar:$(LIB)/crimson.jar:$(LIB)/comm.jar:$(LIB)/jh.jar:$(LIB)/ftp.jar:$(LIB)/hsqldb.jar:$(LIB)/batik.jar:$(LIB)/kawa.jar:$(LIB)/bsh.jar:$(LIB)/log4j-1.2.8.jar \
		$(SRC)/corina/*.java \
		$(SRC)/corina/*/*.java \
		$(SRC)/corina/*/*/*.java

prejar: compile html timestamp
	mkdir $(BUILD)/Images/
	cp $(SRC)/Images/*.png $(BUILD)/Images/
	# OBSOLETE!  but for now:
	cp $(SRC)/Images/Preferences16.gif $(BUILD)/Images/

	cp $(SRC)/Timestamp $(BUILD)

	cp $(SRC)/prefs.properties $(BUILD)
	cp $(SRC)/species.properties $(BUILD)
	cp $(SRC)/countries.properties $(BUILD)

	cp $(SRC)/Translations/*.properties $(BUILD)
	cp $(SRC)/Statistics/*.intervals $(BUILD)

	cp $(SRC)/earth.rez.gz $(BUILD)

	mkdir $(BUILD)/corina/manual/
	cp -r $(MANUAL)/HTML/* $(BUILD)/corina/manual/

jar: Corina.jar
Corina.jar: prejar $(SRC)/Manifest timestamp
	fastjar cfm Corina.jar $(SRC)/Manifest \
		-C $(BUILD) .
#		-C $(BUILD) corina/ \
#		-C $(BUILD) Images/ \
# BUG: doesn't include *.properties (translations, prefs)
# BUG: doesn't include *.intervals (bayesian stats)
# BUG: includes all graphics, not just *.pngs (like what?)
# BUG: includes unused packages like eyes

JUNIT = $(JAVA) -cp $(BUILD):$(LIB)/junit.jar:$(LIB)/crimson.jar \
                junit.textui.TestRunner

# whatever i'm working on, move its test to the top of this list.
# TODO?: depend on jar, then test the jar itself?
test: compile prejar
	$(JUNIT) corina.formats.UnitTests
#	$(JUNIT) corina.cross.UnitTests
#	$(JUNIT) corina.UnitTests
#	$(JUNIT) corina.site.UnitTests
#	$(JUNIT) corina.prefs.UnitTests
#	$(JUNIT) corina.manip.UnitTests
#	$(JUNIT) corina.map.UnitTests
#	$(JUNIT) corina.index.UnitTests
#	$(JUNIT) corina.ui.UnitTests

# FIXME: depends on *.java, package.html's
# CAREFUL: -quiet doesn't exist on 1.3.x, but it's desireable on 1.4.x
# note: i can't depend on .../*/package.html, because make would think
# i wanted to make package.html files for each folder; how to work around?
javadoc: $(SRC)/corina/*.java $(SRC)/corina/*/*.java $(SRC)/corina/*/*/*.java
	$(RM) $(API)
	mkdir $(API)
	$(JAVADOC) -sourcepath $(SRC) \
		-classpath $(LIB)/jython.jar:$(LIB)/crimson.jar:$(LIB)/jh.jar:$(LIB)/comm.jar:$(LIB)/junit.jar:$(LIB)/batik.jar:$(LIB)/bsh.jar \
		-d $(API) \
		-use -author -version \
		-windowtitle "Corina API" \
		-stylesheetfile "$(SRC)/Corina API Style.css" \
		-overview "$(SRC)/overview.html" \
		-group "Core Packages" "corina*" \
		-group "Application-Level Packages" "corina.index:corina.cross:corina.editor:corina.graph:corina.manip:corina.map:corina.map.tools:corina.map.layers:corina.map.projections:corina.browser" \
                -group "User Interface Core Packages" "corina.gui:corina.gui.layouts:corina.gui.menus:corina.ui" \
		-group "Utility Packages" "corina.util:corina.print:corina.prefs" \
		-group "Experimental Packages" "corina.db:corina.batch:corina.eyes:corina.search:corina.sources" \
		-linkoffline http://java.sun.com/j2se/1.3/docs/api/ \
						$(JAVADOC_STUBS)/J2SE/ \
		-linkoffline http://www.gnu.org/software/kawa/api/ \
						$(JAVADOC_STUBS)/Kawa/ \
		corina \
		corina.io corina.formats \
		corina.index corina.cross corina.manip \
		corina.gui corina.gui.layouts corina.gui.menus corina.ui \
		corina.editor corina.browser corina.graph \
		corina.prefs corina.print corina.util \
		corina.map corina.map.tools corina.map.layers corina.map.projections \
		corina.site \
		corina.db corina.batch corina.eyes corina.search corina.sources \
		| grep -v ^Generating
	$(RM) $(WEB)/api/
	cp -r $(API) $(WEB)/api/

# done: java, batik, kawa
# todo: beanshell, jython?, junit, sax, crimson?
#		-link http://www.saxproject.org/apidoc/ \
#		-link http://www.junit.org/junit/javadoc/3.8.1/ \

# LINKOFFLINE:
#    i'd much rather use -linkoffline instead of -link, e.g.,
# -linkoffline http://java.sun.com/j2se/1.3/docs/api/ $(JAVADOC_STUBS)/J2SE/ \
# which would let me build javadoc (with proper links) even when
# i don't have web access.  unfortunately, 1.4.1 (which i'm hacking on now)
# has a bug that prevents use of -linkoffline.
# (see bug #4720957, http://developer.java.sun.com/developer/bugParade/bugs/4720957.html)
# ...
# when that's fixed (1.4.2?), switch to -linkoffline; until then, live with -link.
# -- write rule to grab package-list files automatically, and dump them into $(JAVADOC_STUBS)/
# -- use -linkoffline to refer to those stubs

# copies corina.jar and other libraries (OBSOLETE!) to the "Webpage" folder
web: jar
	cp Corina.jar $(WEB)/run/Corina.jar

	cp $(LIB)/crimson.jar $(WEB)/run/
	cp $(LIB)/jh.jar $(WEB)/run/
	cp $(LIB)/jython.jar $(WEB)/run/
	cp $(LIB)/kawa.jar $(WEB)/run/
# TODO: separate "build" libs from "runtime" libs, somehow

# docbook->html (non-chunked)
# TEMPORARY: i see no use for this, other than "get it into word, and fop sucks".
# or should i offer this, too?
html-1: $(MANUAL_DOCBOOK)/*.xml $(MANUAL)/Images/*.png
	$(RM) $(MANUAL)/html-1/
	mkdir $(MANUAL)/html-1/

	$(JAVA) -cp $(LIB)/saxon.jar:$(LIB)/crimson.jar:$(LIB)/batik.jar:$(LIB)/jimi/ \
		com.icl.saxon.StyleSheet \
		-x org.apache.crimson.parser.XMLReaderImpl \
		-o $(MANUAL)/html-1/Manual.html \
		$(MANUAL_DOCBOOK)/Manual.docbook \
		$(LIB)/docbook/html/docbook.xsl

	cp -r $(MANUAL)/Images/*.png $(MANUAL)/html-1/
	mkdir $(MANUAL)/html-1/images/
	cp -r $(LIB)/docbook/images/ $(MANUAL)/html-1/images/

# docbook->html
html: $(MANUAL)/HTML/index.html
$(MANUAL)/HTML/index.html: $(MANUAL_DOCBOOK_FILES)
	$(RM) $(MANUAL)/HTML/
	mkdir $(MANUAL)/HTML/

	# run XSLT to generate HTML
	cd $(MANUAL)/HTML/ && \
	$(JAVA) -cp "../../$(LIB)/saxon.jar:../../$(LIB)/crimson.jar:../../$(LIB)/batik.jar:/Users/kharris/Documents/Downloads/Jimi/" \
		com.icl.saxon.StyleSheet \
		-x org.apache.crimson.parser.XMLReaderImpl \
		../../$(MANUAL_DOCBOOK)/Manual.docbook \
		../../$(LIB)/docbook/html/chunk.xsl
# why can't i say -o $(MANUAL)/HTML/ here and get rid of the ../../ stuff?o

	# copy in my images
	cp $(MANUAL)/Images/*.png $(MANUAL)/HTML/

	# copy in docbook-xslt images
	mkdir $(MANUAL)/HTML/images/
	cp -r $(LIB)/docbook/images/ $(MANUAL)/HTML/images/

	# copy HTML to web page
	$(RM) $(WEB)/manual/
	cp -r $(MANUAL)/HTML/ $(WEB)/manual/

# run an XSLT transformation:
# - usage: $(XSLT) -o output.bleh input.xml bleh.xsl
XSLT = $(JAVA) -cp $(LIB)/saxon.jar:$(LIB)/crimson.jar \
	com.icl.saxon.StyleSheet \
	-x org.apache.crimson.parser.XMLReaderImpl

# docbook->fo
# (.fo can be made into .pdf or .rtf)
fo: $(MANUAL)/fo/Manual.fo
$(MANUAL)/fo/Manual.fo: $(MANUAL_DOCBOOK_FILES)
	$(RM) $(MANUAL)/fo/
	mkdir $(MANUAL)/fo/

	$(XSLT) -o $(MANUAL)/fo/Manual.fo \
		$(MANUAL_DOCBOOK)/Manual.docbook \
		$(LIB)/docbook/fo/docbook.xsl

# run FOP: converts .fo to .pdf
# - usage: $(FOP) -fo bleh.fo -pdf bleh.pdf
FOP = $(JAVA) -cp $(LIB)/fop.jar:$(LIB)/avalon.jar:$(LIB)/crimson.jar:$(LIB)/batik.jar:$(LIB)/xalan.jar:$(LIB)/saxon.jar:/Users/kharris/Documents/Downloads/Jimi/ \
		org.apache.fop.apps.Fop

# BUG: pdf creation doesn't work.  FOP gives the error:
#    "The id "d0e5293" alredy exists in this document"
# according to http://nagoya.apache.org/bugzilla/show_bug.cgi?id=3497
# this is can be caused by span="all", but if i comment out &include-index;
# (the index is the only part of my document which uses span="all")
# it still happens, so this doesn't appear to be my bug.  hmm...
# TODO: keep commenting things out until it goes away.  maybe i can duck it.
# maybe i can come up with a simple minimal document to help the fop guys.
# NOTES: tried other formats -- it's not pdf-specific.  tried 0.20.5; still there.

# fo->pdf
pdf: $(MANUAL)/Manual.pdf
$(MANUAL)/Manual.pdf: $(MANUAL)/fo/Manual.fo
	$(RM) $(MANUAL)/PDF/
	mkdir $(MANUAL)/PDF/

	cp $(MANUAL)/fo/Manual.fo $(MANUAL)/PDF/Manual.fo

	mkdir $(MANUAL)/PDF/images/
	cp -r $(LIB)/docbook/images/ $(MANUAL)/PDF/images/

	$(FOP) -fo $(MANUAL)/PDF/Manual.fo \
		-pdf $(MANUAL)/Manual.pdf

	cp $(MANUAL)/Manual.pdf $(WEB)/manual.pdf

# fo->rtf (a.k.a., word)
rtf: $(MANUAL)/fo/Manual.fo
	$(RM) $(MANUAL)/rtf/
	mkdir $(MANUAL)/rtf/

	cp $(MANUAL)/fo/Manual.fo $(MANUAL)/rtf/Manual.fo

	mkdir $(MANUAL)/rtf/images/
	cp -r $(LIB)/docbook/images/ $(MANUAL)/rtf/images/

	$(XSLT) -o $(MANUAL)/Manual.rtf \
		$(MANUAL)/fo/Manual.fo \
		$(LIB)/docbook/fo/fo-rtf.xsl
# why doesn't this work?
# ALSO: what's /pdf/ and /rtf/ for?  aren't they identical, and the same as /fo/?
#	cp $(MANUAL)/Manual.pdf $(WEB)/manual/Corina.pdf
# TODO: copy to web page?
# TODO: put link on web page?

# WRITEME:
# - shell scripts: docbook2rtf, docbook2pdf, docbook2html
# --- these already exist in some project (name?)
# - then just call them here
# - shell scripts aren't terribly portable ... how about using something like bsh?

# TODO: make web=javadoc+manual+pdf; rename "manual" to html?
# BETTER: each format should be "manual-html", "manual-pdf", etc.
# BEST: make "manual" its own folder/project

# (TODO: make this explicit?  "Corina.jar"?)
run: jar
	$(JAVA) -jar Corina.jar

# unused, so far.  how to use it?
run-mac: jar
	$(JAVA) -jar Corina.jar \
		-Xdock:icon=Source/Images/tree.icns \
		-Xdock:name=Corina

clean:
	$(RM) $(BUILD)
	$(RM) $(SRC)/Timestamp
	$(RM) Corina.jar

	$(RM) junit*.properties
	$(RM) jikes-*
	$(RM) cachedir

	$(RM) $(API)
	$(RM) $(WEB)/api/

	$(RM) $(MANUAL)/*.html
	$(RM) $(WEB)/manual/
	$(RM) $(MANUAL)/PDF/Manual.fo
	$(RM) $(MANUAL)/Manual.pdf

	$(RM) $(MANUAL)/fo/
	$(RM) $(MANUAL)/HTML/
	$(RM) $(MANUAL)/html-1/
	$(RM) $(MANUAL)/PDF/
	$(RM) $(MANUAL)/rtf/

# i never can remember whether it's "stat" or "stats",
# so just accept either one.
stat: stats

stats:
	@echo -n "Lines of Java:"
	@wc -l `find $(SRC) -name \*.java` | tail -1
	@echo -n "Number of Java files: "
	@find $(SRC) -name \*.java | wc -l
	@echo -n "Lines of DocBook/XML:"
	@wc -l $(MANUAL_DOCBOOK)/*.docbook $(MANUAL_DOCBOOK)/*.chap $(MANUAL_DOCBOOK)/*.appdx | tail -1

# this requires java 1.4 -- findbugs is written in a
# prerelease java+stuff language.  darn.
findbugs: jar
	$(JAVA) -jar $(LIB)/findbugs.jar Corina.jar | grep -v ^Se:

lint: compile
	jlint -source $(SRC) $(BUILD)/corina/{*,*/*,*/*/*}.class

#
# validate the DocBook/XML against the DTD
#
# - PROBLEM: it gives all line numbers as "Manual.docbook:n", where n
# doesn't seem to have any meaning.  it takes a lot of work to find
# out where the error actually occurred.  (probably due to my use of
# file inclusion, but that's helpful, too)
validate-r:
	xmllint --dtdvalid http://www.oasis-open.org/docbook/xml/4.1.2/docbookx.dtd --valid --noout $(MANUAL)/DocBook/Manual.docbook

validate-l:
	xmllint --dtdvalid $(LIB)/docbook-xml-4.2/docbookx.dtd --valid --noout $(MANUAL)/DocBook/Manual.docbook
