import sheffield.*;
 public class Trial{
 	 public static void main(String [] args){
 	 	 EasyReader fileInput = new EasyReader("planets.txt") ;
 	 	 String a =fileInput. readString();
 	 	 String b = a. substring(2,6) ;
 	 	 String c =fileInput. readString();
 	 	 String d = c. substring(2,6) ;
 	 	 System.out.println(b+d);
 	 }
 }
 	 	 
