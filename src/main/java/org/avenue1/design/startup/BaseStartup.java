package org.avenue1.design.startup;

import org.avenue1.design.repository.ContainerRepository;
import org.avenue1.design.repository.DesignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class BaseStartup {

    @Autowired
    protected DesignRepository designRepository;

    @Autowired
    protected ContainerRepository containerRepository;
}
