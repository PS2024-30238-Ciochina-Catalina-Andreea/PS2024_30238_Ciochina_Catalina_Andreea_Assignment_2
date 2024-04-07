package com.example.flowerShop.utils.cardPayment;

import com.example.flowerShop.dto.cardPayment.CardPaymentDTO;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@NoArgsConstructor
public class CardPaymentUtils {
    public boolean validateCategoryMap(CardPaymentDTO cardPaymentDTO) {
        return !Objects.equals(cardPaymentDTO.getNumarCard(), "")
                && !Objects.equals(cardPaymentDTO.getCvv(), "")
                && !Objects.equals(cardPaymentDTO.getTotalPrice(), "");
    }
}
