package org.nearbyshops.shopkeeperappnew.ViewHolderCommon.Models;




public class EmptyScreenDataListItem {


    private String message;
    private String buttonText;
    private String urlForButtonClick;




    public static EmptyScreenDataListItem getCreateMarketData()
    {
        EmptyScreenDataListItem data = new EmptyScreenDataListItem();
        data.setMessage("Create your own Market and help local Economy ... Its free !");
        data.setButtonText("Create Market !");
        data.setUrlForButtonClick("https://nearbyshops.org");

        return data;
    }




    public String getUrlForButtonClick() {
        return urlForButtonClick;
    }

    public void setUrlForButtonClick(String urlForButtonClick) {
        this.urlForButtonClick = urlForButtonClick;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
