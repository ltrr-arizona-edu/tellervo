This folder contains the user manual for Corina.

Note that the user manual doesn't exist as a single word processor
file, like "Manual.doc".  It's generated using a system called
DocBook.  You write text files in DocBook (it's vaguely like HTML),
and ask a translator program to convert it into HTML, PDF, or whatever
format you want.

Files/folders that always exist:

	DocBook for Dummies.txt - help you get started if you've never
	used DocBook before.  See also ESR's "DocBook Demystification
	HOWTO"
	<http://www.tldp.org/HOWTO/DocBook-Demystification-HOWTO/>.

	README.txt - this file

	DocBook/ - the DocBook source files (text files that I wrote -
	these get converted by a program into HTML, PDF, and so forth)

	DocBook/Images/ - images used in the manual

Files/folders that are generated (at compile-time by the makefile:

	HTML/ - the HTML output, one file per section (start at index.html)

	fo/ - the manual in "fo" format (a temporary intermediate format used
        by PDF and RTF)

	PDF/ - temporary files used in PDF creation

	Corina.pdf - the manual in PDF format

Conventions used:

- One file per chapter.  I think this makes it easier.  I'm not sure.
  DocBook/XML doesn't have a very elegant way to do file inclusion
  (it's virtually impossible to do 2-level inclusion).  (Help?)

- Keep lines under 80 characters.

- Use only ASCII characters, and tell processors that it's UTF-8.
  I've seen too many bad implementations of Unicode to trust it for
  any source files just yet.  (I don't feel bad using it for
  Corina-saved files because it's always one implementation of UTF-8
  -- Sun's Java -- that's being used, and one I feel fairly confident
  with.)  If you need a character not in ASCII, escape it.

- Equations and algorithms: use the idiom

  <equation>
    <title>Fermat's Last Theorem</title>
    <alt>x^n + y^n \neq z^n \forall n \neq 2</alt>
    <mediaobject>
      <textobject>
        <para>
	  x<superscript>n</superscript> +
          y<superscript>n</superscript> &#x2260;
          z<superscript>n</superscript> &#x2200;
          n &#x2260; 2
        </para>
      </textobject>
    </mediaobject>
  </equation>

  This looks super-ugly (TeX fans are allowed to puke), but it doesn't
  require MathML-in-DocBook, which requires a bunch of
  still-in-testing junk to process, and it doesn't require running
  external programs to convert equations to images and then include
  the images.  It's self-contained, and it should show up fairly
  painlessly everywhere (buggy Unicode implementations and poor fonts
  notwithstanding).

  I realize this is not the optimal solution, but it works, and
  requires no extra work (on top of DocBook itself) to get working.
  Once MathML-in-DocBook/XML is standard, I'll gladly switch to that.
  (Also, the TeX is in the alt-tag, which could also be used.  Just
  add them to the file:

    \documentclass{article}
    \begin{document}
    \pagestyle{empty}
    PUT-EQUATION-HERE
    \end{document}

  From this, you can create EPS for printing or PNG for browsing.)

- Everything which is linked to should have an "id" attribute.
  This includes everything linked to in the table of contents,
  like chapters, sections, equations, tables, and everything
  in the bibliography.

- Make sure you keep the entire document valid XML.  Errors can be
  nearly impossible to track down in an invalid DocBook document.
  (Sad to say, most DocBook/XML tools are pretty lousy at handling bad
  input, and they don't check for validity first.)

  To validate, use the xmllint program:
    xmllint --dtdvalid http://www.oasis-open.org/docbook/xml/4.1.2/docbookx.dtd --valid --noout Manual.docbook

  To make this easier, I've made Makefile rules for this.  Simply use either:
    make docbook-validate-remote
  or
    make docbook-validate-local

  (The former uses the DTDs at oasis-open.org, which are more up-to-date,
  while the latter uses the DTDs on the local system, which can be used even
  when not connected to the network.)

- The manual should always have correct spelling.  Fortunately every
  system these days has a spell-checker.  The only catch is they don't
  work very well with XML files -- but that's ok.  I just need to
  process the DocBook into plain text of some sort.  I'll lose line
  numbers, but a misspelled word should be easy to find.

  What would be ideal is a Makefile rule: make docbook-spellcheck.  It
  would: process the DocBook into plain text, and run the spell
  checker, also passing it a list of special words it might not know
  (Gleichlaufigkeits, anyone?).

  I should write such a rule...

- (add more here)

Problems:

- The DocBook tag <graphic> will be removed in DocBook 5.0.
  I use this tag.  I should probably switch to <mediaobject>,
  the replacement, ASAP.

  OLD:
    <graphic fileref="image.png" format="PNG"/>

  NEW:
    <mediaobject>
      <imageobject>
        <imagedata fileref="image.png" format="PNG"/>
      </imageobject>
    </mediaobject>

  Other benefits include: multiple <imageobject> tags for multiple
  versions of the image, and a <caption> capability.

- Norm's XSLT doesn't seem to emit a DOCTYPE.  (Can I fix this?)
  (I seem to recall hearing it doesn't generate valid HTML, either.
  Confirm?)

- The "Troubleshooting.appdx" file seems to break FOP.  Looks like a
  FOP bug.

  From the web: "There is a bug in FOP that generates this message
  when in fact there is no duplicate id [...] It might have something
  to do with a list at a page break"

  Bingo.  It's what happens when FOP needs to put a long list across a
  page break -- in my case, a long Q-and-A division.  Both the
  "Indexing" and "Files" divisions are pretty long, and either one
  causes this bug to surface.

- PDF conversion doesn't respect many non-ASCII characters, especially
  Greek letters.  For example, U+2211 (escaped as ASCII, even) in the
  DocBook source ends up as the actual UTF-8 for U+2211 (e2 88 91, in hex)
  in the FO file (whose header even explicitly declares itself as UTF-8).
  Yet the PDF shows this symbol as simply "#".  I don't speak PDF, but the
  FOP-generated PostScript file uses the actual "#" character (U+0023),
  so it's not my PDF viewers which are making up "#" (both Adobe's and
  Gnome's PDF viewers do this, and I think Apple's does, too).

  Short version: Apache FOP doesn't handle some characters properly.
  For example, it changes U+2211 into U+0023.

  It may fall under http://nagoya.apache.org/bugzilla/show_bug.cgi?id=6237
  though that's for "unavailable".  A sigma should always be available,
  since PDF (Adobe) guarantee any PDF reader must have 14 fonts, including
  Symbol, which has a sigma.  (Maybe FOP isn't smart enough to say
  "Hey, it's a letter that's normally only in Symbol, I should switch
  to that".)

Sources:

Norm Walsh wrote the DocBook-XSL stylesheets, so I think he knows what
he's talking about.  His "DocBook: The Definitive Guide"
<http://www.docbook.org/tdg/en/html/docbook.html> is a very nice
reference.

ESR likes David Rugge's "Writing Documentation Using DocBook: A Crash
Course"
<http://opensource.bureau-cornavin.com/crash-course/index.html>.  From
one quick glance, it does look good.
