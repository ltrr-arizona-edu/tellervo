package corina;
import java.security.Principal;
import java.util.Iterator;

import javax.security.auth.Subject;

import com.sun.security.auth.PrincipalComparator;

/**
 * XXX: BUGS BUGS BUGS
 * http://archives.java.sun.com/cgi-bin/wa?A2=ind0307&L=jini-users&P=R3341&I=-3
 * http://archives.java.sun.com/cgi-bin/wa?A2=ind0308&L=jini-users&F=&S=&P=14578
 * http://archives.java.sun.com/cgi-bin/wa?A2=ind0307&L=jini-users&P=R3341&I=-3
 * @author Aaron Hamid
 */
public class PrincipalNameComparator implements PrincipalComparator {
  private String principal;
  public PrincipalNameComparator(String principal) {
    System.out.println("PrincipalNameComparator: " + principal);
    this.principal = principal;
  }

  public boolean implies(Subject subject) {
    Iterator it = subject.getPrincipals().iterator();
    while (it.hasNext()) {
      Principal p = (Principal) it.next();
      System.out.println("Principal found: " + p);
      if (principal.equals(p.getName())) {
        return true;
      }
    }
    return false;
  }
}