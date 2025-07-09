package com.example.demo.services;

import com.example.demo.entities.*;
import com.example.demo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.List;
import java.util.ArrayList;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationParticipantRepository reservationParticipantRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TariffDiscountRepository tariffDiscountRepository;

    public ReservationEntity createReservation(ReservationEntity reservation, List<String> customerRuts) {
        validateReservationTime(reservation.getReservationDate(), reservation.getReservationTime(), customerRuts.size());

        // Validar que todos los RUTs correspondan a clientes existentes
        List<CustomerEntity> customers = new ArrayList<>();
        for (String rut : customerRuts) {
            Optional<CustomerEntity> optionalCustomer = customerRepository.findByRut(rut);
            if (optionalCustomer.isEmpty()) {
                throw new IllegalArgumentException("El cliente con RUT " + rut + " no existe.");
            }
            customers.add(optionalCustomer.get());
        }

        double basePrice = switch (reservation.getTrackTime()) {
            case "10 minutes" -> 15000;
            case "15 minutes" -> 20000;
            case "20 minutes" -> 25000;
            default -> throw new IllegalArgumentException("Invalid track time");
        };

        // Validar solapamiento de reservas por cliente
        for (String rut : customerRuts) {
            List<ReservationParticipantEntity> overlapping = reservationParticipantRepository
                .findByCustomer_RutAndReservation_ReservationDateAndReservation_ReservationTime(
                    rut, reservation.getReservationDate(), reservation.getReservationTime());
            if (!overlapping.isEmpty()) {
                throw new IllegalStateException("El cliente con RUT " + rut + " ya tiene una reserva en este horario.");
            }
        }

        // Solo ahora guardar la reserva
        ReservationEntity savedReservation = reservationRepository.save(reservation);
        List<ReservationParticipantEntity> participantsList = new ArrayList<>();
        int groupSize = customerRuts.size();

        // Calcular descuentos
        double groupDiscount = 0.0;
        if (groupSize >= 3 && groupSize <= 5) groupDiscount = 0.10;
        else if (groupSize >= 6 && groupSize <= 10) groupDiscount = 0.20;
        else if (groupSize >= 11 && groupSize <= 15) groupDiscount = 0.30;

        int birthdayCount = 0;
        int maxBirthday = groupSize >= 6 ? 2 : (groupSize >= 3 ? 1 : 0);
        LocalDate reservationDate = reservation.getReservationDate();

        double totalPrice = 0.0;
        for (CustomerEntity customer : customers) {
            ReservationParticipantEntity participant = new ReservationParticipantEntity();
            participant.setReservation(savedReservation);
            participant.setCustomer(customer);

            // Eliminar descuento por cliente frecuente
            double freqDiscount = 0.0;

            boolean isBirthday = false;
            double birthdayDiscount = 0.0;
            try {
                isBirthday = customer.dateOfBirth != null &&
                    customer.dateOfBirth.getMonthValue() == reservationDate.getMonthValue() &&
                    customer.dateOfBirth.getDayOfMonth() == reservationDate.getDayOfMonth();
            } catch (Exception e) {
                System.err.println("[ERROR] Al comparar cumpleaños: " + e.getMessage());
            }
            if (isBirthday && birthdayCount < maxBirthday) {
                birthdayDiscount = 0.50;
                birthdayCount++;
                participant.setBirthdayDiscount(true);
            } else {
                participant.setBirthdayDiscount(false);
            }

            double maxDiscount = groupDiscount;
            if (freqDiscount > maxDiscount) maxDiscount = freqDiscount;
            if (birthdayDiscount > maxDiscount) maxDiscount = birthdayDiscount;
            participant.setIndividualDiscount(maxDiscount);

            System.out.println("RUT: " + customer.getRut() + ", Descuentos: grupo=" + groupDiscount + ", cumpleaños=" + birthdayDiscount + ", aplicado=" + maxDiscount);

            double priceWithDiscount = basePrice * (1.0 - maxDiscount);
            totalPrice += priceWithDiscount;
            participantsList.add(participant);
        }

        reservationParticipantRepository.saveAll(participantsList);
        savedReservation.setTotalPrice(totalPrice);
        savedReservation.setParticipants(participantsList);
        return reservationRepository.save(savedReservation);
    }

    private void validateReservationTime(LocalDate date, LocalTime time, int newPeople) {
        List<ReservationEntity> reservations = reservationRepository.findByReservationDateAndReservationTime(date, time);
        int totalReserved = reservations.stream().mapToInt(ReservationEntity::getNumberOfPeople).sum();

        if (totalReserved + newPeople > 15) {
            throw new IllegalStateException("Reservation exceeds the 15 people per slot limit.");
        }

        boolean isWeekend = date.getDayOfWeek().getValue() >= 6;
        if ((isWeekend && (time.isBefore(LocalTime.of(10, 0)) || time.isAfter(LocalTime.of(22, 0)))) ||
                (!isWeekend && (time.isBefore(LocalTime.of(14, 0)) || time.isAfter(LocalTime.of(22, 0))))) {
            throw new IllegalArgumentException("Invalid reservation time for the selected date.");
        }
    }

    public List<ReservationEntity> getAllReservations() {
        return reservationRepository.findAll();
    }

    public List<String> getAvailableTimesForDate(LocalDate date) {
        boolean isWeekend = date.getDayOfWeek().getValue() >= 6;
        List<String> timeSlots = new ArrayList<>();

        int startHour = isWeekend ? 10 : 14;
        int endHour = 22;

        for (int hour = startHour; hour < endHour; hour++) {
            String time = String.format("%02d:00", hour);
            List<ReservationEntity> reservations = reservationRepository.findByReservationDateAndReservationTime(date, LocalTime.of(hour, 0));
            int reserved = reservations.stream().mapToInt(ReservationEntity::getNumberOfPeople).sum();
            if (reserved < 15) {
                timeSlots.add(time);
            }
        }

        return timeSlots;
    }

    public void deleteReservationById(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new IllegalArgumentException("Reservation with ID " + id + " not found.");
        }
        reservationRepository.deleteById(id);
    }

    public Map<String, Map<String, Double>> getIncomeByTrackTime(LocalDate startDate, LocalDate endDate) {
        List<ReservationEntity> reservations = reservationRepository.findByReservationDateBetween(startDate, endDate);
        Map<String, Map<String, Double>> report = new LinkedHashMap<>();

        for (ReservationEntity res : reservations) {
            String track = res.getTrackTime(); // 10, 15, 20 minutes
            String month = res.getReservationDate().getMonth().name(); // EJ: JANUARY

            report.putIfAbsent(track, new LinkedHashMap<>());
            Map<String, Double> monthMap = report.get(track);
            monthMap.put(month, monthMap.getOrDefault(month, 0.0) + res.getTotalPrice());
        }

        return report;
    }

    public Map<String, Map<String, Double>> getRevenueByPeopleCount(LocalDate start, LocalDate end) {
        List<ReservationEntity> reservations = reservationRepository.findAll()
                .stream()
                .filter(r -> !r.getReservationDate().isBefore(start) && !r.getReservationDate().isAfter(end))
                .toList();

        Map<String, Map<String, Double>> report = new TreeMap<>(); // Mes -> Grupo -> Total

        for (ReservationEntity res : reservations) {
            String month = res.getReservationDate().getMonth().toString().substring(0, 1).toUpperCase()
                    + res.getReservationDate().getMonth().toString().substring(1).toLowerCase();
            int people = res.getNumberOfPeople();
            String group;

            if (people <= 2) group = "1-2 personas";
            else if (people <= 5) group = "3-5 personas";
            else if (people <= 10) group = "6-10 personas";
            else group = "11-15 personas";

            report.computeIfAbsent(month, k -> new LinkedHashMap<>());
            Map<String, Double> monthData = report.get(month);
            monthData.put(group, monthData.getOrDefault(group, 0.0) + res.getTotalPrice());
        }

        return report;
    }

}
