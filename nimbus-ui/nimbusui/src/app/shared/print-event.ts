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

import { UiNature } from './param-config';
import { Converter } from './object.conversion';

export interface PrintEvent {
    path: string;
    uiEvent: UIEvent;
    printConfig: PrintConfig;
}

export class PrintConfig {
    public static readonly DEFAULT_DELAY = 300;
    autoPrint: boolean = true;
    closeAfterPrint: boolean = true;
    delay: number = PrintConfig.DEFAULT_DELAY;
    stylesheet: string;
    useAppStyles: boolean = false;
    useDelay: boolean = true;

    static fromNature(uiNature: UiNature): PrintConfig {
        let obj = new PrintConfig();
        if (!uiNature) {
            return obj;
        }
        return Converter.convert(uiNature.attributes, obj);
    }
}