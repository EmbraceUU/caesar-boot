package com.demo.commonserver.jobs;

import com.demo.commonserver.service.CurlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class CurlJob implements Job{

    @Autowired
    @Qualifier("curlService")
    private CurlService curlService;

    @Override
    public void execute() {
        curlService.sysCurl();
    }

    @Override
    public void execute(String[] args) {

    }
}
