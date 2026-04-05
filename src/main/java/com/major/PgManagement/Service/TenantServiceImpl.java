package com.major.PgManagement.Service;

import org.springframework.stereotype.Service;

import com.major.PgManagement.Entities.Tenant;
import com.major.PgManagement.Repository.TenantRepository;

import java.util.List;

@Service
public class TenantServiceImpl implements TenantService {

    private final TenantRepository repo;

    public TenantServiceImpl(TenantRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Tenant> getAllTenants() {
        return repo.findAll();
    }

    @Override
    public Tenant saveTenant(Tenant tenant) {
        return repo.save(tenant);
    }

    @Override
    public Tenant getTenantById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public void deleteTenant(Long id) {
        repo.deleteById(id);
    }
}
