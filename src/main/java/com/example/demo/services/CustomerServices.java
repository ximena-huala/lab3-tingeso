package com.example.demo.services;

import com.example.demo.entities.CustomerEntity;
import com.example.demo.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServices {

    @Autowired
    private CustomerRepository customerRepository;

    public List<CustomerEntity> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<CustomerEntity> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public Optional<CustomerEntity> getCustomerByRut(String rut) {
        return customerRepository.findByRut(rut);
    }

    public boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

    public boolean existsByRut(String rut) {
        return customerRepository.existsByRut(rut);
    }

    public CustomerEntity saveCustomer(CustomerEntity customer) {
        return customerRepository.save(customer);
    }

    public CustomerEntity updateCustomer(CustomerEntity customer) { return customerRepository.save(customer); }

    public boolean isBirthdayToday(CustomerEntity customer) {
        if (customer.getDateOfBirth() == null) return false;

        LocalDate today = LocalDate.now();
        return customer.getDateOfBirth().getMonth() == today.getMonth() &&
                customer.getDateOfBirth().getDayOfMonth() == today.getDayOfMonth();
    }

    public String getFrequencyCategory(int visitsCount) {
        if (visitsCount >= 7) return "Very Frequent";
        else if (visitsCount >= 5) return "Frequent";
        else if (visitsCount >= 2) return "Regular";
        else return "Not Frequent";
    }

    public boolean deleteCustomer(Long id) throws Exception {
        try {
            customerRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
