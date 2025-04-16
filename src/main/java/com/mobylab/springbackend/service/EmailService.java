package com.mobylab.springbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendTradeNotification(String to, String asset, double quantity, double price) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Order Placed");
        message.setText(String.format(
                "Your order has been placed:\n\nAsset: %s\nQuantity: %.4f\nPrice: %.2f\n\nThank you for trading!",
                asset, quantity, price
        ));
        mailSender.send(message);
    }
}
