package com.springboot.demo.inheritance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inheritance")
public class InheritanceController {
 
    @Autowired
    private PaymentRepository paymentRepository;
 
    @Autowired
    private VehicleRepository vehicleRepository;
 
    @Autowired
    private AnimalRepository animalRepository;
 
    // STRATEGY 1: SINGLE_TABLE — Payment
    @PostMapping("/payment/credit")
    public Payment saveCreditPayment(@RequestBody CreditCardPayment payment) {
        return paymentRepository.save(payment);
        // Hibernate: INSERT INTO payments (amount, status, type, card_number, card_holder)
        //                        VALUES (5000, 'SUCCESS', 'CREDIT', '4111...', 'Ravi')
    }
 
    @PostMapping("/payment/cash")
    public Payment saveCashPayment(@RequestBody CashPayment payment) {
        return paymentRepository.save(payment);
        // Hibernate: INSERT INTO payments (amount, status, type, bank_name)
        //                        VALUES (1000, 'SUCCESS', 'CASH', 'HDFC Bank')
    }
 
    @GetMapping("/payment")
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
        // Hibernate: SELECT * FROM payments
        // Returns both CreditCardPayment and CashPayment objects (discriminator handles it)
    }
 
    @GetMapping("/payment/{id}")
    public Payment getPaymentById(@PathVariable Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found: " + id));
    }
 
    // STRATEGY 2: JOINED — Vehicle
    @PostMapping("/vehicle/car")
    public Vehicle saveCar(@RequestBody Car car) {
        return vehicleRepository.save(car);
        // Hibernate: INSERT INTO vehicles (brand, price) → gets id=1
        //            INSERT INTO cars (id, num_doors) VALUES (1, 4)
    }
 
    @PostMapping("/vehicle/bike")
    public Vehicle saveBike(@RequestBody Bike bike) {
        return vehicleRepository.save(bike);
        // Hibernate: INSERT INTO vehicles (brand, price) → gets id=2
        //            INSERT INTO bikes (id, bike_type) VALUES (2, 'SPORTS')
    }
 
    @GetMapping("/vehicle")
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
        // Hibernate: SELECT v.*, c.* FROM vehicles v
        //            LEFT JOIN cars c ON v.id=c.id
        //            LEFT JOIN bikes b ON v.id=b.id
    }
 
    @GetMapping("/vehicle/{id}")
    public Vehicle getVehicleById(@PathVariable Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found: " + id));
    }
 
    // STRATEGY 3: TABLE_PER_CLASS — Animal
    @PostMapping("/animal/dog")
    public Animal saveDog(@RequestBody Dog dog) {
        return animalRepository.save(dog);
        // Hibernate: INSERT INTO dogs (id, name, sound, breed)
        //            VALUES (1, 'Rex', 'Bark', 'Labrador')
        // Note: id from shared AUTO sequence — never conflicts with cats
    }
 
    @PostMapping("/animal/cat")
    public Animal saveCat(@RequestBody Cat cat) {
        return animalRepository.save(cat);
        // Hibernate: INSERT INTO cats (id, name, sound, indoor)
        //            VALUES (2, 'Whiskers', 'Meow', true)
        // Note: id=2 (not 1) because AUTO sequence is shared with dogs
    }
 
    @GetMapping("/animal")
    public List<Animal> getAllAnimals() {
        return animalRepository.findAll();
        // Hibernate: SELECT * FROM dogs UNION ALL SELECT * FROM cats
        // This UNION is why TABLE_PER_CLASS is slowest for polymorphic queries
    }
 
    @GetMapping("/animal/{id}")
    public Animal getAnimalById(@PathVariable Long id) {
        return animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Animal not found: " + id));
    }
}
