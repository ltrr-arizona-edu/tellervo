package edu.cornell.dendro.corina;

import java.security.BasicPermission;

/**
 * Permission class for Corina permissions.
 */
public class CorinaPermission extends BasicPermission {
  public CorinaPermission(String name) {
    super(name);
  }
}