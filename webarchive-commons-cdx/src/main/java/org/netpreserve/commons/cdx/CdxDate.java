/*
 * Copyright 2016 The International Internet Preservation Consortium.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.netpreserve.commons.cdx;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.Locale;
import java.util.Objects;

/**
 * A representation of a date.
 * <p>
 * CdxDates are allowed to be of variable granularity. A CdxDate can be produced from strings in either Heritrix date
 * format or WARC date format.
 * <p>
 * A CdxDate is always in UTC.
 * <p>
 * This class is immutable and thread safe.
 */
public final class CdxDate implements Comparable<CdxDate> {

    /**
     * A set of allowed granularities for a date.
     */
    public enum Granularity {

        /**
         * A date with only the year known.
         */
        YEAR,
        /**
         * A date with only the year and month known.
         */
        MONTH,
        /**
         * A date with only the year, month and day known.
         */
        DAY,
        /**
         * A date with only the year, month, day and hour known.
         */
        HOUR,
        /**
         * A date with only the year, month, day, hour and minute known.
         */
        MINUTE,
        /**
         * A date with only the year, month, day, hour, minute and second known.
         */
        SECOND,
        /**
         * A date with the highest possible precision.
         */
        NANOSECOND;

    }

    /**
     * Format for parsing WARC date syntax.
     */
    private static final DateTimeFormatter WARC_DATE_PARSE_FORMAT = new DateTimeFormatterBuilder()
            .appendValue(ChronoField.YEAR, 4)
            .optionalStart()
            .appendLiteral('-')
            .appendValue(ChronoField.MONTH_OF_YEAR, 2)
            .optionalStart()
            .appendLiteral('-')
            .appendValue(ChronoField.DAY_OF_MONTH, 2)
            .optionalStart()
            .appendLiteral('T')
            .appendValue(ChronoField.HOUR_OF_DAY, 2)
            .optionalStart()
            .appendLiteral(':')
            .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
            .optionalStart()
            .appendLiteral(':')
            .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
            .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
            .optionalEnd()
            .optionalStart()
            .appendOffsetId()
            .optionalEnd()
            .optionalEnd()
            .optionalEnd()
            .optionalEnd()
            .optionalEnd()
            .parseDefaulting(ChronoField.OFFSET_SECONDS, 0)
            .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
            .toFormatter(Locale.ENGLISH);

    /**
     * Array of formats for formatting dates of different granularities allowed in the WARC date format.
     */
    private static final DateTimeFormatter[] WARC_DATE_OUTPUT_FORMAT;

    static {
        WARC_DATE_OUTPUT_FORMAT = new DateTimeFormatter[Granularity.values().length];

        DateTimeFormatterBuilder warcDateBuilder = new DateTimeFormatterBuilder()
                .appendValue(ChronoField.YEAR, 4);
        WARC_DATE_OUTPUT_FORMAT[Granularity.YEAR.ordinal()] = warcDateBuilder.toFormatter(Locale.ENGLISH);

        warcDateBuilder.appendLiteral('-')
                .appendValue(ChronoField.MONTH_OF_YEAR, 2);
        WARC_DATE_OUTPUT_FORMAT[Granularity.MONTH.ordinal()] = warcDateBuilder.toFormatter(Locale.ENGLISH);

        warcDateBuilder.appendLiteral('-')
                .appendValue(ChronoField.DAY_OF_MONTH, 2);
        WARC_DATE_OUTPUT_FORMAT[Granularity.DAY.ordinal()] = warcDateBuilder.toFormatter(Locale.ENGLISH);

        warcDateBuilder.appendLiteral('T')
                .appendValue(ChronoField.HOUR_OF_DAY, 2);
        WARC_DATE_OUTPUT_FORMAT[Granularity.HOUR.ordinal()] = warcDateBuilder.toFormatter(Locale.ENGLISH);

        warcDateBuilder.appendLiteral(':')
                .appendValue(ChronoField.MINUTE_OF_HOUR, 2);
        WARC_DATE_OUTPUT_FORMAT[Granularity.MINUTE.ordinal()] = warcDateBuilder.toFormatter(Locale.ENGLISH);

        warcDateBuilder.appendLiteral(':')
                .appendValue(ChronoField.SECOND_OF_MINUTE, 2);
        WARC_DATE_OUTPUT_FORMAT[Granularity.SECOND.ordinal()] = warcDateBuilder.toFormatter(Locale.ENGLISH);

        warcDateBuilder.appendFraction(ChronoField.NANO_OF_SECOND, 9, 9, true)
                .appendOffsetId();
        WARC_DATE_OUTPUT_FORMAT[Granularity.NANOSECOND.ordinal()] = warcDateBuilder.toFormatter(Locale.ENGLISH);
    }

