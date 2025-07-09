package com.example.demo.controllers;


import com.example.demo.entities.CustomerEntity;
import com.example.demo.services.CustomerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*") // Permite recibir peticiones desde el frontend (útil para desarrollo)

public class CustomerController {
    @Autowired
    private CustomerServices customerServices;

    @PostMapping
    public ResponseEntity<CustomerEntity> createCustomer(@RequestBody CustomerEntity customer) {
        CustomerEntity saved = customerServices.saveCustomer(customer);
        System.out.println("Nuevo cliente recibido: " + customer);

        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<CustomerEntity>> getAllCustomers() {
        List<CustomerEntity> customers = customerServices.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerEntity> getCustomerById(@PathVariable Long id) {
        Optional<CustomerEntity> customer = customerServices.getCustomerById(id);
        return customer.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/rut/{rut}")
    public ResponseEntity<CustomerEntity> getCustomerByRut(@PathVariable String rut) {
        Optional<CustomerEntity> customer = customerServices.getCustomerByRut(rut);
        return customer.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/by-ruts")
    public ResponseEntity<List<CustomerEntity>> getCustomersByRuts(@RequestBody List<String> ruts) {
        List<CustomerEntity> customers = ruts.stream()
            .map(customerServices::getCustomerByRut)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();
        return ResponseEntity.ok(customers);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
        try {
            if (customerServices.deleteCustomer(id)) {
                return ResponseEntity.ok("Customer deleted successfully");
            } else {
                return ResponseEntity.badRequest().body("Customer could not be deleted");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerEntity> updateCustomer(@PathVariable Long id, @RequestBody CustomerEntity customer) {
        Optional<CustomerEntity> existingCustomer = customerServices.getCustomerById(id);

        if (existingCustomer.isPresent()) {
            customer.setId(id); // Aseguramos que el ID se mantenga
            CustomerEntity updated = customerServices.updateCustomer(customer);
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/birthday/{id}")
    public ResponseEntity<Boolean> isBirthdayToday(@PathVariable Long id) {
        Optional<CustomerEntity> customer = customerServices.getCustomerById(id);
        return customer.map(c -> ResponseEntity.ok(customerServices.isBirthdayToday(c)))
                .orElse(ResponseEntity.notFound().build());
    }
}
