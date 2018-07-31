package pt.ulisboa.tecnico.sise.autoinsure.datamodel;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class ClaimRecord extends ClaimItem implements Serializable{
    private static final long serialVersionUID = 8410169428857128982L;
    private final String _submissionDate;
    private final String _occurrenceDate;
    private final String _plate;
    private final String _description;
    private String _status;
    private final List<ClaimMessage> _claimMessageList;
    public static final String STATUS_PENDING 	= "pending";	//could be done with enum watching for exceptions
    public static final String STATUS_ACCEPTED 	= "accepted";	//could be done with enum watching for exceptions
    public static final String STATUS_DENIED 	= "denied";		//could be done with enum watching for exceptions

    public ClaimRecord(int claimId, String claimTitle, String submissionDate, String occurrenceDate, String plate,
                       String description, String status, List<ClaimMessage> msgList) {
        super(claimId, claimTitle);
        _submissionDate = submissionDate;
        _occurrenceDate = occurrenceDate;
        _plate = plate;
        _description = description;
        setStatus(status);
        _claimMessageList = msgList;
    }

    public ClaimRecord(int claimId, String claimTitle, String submissionDate, String occurrenceDate, String plate, String description, String status) {
        this(claimId, claimTitle, submissionDate, occurrenceDate, plate, description, status, new ArrayList<ClaimMessage>());
    }

    public String getSubmissionDate() {
        return _submissionDate;
    }

    public String getOccurrenceDate() {
        return _occurrenceDate;
    }

    public String getPlate() {
        return _plate;
    }

    public String getDescription() {
        return _description;
    }

    public String getStatus() {
        return _status;
    }
    
    public boolean setStatus(String status) {
    	if(status.equals(ClaimRecord.STATUS_PENDING) || status.equals(ClaimRecord.STATUS_ACCEPTED) || status.equals(ClaimRecord.STATUS_DENIED)) {
    		_status = status;
    		return true;
    	}
    	return false;
    }

    public List<ClaimMessage> getClaimMessageList() {
        return _claimMessageList;
    }

    public boolean addClaimMessage(ClaimMessage claimMessage) {
        return _claimMessageList.add(claimMessage);
    }

    public boolean removeClaimMessage(ClaimMessage claimMessage) {
        boolean res = false;
        while(_claimMessageList.contains(claimMessage)) {
            res =_claimMessageList.remove(claimMessage);
        }
        return res;
    }

    @Override
    public String toString() {
        return  super.toString() + ", " +
                "Submission Date: " + _submissionDate + ", " +
                "Number Plate: " + _plate + ", " +
                "Description: " + _description + ", " +
                "Messages: " + _claimMessageList + ", " +
                "Status: " + _status + ".";
    }
}
