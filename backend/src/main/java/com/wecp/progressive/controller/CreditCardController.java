package com.wecp.progressive.controller;


import com.wecp.progressive.entity.CreditCard;
import com.wecp.progressive.service.CreditCardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/credit-cards")
public class CreditCardController {
    @Autowired
    private CreditCardService creditCardService;

    @GetMapping
    public ResponseEntity<List<CreditCard>> getAllCreditCards() {
        return new ResponseEntity<>(creditCardService.getAllCreditCards(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreditCard> getCreditCardById(@PathVariable Long id) {
        CreditCard creditCard = creditCardService.getCreditCardById(id);
        if (creditCard==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(creditCard, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CreditCard> createCreditCard(@RequestBody CreditCard creditCard) {
        CreditCard newCreditCard = creditCardService.createCreditCard(creditCard);
        if (newCreditCard == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(newCreditCard, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCreditCard(@PathVariable Long id, @RequestBody CreditCard creditCard) {
        // need to check if id is present or not before updating and respond with 404 Not found
        // if (creditCard.getCardNumber()==null || creditCard.getCardNumber().isEmpty() || creditCard.getCardHolderName()==null || creditCard.getCardHolderName().isEmpty())
        //     return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        creditCard.setId(id);
        creditCardService.updateCreditCard(creditCard);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCreditCard(@PathVariable Long id) {
        // need to check if id is present or not before deleting and respond with 404 Not found
        creditCardService.deleteCreditCard(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
