/*
 * ERelementList.java
 * it is part of the ccistarml Java Package
 * version 0.6
 * Created on July 4 of 2007, By Carlos Cares
 * Updated to v0.6.1  on September 20 of 2007 By Carlos Cares
 */

package istarml;
import java.util.Iterator;
import java.util.LinkedList;

public class ERelementList {

    private LinkedList previousRef = new LinkedList();
    private LinkedList element = new LinkedList();
    public String currentDiagram;
    public String istarml_ID;

    public ERelementList(ccistarmlContent st) {
        this.addpreviousRef(st);
        this.loadXmlContent(st);
    }
    
    public void display(){
        Iterator it = this.element.iterator();
        ERelement e;
        while (it.hasNext()) {
            e = (ERelement)it.next();
            e.display();
        }
    }
    
    public ERelement getElementFromRef(String ref) {
        Iterator it = this.element.iterator();
        ERelement e;
        while (it.hasNext()) {
            e = (ERelement)it.next();
            
            if (e.ID.equalsIgnoreCase(ref))
            	return e;
        }
        
        return null;
    }
    
    public boolean containsRef(String w){
        return previousRef.contains(w);
    }
    
    public void addpreviousRef(ccistarmlContent st) {
        if (st.hasAttribute("id"))
            this.previousRef.add(st.attribute.get("id"));
        Iterator it = st.content.iterator();
        while (it.hasNext()) this.addpreviousRef((ccistarmlContent)it.next());
    }
    
    public void loadXmlContent(ccistarmlContent st){
        st.loadToERelement(this);
    }
    
    public boolean add(ERelement er) {
        return element.add(er);
    }
    
    public LinkedList list(){
        LinkedList r = new LinkedList(this.element);
        return r;
    }
    
}
