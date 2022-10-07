package com.dimka228.asteroids.physics.collusion;

import java.util.ArrayList;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.dimka228.asteroids.physics.RigidBody;
import com.dimka228.asteroids.utils.CollisionUtils;
import com.dimka228.asteroids.utils.VectorUtils;
import static com.dimka228.asteroids.utils.VectorUtils.*;
import static java.lang.Math.*;
public class GJK2D{
  
    public RigidBody bodyA;
    public RigidBody bodyB;
    
    ArrayList<Vector2> simplex;
    
    public boolean collided = false;
    
    Vector2 direction = new Vector2();
    
    // The last index is always the last added point (A)
    
    public GJK2D(RigidBody A, RigidBody B){
      this.bodyA = A;
      this.bodyB = B;
      
      simplex = new ArrayList<Vector2>();
    }
    

    boolean addSupport(Vector2 direction){
        Vector2 support = sub(getSupport(bodyA.getShape(),bodyA.getVelocity()), getSupport(bodyB.getShape(),neg(bodyB.getVelocity())));
        simplex.add(support);
        return dot(support, direction) >= 0;
    }/*/
    boolean addSupport(Polygon p, Vector2 direction){
      Vector2 support = sub(getSupport(p,direction), getSupport(p,direction.cpy().rotateDeg(180)));
      simplex.add(support);
      return VectorUtils.dot(support, direction) >= 0;
    }*/
    
    
    
    // Return 0 means still evolving
    // Return 1 means no intersection
    // Return 2 means found intersecion
  
    int evolveSimplex(){
      //println(simplex.size());
      switch(simplex.size()){
        case 0:{
          direction = sub(bodyB.getPosition(), bodyA.getPosition());
          break;
        }
        case 1:{
          direction = sub(bodyA.getPosition(), bodyB.getPosition()); // Flip direction
          break;
        }
        case 2:{
          // Add the last vertex to the simplex
          Vector2 b = simplex.get(1);
          Vector2 c = simplex.get(0);
          // Vector2 of the first two verts
          Vector2 cb = sub(b, c);
          // Vector2 from the first vert to the origin
          Vector2 co = mult(c, -1);
          
          // Calculate direction perpendicular to b and c in the dir of the origin
          direction = tripleProduct(cb, co, cb);
          break;
        }
        case 3:{
          // When the simplex is full
          // Determine if the simplex contains the origin
          // If not, remove te last vertex, and add a new one
          
          Vector2 a = simplex.get(2);
          Vector2 b = simplex.get(1);
          Vector2 c = simplex.get(0);
          
          Vector2 ao = mult(a, -1);
          Vector2 ab = sub(b, a);
          Vector2 ac = sub(c, a);
          
          //Normal of ab in the dir of the origin
          Vector2 abPerp = tripleProduct(ac, ab, ab);
          Vector2 acPerp = tripleProduct(ab, ac, ac);
          
          if(abPerp.dot(ao) > 0){
            simplex.remove(c);
            direction = abPerp;
          }else if(acPerp.dot(ao) > 0){
            simplex.remove(b);
            direction = acPerp;
          }else{
            collided = true;
            return 2;
          }
          break;
          
        }
      
      }
      boolean isPastOrigin = addSupport(direction);
      if(isPastOrigin) return 0;
      else return 1;
      
  
    }
    
    boolean detect(){
      int result = 0;
      while(result == 0){
        result = evolveSimplex();
      }
      //println(result);
      return result == 2;
    }
    
    Edge findClosestEdge(){
      float closestDistance = 999999;
      Vector2 closestNormal = new Vector2();
      int closestIndex = 0;
      Vector2 edge = new Vector2();
      
      for(int i = 0; i < simplex.size(); i++){
        int j = (i + 1) % simplex.size(); // index of the next vertex
        
        edge = sub(simplex.get(j), simplex.get(i));
        
        Vector2 normal = new Vector2(-edge.y, edge.x);
        normal = normalize(normal);
        
        float distance = normal.dot(simplex.get(i));
        
        if(distance < closestDistance){
          closestDistance = distance;
          closestNormal = normal;
          closestIndex = j;
        }
      }
      return new Edge(closestDistance, closestNormal, closestIndex);
    }
    
    public Vector2 intersect(){
      //getContactManifold();
      if(!detect()){
        return null;
      }
      
      Vector2 intersection = new Vector2();
      for(int i = 0; i < 32; i++){
        Edge edge = findClosestEdge();
        Vector2 support = sub(getSupport(bodyA.getShape(), edge.normal), getSupport(bodyB.getShape(),neg(edge.normal)));
        float distance = dot(support, edge.normal);
        
        intersection = edge.normal.cpy();
        intersection= mult(intersection,distance);
        
        if(abs(distance - edge.distance) <= 0.0000001){
          return intersection;
        }else{
          simplex.add(edge.index, support);
        }
      }
      return intersection;
      
    }
    
