
//APIs for the reservations

Post Reservation
"/api/v1/reservations"

JSON Data for make reservation

{
      "items": [
    {

      "occupants":10,
      "checkinDate":  "2023-12-16T12:34:56",
      "checkoutDate":  "2023-12-16T12:34:56",
       "reservation": {
        "id": 1
      },
      "product": {
          "id":1
      }
    }
  ],
  "customer":{
      "id":1
  }
}