    /**
     * Format for parsing Heritrix date syntax.
     */
    private static final DateTimeFormatter HERITRIX_DATE_PARSE_FORMAT = new DateTimeFormatterBuilder()
            .appendValue(ChronoField.YEAR, 4)
            .optionalStart()
            .appendValue(ChronoField.MONTH_OF_YEAR, 2)
            .optionalStart()
            .appendValue(ChronoField.DAY_OF_MONTH, 2)
            .optionalStart()
            .appendValue(ChronoField.HOUR_OF_DAY, 2)
            .optionalStart()
            .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
            .optionalStart()
            .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
            .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, false)
            .optionalEnd()
            .optionalEnd()
            .optionalEnd()
            .optionalEnd()
            .optionalEnd()
            .parseDefaulting(ChronoField.OFFSET_SECONDS, 0)
            .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
            .toFormatter(Locale.ENGLISH);

    /**
     * Array of formats for formatting dates of different granularities allowed in the Heritrix date format.
     */
    private static final DateTimeFormatter[] HERITRIX_DATE_OUTPUT_FORMAT;

    static {
        HERITRIX_DATE_OUTPUT_FORMAT = new DateTimeFormatter[Granularity.values().length];

        DateTimeFormatterBuilder warcDateBuilder = new DateTimeFormatterBuilder()
                .appendValue(ChronoField.YEAR, 4);
        HERITRIX_DATE_OUTPUT_FORMAT[Granularity.YEAR.ordinal()] = warcDateBuilder.toFormatter(Locale.ENGLISH);

        warcDateBuilder.appendValue(ChronoField.MONTH_OF_YEAR, 2);
        HERITRIX_DATE_OUTPUT_FORMAT[Granularity.MONTH.ordinal()] = warcDateBuilder.toFormatter(Locale.ENGLISH);

        warcDateBuilder.appendValue(ChronoField.DAY_OF_MONTH, 2);
        HERITRIX_DATE_OUTPUT_FORMAT[Granularity.DAY.ordinal()] = warcDateBuilder.toFormatter(Locale.ENGLISH);

        warcDateBuilder.appendValue(ChronoField.HOUR_OF_DAY, 2);
        HERITRIX_DATE_OUTPUT_FORMAT[Granularity.HOUR.ordinal()] = warcDateBuilder.toFormatter(Locale.ENGLISH);

        warcDateBuilder.appendValue(ChronoField.MINUTE_OF_HOUR, 2);
        HERITRIX_DATE_OUTPUT_FORMAT[Granularity.MINUTE.ordinal()] = warcDateBuilder.toFormatter(Locale.ENGLISH);

        warcDateBuilder.appendValue(ChronoField.SECOND_OF_MINUTE, 2);
        HERITRIX_DATE_OUTPUT_FORMAT[Granularity.SECOND.ordinal()] = warcDateBuilder.toFormatter(Locale.ENGLISH);

        warcDateBuilder.appendFraction(ChronoField.NANO_OF_SECOND, 9, 9, false);
        HERITRIX_DATE_OUTPUT_FORMAT[Granularity.NANOSECOND.ordinal()] = warcDateBuilder.toFormatter(Locale.ENGLISH);
    }

    final OffsetDateTime date;

    final Granularity granularity;

    /**
     * Constructs a new immutable CdxDate.
     * <p>
     * @param date the date for this CdxDate.
     * @param granularity the granularity for this CdxDate.
     */
    public CdxDate(final OffsetDateTime date, final Granularity granularity) {
        this.date = date;
        this.granularity = granularity;
    }

    /**
     * Obtains an instance of CdxDate from a text string.
     * <p>
     * The string must represent a valid date-time in one of WARC or Heritrix date formats.
     * <p>
     * @param dateString in WARC or Heritrix date formats.
     * @return the parsed CdxDate, not null.
     * @throws DateTimeParseException if the date cannot be parsed.
     */
    public static CdxDate of(String dateString) {
        String[] tokens = dateString.split("[-T:\\.Z]");

        if (tokens.length == 1 && tokens[0].length() > 4) {
            // No delimiters found and value is longer than year, assume it is a Heritrix formatted date.
            return fromHeritrixDate(dateString);
        } else {
            Granularity granularity = Granularity.values()[tokens.length - 1];
            OffsetDateTime date = WARC_DATE_PARSE_FORMAT.parse(dateString, OffsetDateTime::from);

            return new CdxDate(date, granularity);
        }
    }

    /**
     * Create a new CdxDate from a Heritrix formatted date.
     * <p>
     * @param heritrixDate the date to parse.
     * @return the validated CdxDate.
     */
    static CdxDate fromHeritrixDate(String heritrixDate) {
        Granularity granularity;
        switch (heritrixDate.length()) {
            case 4:
                granularity = Granularity.YEAR;
                break;
            case 6:
                granularity = Granularity.MONTH;
                break;
            case 8:
                granularity = Granularity.DAY;
                break;
            case 10:
                granularity = Granularity.HOUR;
                break;
            case 12:
                granularity = Granularity.MINUTE;
                break;
            case 14:
                granularity = Granularity.SECOND;
                break;
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
                granularity = Granularity.NANOSECOND;
                break;
            default:
                throw new IllegalArgumentException("Could not parse date: " + heritrixDate);
        }

        OffsetDateTime date = HERITRIX_DATE_PARSE_FORMAT.parse(heritrixDate, OffsetDateTime::from);

        return new CdxDate(date, granularity);
    }

    /**
     * Compute the time between this and another CdxDate.
     * <p>
     * @param other the date to compute the distance to.
     * @return the absolute duration between the two dates.
     */
    public Duration distanceTo(CdxDate other) {
        return Duration.between(date, other.date).abs();
    }

    /**
     * Get the immutable temporal of this CdxDate.
     * <p>
     * @return the date.
     */
    public OffsetDateTime getDate() {
        return date;
    }

    /**
     * Get the granularity of this CdxDate.
     * <p>
     * @return the granularity.
     */
    public Granularity getGranularity() {
        return granularity;
    }

    /**
     * Get the date formatted as a WARC date.
     * <p>
     * @return the formatted string.
     */
    public String toWarcDateString() {
        return WARC_DATE_OUTPUT_FORMAT[granularity.ordinal()].format(date);
    }

    /**
     * Get the date formatted as a Heritrix date.
     * <p>
     * @return the formatted string.
     */
    public String toHeritrixDateString() {
        return HERITRIX_DATE_OUTPUT_FORMAT[granularity.ordinal()].format(date);
    }

    @Override
    public String toString() {
        return "CdxDate{" + "date=" + date + ", granularity=" + granularity + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.date);
        hash = 59 * hash + Objects.hashCode(this.granularity);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CdxDate other = (CdxDate) obj;
        if (!Objects.equals(this.date, other.date)) {
            return false;
        }
        if (this.granularity != other.granularity) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(CdxDate other) {
        return this.date.compareTo(other.date);
    }
}
