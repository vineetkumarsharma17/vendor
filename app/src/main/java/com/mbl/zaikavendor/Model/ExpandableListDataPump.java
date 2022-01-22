package com.mbl.zaikavendor.Model;

/**
 * Created by sachin on 7/21/2018.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> faq1 = new ArrayList<String>();
        faq1.add("Signing up with us is easy. You can Register From Online or can contact us to get associated with us.");

        List<String> faq2 = new ArrayList<String>();
        faq2.add("From Left Menu you can update your profile and service.");

        List<String> faq3 = new ArrayList<String>();
        faq3.add("Yes! Absolutely.We are using only authenticated and well known Payment gateways. ");

        List<String> faq4 = new ArrayList<String>();
        faq4.add("You can check your order on SMS, EMail or Mobile App, Also you will get notification when you receive an order.");

        List<String> faq5 = new ArrayList<String>();
        faq5.add("To pay for Order,User can use debit or credit card, net-banking option on checkout page. Your money will be transferred to your Account.");

        List<String> faq6 = new ArrayList<String>();
        faq6.add("We will notify you about your product order placement and request you to let us know if you want to accept order or not.");

        expandableListDetail.put("How do I create My Account?", faq1);
        expandableListDetail.put("How Do I  Update Profile?", faq2);
        expandableListDetail.put("Is payment safe at Hanszaika?", faq3);
        expandableListDetail.put("How Do I receive an order?", faq4);
        expandableListDetail.put("What is the Payment Method?", faq5);
        expandableListDetail.put("How Do I view my Order?", faq6);
        return expandableListDetail;
    }
}