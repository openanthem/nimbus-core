/**
 * @license
 * Copyright Anthem Inc. All Rights Reserved.
 *
 * This source code is released under version 2.0 of the Apache License.
 * The LICENSE information can be found at http://www.apache.org/licenses/LICENSE-2.0
 */

export interface IGenericValue {
    value: any;
}

export interface IGenericType {
    [name: string]: IGenericValue;
}

export class GenericDomain {
    //domain: any = {};
    [name: string]: any;

    addAttribute(name: string, value: any) {
        //this.domain[name] = value;
        this[name] = value;
    }
}
