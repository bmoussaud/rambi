package com.broadcom.tanzu.demos.rambi;

import com.broadcom.tanzu.demos.rambi.image.RambiMovie;

public interface MovieService {
    RambiMovie search(String movieTitle);
}
