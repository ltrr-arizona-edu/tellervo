/********************************
OSBib:
A collection of PHP classes to create and manage bibliographic formatting for OS bibliography software 
using the OSBib standard.

Released through http://bibliophile.sourceforge.net under the GPL licence.
Do whatever you like with this -- some credit to the author(s) would be appreciated.

If you make improvements, please consider contacting the administrators at bibliophile.sourceforge.net 
so that your improvements can be added to the release package.

Adapted from WIKINDX: http://wikindx.sourceforge.net

Mark Grimshaw 2005
http://bibliophile.sourceforge.net

	$Header: /cvsroot/bibliophile/OSBib/create/common.js,v 1.6 2005/11/14 06:38:15 sirfragalot Exp $
********************************/

function init(){ // init() is called in body.tpl
 initPreviewLinks();
}

/**
* Create the preview link for bibliographic style editing, hiding the link for non-javascript-enabled browsers.
* 
* @author	Jess Collicott
* @editor	Mark Tsikanovski and Mark Grimshaw
* @version	2
*/
function initPreviewLinks(){ // collect any links for style template preview, add onclick events and make them visible
 var previewLinkKeyString = 'action=previewStyle'; // use this string to detect Preview links
 var previewLinkKeyRegEx = new RegExp(previewLinkKeyString,'i');
 var links = document.getElementsByTagName('body').item(0).getElementsByTagName('a'); // get collection of all links
 var linksLength = links.length; // cache

// As of 3.1, style previewing is not working in IE so turn it off all together.
var agt = navigator.userAgent.toLowerCase();
  var is_ie = ((agt.indexOf("msie") != -1));
  if (is_ie)
  {
	for (i=0;i<linksLength;i++)
	{
		if (typeof(links[i].href) != 'undefined' && links[i].href.search(previewLinkKeyRegEx) != -1)
		{
			links[i].className = 'linkHidden';
		}
	}
	return;
  }
  
 for (i=0;i<linksLength;i++){
  if (typeof(links[i].href) != 'undefined' && links[i].href.search(previewLinkKeyRegEx) != -1){
    if (links[i].className == 'imgLink linkHidden') {
	  links[i].className = 'imgLink linkCite';
	}
	else {
      links[i].className = 'link linkCite';
	}
  }
 }
}

