package com.major.PgManagement.Service;

import org.springframework.stereotype.Service;

import com.major.PgManagement.Entities.Rent;
import com.major.PgManagement.Repository.RentRepository;

import java.util.List;

@Service
public class RentServiceImpl implements RentService {

    private final RentRepository repo;

    public RentServiceImpl(RentRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Rent> getAllRents() {
        return repo.findAll();
    }

    @Override
    public Rent saveRent(Rent rent) {
        return repo.save(rent);
    }
}
