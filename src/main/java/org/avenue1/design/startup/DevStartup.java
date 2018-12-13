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
import java.util.Random;

@Profile("dev")
@Controller
@Configuration
public class DevStartup extends BaseStartup {



    private static final Logger log = LoggerFactory.getLogger(DevStartup.class);
    @PostConstruct
    private void init() {
        log.debug("Adding required dev data...");

        String[] list = {"flyer", "web", "email"};
        Random r = new Random();
        double rangeMin = 3;
        double rangeMax = 20;
        if ( designRepository.findAll().isEmpty()) {
            log.debug("Adding some sample designs...");
            List<Design> designList = new ArrayList();
            for ( int i = 0 ; i < 50; i++ ) {
                double height = Math.round((rangeMin + (rangeMax - rangeMin) * r.nextDouble()) * 100 ) / 100 ;
                double width = Math.round((rangeMin + (rangeMax - rangeMin) * r.nextDouble()) * 100 ) / 100 ;

                Design design = new Design();
                design.setName("Sample Design " + i);
                design.setInstrumentType(list[r.nextInt(list.length)]);
                design.setCreated(LocalDate.now());
                design.setActive(true);
                design.setUnitOfMeasure(UnitOfMeasureEnum.INCH);
                design.setHeight(height);
                design.setWidth(width);
                design.setMargins(0.1d);
                design.setRows(r.nextInt(10) + 1);
                design.setColumns(r.nextInt(10) + 1);
                designList.add(design);

            }

            designRepository.saveAll(designList);
        }

    }
}
