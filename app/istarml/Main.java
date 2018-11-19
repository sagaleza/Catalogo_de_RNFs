/*
 * Main.java
 * Sample of using ccistarml Java Package
 *
 * it is part of the ccistarml Java Package
 * version 0.6
 * Created on July 4 of 2007, By Carlos Cares
 * Updated to v0.6.1  on September 20 of 2007 By Carlos Cares
 */
package istarml;

public class Main {
    
    public Main() {
    }
    
    public static void main(String[] args) {
		ERelementList erL;
		ERelement erle;
		ccistarmlFile f = new ccistarmlFile ();
		f.loadFile("sample.istarml"); // put your file name here !
		f.xmlParser();
		f.istarmlParser();
		System.out.println("Cantidad de errores: "+f.errors());
		f.displayErrors();
    }
}
    

