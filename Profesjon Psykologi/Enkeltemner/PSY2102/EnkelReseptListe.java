import java.util.*;

public class EnkelReseptListe implements Iterable<Resept>{

    private Node foran;
    private Node bak;

    public boolean settInn(Resept r){
	if(foran==null){
	    foran = new Node(r);
	    bak = foran;
	    return true;
	}else{
	    bak.neste = new Node(r);
	    bak = bak.neste;
	    return true;
	}
	
    }

    public Resept finnResept(int reseptNr){
	Node temp = foran;
	while(temp!=null){
	    if(temp.obj.getReseptNr() == reseptNr){
		return temp.obj;
	    }
	    temp = temp.neste;
	}
	return null;

    }

    public int finnLengde(){
	Node temp = foran;
	int antall = 0;
	while(temp!=null){
	    antall++;
	    temp = temp.neste;
	}
	return antall;
    }

    public Iterator<Resept> iterator(){
	return new MyIterator();
    }



    private class MyIterator implements Iterator<Resept>{
	int lengde = finnLengde();
	int teller;
	Node iteratorPeker = foran;

	public boolean hasNext(){
	    return (teller<=lengde);
	}
	public Resept next(){
	    Resept temp = iteratorPeker.obj;
	    teller++;
	    iteratorPeker = iteratorPeker.neste;
	    return temp;
	}
	public void remove(){}


    }

    private class Node{
	Resept obj;
	Node neste;

	Node(Resept r){
	    obj = r;
	}
    }

}