    int getSupportIndex(Polygon p, Vector2 direction, Vector2 supportVertex){
        float furthestDistance = -999999;
        int furthestVertexIndex = -1;
        
        for(int i = 0; i < p.getVertexCount(); i++){
          float distance = dot(new Vector2(p.getTransformedVertices()[i*2], p.getTransformedVertices()[i*2+1]), direction);
          if(distance > furthestDistance){
            furthestDistance = distance;
            supportVertex.x = p.getTransformedVertices()[i*2];
            supportVertex.y = p.getTransformedVertices()[i*2+1];
            furthestVertexIndex = i;
          }
        }
        
        return furthestVertexIndex;
      }

    
    Vector2 getVertex(Polygon p, int i){
        return new Vector2(p.getTransformedVertices()[i*2], p.getTransformedVertices()[i*2+1]);
    }
    // Calculates the contact manifold
    // This assumes a collision was found
    ArrayList<Vector2> getContactManifold(){
      
      // First get the edges which could actually be in isct
      Vector2 closestVertexA = new Vector2();
      Vector2 closestVertexB = new Vector2();
      
      Vector2 dir = normalize(sub(bodyB.getPosition(), bodyA.getPosition()));
      int closestVertexIndexA = getSupportIndex(bodyA.getShape(),dir, closestVertexA);
      int closestVertexIndexB = getSupportIndex(bodyB.getShape(),mult(dir, -1), closestVertexB);
  
      // Get the indexes off the vertices we need
      int iA1 = closestVertexIndexA;
      int iA0 = (iA1 == 0) ? bodyA.getShape().getVertexCount() - 1 : iA1 - 1;
      int iA2 = (iA1 == bodyA.getShape().getVertexCount() - 1) ? 0 : iA1 + 1;
      
      int iB1 = closestVertexIndexB;
      int iB0 = (iB1 == 0) ? bodyB.getShape().getVertexCount() - 1 : iB1 - 1;
      int iB2 = (iB1 == bodyB.getShape().getVertexCount() - 1) ? 0 : iB1 + 1;
     
      
      // Now do intersection test between each of the lines
      ArrayList<Vector2> contactPoints = new ArrayList<Vector2>();
      // A01 vs B01
      Vector2 point0 = lineLine(getVertex(bodyA.getShape(),iA1), getVertex(bodyA.getShape(),iA0), getVertex(bodyB.getShape(),iB1), getVertex(bodyB.getShape(),iB0));
      // A01 vs B21
      //Vector2 point1 = lineLine(shapeA.worldVertices[iA1], shapeA.worldVertices[iA0], shapeB.worldVertices[iB1], shapeB.worldVertices[iB2]);
      Vector2 point1 = lineLine(getVertex(bodyA.getShape(),iA1), getVertex(bodyA.getShape(),iA0), getVertex(bodyB.getShape(),iB1), getVertex(bodyB.getShape(),iB2));
      // A21 vs B01
      //Vector2 point2 = lineLine(shapeA.worldVertices[iA1], shapeA.worldVertices[iA2], shapeB.worldVertices[iB1], shapeB.worldVertices[iB0]);
      Vector2 point2 = lineLine(getVertex(bodyA.getShape(),iA1), getVertex(bodyA.getShape(),iA2), getVertex(bodyB.getShape(),iB1), getVertex(bodyB.getShape(),iB0));
     
      // A21 vs B21
      //Vector2 point3 = lineLine(shapeA.worldVertices[iA1], shapeA.worldVertices[iA2], shapeB.worldVertices[iB1], shapeB.worldVertices[iB2]);
      Vector2 point3 = lineLine(getVertex(bodyA.getShape(),iA1), getVertex(bodyA.getShape(),iA2), getVertex(bodyB.getShape(),iB1), getVertex(bodyB.getShape(),iB2));
     
      // Check if there was a isct for each test
      if (point0 != null) contactPoints.add(point0);
      if (point1 != null) contactPoints.add(point1);
      if (point2 != null) contactPoints.add(point2);
      if (point3 != null) contactPoints.add(point3);
      
      /*for(Vector2 cp : contactPoints){
        fill(0, 0, 200);
        noStroke();
        ellipse(cp.x, cp.y, 12, 12);
      }*/
      
      return contactPoints;
    }
    
