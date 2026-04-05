package com.major.PgManagement.Service;

import java.util.List;

import com.major.PgManagement.Entities.Tenant;

public interface TenantService {
    List<Tenant> getAllTenants();
    Tenant saveTenant(Tenant tenant);
    Tenant getTenantById(Long id);
    void deleteTenant(Long id);
}
