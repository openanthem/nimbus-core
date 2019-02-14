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
import { Converter } from './../../../shared/object.conversion';
import { Serializable } from './../../../shared/serializable';
import { Enum } from './../../../shared/command.enum';

/**
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
export class DataGroup implements Serializable<DataGroup, string> {
    legend: string;
    dataPoints: DataPoint[];
    deserialize(inJson) {
        let obj = this;
        let ds = [];
        if ( inJson.dataPoints != null && inJson.dataPoints.length > 0 ) {
            for ( var dps in inJson.dataPoints ) {
                ds.push( new DataPoint().deserialize( inJson.dataPoints[dps] ) );
            }
        }
        obj['dataPoints'] = ds;
        obj['legend'] = inJson.legend;
        return obj;
    }
}

export class DataPoint implements Serializable<DataPoint, any> {
    x:any;
    y:any;
    deserialize( inJson ) {
        let obj = this;
        obj = Converter.convert(inJson, obj);
        return obj;
    }
}

export class ChartData {
    labels: string[];
    datasets: DataSets[];
}

export class DataSets {
    label: string;
    data: string[];
}

export class ChartType extends Enum<string> {

    public static readonly bar = new Enum('bar');
    public static readonly line = new Enum('line');
    public static readonly pie = new Enum('pie');
    public static readonly doughnut = new Enum('doughnut');
}