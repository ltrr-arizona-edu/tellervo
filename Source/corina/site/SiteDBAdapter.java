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

package corina.site;

public abstract class SiteDBAdapter implements SiteDBListener {

    public void siteNameChanged(SiteEvent e) { }
    public void siteCodeChanged(SiteEvent e) { }
    public void siteIDChanged(SiteEvent e) { }
    public void siteCountryChanged(SiteEvent e) { }
    public void siteCommentsChanged(SiteEvent e) { }

    public void siteMoved(SiteEvent e) { }

    // -- add other types of events here
}
