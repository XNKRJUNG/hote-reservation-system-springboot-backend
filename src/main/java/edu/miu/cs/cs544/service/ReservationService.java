package edu.miu.cs.cs544.service;
import edu.miu.cs.cs544.domain.Reservation;
import edu.miu.cs.cs544.domain.Status;
import edu.miu.cs.cs544.dto.ReservationResponse;
import edu.miu.cs.cs544.dto.ReserverationReqDTO;
import edu.miu.cs.cs544.dto.ReserverationResDTO;

import java.util.List;
public interface ReservationService {
    ReservationResponse makeReservation(Reservation reservation);
    List<Reservation> getReservations();
//    Reservation getReservation(Long reservationId);
    ReservationResponse updateReservation(Long id, Reservation reservation);

    ReservationResponse getReservation(long reservationNumber);
    //yee code
    List<ReservationResponse> getAllReservationList();

    ReserverationResDTO saveReservation(ReserverationReqDTO r);

    List<ReservationResponse> getAllReservationListByCustomerID(long customerID);

    ReservationResponse checkIn(Long id);

    ReservationResponse deleteReservation(Long reversationNumber, Status status);
    ReservationResponse cancelledReservation(Long id);

    ReservationResponse checkOut(Long id);

    ReservationResponse placeReservation(Long id);

    ReservationResponse paymentProcess(Long id);

}
