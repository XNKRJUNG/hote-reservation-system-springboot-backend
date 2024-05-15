package edu.miu.cs.cs544.service;

import edu.miu.cs.cs544.domain.*;
import edu.miu.cs.cs544.dto.*;
import edu.miu.cs.cs544.dto.response.CustomerResponseDTO;
import edu.miu.cs.cs544.enums.Constants;
import edu.miu.cs.cs544.exception.AppException;
import edu.miu.cs.cs544.repository.CustomerRepository;
import edu.miu.cs.cs544.repository.ProductRepository;
import edu.miu.cs.cs544.repository.ReservationRepository;
import edu.miu.cs.cs544.security.JwtTokenService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static edu.miu.cs.cs544.enums.Constants.RESERVATION_CHECKINGFAIL;

@Service
public class ReservationServiceImpl implements ReservationService{
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CustomerRepository customerRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    JwtTokenService jwtTokenService;
    private Logger logger = LoggerFactory.getLogger(ReservationServiceImpl.class);

    @Override
    public ReservationResponse getReservation(long reservationNumber) {
        Optional<Reservation> reservation = reservationRepository.findById(reservationNumber);
        ReservationResponse reservationResponse = new ReservationResponse();
        if(reservation.isPresent()){
            reservationResponse =  getRevervationInfo(reservation.get());
        }
        else{
            reservationResponse = null;
        }

        return reservationResponse;
    }



    public List<ReservationResponse> getAllReservationList() {
        List<Reservation> allReserve = reservationRepository.findAll();
        List<ReservationResponse> allReservation = new ArrayList<>();
        for (Reservation s : allReserve) {
            allReservation.add(getRevervationInfo(s));

        }
        return allReservation;
    }

    /*
    Convert the Reservation Object to reservationresponse DTO
    * */
    public ReservationResponse getRevervationInfo(Reservation s) {
        ReservationResponse r = new ReservationResponse();
        r.setFirstName(s.getCustomer().getFirstName());
        r.setLastName(s.getCustomer().getLastName());
        r.setEmail(s.getCustomer().getEmail());
        r.setUser_name(s.getCustomer().getUser().getUserName());
        r.setUserType(s.getCustomer().getUser().getUserType());
        r.setStatus(s.getStatus());
        r.setCreated_by(s.getAuditData().getCreatedBy());
        r.setUpdated_by(s.getAuditData().getUpdatedBy());
        r.setUpdated_date(s.getAuditData().getUpdatedOn());
        r.setCreated_date(s.getAuditData().getCreatedOn());
        r.setReservationId(s.getId());
        r.setActive(s.getCustomer().getUser().getActive());
        r.setPhone_num(s.getCustomer().getPhone_num());
        r.setPhysicalLine1(s.getCustomer().getPhysicalAddress().getLine1());
        r.setPhysicalline2(s.getCustomer().getPhysicalAddress().getLine2());
        r.setCustomerId(s.getCustomer().getId());

        List<ItemDTO> items = new ArrayList<>();

        for (Item i : s.getItems()) {
            ItemDTO item = new ItemDTO();
            item.setId(i.getId());
            item.setCheckoutDate(i.getCheckoutDate());
            item.setCheckinDate(i.getCheckinDate());
            item.setOccupants(i.getOccupants());

            ProductDTO productDTO = new ProductDTO();

            productDTO.setId(i.getProduct().getId());
            productDTO.setName(i.getProduct().getName());
            productDTO.setDescription(i.getProduct().getDescription());
            productDTO.setNight_rate(i.getProduct().getNight_rate());
            productDTO.setNumber_of_beds(i.getProduct().getNumber_of_beds());
            productDTO.setType(i.getProduct().getType());
            productDTO.setAvailability(i.getProduct().isAvailability());
            productDTO.setExcerpt(i.getProduct().getExcerpt());
            item.setProductDTO(productDTO);

            items.add(item);
        }
        r.setItems(items);
        return r;
    }

