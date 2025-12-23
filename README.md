## Assignment 2
This is a repository for project 2 of the course - Introduction to Computer Science in Java (Ariel University, 2026 - fall semester - A).

<br></br>
## Contents
### Classes
* Ex2_GUI
* Index2D
* Index2DTest
* Map
* MapTest
* StdDraw

### Interfaces
* Map2D
* Pixel2D

<br></br>
## Usage
<br></br>
### Map

 * Constructs a w * h 2D raster map with an init value v.
 * @param w The width of the map
 * @param h The height of the map
 * @param v The initial value to put in all cells

public Map(int w, int h, int v);

<br></br>
 * Constructs a square map (size*size) with values of 0.
 * @param size The size of the square map
 
public Map(int size);

<br></br>
 * Constructs a map from a given 2D array.
 * @param data The 2D int array of data to construct the map from
 
public Map(int[][] data);

<br></br>
 * Creates a new 2D int array and puts v as the initial value of each cell.
 *
 * @param w the width of the underlying 2D array.
 * @param h the height of the underlying 2D array.
 * @param v the init value of all the entries in the 2D array.
 
public void init(int w, int h, int v);

<br></br>
 * Constructs a new 2D raster map from a given 2D int array (deep copy).
 * @throws RuntimeException if arr == null or if the array is empty or a ragged 2D array.
 * @param arr a 2D int array.

public void init(int[][] arr);

<br></br>
 * Computes a deep copy of the underline 2D matrix.
 * @return a deep copy of the underline matrix.

public int[][] getMap();

<br></br>
 * @return the width of this 2D map (first coordinate).
   
public int getWidth();

<br></br>
 * @return the height of this 2D map (second coordinate).
 
public int getHeight();

<br></br>
 * @param x the x coordinate
 * @param y the y coordinate
 * @return the [x][y] (int) value of the map[x][y].
 
public int getPixel(int x, int y);

<br></br>
 * @param p the x,y coordinate
 * @return the [p.x][p.y] (int) value of the map.
 
public int getPixel(Pixel2D p);

<br></br>
 * Set the [x][y] coordinate of the map to v.
 * @param x the x coordinate
 * @param y the y coordinate
 * @param v the value that the entry at the coordinate [x][y] is set to.
 
public void setPixel(int x, int y, int v);

<br></br>
 * Set the [x][y] coordinate of the map to v.
 * @param p the coordinate in the map.
 * @param v the value that the entry at the coordinate [p.x][p.y] is set to.
 
public void setPixel(Pixel2D p, int v);

<br></br>
 * @param p the 2D coordinate.
 * @return true iff p is with in this map.
 
boolean isInside(Pixel2D p);

<br></br>
 * This method returns true if and only if this Map2D has the same dimensions as p.
 * @param p
 * @return true if and only if this Map2D has the same dimensions as p.
 
public boolean sameDimensions(Map2D p);

<br></br>
 * This method adds the map p to this map - assuming they have the same dimensions (
 * else do nothing).
 * @param p - the map that should be added to this map (just in case they have the same dimensions).
 
public void addMap2D(Map2D p);

<br></br>
 * This method multiplay this map with a scalar (casting to int).
 * @param scalar
 
public void mul(double scalar);

<br></br>
 * Creates a new map with size of width * sx , height * sy
 * and puts in each cell of the new map the value of newMap(x,y) -> oldMap(x / sx, y / sy)
 * @param sx The x scale to multiply the map width by
 * @param sy The y scale to multiply the map height by
 
public void rescale(double sx, double sy);

<br></br>
 * Loops over all the points in a square with size 2*radius around the center
 * and checks for each point if it's in the map and if its distance from the center is smaller than the radius
 * if true it sets the point to color
 * @param center The center of the circle
 * @param rad The radius of the circle (must be bigger than 0)
 * @param color - the new color to draw the circle.
 
public void drawCircle(Pixel2D center, double rad, int newColor);

