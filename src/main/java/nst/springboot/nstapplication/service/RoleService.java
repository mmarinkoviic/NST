package nst.springboot.nstapplication.service;

import nst.springboot.nstapplication.dto.RoleDto;

import java.util.List;
import java.util.Map;

public interface RoleService {
    RoleDto save(RoleDto roleDto);
    List<RoleDto> getAll();
    RoleDto findById(Long id);
}