    public ReserverationResDTO saveReservation(ReserverationReqDTO r) {
        Reservation resvertion = new Reservation();

        resvertion.setAuditData(getInsertAuditData());
        List<Item> itemList = new ArrayList<>();

        for (Item item: r.getItems()) {
            Item i = new Item();
            i.setProduct(item.getProduct());
            i.setAuditData(getInsertAuditData());
            i.setCheckinDate(LocalDateTime.now());
            i.setCheckoutDate(LocalDateTime.now());
            i.setOccupants(item.getOccupants());

            itemList.add(i);
        }
        resvertion.setStatus(r.getStatus());
        resvertion.setItems(itemList);

        ReserverationResDTO reservationResponse = new ReserverationResDTO();
        reservationResponse.setCustomer(r.getCustomer());
        reservationResponse.setStatus(r.getStatus());
        reservationResponse.setItems(r.getItems());
        return reservationResponse;
    }

    @Override
    public List<ReservationResponse> getAllReservationListByCustomerID(long customerID) {
        List<Reservation> reservationList = reservationRepository.getReservationsByCustomer_Id(customerID);
        List<ReservationResponse> reservationResponseList = new ArrayList<>();
        for (Reservation r : reservationList) {
            reservationResponseList.add(getRevervationInfo(r));
        }
        return reservationResponseList;
    }
    /**
     *  Get the AuditData for create reservation
     */

    private AuditData getInsertAuditData(){

        AuditData auditData = new AuditData();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            CustomerResponseDTO customer = (CustomerResponseDTO) authentication.getPrincipal();
            auditData.setUpdatedBy(customer.getUser().getUserName()); // current logged in username
            auditData.setCreatedBy(customer.getUser().getUserName());
            auditData.setCreatedOn(LocalDateTime.now());
            auditData.setUpdatedOn(LocalDateTime.now());
        }
        return auditData;
    }

    /*
     * Get the AuditData for update reservation
     * */

    private AuditData getUpdateAuditData(){

        AuditData auditData = new AuditData();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            CustomerResponseDTO customer = (CustomerResponseDTO) authentication.getPrincipal();
            auditData.setUpdatedBy(customer.getUser().getUserName()); // current logged in username
            auditData.setUpdatedOn(LocalDateTime.now());
        }
        return auditData;
    }
    public ReservationResponse deleteReservation(Long reservationNumber, Status status) {

        try {
            Optional<Reservation> r = reservationRepository.findById(reservationNumber);
            if (r.isPresent()) {
                AuditData audit = getUpdateAuditData();
                audit.setCreatedOn(r.get().getAuditData().getCreatedOn());
                audit.setCreatedBy(r.get().getAuditData().getCreatedBy());
                if(reservationRepository.updateReservationWithStatusAndUpdatedIdAndUpdateDateTime(reservationNumber, status, audit.getUpdatedBy(), audit.getUpdatedOn(),audit.getCreatedBy(),audit.getCreatedOn()).equals(1))
                {
                    //show the latest data return thus why add it
                    reservationRepository.flush();
                    entityManager.clear();

                    return getRevervationInfo(reservationRepository.findByIdWithStatusDelete(reservationNumber).get());
                }
                else {
                    return null;
                }

            } else {
                return null;
            }

        }catch (Exception e){
            throw new AppException("Can't Delete the Reservation");
        }
    }
    @Override
    public ReservationResponse makeReservation(Reservation reservation) {
        try{
            AuditData auditData = getInsertAuditData();

            List<Item> items = new ArrayList<>();
            for(Item item : reservation.getItems()){
                Product product = productRepository.findById(item.getProduct().getId()).get();
                item.setProduct(product);
                item.setReservation(reservation);
                item.setAuditData(auditData);
                items.add(item);
            }
            Customer customer = customerRepository.findById(reservation.getCustomer().getId()).get();
            reservation.setItems(items);
            reservation.setCustomer(customer);
            reservation.setAuditData(auditData);
            reservation.setStatus(Status.NEW);
            Reservation reservation1 = reservationRepository.save(reservation);
            ReservationResponse reservationResponse = getRevervationInfo(reservation1);
            return reservationResponse;
        }catch (Exception e){
            throw  new AppException(Constants.MAKE_RESERVATION_FAIL.getmessage());
        }
    }

    @Override
    public List<Reservation> getReservations() {
        try{
            return reservationRepository.findAll();
        }catch (Exception e){
            throw  new AppException(Constants.GET_RESERVATION_FAIL.getmessage());
        }
    }

