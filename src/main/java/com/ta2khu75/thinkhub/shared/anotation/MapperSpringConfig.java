package com.ta2khu75.thinkhub.shared.anotation;

import org.mapstruct.MapperConfig;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.extensions.spring.SpringMapperConfig;
import com.ta2khu75.thinkhub.shared.service.ConversionServiceAdapter;

@MapperConfig(componentModel = MappingConstants.ComponentModel.SPRING, uses = ConversionServiceAdapter.class, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, typeConversionPolicy = ReportingPolicy.ERROR)
@SpringMapperConfig(conversionServiceAdapterPackage = "com.ta2khu75.thinkhub.shared.service")
public interface MapperSpringConfig {
}
