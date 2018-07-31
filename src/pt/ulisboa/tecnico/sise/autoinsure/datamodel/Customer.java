package pt.ulisboa.tecnico.sise.autoinsure.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Customer extends Person implements Serializable {
    private static final long serialVersionUID = 4895739450784438738L;
    private String _username;
    private String _password;
    private int _policyNumber;
    private int _sessionId = -1;
    private final List<ClaimRecord> _claimList;
    private final List<String> _plateList;

    public Customer(String username, String password, int sessionId, int policyNumber,
                    Person person, List<ClaimRecord> claimList, List<String> plateList) {
        super(person);
        _username = username;
        _password = password;
        _sessionId = sessionId;
        _policyNumber = policyNumber;
        _claimList = claimList;
        _plateList = plateList;
    }

    public Customer(String username, String password, int policyNumber,
                    Person person, List<ClaimRecord> claimList, List<String> plateList) {
        this(username, password, -1, policyNumber, person, claimList, plateList);
    }

    public Customer(String username, String password, int policyNumber, Person person) {
        this(username, password, -1, policyNumber, person, new ArrayList<ClaimRecord>(), new ArrayList<String>());
    }

    public Customer(String username, int sessionId, int policyNumber, Person person) {
        this(username, null, sessionId, policyNumber, person, new ArrayList<ClaimRecord>(), new ArrayList<String>());
    }

    public Customer(int policyNumber, Person person, List<ClaimRecord> claimList, List<String> plateList) {
        this(null, null, -1, policyNumber, person, claimList, plateList);
    }

    public Customer(int sessionId, int policyNumber, Person person) {
        this(null, null, sessionId, policyNumber, person, new ArrayList<ClaimRecord>(), new ArrayList<String>());
    }

    public Customer(int sessionId, String username, String password) {
        this(username, password, sessionId, -1, null, new ArrayList<ClaimRecord>(), new ArrayList<String>());
    }

    public Customer(int sessionId, String username) {
        this(username, null, sessionId, -1, null, new ArrayList<ClaimRecord>(), new ArrayList<String>());
    }

    public Customer(int policyNumber, Person person) {
        this(null, null, -1, policyNumber, person, new ArrayList<ClaimRecord>(), new ArrayList<String>());
    }

    public String getUsername() {
        return _username;
    }

    public void setUsername(String username) {
        this._username = username;
    }

    public String getPassword() {
        return _password;
    }

    public void setPassword(String password) {
        this._password = password;
    }

    public int getPolicyNumber() {
        return _policyNumber;
    }

    public void setPolicyNumber(int _policyNumber) {
        this._policyNumber = _policyNumber;
    }

    public int getSessionId() {
        return _sessionId;
    }

    public void setSessionId(int sessionId) {
        this._sessionId = sessionId;
    }

    public void clearSessionId(int sessionId) {
        setSessionId(-1);
    }

    public List<ClaimRecord> getClaimRecordList() {
        return _claimList;
    }

    public List<String> getPlateList() {
        return _plateList;
    }

    public boolean addClaim(ClaimRecord claimRecord) {
        return _claimList.add(claimRecord);
    }

    public void addPlate(String plate) {
        _plateList.add(plate);
    }

    public boolean removeClaim(ClaimRecord claimRecord) {
        boolean res = false;
        while(_claimList.contains(claimRecord)) {
            res = _claimList.remove(claimRecord);
        }
        return res;
    }

    public boolean removePlate(String plate) {
        boolean res = false;
        while(_plateList.contains(plate)) {
            res = _plateList.remove(plate);
        }
        return res;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) obj;
        if (_username == null) {
            if (other._username != null) {
                return false;
            }
        } else if (!_username.equals(other._username)) {
            return false;
        }
        if (!(_fiscalNumber == other._fiscalNumber)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return super.toString() + ", " +
                "Username:" + _username + ", " +
                "Password:" + _password + ", " +
                "Policy Number:" + _policyNumber + ".";
    }
}
