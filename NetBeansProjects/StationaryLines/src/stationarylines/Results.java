package stationarylines;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author PerDaniel
 */
public class Results {

    ArrayList<Integer> lengthOfLine = new ArrayList<>();
    ArrayList<Integer> timeMillis = new ArrayList<>();
    ArrayList<Integer> actualTimeMillis = new ArrayList<>();
    ArrayList<Integer> heightPlacement = new ArrayList<>();
    ArrayList<Integer> widthPlacement = new ArrayList<>();
    int participantAge;
    int userID;
    boolean participantMale;

    public void addLengthOfLine(int index, int length){
	lengthOfLine.add(index, length);
    }

    public void addActualTimeMillis(int index, int time){
	actualTimeMillis.add(index, time);
    }

    public void addTimeMillis(int index, int time){
	timeMillis.add(index, time);
    }

    public void setParticipantAge(int age){
	participantAge = age;
    }

    public void addHeightPlacement(int index, int height){
	heightPlacement.add(index, height);
    }

    public void addWidthPlacement(int index, int width){
	widthPlacement.add(index, width);
    }

    public void setParticipantMale(boolean male){
	participantMale = male;
    }

    public void writeResultsToFile(String userID) throws FileNotFoundException{
	PrintWriter writer = new PrintWriter(userID+".txt");

	writer.println("UserID: " +userID);
	writer.println("LengthOfLine|WidthPlacement|HeightPlacement|ActualTime|GuessedTime");

	for(int i=0; i<timeMillis.size(); i++){
	    writer.println(lengthOfLine.get(i) + "|" + widthPlacement.get(i) + "|" + heightPlacement.get(i) + "|" + actualTimeMillis.get(i) + "|" + timeMillis.get(i));
	    System.out.println(lengthOfLine.get(i) + "|" + widthPlacement.get(i) + "|" + heightPlacement.get(i) + "|" + actualTimeMillis.get(i) + "|" + timeMillis.get(i));
	}
	writer.close();
    }
}
