
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.acos;
import static java.lang.Math.asin;
import static java.lang.Math.atan;
import static java.lang.Math.sqrt;
import static java.lang.Math.PI;

public class Vector {
    private float x;
    private float y;
    Vector(float x, float y){
        this.x = x;
        this.y = y;
    }
    
// Setters
    public void setX(float x) {
        this.x = x;
    }
    public void setY(float y) {
        this.y = y;
    }
    public void setXY(float x, float y){
        this.x = x;
        this.y = y;
    }
    public void setMag(double magnitude){
        normalize();
        mult((float)magnitude);
    }
    
// Change Vector
    public void add(Vector v){
        x += v.x;
        y += v.y;
    }
    public void sub(Vector v){
        x -= v.x;
        y -= v.y;
    }
    public void mult(float factor){
        x = x * factor;
        y = y * factor;
    }
    public void normalize(){
        if (mag() != 0){
            double tempMag = mag();
            x = (float) (x / tempMag);
            y = (float) (y / tempMag);
        }
    }
    public void rotate(double radians){
        double cos = Math.cos(radians);
        double sin = Math.sin(-radians);
        double rx = x * cos - y * sin;
        y = (float) (x * sin + y * cos);
        x = (float) rx;
    }
    public void rotateDeg(double degrees){
        rotate(degrees*PI/180);
    }
    
// Get something
    public double mag(){
        float sm = (x * x) + (y * y);
        return sqrt(sm);
    }
    public double dot(Vector v){
        return (x * v.x) + (y * v.y);
    }
    public double dist(Vector v){
        float dx = v.x - x;
        float dy = v.y - y;
        return sqrt((dx * dx) + (dy * dy));
    }
    public double getRadians(){
        double r = atan(y/x);
        if (!(acos(x/mag())/PI<=0.5 && asin(y/mag())/PI<=0.5)){
            r+=PI;
        }
        if (r<0){
            r+=2*PI;
        }
        return r;
    }
    
// Getters
    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
    public Vector copy(){
        return new Vector(x, y);
    }
    
    // Static functions
    public static Vector add(Vector v1, Vector v2) {
        return new Vector(v1.getX() + v2.getX(), v1.getY() + v2.getY());
    }
    public static Vector sub(Vector v1, Vector v2){
        v1.x -= v2.x;
        v1.y -= v2.y;
        return v1;
    }
    public static double dot(Vector v1, Vector v2){
        return (v1.x * v2.x) + (v1.y * v2.y);
    }
    public static Vector mult(Vector v, float factor){
        v.setXY(v.x * factor, v.y * factor);
        return v;
    }
    public static Vector diff(Vector v1, Vector v2){ 
        return new Vector(v2.getX() - v1.getX(), v2.getY() - v1.getY());
    }
}
