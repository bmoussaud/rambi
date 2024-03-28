package com.broadcom.tanzu.demos.rambi;

import com.broadcom.tanzu.demos.rambi.image.RambiMovie;

public record RambiRequest(RambiMovie movie1, RambiMovie movie2, String genre) {
}
