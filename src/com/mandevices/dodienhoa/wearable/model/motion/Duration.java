/*
 * (c) 2014-2020, Cypress Semiconductor Corporation or a subsidiary of 
 * Cypress Semiconductor Corporation.  All rights reserved.
 * 
 * This software, including source code, documentation and related 
 * materials ("Software"),  is owned by Cypress Semiconductor Corporation 
 * or one of its subsidiaries ("Cypress") and is protected by and subject to 
 * worldwide patent protection (United States and foreign), 
 * United States copyright laws and international treaty provisions.  
 * Therefore, you may use this Software only as provided in the license 
 * agreement accompanying the software package from which you 
 * obtained this Software ("EULA").
 * If no EULA applies, Cypress hereby grants you a personal, non-exclusive, 
 * non-transferable license to copy, modify, and compile the Software 
 * source code solely for use in connection with Cypress's 
 * integrated circuit products.  Any reproduction, modification, translation, 
 * compilation, or representation of this Software except as specified 
 * above is prohibited without the express written permission of Cypress.
 * 
 * Disclaimer: THIS SOFTWARE IS PROVIDED AS-IS, WITH NO WARRANTY OF ANY KIND, 
 * EXPRESS OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, NONINFRINGEMENT, IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. Cypress 
 * reserves the right to make changes to the Software without notice. Cypress 
 * does not assume any liability arising out of the application or use of the 
 * Software or any product or circuit described in the Software. Cypress does 
 * not authorize its products for use in any products where a malfunction or 
 * failure of the Cypress product may reasonably be expected to result in 
 * significant property damage, injury or death ("High Risk Product"). By 
 * including Cypress's product in a High Risk Product, the manufacturer 
 * of such system or application assumes all risk of such use and in doing 
 * so agrees to indemnify Cypress against all liability.
 */

package com.mandevices.dodienhoa.wearable.model.motion;

import com.mandevices.dodienhoa.wearable.model.ValueWithUnit;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

// TODO: default unit?
public class Duration extends ValueWithUnit<Duration, Duration.Unit> {

    private static final Unit DEFAULT_UNIT = Unit.SECONDS;
    private static final int EXPONENT = 0;
    private static final SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm:ss");
    private static final Calendar mCalendar = new GregorianCalendar();

    public Duration() {
        super(DEFAULT_UNIT);
    }

    public static class Unit extends ValueWithUnit.Unit {

        public static final Unit SECONDS = new Unit("ss");
        public static final Unit MINUTES = new Unit("mm");
        public static final Unit HOURS = new Unit("hh");
        public static final Unit TIME = new Unit("hh:mm:ss");
        private static final Unit[] ALL_UNITS = {SECONDS, MINUTES, HOURS, TIME};

        private Unit(String text) {
            super(text);
        }
    }

    @Override
    public Unit[] getSupportedUnits() {
        return Unit.ALL_UNITS;
    }

    @Override
    protected double convert(double value, Unit from, Unit to) {
        if (from == Unit.SECONDS) {
            if (to == Unit.MINUTES) {
                value /= 60;
            } else if (to == Unit.HOURS) {
                value /= 3600;
            } else if (to == Unit.TIME) {
                // leave value in seconds
            }
        } else {
            if (to != Unit.SECONDS) {
                // first convert to seconds...
                double seconds = convert(value, from, Unit.SECONDS);
                // ... then convert to final unit
                return convert(seconds, Unit.SECONDS, to);
            }
            if (to != Unit.SECONDS) {
                throw new IllegalArgumentException();
            }
            if (from == Unit.MINUTES) {
                value *= 60;
            } else if (from == Unit.HOURS) {
                value *= 3600;
            } else if (from == Unit.TIME) {
                // value already in seconds
            }
        }
        return value;
    }

    @Override
    public String getValueString() {
        if (getUnit() == Unit.TIME) {
            mCalendar.set(Calendar.HOUR_OF_DAY, 0);
            mCalendar.set(Calendar.MINUTE, 0);
            mCalendar.set(Calendar.SECOND, (int) convert(getValue(), Unit.TIME, Unit.SECONDS));
            mCalendar.set(Calendar.MILLISECOND, 0);
            return mFormat.format(mCalendar.getTime());
        }
        return super.getValueString();
    }

    @Override
    protected Unit getDefaultUnit() {
        return DEFAULT_UNIT;
    }

    @Override
    protected double getExponent() {
        return EXPONENT;
    }
}
