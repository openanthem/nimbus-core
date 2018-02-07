import { LabelConfig } from './../../shared/app-config.interface';
import { Component, Input, forwardRef } from '@angular/core';
import { Param } from '../../shared/app-config.interface';
import { FormGroup, NG_VALUE_ACCESSOR } from '@angular/forms';
import { WebContentSvc } from '../../services/content-management.service';
import { BaseControl } from './form/elements/base-control.component';
import { PageService } from '../../services/page.service';

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => FrmGroupCmp),
    multi: true
  };

@Component({
    selector: 'nm-frm-grp',
    providers: [CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR, WebContentSvc],
    template:`
        <div class="col-lg-12 clearfix">
            <ng-template ngFor let-element let-isFirst="first" [ngForOf]="elements">
                <ng-template [ngIf]="isFirst">
                    <legend *ngIf="label && element.visible?.currState">
                        {{label}}
                    </legend>
                </ng-template>
                <ng-template [ngIf]="!element.type?.model?.params?.length || element.collection">
                    <nm-element id="{{id}}" [element]="element" [elementCss]="elementCss" [form]="form"></nm-element>
                </ng-template>
                <ng-template [ngIf]="element.type?.model?.params?.length && element.config?.uiStyles?.attributes?.alias!='ButtonGroup' && !element.collection">
                    <fieldset class="subQuestion" [hidden]="!element?.visible?.currState">
                        <nm-frm-grp [elements]="element.type?.model?.params" [form]="form.controls[element.config?.code]" [elementCss]="elementCss" [parentElement]="element"></nm-frm-grp>
                    </fieldset>
                </ng-template>
                 <ng-template [ngIf]="element.config?.uiStyles?.attributes?.alias=='Button'">
                    <nm-button [form]="form" [element]="element"> </nm-button>
                </ng-template>
                <ng-template [ngIf]="element.config?.uiStyles?.attributes?.alias=='ButtonGroup'">
                    <nm-button-group [form]="form" [buttonList]="element.type?.model?.params" [cssClass]="element.config?.uiStyles?.attributes?.cssClass"> </nm-button-group>
                </ng-template>
            </ng-template>
        </div>
    `
})
export class FrmGroupCmp {
    
       @Input() elements: Param[] = [];
       @Input() form: FormGroup;
       @Input() elementCss : String;
       @Input() parentElement: Param
       private label: string;
       private helpText : string;

       constructor(private wcs: WebContentSvc) {
           
       }

       ngOnInit() {
            if (this.hasParagraph(this.parentElement)) {
                let labelConfig: LabelConfig = this.wcs.findLabelContent(this.parentElement);
                this.label = labelConfig.text;
                this.helpText = labelConfig.helpText;
            }
       }

       hasParagraph(element: Param): boolean {
           return element && element.config && element.config.uiStyles && element.config.uiStyles.attributes && element.config.uiStyles.attributes.alias=='Paragraph';
       }
   }
