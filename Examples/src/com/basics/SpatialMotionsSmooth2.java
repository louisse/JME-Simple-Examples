package com.basics;


import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;



public class SpatialMotionsSmooth2 extends SimpleApplication {

    public static void main(String[] args) {
        SpatialMotionsSmooth2 app = new SpatialMotionsSmooth2();
        
        //set vSinc on to get stable 60 fps
        AppSettings aps = new AppSettings(true);
        aps.setVSync(true);
        app.setSettings(aps);
        app.start();
    }

    Geometry geom;     


    Vector3f vecAim;
    Vector3f vec1;
    Vector3f vec2;     
    Vector3f vec3;  
    float angla;
    float moveMe = 0f;
    Quaternion vectry;
    Vector3f front;
    Vector3f vecB;
    Quaternion qRot=new Quaternion();
    float ang;
    float remainingDist;


    public void vectorz(){
     vec1 = new Vector3f(1,0,1);
     vec2   = new Vector3f(20,0,5);     
     vec3 = new Vector3f(5,0,15);  
   }     
    
              
    @Override
    public void simpleInitApp() {
        
        vectorz();
        
        Box b = new Box(Vector3f.ZERO, 1, 1, 1);
        geom = new Geometry("Box", b);
        geom.setLocalScale(1,1,2);
        geom.updateModelBound();

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);
        geom.setLocalTranslation(vec1);
        rootNode.attachChild(geom);
        
        flyCam.setMoveSpeed(30);
        viewPort.setBackgroundColor(ColorRGBA.Gray);
        cam.setLocation(new Vector3f(23.0f, 35.0f, 33.0f));
        cam.setRotation(new Quaternion (-0.16484f, 0.83351f, -0.40033f, -0.34323f));
        
    }


    public void transObj (float tpf, Spatial spatial, boolean boo) {
         
        //Aim to move
        if (spatial.getLocalTranslation().equals(vec1) == true) vecAim = vec2;
        if (spatial.getLocalTranslation().equals(vec2) == true) vecAim = vec3;
        if (spatial.getLocalTranslation().equals(vec3) == true) vecAim = vec1;
        
      
      //rotation  
      vectry = spatial.getWorldRotation();
      front = vectry.mult(Vector3f.UNIT_Z).normalize();
      
      vecB = spatial.getWorldTranslation().subtract(vecAim).setY(spatial.getWorldTranslation().y).normalize();
      
      qRot.lookAt(vecB, Vector3f.UNIT_Y);
      
      ang = vecB.angleBetween(front);
      
      if (ang>0.03f) { 
      angla += tpf/5f; //speed    
      vectry.slerp(qRot, angla);        
      spatial.setLocalRotation(vectry);
      }
      else {
          angla=0;
          spatial.setLocalRotation(qRot);
      }
      
      
     
      //translation
     moveMe += tpf/10f; //speed
     remainingDist = spatial.getLocalTranslation().distance(vecAim);
     
     if (remainingDist > 0.1f && !spatial.getWorldTranslation().equals(vecAim)) {
     spatial.setLocalTranslation(FastMath.interpolateLinear(moveMe, spatial.getLocalTranslation(), vecAim));
     }
     else {
         if (remainingDist <= 0.2f && !spatial.getWorldTranslation().equals(vecAim)) {
             moveMe = 0;
             spatial.setLocalTranslation(vecAim);
         }
     }
     System.out.println(moveMe);
    }
     
    
@Override
public void simpleUpdate(float tpf)
{
    
        transObj(tpf,geom,true);
        
 }

}
