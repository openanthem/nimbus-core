import { BaseElement } from './../../base-element.component';
import { WebContentSvc } from './../../../../services/content-management.service';
import { PageService } from './../../../../services/page.service';
import { Param } from './../../../../shared/app-config.interface';
import { ComponentFactoryResolver, ViewChild,
    ComponentRef, ViewContainerRef, Input } from '@angular/core';
import { Component, OnInit, forwardRef, SimpleChanges } from '@angular/core';
import { ComboBox } from './combobox.component';
import { TextArea } from './textarea.component';
import { InputText } from './textbox.component';
import { BaseControl } from './base-control.component';
import { NG_VALUE_ACCESSOR } from '@angular/forms';

export const InputComponents = [
    InputText,
    TextArea,
    ComboBox
];

@Component({
    selector: 'inplace-editor',
    template: `
        <div class="form-group {{editClass}}">
            <label *ngIf="label">
                {{label}}
                <nm-tooltip *ngIf="helpText" [helpText]='helpText'></nm-tooltip>
            </label>
            <a class="form-control-static editTrigger" href="javascript:void(0);" (click)="enableEdit()" *ngIf="element.enabled?.currState">
                <span *ngIf="displayValue==UNASSIGNVALUE" class="unassigned">{{displayValue}}</span>
                <span *ngIf="displayValue!=UNASSIGNVALUE">{{displayValue}}</span>
            </a>
            <div *ngIf="!element.enabled?.currState">
                <span *ngIf="displayValue==UNASSIGNVALUE" class="unassigned">{{displayValue}}</span>
                <span *ngIf="displayValue!=UNASSIGNVALUE">{{displayValue}}</span>
            </div>
            <div class="editTarget">
                <div #container></div>
            </div>
            <div class="saveOptions editTarget">
                <button class="editSubmit" (click)="onSubmit()" title="press enter to submit"></button>
                <button class="editCancel" (click)="cancel()" title="press escape to cancel"></button>
            </div>
        </div>
    `,
    providers: [
        WebContentSvc,
        {provide: NG_VALUE_ACCESSOR,
        useExisting: forwardRef(() => InPlaceEditorComponent),
        multi: true
    }],
    entryComponents: InputComponents
})

export class InPlaceEditorComponent extends BaseElement implements OnInit {
    
    public editClass: string;
    public label: string;
    public UNASSIGNVALUE = 'Unassigned';
    public displayValue = '';
    private _value = '';
    private preValue = '';

    private componentRef: ComponentRef<{}>;
    @ViewChild('container', { read: ViewContainerRef })
    private container: ViewContainerRef;
    private inputInstance: BaseControl<String>;

    // Input tyles
    private components: { [key: string]: any } = {
        Text: InputText,
        Textarea: TextArea,
        ComboBox: ComboBox
    };

    public get value(): any { return this._value; };

    public set value(newValue: any) {
        if (newValue !== this._value) {
            this._value = newValue;
            if (this._value && this._value !== '') {
                this.setDisplayValue(this._value);
            } else {
                this.displayValue = this.UNASSIGNVALUE;
            }
        }
    }

    constructor(public componentFactoryResolver: ComponentFactoryResolver, private pageService: PageService, private _wcs: WebContentSvc) {
        super(_wcs);
    }

    ngOnInit() {
        super.ngOnInit();
        this.editClass = '';
        this.value = this.element.leafState;
        this.setDisplayValue(this.value);
        this.generateComponent(this.type);
        
        this.pageService.eventUpdate$.subscribe(event => {
            if(this.element.config.code === event.config.code)
                this.setDisplayValue(event.leafState);
        });
    }

    ngOnChanges(changes: SimpleChanges) {
        if(changes['element']) {
            this.setDisplayValue(this.element.leafState);
        }
    }

    enableEdit() {
        this.preValue = this.value;
        this.editClass = 'editOn';
    }

    onSubmit() {
        this.editClass = '';
        this.pageService.postOnChange(this.element.path, 'state', JSON.stringify(this.value));
    }

    cancel() {
        this.value = this.preValue;
        this.editClass = '';
    }

    setDisplayValue(leafState: string) {
        // if list values, get description for code
        if (!leafState) {
            this.displayValue = this.UNASSIGNVALUE;
            return;
        }
        if (this.element.values && this.element.values.length > 0) {
            let desc: string = undefined;
            let indexVal: any = undefined;
            for (let i = 0; i < this.element.values.length; i++) {
                indexVal = this.element.values[i];
                if (indexVal.code === leafState) {
                    desc = indexVal.label;
                    break;
                }
            }
            if (desc) {
                this.displayValue = desc;
            } else  {
                this.displayValue = this.element.leafState;
            }
        } else {
            this.displayValue = this.element.leafState;
        }

        if (this.displayValue === '') {
            this.displayValue = this.UNASSIGNVALUE;
        }
    }

    public get type(): string {
        return this.element.config.uiStyles.attributes.inplaceEditType;
    }

    private getComponentType(typeName: string): any {
        const type = this.components[typeName];

        if (!type) {
            throw new Error('Type not implemented yet!');
        }

        return type;
    }

    private generateComponent(type: string) {
        const componentType = this.getComponentType(type);
        this.inputInstance = this.createInputInstance(componentType);
        this.inputInstance.element = this.element;
        this.inputInstance.setInPlaceEditContext(this);
    }

    private createInputInstance(componentType): BaseControl<String> {
        const factory = this.componentFactoryResolver.resolveComponentFactory(componentType);
        this.componentRef = this.container.createComponent(factory);

        return <BaseControl<String>>this.componentRef.instance;
    }

}