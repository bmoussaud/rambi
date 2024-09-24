package org.moussaud.demos.moviegenerator;

public interface PitchGeneratorService {
    
    GeneratedRambiMovie generate(RambiMovie first, RambiMovie second, String genre);

}
