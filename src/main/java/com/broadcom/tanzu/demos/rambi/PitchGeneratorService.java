package com.broadcom.tanzu.demos.rambi;

import com.broadcom.tanzu.demos.rambi.image.GeneratedRambiMovie;
import com.broadcom.tanzu.demos.rambi.image.RambiMovie;

public interface PitchGeneratorService {
    
    GeneratedRambiMovie generate(RambiMovie first, RambiMovie second, String genre);

}
