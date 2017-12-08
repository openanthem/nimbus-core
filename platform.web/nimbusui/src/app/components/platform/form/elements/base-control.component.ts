'use strict';
import { BaseControlValueAccessor } from './control-value-accessor.component';
import { Input, Output, EventEmitter } from '@angular/core';
import { FormGroup, NgModel } from '@angular/forms';
import { Param } from '../../../../shared/app-config.interface';
import { PageService } from '../../../../services/page.service';
import { WebContentSvc } from '../../../../services/content-management.service';
import { GenericDomain } from '../../../../model/generic-domain.model';

export abstract class BaseControl<T> extends BaseControlValueAccessor<T> {
    @Input() element: Param;
    @Input() form: FormGroup;
    @Output() controlValueChanged =new EventEmitter();
    protected abstract model: NgModel;
    label: string;
    inPlaceEditContext: any;
    showLabel: boolean = true;

    constructor(private pageService: PageService, private wcs: WebContentSvc) {
        super();
        wcs.content$.subscribe(result => {
            if (this.element && result.id === this.element.config.code) {
                this.label = result.label;
            }
        });
    }

    setState(event:any,frmInp:any) {
        frmInp.element.leafState = event;
        //console.log(frmInp.element.leafState);
    }

    emitValueChangedEvent(formControl:any,$event:any) {
        if (this.inPlaceEditContext) {
            this.inPlaceEditContext.value = formControl.value;
        }
        this.controlValueChanged.emit(formControl.element);
    }

    ngOnInit() {
        this.value = this.element.leafState;
        if(this.form!= undefined && this.form.controls[this.element.config.code]!= null) {
            this.form.controls[this.element.config.code].valueChanges.subscribe(($event) => this.setState($event,this));
        }
        this.wcs.getContent(this.element.config.code);
        this.controlValueChanged.subscribe(($event) => {
             //console.log($event);
             if ($event.config.uiStyles.attributes.postEventOnChange) {
                this.pageService.postOnChange($event.path, 'state', JSON.stringify($event.leafState));
             } else if($event.config.uiStyles.attributes.postButtonUrl) {
                let item: GenericDomain = new GenericDomain();
                //item.addAttribute($event.config.code,$event.leafState);
                this.pageService.processPost(this.element.config.uiStyles.attributes.postButtonUrl, null, $event.leafState, 'POST');
             }
         });
    }

    /** invoked from InPlaceEdit control */
    setInPlaceEditContext(context: any) {
        this.showLabel = false;
        this.inPlaceEditContext = context;
    }

    /**
     * The hidden attribute for this param
     */
    public get hidden(): boolean {
        return this.element.config.uiStyles.attributes.hidden;
    }

    /**
     * The help attribute for this param
     */
    public get help(): string {
        return this.element.config.uiStyles.attributes.help;
    }

    /**
     * The help readOnly for this param
     */
    public get readOnly(): boolean {
        return this.element.config.uiStyles.attributes.readOnly;
    }

    /**
     * The type attribute for this param
     */
    public get type(): string {
        return this.element.config.uiStyles.attributes.type;
    }
}
