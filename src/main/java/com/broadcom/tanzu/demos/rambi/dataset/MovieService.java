package com.broadcom.tanzu.demos.rambi.dataset;

import com.broadcom.tanzu.demos.rambi.RambiMovie;

public interface MovieService {
    RambiMovie search(String movieTitle);
}
