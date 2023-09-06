package com.mc.helper;

import java.sql.Timestamp;
import java.time.*;

/**
 * Utility class for converting and working with timestamps and time zones.
 * Provides methods to convert between UTC, EST, and the system's local time zone.
 * Also includes functionality to calculate durations and compare time differences.
 * @author Michael Cassidy
 */
public abstract class TimeUtils {
    private static final ZoneId userTimeZone = ZoneId.systemDefault();
    private static final ZoneId estTimeZone = ZoneId.of("America/New_York");

    /**
     * Converts a Timestamp object to a LocalTime object in the user's time zone.
     *
     * @param timestamp The Timestamp object to be converted.
     * @return The LocalTime object representing the time in the user's time zone.
     */
    public static LocalTime getTimeFromTimestamp(Timestamp timestamp) {
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(userTimeZone);
        return zonedDateTime.toLocalTime();
    }

    /**
     * Converts a Timestamp object to a ZonedDateTime object in the user's time zone.
     *
     * @param timestamp The Timestamp object to be converted.
     * @return The ZonedDateTime object representing the date and time in the user's time zone.
     */
    public static ZonedDateTime convertToLocalDateTime(Timestamp timestamp) {
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        return localDateTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(userTimeZone);
    }

    /**
     * Converts a LocalDate and LocalTime object to a UTC Timestamp.
     *
     * @param date The LocalDate object representing the date.
     * @param time The LocalTime object representing the time.
     * @return The Timestamp object representing the date and time in UTC.
     */
    public static Timestamp convertToUtcTimestamp(LocalDate date, LocalTime time) {
        LocalDateTime localDateTime = LocalDateTime.of(date, time);
        ZonedDateTime userDateTime = localDateTime.atZone(userTimeZone);
        ZoneOffset offset = userDateTime.getOffset();
        Instant instant = userDateTime.toInstant().minusSeconds(offset.getTotalSeconds());
        return Timestamp.from(instant);
    }

    /**
     * Checks if the difference between two timestamps is within 15 minutes.
     *
     * @param appointmentTimestamp The first Timestamp object.
     * @param currentTimestamp The second Timestamp object.
     * @return true if the difference between the two timestamps is within 15 minutes, false otherwise.
     */
    public static boolean isWithinFifteenMinutes(Timestamp appointmentTimestamp, Timestamp currentTimestamp) {
        long diffInMillis = Math.abs(currentTimestamp.getTime() - appointmentTimestamp.getTime());
        Duration duration = Duration.ofMillis(diffInMillis);
        Duration fifteenMinutes = Duration.ofMinutes(15);
        return duration.compareTo(fifteenMinutes) <= 0;
    }

    /**
     * Gets the local time equivalent to a specified EST time.
     *
     * @param hour The hour in EST.
     * @param minute The minute in EST.
     * @return The LocalTime object representing the local time equivalent to the specified EST time.
     */
    public static LocalTime getEstTimeInLocalTime(int hour, int minute) {
        // Create a LocalDateTime object with the local hour and minute
        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(hour, minute));

        // Convert the local LocalDateTime to a ZonedDateTime in the local time zone
        ZoneId localZone = ZoneId.systemDefault();
        ZonedDateTime localZonedDateTime = localDateTime.atZone(localZone);

        // Convert the local ZonedDateTime to EST
        ZoneId estZone = ZoneId.of("America/New_York");
        ZonedDateTime estZonedDateTime = localZonedDateTime.withZoneSameInstant(estZone);

        // Extract the EST LocalTime component from the ZonedDateTime and return it
        return estZonedDateTime.toLocalTime();
    }


    /**
     * Converts a given EST LocalTime to a LocalTime in the user's time zone.
     *
     * @param estTime The EST LocalTime to be converted.
     * @return The LocalTime object representing the time in the user's time zone.
     */
    public static LocalTime convertEstToLocalTime(LocalTime estTime) {
        // Create a ZonedDateTime object for the specified EST time on a specific date
        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), estTime);
        ZonedDateTime estDateTime = ZonedDateTime.of(localDateTime, estTimeZone);

        // Convert the EST time to your system's time zone and extract the LocalTime component
        ZonedDateTime localDateTimeZone = estDateTime.withZoneSameInstant(ZoneId.systemDefault());
        LocalTime localTime = localDateTimeZone.toLocalTime();

        return localTime;
    }

    /**
     * Converts a given UTC Timestamp to an EST LocalTime.
     *
     * @param utcTimestamp The UTC Timestamp to be converted.
     * @return The LocalTime object representing the time in EST.
     */
    public static LocalTime convertUtcToEst(Timestamp utcTimestamp) {
        LocalDateTime utcLocalDateTime = utcTimestamp.toLocalDateTime();
        ZonedDateTime utcZonedDateTime = utcLocalDateTime.atZone(ZoneId.of("UTC"));

        // Convert the UTC ZonedDateTime to EST
        ZoneId estZone = ZoneId.of("America/New_York");
        ZonedDateTime estZonedDateTime = utcZonedDateTime.withZoneSameInstant(estZone);

        // Extract the EST LocalTime component from the ZonedDateTime and return it
        return estZonedDateTime.toLocalTime();
    }
}
