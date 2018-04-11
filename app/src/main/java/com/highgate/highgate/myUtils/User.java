package com.highgate.highgate.myUtils;

/**
 * Created by David on 8/7/2017.
 */

public class User {
    private int id = -1;
    private String firstName = "";
    private String lastName = "";
    private String emailAddress = "";
    private String companyName = "";
    private String address = "";
    private String suburb = "";
    private String postCode = "";
    private String state = "";
    private String freightMethod = "";
    private int debtor_id = -1;
    private int group_id = -1;


    public User(){
        id = -1;
        firstName = "";
        lastName = "";
        emailAddress = "";
        companyName = "";
        address = "";
        suburb = "";
        postCode = "";
        state = "";
        freightMethod = "";
        debtor_id = -1;
        group_id = -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFreightMethod() {
        return freightMethod;
    }

    public void setFreightMethod(String freightMethod) {
        this.freightMethod = freightMethod;
    }

    public int getDebtor_id() {
        return debtor_id;
    }

    public void setDebtor_id(int debtor_id) {
        this.debtor_id = debtor_id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String toString(){
        return "FirstName: " + firstName +
                " LastName: " + lastName +
                " Email: " + emailAddress +
                " Company: " + companyName +
                " Address" + address +
                " Suburb: " + suburb +
                " PostCod: " + postCode +
                " State: " + state +
                " Freight: " + freightMethod;
    }
}
