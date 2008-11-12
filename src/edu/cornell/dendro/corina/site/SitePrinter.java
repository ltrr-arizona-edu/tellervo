//
// This file is part of Corina.
// 
// Corina is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
// Corina is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with Corina; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// Copyright 2001 Ken Harris <kbh7@cornell.edu>
//

package edu.cornell.dendro.corina.site;

import edu.cornell.dendro.corina.print.Printer;
import edu.cornell.dendro.corina.print.Line;
import edu.cornell.dendro.corina.print.TextLine;
import edu.cornell.dendro.corina.print.EmptyLine;
import edu.cornell.dendro.corina.print.ThinLine;
import edu.cornell.dendro.corina.print.ByLine;
import edu.cornell.dendro.corina.print.TabbedLineFactory;

import java.util.List;

/**
   Print a List of Sites (to the printer).

   <p>The sites are grouped by country.  The output for a country
   looks something like this:</p>

<blockquote class="paper">
<h3>Russian Federation (RU)</h3>
<p>2 sites</p>
<table width="100%" border="0">
  <tr>
    <td>ARZ</td>
    <td>500</td>
    <td width="*">Arzhan Kurgan</td>
    <td align="right">Larix siberica</td>
    <td align="right">3</td>
  </tr>
  <tr>
    <td>KTL</td>
    <td>199</td>
    <td width="*">Kutlak Fortress (Crimea)</td>
    <td align="right">Juniperus</td>
    <td align="right">3</td>
  </tr>
</table>
</blockquote>

   @see LegacySite

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class SitePrinter extends Printer {
    /*
      TODO:
      misc:
      -- SiteDB itself should be a full PrintableDocument
      -- follow the format of the SiteListPanel (columns/sizes)?
         or be more flexible, like "print... -> group by [countries,
	 species, type, none]"?
	 general boilerplate:
      -- rename to "SiteListPrinter"?
      output quality:
      -- fix "type" column: actually use names
      -- put "1 site" text right-aligned on same line as country name
      -- # of samples column?
      higher-level stuff (for corina.print):
      -- should lines be visible here?  why not just expose an
         addLine() method?
      -- don't split pages after section titles?
      -- header?  ("top of each page" header for PrintableDocument)
      -- better header (what's used now?)
      -- wrapping / use multiple lines if text too long for its tab stop
      bugs:
      -- today (first time) some stuff is getting printed upside-down.
         possibly related to the fact that i've switched to storing things
	 in UTF-8 ... could a valid unicode value be screwing up printing?
	 (they've only printed as blank boxes before, but it's possible.)
    */

    /**
       Make a new SitePrinter from a List of Sites.

       @param sites the List of sites to print
    */
    public SitePrinter(List sites) {
	this.sites = sites;

	// title
	lines.add(new TextLine("Sites", Line.TITLE_SIZE));
	lines.add(new EmptyLine());

	// get ready to print a table
	table = new TabbedLineFactory("> 6% > 6% > 85% < 3% <");

	// print each country
	List countries = LegacySiteDB.getSiteDB().getCountriesInOrder();
	for (int i=0; i<countries.size(); i++) {
	    String country = (String) countries.get(i);
	    printCountry(country);
	}

	// print byline
	lines.add(new EmptyLine());
	lines.add(new ThinLine(0.0f, 0.3f));
	lines.add(new ByLine());
    }

    // used for printing a table
    private TabbedLineFactory table;

    // the list of sites to print
    private List sites;

    // print a title, and all of the sites in this country.
    private void printCountry(String country) {
	// print section line as, e.g., "Turkey (TU)"
	String name = Country.getName(country);
	String countryHeader = name + " (" + country + ")";
	lines.add(new TextLine(countryHeader, Line.SECTION_SIZE));

	// print how many sites there are
	int num = countSitesInCountry(country);
	lines.add(new TextLine(num==1 ? "1 site" : num + " sites"));

	// print each site in this country.
	// TODO: put them in alphabetical order by name.
	for (int i=0; i<sites.size(); i++) {
	    LegacySite site = (LegacySite) sites.get(i);
	    if (site.getCountry().equals(country))
		printSite(site);
	}

	// an empty line to separate country sections
	lines.add(new EmptyLine());
    }

    // count the number of sites with this country code
    private int countSitesInCountry(String country) {
	int num = 0;
	for (int i=0; i<sites.size(); i++) {
	    LegacySite site = (LegacySite) sites.get(i);
	    if (site.getCountry().equals(country))
		num++;
	}
	return num;
    }

    // print one site, as a table row.
    private void printSite(LegacySite site) {
	String args[] = new String[] {
	    site.getCode(),
	    site.getId(),
	    site.getName(),
	    site.getSpecies(),
	    site.getTypeString(), // FIXME: .getTypesAsString() (extract)
	};

	// ASSUMES: none of these fields was null, or
	// contained a '\t' character

	lines.add(table.makeLine(args[0] + "\t" +
				 args[1] + "\t" +
				 args[2] + "\t" +
				 args[3] + "\t" +
				 args[4]));
    }
}
