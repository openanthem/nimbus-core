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
/**
 * \@author Dinakar.Meda
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
import { Serializable } from './serializable';

export class CardDetailsGrid implements Serializable<CardDetailsGrid,string> {
    cards: Array<CardDetails>;
    deserialize( inJson ) {
        this.cards = [];

        return this;
    }
}

export class CardDetails implements Serializable<CardDetails,string> {
    id: string;
    number: number;
    header: CardDetailsHeader;
    body: CardDetailsBody;
    footer: CardDetailsFooter;

    deserialize( inJson ) {
        this.id = inJson.id;
        this.number = inJson.number;
        this.header = new CardDetailsHeader().deserialize( inJson.header );
        this.body = new CardDetailsBody().deserialize( inJson.body );
        this.footer = new CardDetailsFooter().deserialize( inJson.footer );
        return this;
    }
}

export class CardDetailsHeader implements Serializable<CardDetailsHeader,string> {
    title: FieldValue;
    date: FieldValue;
    status: FieldValue;

    deserialize( inJson ) {
        this.title = new FieldValue().deserialize( inJson.title );
        this.date = new FieldValue().deserialize( inJson.date );
        this.status = new FieldValue().deserialize( inJson.status );
        return this;
    }
}

export class CardDetailsBody implements Serializable<CardDetailsBody,string> {
    fields: Array<FieldValue>;

    deserialize( inJson ) {
        this.fields = [];
        for ( var p in inJson.fields ) {
            this.fields.push( new FieldValue().deserialize( inJson.fields[p] ) );
        }
        return this;
    }
}

export class CardDetailsFooter implements Serializable<CardDetailsFooter,string> {
    fields: Array<FieldValue>;

    deserialize( inJson ) {
        this.fields = [];
        for ( var p in inJson.fields ) {
            this.fields.push( new FieldValue().deserialize( inJson.fields[p] ) );
        }
        return this;
    }
}

export class FieldValue implements Serializable<FieldValue,string> {
    field: string;
    value: any;

    deserialize( inJson ) {
        this.field = inJson.field;
        this.value = inJson.value;
        return this;
    }
}
