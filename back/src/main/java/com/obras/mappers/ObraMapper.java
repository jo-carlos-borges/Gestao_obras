package com.obras.mappers;

import org.springframework.web.bind.annotation.Mapping;

//mappers/ObraMapper.java
@Mapper(componentModel = "spring")
public interface ObraMapper {
 
 @Mapping(target = "status", source = "status.descricao")
 ObraResponse toResponse(Obra obra);
 
 @Mapping(target = "id", ignore = true)
 @Mapping(target = "createdDate", ignore = true)
 @Mapping(target = "lastModifiedDate", ignore = true)
 Obra toEntity(ObraRequest request);
}
