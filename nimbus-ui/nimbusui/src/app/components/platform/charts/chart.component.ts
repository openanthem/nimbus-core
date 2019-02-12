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
import { Component, Input } from '@angular/core';
import { Param } from '../../../shared/param-state';
import { DataSets, ChartData, DataGroup, ChartType } from './chartdata';
import { WebContentSvc } from './../../../services/content-management.service';
import { PageService } from './../../../services/page.service';
/**
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component({
  selector: 'nm-chart',
  template: `
        <ng-template [ngIf]="element?.visible"> 
            <nm-input-label *ngIf="!isLabelEmpty" [element]="element" [for]="element.config?.code" [required]="requiredCss">
            </nm-input-label>
            <p-chart [type]="chartType" [data]="data" [options]="options"></p-chart>
        </ng-template>
   `
})
export class NmChart {
    @Input() element: Param;
    @Input() data: any;
    chartType: String;
    options: any;
    constructor(private pageSvc: PageService) {}
    
    ngOnInit() {
      this.chartType = this.element.config.uiStyles.attributes.value.toLocaleLowerCase();
      if(this.chartType !== ChartType.pie.toString() && this.chartType !== ChartType.doughnut.toString()) {
        this.options = {
            scales: {
                xAxes: [ {
                    scaleLabel: {
                        display: this.element.config.uiStyles.attributes.xAxisLabel?true:false,
                        labelString: this.element.config.uiStyles.attributes.xAxisLabel,
                        fontStyle: 'bold'
                    },
                    ticks: {
                        fontStyle: 'bold',
                        autoSkip: false
                    }
                } ],
                yAxes: [{
                    scaleLabel: {
                        display: this.element.config.uiStyles.attributes.yAxisLabel?true:false,
                        labelString: this.element.config.uiStyles.attributes.yAxisLabel,
                        fontStyle: 'bold'
                    },
                    ticks: {
                        fontStyle: 'bold',
                        stepSize: this.element.config.uiStyles.attributes.stepSize?this.element.config.uiStyles.attributes.stepSize:null
                    }
                }]
            }
        };
      }

      this.pageSvc.eventUpdate$.subscribe(event => {
        if(event.path === this.element.path) {
            this.data = this.createChartData(this.element.leafState);
        }
      });
    }

    private  createChartData(data: DataGroup[]): ChartData {
        let chartData: ChartData  = new ChartData();
        let datasets: DataSets[] = [];
        chartData.labels = [];
        data.forEach(dtGroup => {
            let dts: DataSets = new DataSets();
            this.setMetadata(dts, dtGroup);
            dts.data = [];
            dtGroup.dataPoints.forEach(dp => {
                if(!chartData.labels.includes(dp.x)){
                    chartData.labels.push(dp.x);
                }
                if(this.chartType === ChartType.pie.toString() || this.chartType === ChartType.doughnut.toString()) {
                    dts['backgroundColor'].push(this.generateColor(null));
                    dts['hoverBackgroundColor'].push(this.generateColor(null));
                }
                dts.data.push(dp.y);
            })
            datasets.push(dts);
        }); 
        chartData.datasets = datasets;
        return chartData;
    }

    setMetadata(dts: DataSets, dtGroup: DataGroup) {
        if(this.chartType === ChartType.bar.toString()) {
            dts['backgroundColor'] = this.generateColor(null);
            dts['borderColor'] = this.generateColor(null);
            dts.label = dtGroup.legend;
        } else if(this.chartType === ChartType.pie.toString() || this.chartType === ChartType.doughnut.toString()) {
            dts['backgroundColor'] = [];
            dts['hoverBackgroundColor'] = [];
        }
        else if(this.chartType === ChartType.line.toString()) {
            dts['borderColor'] = this.generateColor(null);
            dts['fill'] = false;
            dts.label = dtGroup.legend;
        }
    }

    private generateColor(ranges) {
        if (!ranges) {
            ranges = [
                [0,256],
                [0, 256],
                [0, 256]
            ];
        }
        var g = function() {
            var range = ranges.splice(Math.floor(Math.random()*ranges.length), 1)[0];
            return Math.floor(Math.random() * (range[1] - range[0])) + range[0];
        }
        return "rgb(" + g() + "," + g() + "," + g() +")";
    };
}
