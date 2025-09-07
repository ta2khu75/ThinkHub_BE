package com.ta2khu75.thinkhub;

import java.util.stream.Stream;

import org.springframework.modulith.core.ApplicationModuleDetectionStrategy;
import org.springframework.modulith.core.ApplicationModuleInformation;
import org.springframework.modulith.core.JavaPackage;
import org.springframework.modulith.core.NamedInterfaces;

public class CustomApplicationModuleDetectionStrategy implements ApplicationModuleDetectionStrategy {

	@Override
	public Stream<JavaPackage> getModuleBasePackages(JavaPackage rootPackage) {
		// TODO Auto-generated method stub
		return rootPackage.getDirectSubPackages().stream().filter(pkg -> !pkg.getName().endsWith("shared") && !pkg.getName().endsWith("config"));
	}

	@Override
	public NamedInterfaces detectNamedInterfaces(JavaPackage basePackage, ApplicationModuleInformation information) {
		return NamedInterfaces.builder(basePackage).recursive().matching("api").build();
	}
}
