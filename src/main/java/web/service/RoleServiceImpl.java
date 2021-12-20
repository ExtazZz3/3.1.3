package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.dao.RoleDao;
import web.model.Role;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleDao dao;

    @Override
    @Transactional
    public void saveRole(Role role) {
        dao.saveRole(role);
    }

    @Override
    @Transactional(readOnly = true)
    public Role getRoleById(Long id) {
        return dao.getRoleById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Role getRoleByType(String type) {
        return dao.getRoleByType(type);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {
        return dao.getAllRoles();
    }
}