<br></br>
 * This method draws a line by changing the pixels between p1 to p2 to the newColor.
 * assuming dx = |p2.x-p1.x|, dy = |p2.y-p1.y|, and both p1 and p2 are within this map.
 * Note:
 * 1. if p1 equals p2 - a single pixel will be drawn.
 * 2. assuming dx>=dy & p1.x<p2.x: dx+1 pixels will be drawn.
 * let f(x) be the linear function going throw p1&p2.
 * let x=p1.x, p1.x+1, p1.x+2...p1.x+dx (=p2.x)
 * all the pixels (x,round(f(x)) will be drawn.
 * 3. assuming dx>=dy & p1.x>p2.x: the line p2,p1 will be drawn.
 * 4. assuming dx<dy & p1.y<p2.y: dy+1 pixels will be drawn.
 * let g(y) be the linear function going throw p1&p2.
 * let y=p1.y, p1.y+1, p1.y+2...p1.y+dy (=p2.y)
 * all the pixels (y,round(g(y)) will be drawn.
 * 5. assuming dy>dx & p1.y>p2.y: the line p2,p1 will be drawn.
 * @param p1
 * @param p2
 * @param newColor - the (new) color to be used in the drawing.
 
public void drawLine(Pixel2D p1, Pixel2D p2, int newColor);

<br></br>
 * This method draws a rectangle by changing all the pixels in this map
 * which are within the [p1,p2] range to color.
 * @param p1
 * @param p2
 * @param newColor - the (new) color to be used in the drawing.
 
public void drawRect(Pixel2D p1, Pixel2D p2, int newColor);

<br></br>
 * @param m the reference object with which to compare.
 * @return true if and only if this Map2D has the same (dimensions) and values.
 
public boolean equals(Object m);

///////////////// Algorithms //////////////////
<br></br>
 * Fill the connected component of p in the new color (new_v).
 * Note: the connected component of p are all the pixels in the map with the same "color" of map[p] which are connected to p.
 * Note: two pixels (p1,p2) are connected if there is a path between p1 and p2 with the same color (of p1 and p2).
 * @param p the pixel to start from.
 * @param new_v the new "color" to be filled in p's connected component.
 * @param cyclic if true --> the matrix is assumed to be cyclic.
 * @return the number of "filled" pixels.

public int fill(Pixel2D p, int new_v, boolean cyclic);

<br></br>
 * Compute the shortest valid path between p1 and p2.
 * A valid path between p1 and p2 is defined as a path between p1 and p2 does NOT contain the absColor.
 * A path is an ordered set of pixels where each consecutive pixels in the path are neighbors in this map.
 * Two pixels are neighbors in the map, iff they are a single pixel apart (up,down, left, right).
 * In case there is no valid path between p1 and p2 should return null;
 * If this map is cyclic:
 * 1. the pixel to the left of (0,i) is (getWidth()-1,i).
 * 2. the pixel to the right of (getWidth()-1,i) is (0,i).
 * 3. the pixel above (j,getHeight()-1) is (j,0).
 * 4. the pixel below (j,0) is (j,getHeight()-1).
 * Where 0<=i<getWidth(), 0<=j<getWidth().
 *
 * @param p1 first coordinate (start point).
 * @param p2 second coordinate (end point).
 * @param obsColor the color which is addressed as an obstacle.
 * @return the shortest path as an array of consecutive pixels, if none - returns null.
 
public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor, boolean cyclic);

<br></br>
* Creates a new map for the distances, starts from the start cell and loops through the explored queue,
* for each cell in ExQ put its distance from start in the new map and search for any valid neighbor.
* remove the current cell and add the neighbors to the ExQ and move to the next cell until ExQ is empty.
*
* @param start the source (starting) point
* @param obsColor the color representing obstacles
* @param cyclic If the map is cyclic
* @return A map containing the distance of each tile from the start pos
 
public Map2D allDistance(Pixel2D start, int obsColor, boolean cyclic);

<br></br>
### Ex2_GUI

* Draws the map and interface with StdDraw

public static void drawMap()

<br></br>
* Loads a map from a txt file
* @param mapFileName The file to load from
* @return A new map object containing the map from the file

public static Map2D loadMap(String mapFileName) throws FileNotFoundException

<br></br>
* Saves the map into a txt file
* @param map The map to save
* @param mapFileName The name of the file to save into

public static void saveMap(Map2D map, String mapFileName)

<br></br>
public static void main(String[] a)
<br></br>
#### After running Ex2_GUI
<br></br>
  <img width="514" height="568" alt="image" src="https://github.com/user-attachments/assets/10a2f493-e7aa-45a1-9754-fe4ffc60a70e" />
  <br></br>
  this image shows the first screen of the program. you can move between screens with '1-4'. In this screen you are the blue circle and you are running from the red circle, which is using the shortest path algorithm to find you.
  <br></br>
  <img width="513" height="567" alt="image" src="https://github.com/user-attachments/assets/755951ef-7115-45c1-b9ee-dcd0ed498c8b" />
  <br></br>
  this image shows the second screen. In this screen you are the blue circle and you are running from the red circle, which is using the shortest path algorithm to find you. in this screen you also have an obstacle in the center.
  <br></br>
  <img width="514" height="566" alt="image" src="https://github.com/user-attachments/assets/6853ffdc-b865-477f-8c4f-86b669e33e63" />
  <br></br>
  this image shows the third screen. In this screen you are the blue circle and you are running from the red circle, which is using the shortest path algorithm to find you. in this screen you are inside a maze and you have yellow points to collect.
  <br></br>
  <img width="510" height="564" alt="image" src="https://github.com/user-attachments/assets/fd9701c1-082a-45db-8d01-52054b3442a5" />
  <br></br>
  this image shows the fourth screen. In this screen you are the blue circle and you are running from the red circle, which is using the shortest path algorithm to find you. in this screen the map is cyclic.

