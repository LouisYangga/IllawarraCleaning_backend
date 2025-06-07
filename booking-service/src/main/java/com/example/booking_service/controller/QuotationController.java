package com.example.booking_service.controller;

import com.example.booking_service.dto.QuotationRequest;
import com.example.booking_service.dto.QuotationResponse;
import com.example.booking_service.service.QuotationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quotations")
public class QuotationController {

    private final QuotationService quotationService;

    public QuotationController(QuotationService quotationService) {
        this.quotationService = quotationService;
    }

    // Create a new quotation and return the quotationId and details
    @PostMapping
    public ResponseEntity<QuotationResponse> createQuotation(@RequestBody QuotationRequest request) {
        QuotationResponse response = quotationService.createQuotation(request);
        return ResponseEntity.ok(response);
    }

    // Retrieve a quotation by its ID (for booking creation)
    @GetMapping("/{quotationId}")
    public ResponseEntity<?> getQuotationFromCache(@PathVariable String quotationId) {
        QuotationResponse response = quotationService.getQuotation(quotationId);
        if (response == null) {
            return ResponseEntity
                    .status(404)
                    .body("Quotation with ID " + quotationId + " not found.");
        }
        return ResponseEntity.ok(response);
    }
}
