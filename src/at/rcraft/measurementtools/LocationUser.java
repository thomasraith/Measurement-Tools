/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.rcraft.measurementtools;

import org.bukkit.Location;

/**
 *
 * @author Thomas Raith
 */
public class LocationUser {
      protected String user;
      protected int x1;
      protected int y1;
      protected int z1;
      protected int x2;
      protected int y2;
      protected int z2;
      protected Location l1;
      protected Location l2;
      protected Location lc;
      
      
  LocationUser()       
  {
      String user = new String();
  }
  
  LocationUser(String User)       
  {
      String user = new String();
      this.user = User;
  }
 
  public String getUser()
  {
     return user;
  }
 
  public void setLocation1(int x, int y, int z, Location l)
  {
      this.x1 = x;
      this.y1 = y;
      this.z1 = z;
      this.l1 = l;
  }
  
    public void setLocation2(int x, int y, int z, Location l)
  {
      this.x2 = x;
      this.y2 = y;
      this.z2 = z;
      this.l2 = l;
  }


}
