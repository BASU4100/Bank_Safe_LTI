package com.wecp.progressive.service;


import com.wecp.progressive.entity.CreditCard;
import com.wecp.progressive.repository.CreditCardRepository;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CreditCardService {
    private final CreditCardRepository creditCardRepository;

    public CreditCardService(CreditCardRepository creditCardRepository) {
        this.creditCardRepository = creditCardRepository;
    }

    public List<CreditCard> getAllCreditCards() {
        return creditCardRepository.findAll();
    }

    public CreditCard getCreditCardById(Long id) {
        return creditCardRepository.findById(id).orElse(null);
    }

    public CreditCard createCreditCard(CreditCard creditCard) {
        if (creditCardRepository.findByCardNumber(creditCard.getCardNumber())!=null)
            return null;
        return creditCardRepository.save(creditCard);
    }

    public void updateCreditCard(CreditCard creditCard) {
        CreditCard fetchCreditCard = creditCardRepository.findById(creditCard.getId()).get();
        fetchCreditCard.setCardHolderName(creditCard.getCardHolderName());
        creditCardRepository.save(fetchCreditCard);    
    }

    public void deleteCreditCard(Long id) {
        creditCardRepository.deleteById(id);
    }
}
