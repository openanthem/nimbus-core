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

import { ServiceConstants } from '../services/service.constants';
import { Param } from './param-state';
import { LabelConfig } from './param-config';
import { UiNature } from './param-config';

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
        if (!value) {
            return null;
        }

        var serverDateTime = new Date(value);
        switch(typeClassMapping) {
            case ParamUtils.DATE_TYPE_METADATA.LOCAL_DATE.name: {
                return new Date(serverDateTime.getUTCFullYear(), serverDateTime.getUTCMonth(), serverDateTime.getUTCDate());
            }

            case ParamUtils.DATE_TYPE_METADATA.LOCAL_DATE_TIME.name: {
                return new Date(serverDateTime.getUTCFullYear(), serverDateTime.getUTCMonth(), serverDateTime.getUTCDate(), 
                    serverDateTime.getHours(), serverDateTime.getMinutes(), serverDateTime.getSeconds());
            }

            default: {
                return new Date(serverDateTime.getFullYear(), serverDateTime.getMonth(), serverDateTime.getDate(), 
                    serverDateTime.getHours(), serverDateTime.getMinutes(), serverDateTime.getSeconds());
            }
        }
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

    /**
     * <p>Applies defined transformations on the provided leaf state <tt>obj</tt> that 
     * should be applied when deserializing the server-side response of a param into the 
     * <tt>Param</tt> the UI understands.</p>
     * 
     * <p>e.g. For Dates:</p>
     * 
     * <pre>
     * {
     *      "id": "1",
     *      "startDate": "2018-03-29T00:00:00.000Z",
     *      "additionalInformation": {
     *          "anotherDate": "2018-03-30T00:00:00.000Z"
     *      }
     * }
     * 
     * becomes:
     * 
     * {
     *      "id": "1",
     *      "startDate": Thu Mar 29 2018 00:00:00 GMT-0400 (EST) {},
     *      "additionalInformation": {
     *          "anotherDate": Thu Mar 30 2018 00:00:00 GMT-0400 (EST) {}
     *      }
     * }
     * </pre>
     * 
     * @param obj the leaf state or nested elements within leaf state to be transformed
     * @param relativeParam The param containing the leafState(<tt>obj</tt>).
     */
    public static applyLeafStateTransformations(obj: any, relativeParam: Param): any {

        // the transformed object to return
        var transformed = obj;

        // Check for collectionElement and add elementId to leafstate 
        if (relativeParam && relativeParam.collectionElem) { 
            transformed['elemId'] = relativeParam.elemId; 
        }

        // iterate over each of the properties of the object.
        for (var x in obj) {

            // Find the nested param associated with this obj[x].
            let x_param = ParamUtils.findParamByPath(relativeParam, x);

            // Check for collectionElement and add elementId to leafstate 
            if (x_param && x_param.collectionElem) { 
                if(transformed[x] instanceof Object)
                    transformed[x]['elemId'] = x_param.elemId; 
            }

            if (x_param ) {

                // if the param identified by x is a collection or nested element...
                if (x_param.config && (x_param.config.type.collection || x_param.config.type.nested)) {
                    
                    // if we have what we need, then apply the transformations recursively.
                    if (x_param.type.model && x_param.type.model.params) {
                        transformed[x] = this.applyLeafStateTransformations(obj[x], x_param);
                    
                    // if model or params are not found, we can't continue.
                    } else {
                        console.warn(`Unable to find params for ${x} in ${obj[x]}`);
                        return;
                    }
                } else if (x_param.collectionElem) {
                    // apply the transformations recursively.
                    if (x_param.type.model && x_param.type.model.params) {
                        transformed[x] = this.applyLeafStateTransformations(obj[x], x_param);
                    }
                } else { // Otherwise, this element is a "simple type" and we can apply the transformations
                    
                    // Handle Date transformations
                    if (x_param.config.type && ParamUtils.isKnownDateType(x_param.config.type.name)) {
                       transformed[x] = ParamUtils.convertServerDateStringToDate(obj[x], x_param.config.type.name);
                    }
                }
            }
        }

        return transformed;
    }

    /**
     * <p>Traverses from <tt>relativeParam</tt> to find the param identified by <tt>key</tt>.
     * If a param is unable to be located, <tt>null</tt> is returned.</p>
     * 
     * <p>Uses the path found in <tt>relativeParam</tt> and recursively traverses for each element
     * in <tt>key</tt> delimited by the path separator.</p>
     * 
     * <p>e.g. Assume param is given and param.path is '/page/tile/section/form'. Then: </p>
     * 
     * <ul>
     * <li>ParamUtils.findParamByPath(param, 'textbox') returns the param at '/page/tile/section/form/textbox'</li>
     * <li>ParamUtils.findParamByPath(param, 'section/textbox') returns the param at '/page/tile/section/form/section/textbox'</li>
     * </ul>
     * 
     * <p>Currently, support for findParamByPath only works traversing forward in the param path, not backwards.</p>
     * 
     * @param relativeParam the relative param to use in locating the param to return
     * @param key the key used to locate the param to return (relative to relativeParam)
     */
    public static findParamByPath(relativeParam: Param, key: string): Param {
        if (!relativeParam) {
            console.warn('relativeParam can not be null or undefined.');
            return null;
        }
        if (!key) {
            console.warn('key can not be null or undefined.');
            return null;
        }
        if (!(relativeParam.type && relativeParam.type.model && relativeParam.type.model.params)) {
            console.warn(`Failed to find children params of param: ${relativeParam}`);
            return null;
        }

        let key_parts = key.split(ServiceConstants.PATH_SEPARATOR);
        let path = `${relativeParam.path}/${key_parts[0]}`;
        let params = relativeParam.type.model.params.filter(p => p.path == path);
        if (params.length > 1) {
            console.warn(`Found more than 1 params for path '${path}' in ${params}`);
            return null;
        }

        if (key_parts.length === 1) {
            return params[0];
        } else {
            return ParamUtils.findParamByPath(params[0], key_parts.slice(1).join(ServiceConstants.PATH_SEPARATOR));
        }
    }

    /**
     * <p>Checks to see if JSON object is empty or not
     */
    static isEmpty(obj): boolean {
        for(var prop in obj) {
            if(obj.hasOwnProperty(prop))
                return false;
        }
    
        return true;
    }

    static getHelpText(labelConfig: LabelConfig) {
        if (!labelConfig) {
            return undefined;
        }
        return labelConfig.helpText;
    }

    static getLabelText(labelConfig: LabelConfig) {
        if (!labelConfig) {
            return undefined;
        }
        return labelConfig.text;
    }

    /**
     * Retrieve a UiNature by name from uiNatures. If not uiNatures is undefined or the uiNature 
     * by name is not found, undefined is returned.
     * @param param The param to inspect
     * @param name The name of the uiNature on this param to find
     */
    static getUiNature(param: Param, name: string): UiNature {
        let uiNatures = ParamUtils.getUiNatures(param);
        return !uiNatures ? undefined : uiNatures.find(uiNature => uiNature.name === name);
    }
    
    /**
     * Get the UiNature[] if it exists and is non-empty on this instance
     * @param param The param to inspect
     */
    static getUiNatures(param: Param): UiNature[] {
        if (param.config && param.config.uiNatures && param.config.uiNatures.length > 0) {
            return param.config.uiNatures;
        } else {
            return undefined;
        }
    }

    static getDomainIdFromPath(path: string): string {
        if (!path) {
            return undefined;
        }
        return path.split('/')[1];
    }

    static getPageIdFromPath(path: string): string {
        if (!path) {
            return undefined;
        }
        return path.split('/')[2];
    }

    static getDomainPageFromPath(path: string): string {
        let domain = ParamUtils.getDomainIdFromPath(path);
        if (!domain) {
            return undefined;
        }
        let page = ParamUtils.getPageIdFromPath(path);
        if (!page) {
            return undefined;
        }
        return `/${domain}/${page}`;
    }

    /**
     * <p>This method resolves a relative path based on the current param's path.</p>
     * <p> If paramPath is /currentDomain/page/section/grid , then various examples of relative path that can be resolved are :
     * '../postButton' =>  /currentDomain/page/section/postButton
     * '<!#this!>/../postButton' => /currentDomain/page/section/postButton
     * '/p/anotherdomain/page/section' => null (This will throw an error as cross domain requests need to be used using @Config)
     * '/currentdomain/page/section/tile' => /currentdomain/page/section/tile
     * '../../tile2/section2/postButton' => /currentdomain/page/tile2/section2/postButton
     * .d/page/tile/section
     * @param paramPath
     * @param pathToResolve 
     */
    public static resolveParamUri(paramPath: string, pathToResolve: string): string {
        if (pathToResolve === undefined || pathToResolve === null || pathToResolve.length === 0) {
           return null;
       }
        if (pathToResolve.startsWith(`${ServiceConstants.PLATFORM_SEPARATOR}/`)) {
           console.error('Cannot use cross domain uri here. Consider using @Config on a param for cross domain requests');
           return null;
       }
        const key_parts = pathToResolve.split(ServiceConstants.PATH_SEPARATOR);
       if (key_parts.length === 0) {
           return pathToResolve;
       }
       // If absolute uri is given - exit immediately with the pathToResolve.
       if (pathToResolve.indexOf(ServiceConstants.PATH_SEPARATOR) === 0) {
           return pathToResolve;
       }
        const currTopParamPathSegment = key_parts[0];
       const currentTopNestedParamPath = this.resolvePathConstants(paramPath, currTopParamPathSegment);
        if (key_parts.length === 1) {
           return currentTopNestedParamPath;
       }
       return this.resolveParamUri(currentTopNestedParamPath, key_parts.slice(1).join(ServiceConstants.PATH_SEPARATOR));
   }
    public static resolvePathConstants(currentPath: string, resolvePathSegment: string): string {
        const currentPath_parts = currentPath.split(ServiceConstants.PATH_SEPARATOR);
       // If resolvePathSegment is '..', then strip the last param of current path
       // Ex: currentPath = /currentdomain/page/section/grid, resolvePathSegment = '..' -> return '/currentdomain/page/section'
       if (resolvePathSegment === ServiceConstants.SEPARATOR_URI_PARENT) {
           return currentPath_parts.slice(0 , currentPath_parts.length - 1).join(ServiceConstants.PATH_SEPARATOR);
        } else if (resolvePathSegment === ServiceConstants.MARKER_COMMAND_PARAM_CURRENT_SELF) {
       // Ex: currentPath = /currentdomain/page/section/grid, resolvePathSegment = '<!#this!>' -> return '/currentdomain/page/section/grid'
           return currentPath;
        } else if (resolvePathSegment === ServiceConstants.SEPARATOR_URI_ROOT_DOMAIN) {
       // Ex: currentPath = /currentdomain/page/section/grid, resolvePathSegment = '.d' -> return '/currentdomain'
           return currentPath_parts.slice(0, 2).join(ServiceConstants.PATH_SEPARATOR);
        } else {
   // Ex: currentPath = /currentdomain/page/section/grid, resolvePathSegment = 'button' -> return '/currentdomain/page/section/grid/button'
           return currentPath.concat(ServiceConstants.PATH_SEPARATOR).concat(resolvePathSegment);
       }
   }
}