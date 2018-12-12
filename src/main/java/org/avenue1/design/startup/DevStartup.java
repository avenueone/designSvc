package org.avenue1.design.startup;

import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;
import org.avenue1.design.domain.Design;
import org.avenue1.design.domain.enumeration.UnitOfMeasureEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
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
            for ( int i = 0 ; i < 3; i++ ) {
                Design design = new Design();
                design.setName("Sample Design " + i);
                design.setInstrumentType("flyer");
                design.setCreated(LocalDate.now());
                design.setActive(true);
                design.setUnitOfMeasure(UnitOfMeasureEnum.INCH);
                design.setHeight(11d);
                design.setWidth(8.5d);
                design.setMargins(0.1d);
                designList.add(design);

            }

            designRepository.saveAll(designList);
        }

    }
}
