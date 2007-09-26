<?php

class DendroBib
{
/**
* Constructor.
*/
	function DendroBib($style)
	{
		$this->style = $style;
	}
/*
* function execute()
*
* Start the whole process
*/
	function execute()
	{
// Load the test data
                global $basepath;
                $this->getData();
		include_once("BIBFORMAT.php");
		$this->bibformat = new BIBFORMAT();
// Load the bibliographic style
		list($info, $citation, $footnote, $styleCommon, $styleTypes) = 
			$this->bibformat->loadStyle($basepath."phpinc/OSBiB/styles/bibliography/", $this->style);
		$this->bibformat->getStyle($styleCommon, $styleTypes, $footnote);
		unset($info, $citation, $footnote, $styleCommon, $styleTypes); // no longer required here so conserve memory
// Cycle through the resources we want to format
		foreach($this->row as $row)
			$this->bibResult[] = $this->processBib($row);
// For citation formatting, BIBFORMAT must be reinitialised.
		include_once("BIBFORMAT.php");
		$this->bibformat = new BIBFORMAT();
		include_once("CITEFORMAT.php");
// Pass the bibstyle object to CITEFORMAT() as the first argument.
// The second argument is the name of the method within the bibstyle object that starts the formatting of a bibliographic item.
		$this->citeformat = new CITEFORMAT($this, "processBib", "../");
		$this->bibformat->output = $this->citeformat->output = 'html'; // output format (default)
		list($info, $citation, $footnote, $styleCommon, $styleTypes) = 
			$this->bibformat->loadStyle($basepath."phpinc/OSBiB/styles/bibliography/", $this->style);
		$this->bibformat->getStyle($styleCommon, $styleTypes, $footnote);
		$this->citeformat->getStyle($citation, $footnote);
		unset($info, $citation, $footnote, $styleCommon, $styleTypes); // no longer required here so conserve memory
		$this->formattedText = $this->processCite();
		$this->printToScreen();
	}
/*
* function processBib()
*
* Produce a bibliography from $this->row.
*/
	function processBib($row)
	{
		$id = $row['id'];
// Add the resource type
		$this->bibformat->type = $type = $row['type'];
		$row = $this->bibformat->preProcess($type, $row);
		$type = $this->bibformat->type; // may have been changed in preProcess so reset.
// Add the title.  The 2nd and 3rd parameters indicate what bracketing system is used to conserve uppercase characters in titles.
		$this->bibformat->formatTitle($row['title'], "{", "}");
// Add creator names.  Up to 5 types are allowed - for mappings depending on resource type see STYLEMAP.php
		for($index = 1; $index <= 5; $index++)
		{
			$creatorSlot = 'creator' . $index;
// If we have creator name in this slot OR that creator slot is not defined in STYLEMAP.php, do nothing
			if(array_key_exists($creatorSlot, $row) && 
			array_key_exists($creatorSlot, $this->bibformat->styleMap->$type))
				$this->bibformat->formatNames($row[$creatorSlot], $creatorSlot);
		}
// Edition
		if(array_key_exists('edition', $row) && array_search('edition', $this->bibformat->styleMap->$type))
			$this->bibformat->formatEdition($row['edition']);
// Pages
		if(array_key_exists('pageStart', $row) && array_search('pages', $this->bibformat->styleMap->$type))
		{
			$end = array_key_exists('pageEnd', $row) ? $row['pageEnd'] : FALSE;
			$this->bibformat->formatPages($row['pageStart'], $end);
		}
// All other database resource fields that do not require special formatting/conversion.
		$this->bibformat->addAllOtherItems($row);
// For citation formatting later on, get the placeholder to deal with ambiguous in-text citations.  Must be keyed by unique resource identifier.
		$this->citeformat->bibliographyIds[$id] = FALSE;
// Return the result of the bibliographic formatting removing any extraneous braces
		return preg_replace("/{(.*)}/U", "$1", $this->bibformat->map());
	}
/*
* function processCite()
*
* Parse $this->text for citation tags and format accordingly.
*/
	function processCite()
	{
// Must be initialised.
		$this->pageStart = $this->pageEnd = $this->preText = $this->postText = $this->citeIds = array();
// Parse $this->text
// Capture any text after last [cite]...[/cite] tag
		$explode = explode("]etic/[", strrev($this->text), 2);
		$this->tailText = strrev($explode[0]);
		$text = strrev("]etic/[" . $explode[1]);
		preg_match_all("/(.*)\s*\[cite\](.*)\[\/cite\]/Uis", $this->text, $match);
		foreach($match[1] as $value)
			$this->matches[1][] = $value;
		$this->citeformat->count = 0;
		foreach($match[2] as $index => $value)
		{
			++$this->citeformat->count;
			if($id = $this->parseCiteTag($index, $value))
				$this->citeIds[] = $id;
		}
// If empty($this->citeIds), there are no citations to scan for (or user has entered invalid IDs) so return $text unchanged.
		if(empty($this->citeIds))
			return $text;
		$this->citeformat->processEndnoteBibliography($this->row, $this->citeIds);
/*
* $matches[1]is an array of $1 above
* $matches[2] is an array of $2 (the citation references)
* e.g. 
* [1] => Array ( [0] => First [1] => [2] => [3] => [4] => blah blah see ) [2] => Array ( [0] => 1 [1] => 2 [2] => 3 [3] => 4 [4] => 2 )
* might represent:
* First [cite]1[/cite] [cite]2[/cite] [cite]3[/cite]
* [cite]1[/cite] blah blah see[cite]2[/cite]
*
* Note that having both [1][0] and [2][0] populated means that the citation reference [2][0] _follows_ the text in [1][0].
* Any unpopulated elements of matches[1] indicates multiple citations at that point.  e.g., in the example above, 
* there are multiple citations (references 1, 2, 3 and 4) following the text 'First' and preceeding the text 'blah blah see'.
*
* N.B. the preg_match_all() above does not capture any text after the final citation so this must be handled manually and appended to any final output - 
* this is $this->tailText above.
*/
//		$this->row = array();
		$this->citeformat->count = 0;
		$citeIndex = 0;
		while(!empty($this->matches[1]))
		{
			$this->citeformat->item = array(); // must be reset each time.
			$id = $this->citeIds[$citeIndex];
			++$citeIndex;
			++$this->citeformat->count;
			$text = array_shift($this->matches[1]);
			$this->citeformat->items[$this->citeformat->count]['id'] = $id;
			$this->createPrePostText(array_shift($this->preText), array_shift($this->postText));
// For each element of $bibliography, process title, creator names etc.
			if(array_key_exists($id, $this->row))
				$this->processCitations($this->row[$id], $id);
// $this->rowSingle is set in $this->processCitations().
// 'type' is the type of resource (book, journal article etc.).  In WIKINDX, this is part of the row returned by SQL:  you may 
// need to set this manually if this is not the case for your system.  'type' is used in CITEFORMAT::prependAppend() to add any special strings to the citation within 
// the text (e.g. the XML style file might state that 'Personal communication: ' needs to be appended to any in-text citations for resources of type 'email'.
// CITEFORMAT::prependAppend() will map 'type' against the $types array in STYLEMAP as used in BIBFORMAT.
			$this->citeformat->items[$this->citeformat->count]['type'] = $this->rowSingle['type'];
			$this->citeformat->items[$this->citeformat->count]['text'] = $text;
		}
		$pString = $this->citeformat->process() . $this->tailText;
// Endnote-style citations so add the endnotes bibliography
		if($this->citeformat->style['citationStyle'])
		{
			$pString = $this->citeformat->printEndnoteBibliography($pString);
			if($this->citeformat->style['endnoteStyle'] != 2) // Not footnotes.
				return $pString;
		}
// In-text citations and footnotes - output the appended bibliography
		$bib = $this->printBibliography($this->row);
		return $pString . $bib;
	}
/**
* Parse the cite tag by extracting resource ID and any page numbers. Check ID is valid
* PreText and postText can also be encoded: e.g. (see Grimshaw 2003; Boulanger 2004 for example)
* [cite]23:34-35|see ` for example[/cite].  For multiple citations, only the first encountered preText and postText will be used to enclose the citations.
*/
	function parseCiteTag($matchIndex, $tag)
	{
// When a user cut's 'n' pastes in HTML design mode, superfluous HTML tags (usually <style lang=xx></span>) are inserted.  Remove anything that looks like HTML
		$tag = preg_replace("/<.*?>/si", "", $tag);
		$rawCitation = explode("|", $tag);
		$idPart = explode(":", $rawCitation[0]);
		$id = $idPart[0];
		if(array_key_exists('1', $idPart))
		{
			$pages = explode("-", $idPart[1]);
			$pageStart = $pages[0];
			$pageEnd = array_key_exists('1', $pages) ? $pages[1] : FALSE;
		}
		else
			$pageStart = $pageEnd = FALSE;
		$this->citeformat->formatPages($pageStart, $pageEnd);
		if(array_key_exists('1', $rawCitation))
		{
			$text = explode("`", $rawCitation[1]);
			$this->preText[] = $text[0];
			$this->postText[] = array_key_exists('1', $text) ? $text[1] : FALSE;
		}
		else
			$this->preText[] = $this->postText[] = FALSE;
		return $id;
	}
// Accept a SQL result row of raw bibliographic data and process it.
// We build up the $citeformat->item array with formatted parts from the raw $row
	function processCitations($row, $id)
	{
		$this->rowSingle = $row;
		unset($row);
		$this->citeformat->formatNames($this->rowSingle['creator1'], $id); // Use 'creator1' array -- primary creators.
// The title of the resource
		$this->citeformat->formatTitle($this->rowSingle['title'], "{", "}");
// Publication year of resource.  If no publication year, we create a dummy key entry so that CITEFORMAT can provide a replacement string if required by the style.
		if(!array_key_exists('year1', $this->rowSingle))
			$this->rowSingle['year1'] = FALSE;
		$this->citeformat->formatYear(stripslashes($this->rowSingle['year1']));
	}
/* Create preText and postText.  This is for in-text citations where a string of citations tags:
[cite]1:24|See ` for example[/cite][cite]33:54[/cite]
might result in:
(See Grimshaw et al., 2005 p. 24 and Grimshaw, 1999 p. 54 for example).
*/
	function createPrePostText($preText, $postText)
	{
		if(!$preText && !$postText) // empty field
			return;
		$this->citeformat->formatPrePostText($preText, $postText);
	}
// Process bibliography array into string for output -- used for in-text citations and appended bibliographies for footnotes
	function printBibliography($bibliography)
	{
		foreach($bibliography as $id => $row)
		{
			$row['id'] = $id;
// Do not add if cited resource type shouldn't be in the appended bibliography
			if(array_key_exists($row['type'] . "_notInBibliography", $this->citeformat->style))
				continue;
// If we're disambiguating citations by adding a letter after the year, we need to insert the yearLetter into $row before formatting the bibliography.
			if($this->citeformat->style['ambiguous'] && 
				array_key_exists($id, $this->citeformat->yearsDisambiguated))
				$row['year1'] = $this->citeformat->yearsDisambiguated[$id];
			$this->citeformat->processIntextBibliography($row);
		}
		return $this->citeformat->collateIntextBibliography();
	}


function getData()
{
        global $basepath;
        global $authorfilter;
        global $bibfile;
        global $sortChronologically;

        // Load and parse BIB file
	include($basepath."phpinc/bibtexParse/PARSEENTRIES.php");
	include($basepath."phpinc/bibtexParse/PARSECREATORS.php");
	$biblio = NEW PARSEENTRIES();
	$biblio->openBib($bibfile);
	$biblio->extractEntries();
	$biblio->closeBib();
	list($preamble, $strings, $entries, $undefinedStrings) = $biblio->returnArrays();

        // Sort references by author and date
        function sortonauthor($a, $b) 
        { 
            $retval = strnatcmp($a['author'], $b['author']); 
            if(!$retval) return 1- strnatcmp($a['year'], $b['year']);
            return $retval;
        } 
        function sortChronologically($a, $b) 
        { 
            $retval = strnatcmp($b['year'], $a['year']); 
            if(!$retval) return 1- strnatcmp($a['author'], $b['author']); 
            return $retval;
        } 
       

        if($sortChronologically)
        {
            usort($entries, 'sortChronologically');
        }
        else
        {
            usort($entries, 'sortonauthor');
        }


        // Loop through all the references
	$i = 0;
	//print_r($entries);
	foreach($entries as $ref)
        {
            // Skip all records except where the author matches the search 
            // where an author filter has been specified
            if($authorfilter)
            {
                $pos = strpos($ref['author'], $authorfilter);
                if($pos === false)
                {
                    $i++;
                    continue;
                }
            }

            //Set reference id
            $this->row[$i]['id']=$i;
            
            //Set correct reference type
            switch($ref['bibtexEntryType'])
            {
            case "article":
                $this->row[$i]['type']= 'journal_article';
                break;
            case "book":
                $this->row[$i]['type']= 'book';
                break;
            case "inproceedings":
                $this->row[$i]['type']= 'proceedings_article';
                break;
            case "inbook":
                $this->row[$i]['type']= 'book_article';
                break;
            case "phdthesis":
                $this->row[$i]['type']= 'thesis';
                break;
            }
            
            //Set title
            if($this->row[$i]['type']== 'book_article')
            {
                $this->row[$i]['title']= $ref['chapter'];
            }
            else
            {
                $this->row[$i]['title']= $ref['title'];
            }

            //Extract authors from string
            $creator = NEW PARSECREATORS();
            $creatorArray = $creator->parse($ref['author']);
            $j=0;
            foreach($creatorArray as $author)
            {
                $this->row[$i]['creator1'][$j]['surname']= trim($author[2]);
                $this->row[$i]['creator1'][$j]['firstname']= trim($author[0]);
                $this->row[$i]['creator1'][$j]['initials']= trim($author[1]);
                $this->row[$i]['creator1'][$j]['prefix']= $author[3];
                $this->row[$i]['creator1'][$j]['id']= $j;
                $j++;
            }
            
            //Extract editors from string
            $editor = NEW PARSECREATORS();
            $editorArray = $editor->parse($ref['editor']);
            $j=0;
            foreach($editorArray as $editor)
            {
                $this->row[$i]['creator2'][$j]['surname']= trim($editor[2]);
                $this->row[$i]['creator2'][$j]['firstname']= trim($editor[0]);
                $this->row[$i]['creator2'][$j]['initials']= trim($editor[1]);
                $this->row[$i]['creator2'][$j]['prefix']= $editor[3];
                $this->row[$i]['creator2'][$j]['id']= $j;
                $j++;
            }

            //Set remaining reference details
            $this->row[$i]['bookTitle']= $ref['title'];
            $this->row[$i]['conference']= $ref['booktitle'];
            $this->row[$i]['year']= $ref['year'];
            $this->row[$i]['year1']= $ref['year'];
            $this->row[$i]['year2']= $ref['year'];
            $this->row[$i]['publisherName']= $ref['publisher'];
            $this->row[$i]['publisherLocation']= $ref['address'];
            $this->row[$i]['collectionTitle']= $ref['journal'];
            $this->row[$i]['volume']= $ref['volume'];
            //$this->row[$i]['field1']= $ref['volume'];
            $this->row[$i]['number']= $ref['number'];
            $this->row[$i]['ISSN']= '';
            $this->row[$i]['pages']= $ref['pages'];
            $this->row[$i]['pdf']= $ref['pdf'];
            $this->row[$i]['url']= $ref['url'];
            $i++;

            //print_r($this);
	}

}

/*
* function printToScreen()
*
* Print to the browser
*/
	function printToScreen()
        {
                //print_r($this);
		echo "<ul>\n";
		foreach($this->bibResult as $result)
			echo "   <li>$result</li>\n";
		echo "</ul>\n";
	}
}
