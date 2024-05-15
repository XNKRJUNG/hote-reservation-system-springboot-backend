package edu.miu.cs.cs544.controller;
import edu.miu.cs.cs544.domain.Status;
import edu.miu.cs.cs544.dto.ReservationResponse;
import edu.miu.cs.cs544.service.ReservationService;
import jakarta.servlet.http.HttpSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Arrays;
import java.util.List;



@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest

public class ReservationControlTest {
    @Autowired
    MockMvc mock;

    @MockBean
    ReservationService reservationService;


    /*
    Testing of Reservation Number by reservation ID "successful" case.
    * */

    @Test
    @WithMockUser(username = "yamin", roles = "USER")
    public void testGetReservationByReservationNumberSuccessful() throws Exception{
        Mockito.when(reservationService.getReservation(5L))
                .thenReturn(new ReservationResponse(5L,2L,"john","smith","john@gmail.com","2939999","yamin") );
        mock.perform(get("/reservations/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId").value(5L))
                .andExpect(jsonPath("$.customerId").value(2L))
                .andExpect(jsonPath("$.firstName").value("john"))
                .andExpect(jsonPath("$.lastName").value("smith"))
                .andExpect(jsonPath("$.email").value("john@gmail.com"))
                .andExpect(jsonPath("$.phone_num").value("2939999"))
                .andExpect(jsonPath("$.user_name").value("yamin"));
    }

    /*
    Testing of Reservation Number by reservation ID "not found out ID" case.
    * */

    @Test
    @WithMockUser(username = "yamin", roles = "USER")
    public void testGetReservationByReservationNumberNotFound() throws Exception{
        Mockito.when(reservationService.getReservation(100L))
                .thenReturn(new ReservationResponse(100L,2L,"john","smith","john@gmail.com","2939999","yamin") );
        mock.perform(get("/reservations/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId").doesNotExist())
                .andExpect(jsonPath("$.customerId").doesNotExist())
                .andExpect(jsonPath("$.firstName").doesNotExist())
                .andExpect(jsonPath("$.lastName").doesNotExist())
                .andExpect(jsonPath("$.email").doesNotExist())
                .andExpect(jsonPath("$.phone_num").doesNotExist())
                .andExpect(jsonPath("$.user_name").doesNotExist());
    }

    /*
   Testing of Reservation Number by reservation ID "Error 400 error" case.
   * */
    @Test
    @WithMockUser(username = "yamin", roles = "USER")
    public void testGetReservationByReservationNumberException() throws Exception{
        Mockito.when(reservationService.getReservation(40L))
                .thenReturn(new ReservationResponse(40L,2L,"john","smith","john@gmail.com","2939999","yamin") );
        mock.perform(get("/reservation/1000"))
                .andExpect(status().is4xxClientError());
    }

    /*
    ##Get All Reservation##
    Testing of All Reservation of "successful" case.
    * */

    @Test
    @WithMockUser(username = "yamin", roles = "USER")
    public void testGetAllReservationSuccessful() throws Exception{
        List<ReservationResponse> mockResponseList = Arrays.asList(
                new ReservationResponse(4L,2L,"john","smith","john@gmail.com","2939999","yamin"),
                new ReservationResponse(5L,2L,"john","smith","john@gmail.com","2939999","yamin")
        );
        Mockito.when(reservationService.getAllReservationList())
                .thenReturn(mockResponseList);
        mock.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reservationId").value(4L))
                .andExpect(jsonPath("$[0].customerId").value(2L))
                .andExpect(jsonPath("$[0].firstName").value("john"))
                .andExpect(jsonPath("$[0].lastName").value("smith"))
                .andExpect(jsonPath("$[0].email").value("john@gmail.com"))
                .andExpect(jsonPath("$[0].phone_num").value("2939999"))
                .andExpect(jsonPath("$[0].user_name").value("yamin"))
                .andExpect(jsonPath("$[1].reservationId").value(5L))
                .andExpect(jsonPath("$[1].customerId").value(2L))
                .andExpect(jsonPath("$[1].firstName").value("john"))
                .andExpect(jsonPath("$[1].lastName").value("smith"))
                .andExpect(jsonPath("$[1].email").value("john@gmail.com"))
                .andExpect(jsonPath("$[1].phone_num").value("2939999"))
                .andExpect(jsonPath("$[1].user_name").value("yamin"));

    }

