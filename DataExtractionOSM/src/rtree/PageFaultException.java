//PageFaultException.java
//  
//This library is free software; you can redistribute it and/or
//modify it under the terms of the GNU Lesser General Public
//License as published by the Free Software Foundation; either
//version 2.1 of the License, or (at your option) any later version.
//  
//This library is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//Lesser General Public License for more details.
package rtree;

/**When a page fault occurs in the CachedNodes class
   @author Prachuryya Barua
*/
class PageFaultException extends Exception
{
  PageFaultException(String msg)
  {
    super(msg);
  }
}
