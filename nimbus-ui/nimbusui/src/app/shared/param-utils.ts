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

    public static DATE_TYPE_MAPPINGS: any = {
        DATE: 'Date',
        LOCAL_DATE: 'LocalDate',
        LOCAL_DATE_TIME: 'LocalDateTime',
        ZONED_DATE_TIME: 'ZonedDateTime'
    };

    /**
     * <p>Checks to see if the provided the <tt>dataType</tt> is matching a known
     * type that is identified by the UI framework as a <tt>Date<tt> object.</p>
     * 
     * @param dataType the name of the type to check if whether or not it is a known
     * date type.
     */
    public static isKnownDateType(dataType: string): boolean {
        for(let x in ParamUtils.DATE_TYPE_MAPPINGS) {
            if (ParamUtils.DATE_TYPE_MAPPINGS[x] === dataType) {
                return true;
            }
        }
        return false;
    }
}