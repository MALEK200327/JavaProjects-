package GUI; 

class BankDetails {
    private String nickname;
    private String cardNumber;
    private String cardName;
    private String expiryDate;
    private String securityCode; //Correct name for variable??

    public BankDetails(String nickname, String cardNumber, String cardName, String expiryDate, String securityCode) {
        this.nickname = nickname;
        this.cardNumber = cardNumber;
        this.cardName = cardName;
        this.expiryDate = expiryDate;
        this.securityCode = securityCode;
    }

    //Methods for retrieving and setting Bank Details

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }
}
