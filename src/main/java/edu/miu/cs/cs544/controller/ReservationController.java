package edu.miu.cs.cs544.controller;

import edu.miu.cs.cs544.domain.Reservation;

import edu.miu.cs.cs544.domain.Status;
import edu.miu.cs.cs544.dto.AuditDTO;
import edu.miu.cs.cs544.dto.*;
import edu.miu.cs.cs544.dto.ReserverationReqDTO;
import edu.miu.cs.cs544.dto.ReserverationResDTO;
import edu.miu.cs.cs544.repository.ProductRepository;
import edu.miu.cs.cs544.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/reservations")
public class ReservationController {
    @Autowired
    ReservationService reservationService;
    @Autowired
    ProductRepository productRepository;

//    @GetMapping
//    ResponseEntity<List<Reservation>> getReservations(){
//        return new ResponseEntity<>(reservationService.getReservations(), HttpStatus.OK);
//    }
    @PostMapping
    ResponseEntity<?> makeReservation(@RequestBody Reservation reservation){
        try {
            return new ResponseEntity<ReservationResponse>(reservationService.makeReservation(reservation), HttpStatus.CREATED);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/{reservationId}")
    ResponseEntity<?> updateReservation(@PathVariable Long reservationId, @RequestBody  Reservation reservation ){
        try {
            return new ResponseEntity<ReservationResponse>(reservationService.updateReservation(reservationId, reservation), HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/{reservationId}/cancel")
    ResponseEntity<?> cancelledReservation(@PathVariable Long reservationId ){

        try {
            return new ResponseEntity<ReservationResponse>(reservationService.cancelledReservation(reservationId), HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/customer/{customerNumber}")
    public ResponseEntity<?> getReservationByCustomer(@PathVariable long customerNumber){
        try {
            return new ResponseEntity<List<ReservationResponse>>(reservationService.getAllReservationListByCustomerID(customerNumber), HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/{reservationNumber}")
    public ResponseEntity<?> getReservationById(@PathVariable long reservationNumber){
        try {
            return new ResponseEntity<ReservationResponse>(reservationService.getReservation(reservationNumber), HttpStatus.OK);
        }catch(Exception ex){

            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
//    @PostMapping
//    public ResponseEntity<?> savedReservation(@RequestBody ReserverationReqDTO res){
//        try {
//            return new ResponseEntity<ReserverationResDTO>(reservationService.saveReservation(res), HttpStatus.OK);
//        }catch(Exception ex){
//            System.out.println("getReservation error :" + ex.getMessage());
//            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }

    @GetMapping
    public ResponseEntity<?> getReservation(){
        try {
            return new ResponseEntity<List<ReservationResponse>>(reservationService.getAllReservationList(), HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/deleteReservation/{reservationNumber}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long reservationNumber){
        try {
            return new ResponseEntity<ReservationResponse>(reservationService.deleteReservation (reservationNumber, Status.DELETED), HttpStatus.OK);

        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



    @PutMapping("/{reservationId}/checkin")
    ResponseEntity<?> checkIn(@PathVariable Long reservationId ){
        try {
            return new ResponseEntity<ReservationResponse>(reservationService.checkIn(reservationId),HttpStatus.OK);

        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{reservationId}/checkout")
    ResponseEntity<?> checkOut(@PathVariable Long reservationId ){
        try {
            return new ResponseEntity<ReservationResponse>(reservationService.checkOut(reservationId),HttpStatus.OK);

        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{reservationId}/place")
    ResponseEntity<?> placeReservation(@PathVariable Long reservationId ){
        try {
            return new ResponseEntity<ReservationResponse>(reservationService.placeReservation(reservationId),HttpStatus.OK);

        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{reservationId}/payment")
    ResponseEntity<?> paymentReservation(@PathVariable Long reservationId ){
        try {
            return new ResponseEntity<ReservationResponse>(reservationService.paymentProcess(reservationId),HttpStatus.OK);

        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
