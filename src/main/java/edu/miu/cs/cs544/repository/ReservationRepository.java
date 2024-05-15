package edu.miu.cs.cs544.repository;

import edu.miu.cs.cs544.domain.Reservation;
import edu.miu.cs.cs544.domain.Status;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("select r from Reservation r where r.status <> 'DELETED' and r.customer.id = :customerNumber")
    public List<Reservation> getReservationsByCustomer_Id(Long customerNumber);
    /*
    genterated the reservation info with 'DELETED' status
    * */
    @Query("select r from Reservation r where r.status <> 'DELETED'")
    public List<Reservation> findAll();
    /*
    For generate not delete status reservation
    * */
   // @Query("select r from Reservation r join r.customer c join c.billingAddress ba join c.physicalAddress pa join pa.state pas join ba.state bas join bas.country basc join pas.country pasc where r.status <> 'DELETED' and r.id = :reserveraionID")
    public Optional<Reservation> findById(Long reserveraionID);
    /*
    For generate the delete status reservation
    * */

    @Query("select r from Reservation r where r.status = 'DELETED' and r.id = :reserveraionID")
    public Optional<Reservation> findByIdWithStatusDelete(Long reserveraionID);
    /*
    Update the status of deleted transaction of 'NEW' or 'PLACE' status. The other status can't delete
    * */
    @Modifying
    @Transactional
    @Query("update Reservation r  set r.status = :status, r.auditData.updatedBy = :updateId, r.auditData.updatedOn = :updateDateTime , r.auditData.createdBy = :createdId , r.auditData.createdOn = :createdDateTime where r.id = :reservationNumber and (r.status = 'NEW' or r.status ='PLACE')")
    public Integer updateReservationWithStatusAndUpdatedIdAndUpdateDateTime (Long reservationNumber, Status status, String updateId, LocalDateTime updateDateTime,String createdId, LocalDateTime createdDateTime);

    @Modifying
    @Transactional
    @Query("update Reservation r  set  r.status= :status where (r.status ='NEW' or r.status ='PLACE' or r.status='PROCESS') and r.id = :reservationNumber")
    Integer updateByReServationIdWithStatusCancel (Long reservationNumber, Status status);

    @Modifying
    @Transactional
    @Query("update Reservation r  set  r.status= :status where r.id = :reservationNumber")
    Integer updateByReServationId (Long reservationNumber, Status status);

    @Modifying
    @Transactional
    @Query("update Reservation r set r.status= :status , r.auditData.updatedOn = :checkInDate, r.auditData.updatedBy = :updatedBy where r.id = :reservationNumber")
    Integer updateByCheckInReServation (Long reservationNumber, Status status,LocalDateTime checkInDate, String updatedBy);

    @Modifying
    @Transactional
    @Query("update Item i set i.checkinDate = :checkInDate , i.auditData.updatedOn = :checkInDate, i.auditData.updatedBy = :updatedBy where i.reservation.id = :reservationNumber and i.id = :itemId")
    Integer updateByCheckInReServationItem (Long reservationNumber,LocalDateTime checkInDate, String updatedBy, Integer itemId);

    @Modifying
    @Transactional
    @Query("update Item i set i.checkoutDate = :checkOutDate , i.auditData.updatedOn = :checkOutDate, i.auditData.updatedBy = :updatedBy where i.reservation.id = :reservationNumber and i.id = :itemId")
    Integer updateByCheckOutReServationItem (Long reservationNumber,LocalDateTime checkOutDate, String updatedBy, Integer itemId);


}
