L10n notes for Corina:

-- To make a new language, copy the file TextBundle.properties, rename
it to something like TextBundle_de.properties (for de=Deutsch=German),
and translate all of the strings.  The build process will
automatically pick it up, if it's in the same folder.

-- Lots of things aren't localized yet, especially error messages
(which should probably go away or be rewritten).

-- Properties files are always UTF-8.  (Especially important for
non-Latin alphabets, where you might be tempted to use something more
native.)  (No, they're not.  That was a lie.  They're
Java-Unicode-encoded.)

-- Apparently Java provides no support for localizing mnemonics and
accelerators.  But I wrote it!  So now you can just say:

	copy = &Copy [accel C]

See the JavaDoc documentation for corina/ui/I18n.java for more
details.

-- In addition, I use the convention that entries that end with "..."
have keys that end with "...", because I might need both versions.

	reverse = Reverse
	truncate... = Truncate...

-- Some parts of the existing properties files aren't actually used, or
are otherwise poorly organized.  Clean 'em up.

-- Use correct quotes.  According to Unicode, U+201C and U+201D are
the proper curvy-quotes, and U+2019 is an apostrophe (especially
important in French).  But Windows support for this is sketchy.  I
should decide which versions of Windows/Java I want to support, and
then use all of their features I want.

-- An em-dash is \u2014, but I don't know if Windows supports it.
(Our HP laser printer doesn't, so I can't use it for printing, yet.)
