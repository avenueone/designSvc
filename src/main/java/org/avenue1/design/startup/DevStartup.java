package org.avenue1.design.startup;

import org.avenue1.design.domain.Design;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Profile("dev")
@Controller
@Configuration
public class DevStartup extends BaseStartup {

    private static final Logger log = LoggerFactory.getLogger(DevStartup.class);
    @PostConstruct
    private void init() {
        log.debug("Adding required dev data...");

        if ( designRepository.findAll().isEmpty()) {
            log.debug("Adding some sample designs...");
            List<Design> designList = new ArrayList();
            for ( int i = 0 ; i < 10; i++ ) {
                Design design = new Design();
                design.setName("Sample Design " + i);
                design.setInstrumentTypes("flyer");
                design.setCreated(LocalDate.now());
                design.setActive(true);
                design.setHeight(11d);
                design.setWidth(8.5d);
                design.setMargins(0.1d);
                designList.add(design);

            }

            designRepository.saveAll(designList);
        }

    }
}
