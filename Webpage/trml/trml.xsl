<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
  <html>
  <head>
    <title><xsl:value-of select="treerings/metadata/title"/></title>
  </head>
  <body>
    <h1><xsl:value-of select="treerings/metadata/title"/></h1>

    <h2>Metadata</h2>
    <table border="0" width="100%">
      <tr>
        <th align="left">Key</th><th align="left">Value</th>
      </tr>
      <tr>
        <td align="left">Title</td><td><xsl:value-of select="treerings/metadata/title"/></td>
      </tr>
    </table>

<!--
    <table border="1">
    <tr bgcolor="#9acd32">
      <th align="left">Title</th>
      <th align="left">Artist</th>
    </tr>
    <xsl:for-each select="data/v">
    <tr>
      <td><xsl:value-of select="title"/></td>
      <td><xsl:value-of select="artist"/></td>
    </tr>
    </xsl:for-each>
    </table>

    <xsl:template match="elements">
      <h2>Elements</h2>
    </xsl:template>
-->

  </body>
  </html>
</xsl:template>

</xsl:stylesheet>
