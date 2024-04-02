package nst.springboot.nstapplication.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentDto  {

    private Long id;
    private String href;
    private String name;
    private String shortName;


}