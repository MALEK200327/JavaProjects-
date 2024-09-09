import sheffield.*;
public class Train {
public static void main(String[] args) { 
EasyGraphics g = new EasyGraphics(400,300); 
g.drawRectangle(60,150,50,80); // funnel 
g.setColor(128,180,245); 
g.fillRectangle(50,50,200,100); // boiler 
g.setColor(0,0,100); 
g.fillRectangle(250,50,100,150); // cabin 
g.setColor(200,0,0); 
g.fillEllipse(80,20,80,80); // left wheel 
g.fillEllipse(220,20,80,80); // right wheel 
g.setColor(220,220,220); 
g.fillEllipse(120,240,50,20); // small puff 
g.fillEllipse(180,260,100,30); // big puff
} 
}