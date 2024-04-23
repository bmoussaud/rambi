package com.broadcom.tanzu.demos.rambi;

public interface PitchGeneratorService {
    
    GeneratedRambiMovie generate(RambiMovie first, RambiMovie second, String genre);

}
