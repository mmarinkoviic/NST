package nst.springboot.nstapplication.service.impl;

import lombok.RequiredArgsConstructor;
import nst.springboot.nstapplication.converter.impl.RoleConverter;
import nst.springboot.nstapplication.domain.Role;
import nst.springboot.nstapplication.dto.RoleDto;
import nst.springboot.nstapplication.exception.EntityAlreadyExistsException;
import nst.springboot.nstapplication.exception.EntityNotFoundException;
import nst.springboot.nstapplication.repository.RoleRepository;
import nst.springboot.nstapplication.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleConverter roleConverter;
    private final RoleRepository roleRepository;

    @Override
    public RoleDto save(RoleDto roleDto) {
        Optional<Role> eTitle = roleRepository.findByName(roleDto.getName());
        if (eTitle.isPresent()) {
            throw new EntityAlreadyExistsException("Education title with that name already exists!");
        } else {
            return roleConverter.toDto(roleRepository.save(roleConverter.toEntity(roleDto)));
        }
    }

    @Override
    public List<RoleDto> getAll() {
        return roleRepository.findAll()
                .stream()
                .map(roleConverter::toDto)
                .collect(Collectors.toList());

    }
    @Override
    public RoleDto findById(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        if (role.isPresent()) {
            return roleConverter.toDto(role.get());
        } else {
            throw new EntityNotFoundException("Role does not exist!");
        }
    }


}