    /*
       Testing of All Reservation of "Failed" case. One actual ID mismatch from mock.
       mock reservation id is 700L
       actual reservation id is 7L
    * */

    @Test
    @WithMockUser(username = "yamin", roles = "USER")
    public void testGetAllReservationNotFound() throws Exception{
        List<ReservationResponse> mockResponseList = Arrays.asList(
                new ReservationResponse(5L,2L,"john","smith","john@gmail.com","2939999","yamin"),
                new ReservationResponse(700L,1L,"john","smith","john@gmail.com","2939999","yamin")

        );
        Mockito.when(reservationService.getAllReservationList())
                .thenReturn(mockResponseList);
        mock.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reservationId").value(5L))
                .andExpect(jsonPath("$[0].customerId").value(2L))
                .andExpect(jsonPath("$[0].firstName").value("john"))
                .andExpect(jsonPath("$[0].lastName").value("smith"))
                .andExpect(jsonPath("$[0].email").value("john@gmail.com"))
                .andExpect(jsonPath("$[0].phone_num").value("2939999"))
                .andExpect(jsonPath("$[0].user_name").value("yamin"))
                .andExpect(jsonPath("$[2].reservationId").doesNotExist())
                .andExpect(jsonPath("$[2].customerId").doesNotExist())
                .andExpect(jsonPath("$[2].firstName").doesNotExist())
                .andExpect(jsonPath("$[2].lastName").doesNotExist())
                .andExpect(jsonPath("$[2].email").doesNotExist())
                .andExpect(jsonPath("$[2].phone_num").doesNotExist())
                .andExpect(jsonPath("$[2].user_name").doesNotExist());
    }

    /*
   Testing of All Reservation using wrong path so that get "Error 400 error" case.
   * */
    @Test
    @WithMockUser(username = "yamin", roles = "USER")
    public void testGetAllReservationException() throws Exception{
        List<ReservationResponse> mockResponseList = Arrays.asList(
                new ReservationResponse(6L,1L,"john","smith","john@gmail.com","2939999","yamin"),
                new ReservationResponse(7L,1L,"john","smith","john@gmail.com","2939999","yamin")
        );
        Mockito.when(reservationService.getAllReservationList())
                .thenReturn(mockResponseList);
        mock.perform(get("/reservation"))
                .andExpect(status().is4xxClientError());
    }

    /*
    Testing of All Reservation By Customer ID of "successful" case.
    * */

    @Test
    @WithMockUser(username = "yamin", roles = "USER")
    public void testGetAllReservationByCustomerIDSuccessful() throws Exception{
        List<ReservationResponse> mockResponseList = Arrays.asList(
                new ReservationResponse(6L,1L,"john","smith","john@gmail.com","2939999","yamin"),
                new ReservationResponse(7L,1L,"john","smith","john@gmail.com","2939999","yamin")
        );
        Mockito.when(reservationService.getAllReservationListByCustomerID(1L))
                .thenReturn(mockResponseList);

        mock.perform(get("/reservations/customer/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reservationId").value(6L))
                .andExpect(jsonPath("$[0].customerId").value(1L))
                .andExpect(jsonPath("$[0].firstName").value("john"))
                .andExpect(jsonPath("$[0].lastName").value("smith"))
                .andExpect(jsonPath("$[0].email").value("john@gmail.com"))
                .andExpect(jsonPath("$[0].phone_num").value("2939999"))
                .andExpect(jsonPath("$[0].user_name").value("yamin"))
                .andExpect(jsonPath("$[1].reservationId").value(7L))
                .andExpect(jsonPath("$[1].customerId").value(1L))
                .andExpect(jsonPath("$[1].firstName").value("john"))
                .andExpect(jsonPath("$[1].lastName").value("smith"))
                .andExpect(jsonPath("$[1].email").value("john@gmail.com"))
                .andExpect(jsonPath("$[1].phone_num").value("2939999"))
                .andExpect(jsonPath("$[1].user_name").value("yamin"));

    }

