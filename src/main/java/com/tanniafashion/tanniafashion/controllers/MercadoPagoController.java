package com.tanniafashion.tanniafashion.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.net.HttpStatus;
import com.mercadopago.resources.payment.Payment;
import com.tanniafashion.tanniafashion.services.MercadoPagoService;
import com.tanniafashion.tanniafashion.services.OrderService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/mercadopago")
@CrossOrigin("*")
public class MercadoPagoController {
    @Autowired
    private MercadoPagoService mercadoPagoService;

    @Autowired
    private OrderService orderService;

    @PostMapping("/crear-preferencia")
    public ResponseEntity<Map<String, String>> crearPreferencia(@RequestBody Map<String, Object> request, HttpServletRequest httpRequest) { 
        try {

            if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
                return ResponseEntity.ok().build();
            }

            System.out.println("Creando preferencia...");
            Thread.sleep(5000);

            System.out.println("Solicitud recibida en /crear-preferencia: " + request);

            Long userId = Long.valueOf(request.get("userId").toString());
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> cartItems = (List<Map<String, Object>>) request.get("cartItems");

            if(cartItems == null || cartItems.isEmpty()) { 
                return ResponseEntity.badRequest().body(Map.of("error", "La orden no tiene productos."));
            }

            System.out.println("Creando preferencia para el usuario ID: " + userId);
            System.out.println("Productos en la orden: " + cartItems);

            String urlPago = mercadoPagoService.createPreference(userId, cartItems);

            if (urlPago == null) { 
                return ResponseEntity.badRequest().body(Map.of("error", "No se pudo generar la preferencia de pago"));
            }

            return ResponseEntity.ok(Map.of("urlPago", urlPago));

        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(Map.of("error", "Datos invalidos"));
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> recibirWebhook(@RequestBody Map<String, Object> request) { 
        try { 
            System.out.println("Webhook recibida: " + request);

            if ("payment".equals(request.get("type"))) {
                @SuppressWarnings("unchecked")
                Map<String, Object> data = (Map<String, Object>) request.get("data");
                if (data != null && data.get("id") != null) { 
                    String paymentId = data.get("id").toString();
                    procesarPago(paymentId);
                } else { 
                    System.err.println("Error: Webhook recibido sin ID de pago");
                }
            }

            return ResponseEntity.ok("Webhook recibido correctamente");
        
        } catch (Exception exception) { 
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error procesando webhook");
        }
    }

    private void procesarPago(String paymentId) { 
        try { 
            System.out.println("Procesando pago con ID: " + paymentId);

            Long orderId = obtenerOrderIdDesdePago(paymentId);
            if(orderId != null) { 
                orderService.updateOrderStatus(orderId, "Pagado");
            } else { 
                System.err.println("No se encontro una orden para el pago ID: " + paymentId);
            }

        } catch (Exception exception) { 
            exception.printStackTrace();
        }
    }

    private Long obtenerOrderIdDesdePago(String paymentId) { 
        try { 
            MercadoPagoConfig.setAccessToken(mercadoPagoService.getAccessToken());

            PaymentClient paymentClient = new PaymentClient();
            Long pagoId = Long.valueOf(paymentId);
            Payment payment = paymentClient.get(pagoId);

            if( payment.getExternalReference() != null) { 
                return Long.valueOf(payment.getExternalReference());
            }

        } catch (Exception exception) { 
            System.err.println("Error obteniendo la orden desde MercadoPago: " + exception.getMessage());
        }

        return null;
    }
}
