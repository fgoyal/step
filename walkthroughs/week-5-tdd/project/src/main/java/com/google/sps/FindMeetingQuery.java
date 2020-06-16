// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.Iterator;

public final class FindMeetingQuery {
  private static final long MAX_DURATION = TimeRange.WHOLE_DAY.duration();

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {

    long minDuration = request.getDuration();
    Collection<String> mandatoryAttendees = request.getAttendees();
    Collection<String> optionalAttendees = request.getOptionalAttendees();

    if (minDuration > MAX_DURATION) {
        return new ArrayList<TimeRange>();
    }

    List<TimeRange> mandatoryEventTimes = getBusyTimeRanges(events, mandatoryAttendees);
    mandatoryEventTimes.sort(TimeRange.ORDER_BY_START);
    removeNestedTimeRanges(mandatoryEventTimes);

    return getFreeTimeRanges(mandatoryEventTimes, minDuration);
  }

  private List<TimeRange> getFreeTimeRanges(List<TimeRange> busyTimes, long minDuration) {
    List<TimeRange> availableTimes = new ArrayList<TimeRange>();

    TimeRange gap;
    if (busyTimes == null || busyTimes.isEmpty()) {
      gap = TimeRange.WHOLE_DAY;
      addIfValid(availableTimes, gap, minDuration);
      return availableTimes;
    }

    return availableTimes;
  }

  private boolean isRangeValid(TimeRange timeRange, long minDuration) {
    return (timeRange.start() < timeRange.end()) && (timeRange.duration() >= minDuration);
  }

  private void addIfValid(List<TimeRange> availableTimes, TimeRange timeRange, long minDuration) {
    if (isRangeValid(timeRange, minDuration)) {
      availableTimes.add(timeRange);
    }
  }

  /**
   * Removes any nested TimeRanges from the list given
   */
  private void removeNestedTimeRanges(List<TimeRange> timeRanges) {
    Iterator it = timeRanges.iterator();
    TimeRange currentRange = null;
    TimeRange prevRange = null;
    
    while (it.hasNext()) {
      currentRange = (TimeRange) it.next();
      if (prevRange != null && prevRange.contains(currentRange)) {
        it.remove();
      } else {
        prevRange = currentRange;
      }
    }
  }

  /**
   * Returns a List of all the TimeRanges for events with at least one attendee from the attendees Collection
   * @param events: the events to filter by attendee
   * @param attendees: the attendees to check the events for
   */
  private List<TimeRange> getBusyTimeRanges(Collection<Event> events, Collection<String> attendees) {
    return events
      .stream()
      .filter(event -> !Collections.disjoint(event.getAttendees(), attendees))
      .map(event -> event.getWhen())
      .collect(Collectors.toList());
  }
}
