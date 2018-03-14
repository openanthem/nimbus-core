import { Param } from './app-config.interface';
/**
 * @license
 * Copyright 2016-2018 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
'use strict';

import { ConfigService } from './../services/config.service';
import { SortAs } from "../components/platform/grid/sortas.interface";
import { PageService } from '../services/page.service';
import { GridService } from '../services/grid.service';

/**
 * \@author Tony.Lopez
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
export class ParamUtils {

    // TODO - Move default date formats to spring config
    public static DATE_TYPE_METADATA: any = {
        DATE:               { name: 'Date', defaultFormat: 'MM/dd/yyyy hh:mm a' },
        LOCAL_DATE:         { name: 'LocalDate', defaultFormat: 'MM/dd/yyyy' },
        LOCAL_DATE_TIME:    { name: 'LocalDateTime', defaultFormat: 'MM/dd/yyyy hh:mm a' },
        ZONED_DATE_TIME:    { name: 'ZonedDateTime', defaultFormat: 'MM/dd/yyyy hh:mm a' },
    };

    public static DEFAULT_DATE_FORMAT: string = 'MM/dd/yyyy hh:mm a';

    /**
     * <p>Checks to see if the provided the <tt>typeClassMapping</tt> is matching a known
     * type that is identified by the UI framework as a <tt>Date<tt> object.</p>
     * 
     * @param typeClassMapping the name of the type to check if whether or not it is a known
     * date type.
     */
    public static isKnownDateType(typeClassMapping: string): boolean {
        for(let x in ParamUtils.DATE_TYPE_METADATA) {
            if (ParamUtils.DATE_TYPE_METADATA[x].name === typeClassMapping) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>Converts any date objects coming from the server (see: 
     * <tt>ParamUtils.DATE_TYPE_MAPPINGS.LOCAL_DATE</tt>) to an equivalent Javascript <tt>Date</tt>
     * object.</p>
     * 
     * <p>The server time string is under contract to always come back in the UTC time zone, in the
     * ISO Date Format of yyyy-MM-ddThh:mm:ss.SSSZ. Consequently, the converted <tt>Date</tt> object will 
     * be converted to the browser's current time zone.</p>
     * 
     * <p>This method will effectively ignore the time zone by adding the offset time 
     * between the UTC time zone and the browser time zone when doing the <tt>Date</tt> conversion. e.g. A 
     * <tt>LocalDate.of(1931, 2, 4)</tt> from the server would give:</p>
     * 
     * <ul>
     *  <li>Response from server: '1931-02-04T00:00:00.000Z'</li>
     *  <li>*Converted <tt>Date</tt>: Wed Feb 04 1931 00:00:00 GMT-0500 (EST)</li>
     * </ul>
     * 
     * <p>instead of:</p>
     * 
     * <ul>
     *  <li>Response from server: '1931-02-04T00:00:00.000Z'</li>
     *  <li>*Converted <tt>Date</tt>: Tue Feb 03 1931 19:00:00 GMT-0500 (EST)</li>
     * </ul>
     * 
     * <p>* Assumes browser is in EST timezone.</p>
     * 
     * @param value the server date string
     * @param typeClassMapping the class type of the server date object
     */
    public static convertServerDateStringToDate(value: string, typeClassMapping: string): Date {
        if (value) {
            var serverDateTime = new Date(value);
            if (typeClassMapping === ParamUtils.DATE_TYPE_METADATA.LOCAL_DATE.name) {
                return new Date(serverDateTime.getUTCFullYear(), serverDateTime.getUTCMonth(), serverDateTime.getUTCDate());
            } else {
                return new Date(serverDateTime.getUTCFullYear(), serverDateTime.getUTCMonth(), serverDateTime.getUTCDate(), 
                    serverDateTime.getHours(), serverDateTime.getMinutes(), serverDateTime.getSeconds());
            }
        }
        return null;
    }

    /**
     * <p>Provides a default format based on the type of object returned from the server.</p>
     * 
     * <p>Returns <tt>null</tt> if a default format is not found for <tt>typeClassMapping</tt>.
     * 
     * @param typeClassMapping the class type of the server date object
     */
    public static getDateFormatForType(typeClassMapping: string): string {
        for(let x in ParamUtils.DATE_TYPE_METADATA) {
            let dateTypeMetadata = ParamUtils.DATE_TYPE_METADATA[x];
            if (dateTypeMetadata.name === typeClassMapping) {
                return dateTypeMetadata.defaultFormat;
            }
        }
        return null;
    }

    public static stringify(obj: any): string {
        var json = JSON.stringify(obj, function(key, value) {
            if (typeof value === 'object' && value !== null) {
                    delete value['_params']
            }
            return value;
        });
        // console.log(`ParamUtils#stringify(string): json string value is: ${json}`);
        return json;
    }
}