/**
* pop-up window for style previews
* 
* @Author Mark Grimshaw with a lot of help from Christian Boulanger
*/
function openPopUpStylePreview(url, height, width, templateName)
{
 browserDimensions();
 var w = browserWidth * 0.95;
 var h = browserHeight * 0.2;
	var fieldArray = new Array ("style_titleCapitalization", "style_primaryCreatorFirstStyle", 
			"style_primaryCreatorOtherStyle", "style_primaryCreatorInitials", 
			"style_primaryCreatorFirstName", "style_otherCreatorFirstStyle", 
			"style_otherCreatorOtherStyle", "style_otherCreatorInitials", "style_dayFormat", 
			"style_otherCreatorFirstName", "style_primaryCreatorList", "style_otherCreatorList",
			"style_primaryCreatorListAbbreviationItalic", "style_otherCreatorListAbbreviationItalic", 
			"style_monthFormat", "style_editionFormat", "style_primaryCreatorListMore", 
			"style_primaryCreatorListLimit", "style_dateFormat", 
			"style_primaryCreatorListAbbreviation", "style_otherCreatorListMore", 
			"style_runningTimeFormat", "style_primaryCreatorRepeatString", "style_primaryCreatorRepeat", 
			"style_otherCreatorListLimit", "style_otherCreatorListAbbreviation", "style_pageFormat", 
			"style_editorSwitch", "style_editorSwitchIfYes", "style_primaryCreatorUppercase", 
			"style_otherCreatorUppercase", "style_primaryCreatorSepFirstBetween", 
			"style_primaryCreatorSepNextBetween", "style_primaryCreatorSepNextLast", 
			"style_otherCreatorSepFirstBetween", "style_otherCreatorSepNextBetween", 
			"style_otherCreatorSepNextLast", "style_primaryTwoCreatorsSep", "style_otherTwoCreatorsSep", 
			"style_userMonth_1", "style_userMonth_2", "style_userMonth_3", "style_userMonth_4", 
			"style_userMonth_5", "style_userMonth_6", "style_userMonth_7", "style_userMonth_8", 
			"style_userMonth_9", "style_userMonth_10", "style_userMonth_11", "style_userMonth_12", 
			"style_dateRangeDelimit1", "style_dateRangeDelimit2", "style_dateRangeSameMonth", 
			"style_dateMonthNoDay", "style_dateMonthNoDayString", "style_dayLeadingZero"
		);
	var arrayKey;
	var fieldName;
	var currFormField;
	var styleArray = new Array ();
	var creatorArray = new Array ();
// Adde rewrite creator strings for the resource type
	var creators = new Array("creator1", "creator2", "creator3", "creator4", "creator5");
	for (index = 0; index < creators.length; index++)
	{
		arrayKey = creators[index] + "_firstString";
		fieldName = templateName + "_" + creators[index] + "_firstString";
		currFormField = document.forms[0][fieldName];
		if(typeof(currFormField) != 'undefined')
			creatorArray[arrayKey] = currFormField.value; // input and textarea
		
		arrayKey = creators[index] + "_firstString_before";
		fieldName = templateName + "_" + creators[index] + "_firstString_before";
		currFormField = document.forms[0][fieldName];
		if ((typeof(currFormField) != 'undefined') && currFormField.checked) // checkbox
			creatorArray[arrayKey] = "on";
			
		arrayKey = creators[index] + "_remainderString";
		fieldName = templateName + "_" + creators[index] + "_remainderString";
		currFormField = document.forms[0][fieldName];
		if(typeof(currFormField) != 'undefined')
			creatorArray[arrayKey] = currFormField.value; // input and textarea
		
		arrayKey = creators[index] + "_remainderString_before";
		fieldName = templateName + "_" + creators[index] + "_remainderString_before";
		currFormField = document.forms[0][fieldName];
		if ((typeof(currFormField) != 'undefined') && currFormField.checked) // checkbox
			creatorArray[arrayKey] = "on";
			
		arrayKey = creators[index] + "_remainderString_each";
		fieldName = templateName + "_" + creators[index] + "_remainderString_each";
		currFormField = document.forms[0][fieldName];
		if ((typeof(currFormField) != 'undefined') && currFormField.checked) // checkbox
			creatorArray[arrayKey] = "on";
	}
	for (index = 0; index < fieldArray.length; index++)
	{
		currFormField = document.forms[0][fieldArray[index]];
		if ((currFormField.type == "checkbox") && currFormField.checked)
			styleArray[fieldArray[index]] = "on"; // checkbox
		else if (currFormField.type != "checkbox")
			styleArray[fieldArray[index]] = currFormField.value; // input and textarea
    }
// rewrite creator array
    var a_php = "";
    var total = 0;
	var key;
    for (key in creatorArray)
    {
        ++ total;
		escapeKey = escape(String(styleArray[key]).replace(/ /g, '__WIKINDX__SPACE__'));
		escapeKey = escape(String(creatorArray[key]));
		if(escapeKey.match(/%u/)) // unicode character so length changes
		{
			a_php = a_php + "s:" +
				String(key).length + ":\"" + String(key) + "\";s:" +
				escapeKey.length + ":\"" + 
				escapeKey + "\";";
		}
		else
		{
			a_php = a_php + "s:" +
                String(key).length + ":\"" + String(key) + "\";s:" +
                String(creatorArray[key]).length + ":\"" + String(creatorArray[key]) + "\";";
		}
    }
    a_php = "a:" + total + ":{" + a_php + "}";
    var creatorQuery = "&rewriteCreator=" + escape(a_php);
// style definition array
    a_php = "";
    total = 0;
	var key;
	var escapeKey;
    for (key in styleArray)
    {
        ++ total;
		escapeKey = escape(String(styleArray[key]).replace(/ /g, '__WIKINDX__SPACE__'));
		if(escapeKey.match(/%u/)) // unicode character so length changes
		{
			a_php = a_php + "s:" +
				String(key).length + ":\"" + String(key) + "\";s:" +
				escapeKey.length + ":\"" + 
				escapeKey + "\";";
		}
		else
		{
			a_php = a_php + "s:" +
				String(key).length + ":\"" + String(key) + "\";s:" +
				String(styleArray[key]).length + ":\"" + 
				String(styleArray[key]) + "\";";
		}
    }
    a_php = "a:" + total + ":{" + a_php + "}";
    url = url + "&style=" + escape(a_php);
	var templateString = document.forms[0][templateName].value; 
	url = url + creatorQuery + "&templateName=" + templateName + 
		"&templateString=" + escape(templateString);
	var popUp = window.open(url,'winMeta','height=' + h + 
		',width=' + w + ',left=10,top=10,status,scrollbars,resizable,dependent');
}


// Get browser dimensions.
// Code adapted from http://www.howtocreate.co.uk/tutorials/index.php?tut=0&part=16
function browserDimensions()
{
	if( typeof( window.innerWidth ) == 'number' )
	{
//Non-IE
		browserWidth = window.innerWidth;
		browserHeight  = window.innerHeight;
	}
	else if( document.documentElement &&
      ( document.documentElement.clientWidth || document.documentElement.clientHeight ) )
	{
//IE 6+ in 'standards compliant mode'
		browserWidth = document.documentElement.clientWidth;
		browserHeight  = document.documentElement.clientHeight;
	}
	else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) )
	{
//IE 4 compatible
		browserWidth = document.body.clientWidth;
		browserHeight  = document.body.clientHeight;
	}
}

/* ===== common JavaScript functions ===== */

// placeholder
