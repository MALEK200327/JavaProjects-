import sheffield.*;
 public class Trial2{
 	 public static void main(String [] args){
 	 	 EasyReader fileInput = new EasyReader("planets.txt") ;
         String a =fileInput. readString();
         String b = a.substring(14,a. indexOf("g"));
         String c = fileInput. readString();
         String d = c.substring(0, c. indexOf("is","g")) ;
         System.out.println(b);
         System.out.println(d);
 	 }
 }
 	 	 