    // This assumes a collision was found
    public Vector2 getContactPoint(Vector2 intersection){
      // This uses a trick
      // It moves the polygons 90% away from each other
      // Then it calculates the cps and averages them to one point
      
      Vector2 partialIntersection = mult(intersection, 0.99f);
      
      // First get the edges which could actually be in isct
      Vector2 closestVertexA = new Vector2();
      Vector2 closestVertexB = new Vector2();
      
      Vector2 dir = normalize(sub(bodyB.getPosition(), bodyA.getPosition()));
      int closestVertexIndexA = getSupportIndex(bodyA.getShape(),dir, closestVertexA);
      int closestVertexIndexB = getSupportIndex(bodyB.getShape(),mult(dir, -1), closestVertexB);
  
      // Get the indexes off the vertices we need
      int iA1 = closestVertexIndexA;
      int iA0 = (iA1 == 0) ? bodyA.getShape().getVertexCount() - 1 : iA1 - 1;
      int iA2 = (iA1 == bodyA.getShape().getVertexCount() - 1) ? 0 : iA1 + 1;
      
      int iB1 = closestVertexIndexB;
      int iB0 = (iB1 == 0) ? bodyB.getShape().getVertexCount() - 1 : iB1 - 1;
      int iB2 = (iB1 == bodyB.getShape().getVertexCount() - 1) ? 0 : iB1 + 1;
      
      // All of these values are translated by 90% of the intersection Vector2
      Vector2 A0translated = add(getVertex(bodyA.getShape(), iA0), partialIntersection);
      Vector2 A1translated = add(getVertex(bodyA.getShape(), iA1), partialIntersection);//add(shapeA.worldVertices[iA1], partialIntersection);
      Vector2 A2translated = add(getVertex(bodyA.getShape(), iA2), partialIntersection);//add(shapeA.worldVertices[iA2], partialIntersection);
      
      Vector2 B0translated = sub(getVertex(bodyB.getShape(), iB0), partialIntersection);//sub(shapeB.worldVertices[iB0], partialIntersection);
      Vector2 B1translated = sub(getVertex(bodyB.getShape(), iB1), partialIntersection);//sub(shapeB.worldVertices[iB1], partialIntersection);
      Vector2 B2translated = sub(getVertex(bodyB.getShape(), iB2), partialIntersection);//sub(shapeB.worldVertices[iB2], partialIntersection);
      
      // Now do intersection test between each of the lines
      ArrayList<Vector2> contactPoints = new ArrayList<Vector2>();
      // A01 vs B01
      Vector2 point0 = lineLine(A1translated, A0translated, B1translated, B0translated);
      // A01 vs B21
      Vector2 point1 = lineLine(A1translated, A0translated, B1translated, B2translated);
      // A21 vs B01
      Vector2 point2 = lineLine(A1translated, A2translated, B1translated, B0translated);
      // A21 vs B21
      Vector2 point3 = lineLine(A1translated, A2translated, B1translated, B2translated);
      
      // Check if there was a isct for each test
      if (point0 != null) contactPoints.add(point0);
      if (point1 != null) contactPoints.add(point1);
      if (point2 != null) contactPoints.add(point2);
      if (point3 != null) contactPoints.add(point3);
      
      //for(Vector2 cp : contactPoints){
      //  fill(0, 0, 200);
      //  noStroke();
      //  ellipse(cp.x, cp.y, 12, 12);
      //}
      Vector2 cp = null;
      if(contactPoints.size() == 2){
        System.out.println("cp = 2");
        // Calculate the average
        cp = new Vector2();
        cp.x = (contactPoints.get(0).x + contactPoints.get(1).x) / 2;
        cp.y = (contactPoints.get(0).y + contactPoints.get(1).y) / 2;
      }else if (contactPoints.size() ==  1){
        System.out.println("cp = 1");
        cp = contactPoints.get(0);
      }else{
        System.out.println("cp = 0");
      }
       
      return cp;
    }
    
    Vector2 lineLine(Vector2 a, Vector2 b, Vector2 c, Vector2 d){
      return lineLine(a.x, a.y, b.x, b.y, c.x, c.y, d.x, d.y);
    }
    
    Vector2 lineLine(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4){
      // All formulas can be found on
      // https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection
      float noemer = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
    
      // If denominator == 0 then the lines run parrallel.
      if(noemer == 0){
        return null;
      }
      
      // Calculate how far we are along the first line segment
      float t = ((x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4)) / noemer;
      // Calculate how far along the second line we are
      float u = -((x1 - x2) * (y1 - y3) - (y1 - y2) * (x1 - x3)) / noemer;
      
      // Now comparing for 1 line seg and one infinite line
      // If you want to compare two line segs,
      // Add "&& u < 1" to the if statement
      if(t > 0 && t < 1 && u > 0 && u < 1){
        Vector2 point = new Vector2(x1 + (t * (x2 - x1)), y1 + (t * (y2 - y1)));
        return point;
      }else{
        return null;
      }
    }
  }
  
// Class for the closest Edge
class Edge{
  float distance;
  Vector2 normal;
  int index;
  
  Edge(float cd, Vector2 cn, int ci){
    distance = cd;
    normal = cn;
    index = ci;
  }
}