@Transactional
    @Override
    public ReservationResponse updateReservation(Long id, Reservation reservation) {
        try{
            Optional<Reservation> r = reservationRepository.findById(id);
            if (r.isPresent()) {
                r.get().setItems(reservation.getItems());
                AuditData auditData = getUpdateAuditData();
                auditData.setCreatedBy(r.get().getAuditData().getCreatedBy());
                auditData.setCreatedOn(r.get().getAuditData().getCreatedOn());
                r.get().setAuditData(auditData);
                List<Item> items = new ArrayList<>();
                for(Item i : r.get().getItems()){
                    i.setReservation(r.get());
                    i.setAuditData(auditData);
                    items.add(i);

                }
                r.get().setItems(items);
                Reservation reservation1 = reservationRepository.save(r.get());
               return getRevervationInfo(reservation1);
            } else {
                return null;
            }
        }catch (Exception e){
            throw new AppException(Constants.UPDATE_RESERVATION_FAIL.getmessage());
        }
    }

    @Override


    public ReservationResponse cancelledReservation(Long id) {
        try {
            Reservation reservation = reservationRepository.findById(id).get();
            if(reservation !=null){
                AuditData auditData = getUpdateAuditData();
                auditData.setCreatedOn(reservation.getAuditData().getCreatedOn());
                auditData.setCreatedBy(reservation.getAuditData().getCreatedBy());
                reservation.setAuditData(auditData);
                if(reservationRepository.updateByReServationId(id, Status.CANCELED).equals(1))
                {
                    //show the latest data return thus why add it
                    reservationRepository.flush();
                    entityManager.clear();

                    return getRevervationInfo(reservationRepository.findByIdWithStatusDelete(id).get());
                }
                else{
                    return null;
                }
            }
            else{
                return null;
            }

        }catch (Exception e){
            throw new AppException(Constants.CANCEl_RESERVATION_FAIL.getmessage());
        }
    }

    @Override
    public ReservationResponse checkIn(Long id) {
        try {
            Reservation reservation = reservationRepository.findById(id).get();
            if(reservation !=null){
                AuditData auditData = getUpdateAuditData();
                auditData.setCreatedOn(reservation.getAuditData().getCreatedOn());
                auditData.setCreatedBy(reservation.getAuditData().getCreatedBy());
                reservation.setAuditData(auditData);
                double totalprice = 0.0;
                for(Item i: reservation.getItems()){
                    long daysStayed = ChronoUnit.DAYS.between(i.getCheckinDate(),i.getCheckoutDate());
                    totalprice += i.getProduct().getNight_rate()*i.getProduct().getNumber_of_beds()*daysStayed;

                    reservationRepository.updateByCheckInReServationItem(id,i.getCheckinDate(),auditData.getUpdatedBy(), i.getId());
                }

                if(reservationRepository.updateByReServationId(id, Status.ARRIVED).equals(1))
                {
                    //show the latest data return thus why add it
                    reservationRepository.flush();
                    entityManager.clear();
                    ReservationResponse response = getRevervationInfo(reservationRepository.findById(id).get());
                    response.setTotalPayment(totalprice);
                    return response;
                }
                else{
                    return null;
                }

            }
            else{
                return null;
            }

        }catch (Exception e){
            throw new AppException(RESERVATION_CHECKINGFAIL.getmessage());
        }
    }


    @Override
    public ReservationResponse checkOut(Long id) {
        try {
            Reservation reservation = reservationRepository.findById(id).get();
            if(reservation !=null){
                AuditData auditData = getUpdateAuditData();
                auditData.setCreatedOn(reservation.getAuditData().getCreatedOn());
                auditData.setCreatedBy(reservation.getAuditData().getCreatedBy());
                reservation.setAuditData(auditData);
                double totalprice=0.0;
                for(Item i: reservation.getItems()){

                    long daysStayed = ChronoUnit.DAYS.between(i.getCheckinDate(),i.getCheckoutDate());
                    totalprice += i.getProduct().getNight_rate()*i.getProduct().getNumber_of_beds()*daysStayed;

                    reservationRepository.updateByCheckOutReServationItem(id,i.getCheckoutDate(),auditData.getUpdatedBy(), i.getId());
                }

                if(reservationRepository.updateByReServationId(id, Status.DEPARTED).equals(1))
                {
                    //show the latest data return thus why add it
                    reservationRepository.flush();
                    entityManager.clear();

                    ReservationResponse response = getRevervationInfo(reservationRepository.findById(id).get());
                    response.setTotalPayment(totalprice);
                    return response;
                }
                else{
                    return null;
                }

            }
            else{
                return null;
            }

        }catch (Exception e){
            throw new AppException(RESERVATION_CHECKINGFAIL.getmessage());
        }
    }


    @Override
    public ReservationResponse placeReservation(Long id) {
        try {
            Reservation reservation = reservationRepository.findById(id).get();
            if(reservation !=null){
                AuditData auditData = new AuditData();
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication != null && authentication.isAuthenticated()) {
                    CustomerResponseDTO customer = (CustomerResponseDTO) authentication.getPrincipal();
                    auditData.setUpdatedBy(customer.getUser().getUserName()); // current logged in username
                    auditData.setUpdatedOn(LocalDateTime.now());
                    auditData.setCreatedOn(reservation.getAuditData().getCreatedOn());
                    auditData.setCreatedBy(reservation.getAuditData().getCreatedBy());
                    reservation.setAuditData(auditData);

                    if(reservationRepository.updateByReServationId(id, Status.PLACE).equals(1))
                    {
                        //show the latest data return thus why add it
                        reservationRepository.flush();
                        entityManager.clear();
                        ReservationResponse reservationResponse = getRevervationInfo(reservationRepository.findById(id).get());
                        return reservationResponse;
                    }
                    else{
                        return null;
                    }
                }
                else{
                    return null;
                }
            }
            else{
                return null;
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new AppException(RESERVATION_CHECKINGFAIL.getmessage());
        }
    }

    @Override
    public ReservationResponse paymentProcess(Long id) {
        try {
            Reservation reservation = reservationRepository.findById(id).get();
            if(reservation !=null){
                AuditData auditData = getUpdateAuditData();
                auditData.setCreatedOn(reservation.getAuditData().getCreatedOn());
                auditData.setCreatedBy(reservation.getAuditData().getCreatedBy());
                reservation.setAuditData(auditData);
                Double totalprice = 0.0;

                for(Item i: reservation.getItems()){
                    long daysStayed = ChronoUnit.DAYS.between(i.getCheckinDate(),i.getCheckoutDate());
                    totalprice += i.getProduct().getNight_rate()*i.getProduct().getNumber_of_beds()*daysStayed;
                }

                if(reservationRepository.updateByReServationId(id, Status.PROCESS).equals(1))
                {
                    //show the latest data return thus why add it
                    reservationRepository.flush();
                    entityManager.clear();
                    ReservationResponse response = getRevervationInfo(reservationRepository.findById(id).get());
                    response.setTotalPayment(totalprice);
                    return response;
                }
                else{
                    return null;
                }

            }
            else{
                return null;
            }

        }catch (Exception e){
            throw new AppException(RESERVATION_CHECKINGFAIL.getmessage());
        }
    }

}
