package com.tanniafashion.tanniafashion.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;


@Service
public class MercadoPagoService {
    @Value("${mercadopago.access-token}")
    private String accessToken;

    public String getAccessToken() { 
        return accessToken;
    }

    public String createPreference(Long userId, List<Map<String, Object>> cartItems) {
        try { 
            MercadoPagoConfig.setAccessToken(accessToken);

            System.out.println("Creando preferencia para usuario ID: " + userId);
            System.out.println("Productos: " + cartItems);

            List<PreferenceItemRequest> items = cartItems.stream()
                .map(cartItem -> {
                    String productName = (String) cartItem.get("productName");
                    int quantity = ((Number) cartItem.get("quantity")).intValue();

                    Object priceObj = cartItem.get("pricePerUnit");
                    BigDecimal pricePerUnit;
                    try {
                        pricePerUnit = (priceObj instanceof BigDecimal)
                            ? (BigDecimal) priceObj
                            : new BigDecimal(priceObj.toString());
                    } catch (NumberFormatException e) {
                        System.err.println("Error al convertir el precio del producto: " + productName);
                        throw new RuntimeException("Precio inválido para " + productName);
                    }

                    return PreferenceItemRequest.builder()
                        .title(productName)
                        .quantity(quantity)
                        .currencyId("PEN")
                        .unitPrice(pricePerUnit)
                        .build();
                })
                .collect(Collectors.toList());

                PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items)
                    .externalReference(userId.toString())
                    .build();

                PreferenceClient client = new PreferenceClient();
                Preference preference = client.create(preferenceRequest);

                System.out.println("Preferencia creada: " + preference.getId());
                System.out.println("URL de pago: " + preference.getInitPoint());
           
                return preference.getInitPoint();
        } catch (Exception exception) { 
            System.err.println("Error creando la preferencia de pago:");
            exception.printStackTrace();
            return null;
        }
    }
}
