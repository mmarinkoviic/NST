package nst.springboot.nstapplication.dto;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleDto implements Serializable {

    Long id;

    String name;
}