    /*
    Testing of All Reservation of "Failed" case. The actual customer ID mismatch of mock's customer ID.
    mock customer id is 100L
    actual customer id is 1L
    * */

    @Test
    @WithMockUser(username = "yamin", roles = "USER")
    public void testGetAllReservationByCustomerIdNotFound() throws Exception{
        List<ReservationResponse> mockResponseList = Arrays.asList(
                new ReservationResponse(6L,100L,"john","smith","john@gmail.com","2939999","yamin"),
                new ReservationResponse(7L,100L,"john","smith","john@gmail.com","2939999","yamin")

        );
        Mockito.when(reservationService.getAllReservationList())
                .thenReturn(mockResponseList);
        mock.perform(get("/reservations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reservationId").doesNotExist())
                .andExpect(jsonPath("$[0].customerId").doesNotExist())
                .andExpect(jsonPath("$[0].firstName").doesNotExist())
                .andExpect(jsonPath("$[0].lastName").doesNotExist())
                .andExpect(jsonPath("$[0].email").doesNotExist())
                .andExpect(jsonPath("$[0].phone_num").doesNotExist())
                .andExpect(jsonPath("$[0].user_name").doesNotExist())
                .andExpect(jsonPath("$[2].reservationId").doesNotExist())
                .andExpect(jsonPath("$[2].customerId").doesNotExist())
                .andExpect(jsonPath("$[2].firstName").doesNotExist())
                .andExpect(jsonPath("$[2].lastName").doesNotExist())
                .andExpect(jsonPath("$[2].email").doesNotExist())
                .andExpect(jsonPath("$[2].phone_num").doesNotExist())
                .andExpect(jsonPath("$[2].user_name").doesNotExist());
    }

    /*
   Testing of All Reservation using wrong path so that get "Error 400 error" case.
   input the String variable of long parameter
   * */
    @Test
    @WithMockUser(username = "yamin", roles = "USER")
    public void testGetAllReservationByCustomerException() throws Exception{
        List<ReservationResponse> mockResponseList = Arrays.asList(
                new ReservationResponse(6L,1L,"john","smith","john@gmail.com","2939999","yamin"),
                new ReservationResponse(7L,1L,"john","smith","john@gmail.com","2939999","yamin")
        );
        Mockito.when(reservationService.getAllReservationList())
                .thenReturn(mockResponseList);
        mock.perform(get("/reservations/customer/cust"))
                .andExpect(status().is4xxClientError());
    }

    /*
    ##Updated of deleted the reservation transactions.##
    Testing of Reservation Number by reservation ID "successful" case.
    * */

    @Test
    @WithMockUser(username = "yamin", roles = "USER")
    public void testDeleteReservationByReservationIDSuccessful() throws Exception{
        mock.perform(put("/reservations/deleteReservation/10"))
                        .andExpect(status().isOk());

        verify(reservationService,times(1)).deleteReservation(10L, Status.DELETED);

    }

    /*
    ##Updated of deleted the reservation transactions.##
    Testing of Reservation Number by reservation ID "not found out ID" case.
    * */

    @Test
    @WithMockUser(username = "yamin", roles = "USER")
    public void testDeleteReservationByReservationNumberNotFound() throws Exception{
        mock.perform(put("/reservations/700")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"updatedBy\": \"Admin\"\n" +
                                "}"))
                .andExpect(status().isOk());
        verify(reservationService,times(0)).deleteReservation(700L, Status.DELETED);

    }

    /*
    ##Updated of deleted the reservation transactions.##
    Testing of Reservation Number by reservation ID "Error 400 error" case.
   * */
    @Test
    @WithMockUser(username = "yamin", roles = "USER")
    public void testDeleteReservationByReservationNumberException() throws Exception{
        mock.perform(put("/reservations/Id"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
        verify(reservationService,times(0)).deleteReservation(7L, Status.DELETED);

    }



}
