package edu.bsu.cs;

public class Assignment2Record {
	public SearchTerm searchTerm;
	public String state;
	public Boolean easyApply;
	public Double latitude;
	public Double longitude;
	public Double latZScore;
	public Double longZScore;
	
	public double getdifference(Assignment2Record other, double maxLatLongDistance){
		double searchTermDifference = Math.abs(searchTerm.ordinal() - other.searchTerm.ordinal())/ (SearchTerm.values().length - 1);
		double stateDifference = (state.equalsIgnoreCase(other.state)) ? 0.0 : 1.0;
		double easyApplyDifference = (easyApply == other.easyApply) ? 0.0 : 1.0;
		double latLongDifference = calculateLatLongDifferent(other, maxLatLongDistance);
		double averageDistanceAllEqualWeight = (searchTermDifference + stateDifference + easyApplyDifference + latLongDifference ) / 4;
		return averageDistanceAllEqualWeight;
	}
	
	private double calculateLatLongDifferent(Assignment2Record other, double maxDistance) {
		double distance = calculateDistance(other.latitude, other.longitude);
		return distance / maxDistance;
	}
	
	public double calculateDistance(double otherLat, double otherLong){
		double latDifference = Math.abs(latitude - otherLat);
		double longDifference = Math.abs(longitude - otherLong);
		return Math.sqrt(Math.pow(latDifference, 2) + Math.pow(longDifference, 2));
	}
	
	public void calculateAndStoreLatZScore(double averageLat, double latStdD) {
		latZScore = (latitude - averageLat) / latStdD;
		if(Math.abs(latZScore) >= 3){
			System.out.println("Greater than 3! " + latZScore + " " + this);
		}
	}
	
	public void calculateAndStoreLongZScore(double averageLong, double longStdD) {
		longZScore = (longitude - averageLong) / longStdD;
		if(Math.abs(longZScore) >= 3){
			System.out.println("Greater than 3!"+ longZScore + " " + this);
		}
	}

	@Override
	public String toString(){
		return "SearchTerm: " + searchTerm +
				", State: " + state +
				", EasyApply: " + easyApply +
				", LatLong: (" + latitude + ", " + longitude + ")";
	}

	
}
