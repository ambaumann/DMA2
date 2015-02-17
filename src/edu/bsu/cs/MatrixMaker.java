package edu.bsu.cs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class MatrixMaker {

	public static void main(String[] args) {
		doAssignment2();

	}
	
	private static void doAssignment2(){
		//read in file.
		ArrayList<Assignment2Record> records = new ArrayList<Assignment2Record>();
		File file = new File("data.csv");
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			while(br.ready()){
				String line = br.readLine();
				Assignment2Record record = adaptFileToRecord(line);
				records.add(record);
			}
			br.close();
			
			
			
			
			//second
			//count search frequencies
			for(SearchTerm term : SearchTerm.values()){
				int count = 0;
				for(Assignment2Record record : records){
					if(record.searchTerm.equals(term)){
						count++;
					}
				}
				System.out.println(term + " count " + count);
			}
			//count number of states and frequencies
			HashMap<String, Integer> statesToCount = new HashMap<String, Integer>();
			for(Assignment2Record record : records){
				if(statesToCount.containsKey(record.state)){
					statesToCount.put(record.state, statesToCount.get(record.state) + 1);
				} else{
					statesToCount.put(record.state, 1);
				}
			}
			for(Entry<String, Integer> entry : statesToCount.entrySet()){
				System.out.println("Key " + entry.getKey() + ", Value " + entry.getValue());
			}
			//find number of easy apply and number of non easy apply
			int easyApplyTrue = 0;
			int easyApplyFalse = 0;
			
			for(Assignment2Record record : records){
				if(record.easyApply){
					easyApplyTrue++;
				}else{
					easyApplyFalse++;
				}
			}
			System.out.println("Easy Apply True: " + easyApplyTrue);
			System.out.println("Easy Apply False: " + easyApplyFalse);
			
			//find standard deviation of group and then find z score of each item
			double sumLat = 0.0;
			double sumLong = 0.0;
			for(Assignment2Record record : records){
				sumLat += record.latitude;
				sumLong += record.longitude;
			}
			double averageLat = sumLat / records.size();
			double averageLong = sumLong / records.size();
			double sumOfOffMeansLat = 0.0;
			double sumOfOffMeansLong = 0.0;
			
			for(Assignment2Record record : records){
				sumOfOffMeansLat += (Math.pow(record.latitude, 2) - Math.pow(averageLat, 2));
				sumOfOffMeansLong += (Math.pow(record.longitude, 2) - Math.pow(averageLong, 2));
			}
			double latStdD = Math.sqrt(sumOfOffMeansLat / records.size());
			double longStdD = Math.sqrt(sumOfOffMeansLong / records.size());
			System.out.println("Lat Std D: " + latStdD);
			System.out.println("Long Std D: " + longStdD);
			
			for(Assignment2Record record : records){
				record.calculateAndStoreLatZScore(averageLat, latStdD);
				record.calculateAndStoreLongZScore(averageLong, longStdD);
			}
			
			//output 
			
			//third
			double maxLatLongDistance = findMaxLatLongDistance(records);
			double[][] matrix = new double[records.size()][records.size()];
			File writeFile = new File("TestMatrix.csv");
			writeFile.createNewFile();
			FileWriter fw = new FileWriter(writeFile);
			
			BufferedWriter bw = new BufferedWriter(fw);
			for(int i = 0; i < records.size(); i++){
				for(int k = 0; k < records.size(); k++){
					Double distance = records.get(i).getdifference(records.get(k), maxLatLongDistance);
					bw.write(distance.toString());
					if(k != records.size()-1){
						bw.write(", ");
					}else{
						bw.newLine();
					}
				}
			}
			
			bw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static Assignment2Record adaptFileToRecord(String line){
		String[] attributes = line.split(",");
		Assignment2Record record = new Assignment2Record();
		record.searchTerm = SearchTerm.valueOf(attributes[0].toUpperCase());
		record.state = attributes[1];
		record.easyApply = Boolean.valueOf(attributes[2]);
		record.latitude = Double.parseDouble(attributes[3]);
		record.longitude = Double.parseDouble(attributes[4]);
		return record;
	}
	
	private static double findMaxLatLongDistance(ArrayList<Assignment2Record> records){
		double maxDistance = 0.0;
		for(int i = 0; i < records.size(); i++){
			for(int k = i; k < records.size(); k++){
				Assignment2Record aRecord = records.get(i);
				Assignment2Record bRecord = records.get(k);
				double distance = aRecord.calculateDistance(bRecord.latitude, bRecord.longitude);
				if(distance > maxDistance){
					maxDistance = distance;
				}
			}
		}
		return maxDistance;
	}
}
