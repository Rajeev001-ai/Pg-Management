package com.major.PgManagement.Service;

import java.util.List;

import com.major.PgManagement.Entities.Rent;

public interface RentService {
    List<Rent> getAllRents();
    Rent saveRent(Rent rent);
}
