package com.jixiang.chat.httpdns;

public interface DegradationFilter {
    boolean shouldDegradeHttpDNS(String hostName);
}
