package org.gbl.common.gateway;

import java.time.ZoneId;

public record GetUpcomingBirthdaysRequest(int size, ZoneId clientZoneId) {}
