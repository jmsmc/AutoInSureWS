package pt.ulisboa.tecnico.sise.autoinsure.datamodel;

import java.io.Serializable;

public class ClaimMessage  implements Serializable{
	private static final long serialVersionUID = 1952214447730841539L;
	private final String _sender;
	private final String _message;
    private final String _date;

    public ClaimMessage(String sender, String message, String date) {
        _sender = sender;
        _message = message;
        _date = date;
    }

    public String getMessage() {
        return _message;
    }

    public String getDate() {
        return _date;
    }
    
    public String getSender() {
        return _sender;
    }

    
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ClaimMessage)) {
			return false;
		}
		ClaimMessage other = (ClaimMessage) obj;
		if (_date == null) {
			if (other._date != null) {
				return false;
			}
		} else if (!_date.equals(other._date)) {
			return false;
		}
		if (_message == null) {
			if (other._message != null) {
				return false;
			}
		} else if (!_message.equals(other._message)) {
			return false;
		}
		if (_sender == null) {
			if (other._sender != null) {
				return false;
			}
		} else if (!_sender.equals(other._sender)) {
			return false;
		}
		return true;
	}

	@Override
    public String toString() {
        return  "Sender: " + _sender + ", " +
                "Message: " + _message + ", " +
                "Date: " + _date + ".";
    }   
}
