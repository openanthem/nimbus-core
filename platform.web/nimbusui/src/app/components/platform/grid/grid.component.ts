import { FormGroup } from '@angular/forms';
import { Component, Input, Output, ViewChild, EventEmitter, ViewEncapsulation } from '@angular/core';

import { GenericDomain } from '../../../model/generic-domain.model';
import { Param, ParamConfig } from '../../../shared/app-config.interface';
import { PageService } from '../../../services/page.service';
import { GridService } from '../../../services/grid.service';
import { WebContentSvc } from '../../../services/content-management.service';
import { DataTable, OverlayPanel } from 'primeng/primeng';
import { ServiceConstants } from './../../../services/service.constants';

@Component({
    selector: 'infinite-scroll-grid',
    providers: [ WebContentSvc ],
    encapsulation: ViewEncapsulation.None,
    templateUrl:'./grid.component.html'
})
export class InfiniteScrollGrid {
    @Input() element: Param;
    @Input() data: any[];
    @Output() onScrollEvent: EventEmitter<any> = new EventEmitter();
    @Input() params: Param[];

//    references DataTable named 'flex' in the view
    @ViewChild('flex') flex: DataTable;
    @ViewChild('dt') dt: DataTable;
    @ViewChild ('op') overlayPanel: OverlayPanel;
    
    summaryData: any;
    rowHover:boolean;
    selectedRows: any[];
    filterState: boolean = false;
    postButtonLabel: string;

    fg= new FormGroup({}); // TODO this is for the filter controls that need to be embedded in the grid 
    private imagesPath: string;
    
    constructor(private pageSvc : PageService, private wcs: WebContentSvc, private gridService: GridService) {
        this.wcs.content$.subscribe(result => {
            this.params.forEach(element => {
                if(element != null) {
                    switch (result.id) {
                        case element.config.code: {
                            element.config.label = result.label;
                            break;
                        }
                        case this.element.config.uiStyles.attributes.postButtonAlias: {
                            this.postButtonLabel = result.label;
                            break;
                        }
                    }
                }
            });
        });
    }

    ngOnInit() {
        this.imagesPath = ServiceConstants.IMAGES_URL;
        if (this.params != null) {
            this.params.forEach(element => {
                if(element != null) {
                    this.wcs.getContent(element.config.code);
                }
                if(element.config.uiStyles && element.config.uiStyles.attributes 
                        && element.config.uiStyles.attributes.filterValue && element.config.uiStyles.attributes.filterValue !== '') {
                        let filterValue = element.config.uiStyles.attributes.filterValue;
                        this.flex.filter(filterValue, element.config.code, element.config.uiStyles.attributes.filterMode);
                }
            });
        }

        if (this.element.config.uiStyles.attributes.onLoad === true) {
            this.pageSvc.processEvent(this.element.path, '$execute', new GenericDomain(), 'GET');
        }

        this.rowHover=true;
        this.gridService.eventUpdate$.subscribe(data => {
            this.summaryData = data;
        });

        this._initPostButton();
    }

    getContentById(id) {
        if (id != undefined) {
            this.wcs.getContent(id);
        }
    }

    processOnClick(col: Param, item: any) {
        let uri=this.element.path + '/' + item.elemId + '/' + col.config.code;

        let uriParams = this.getAllURLParams(uri);
        if(uriParams!=null) {
            for (let uriParam of uriParams) {
                let p = uriParam.substring(1, uriParam.length-1);
                if (item[p]) {
                    uri = uri.replace(new RegExp(uriParam, 'g'), item[p]);
                }
            }
        }
        this.pageSvc.processEvent(uri, col.config.uiStyles.attributes.b, item, col.config.uiStyles.attributes.method);
    }

    /* look for parameters in URI {} */
    getAllURLParams (uri: string): string[] {
        var pattern = /{([\s\S]*?)}/g;
        return uri.match(pattern);
    }

    updateHeaders() {
        var flex = this.flex;
        //after the grid view is rendered, get the content management header values
        if (flex!=null) {
            this.params.forEach(element => {
                this.wcs.getContent(element.config.code);
            });
        }
    }

    toggleFilter(event: any) {
        //console.log(event);
        this.filterState = !this.filterState;
    }

    postGridData(obj) {
        let item: GenericDomain = new GenericDomain();
        let elemIds = [];
        this.selectedRows.forEach(element => {
            elemIds.push(element.elemId);
        });
        
        item.addAttribute(this.element.config.uiStyles.attributes.postButtonTargetPath,elemIds);
        this.pageSvc.processEvent(this.element.config.uiStyles.attributes.postButtonUrl, null, item, 'POST');
    }

    onRowSelect(event) {
        //console.log(event);
        //this.pageService.postOnChange($event.path, '_update', 'state', JSON.stringify(true));
    }

    onRowUnselect(event) {
        //console.log(event);
        //this.pageService.postOnChange($event.path, '_update', 'state', JSON.stringify(true));
    }
    onRowClick(event: any) {
        //console.log(event);
        //this.pageService.postOnChange($event.path, '_update', 'state', JSON.stringify(true));
    }
    onRowUnSelect(event) {
        //console.log(event);
        //this.pageService.postOnChange($event.path, '_update', 'state', JSON.stringify(false));
    }

    postOnChange(col: Param, item: any) {
        let uri=this.element.path + '/' + item.elemId + '/' + col.config.code;
        //console.log(event);
        this.pageSvc.postOnChange(uri, 'state', JSON.stringify(event.target['checked']));
    }

    getAddtionalData(event: any) {
        let elemPath = '';
        this.params.forEach(param => {
            if (param.config.uiStyles && param.config.uiStyles.attributes.alias == 'Grid') {
                elemPath = this.element.path + '/' + event.data.elemId + '/' + param.config.code;
            }
        });
        
        this.pageSvc.processEvent(elemPath, '$execute', new GenericDomain(), 'GET' );
    }

    private _initPostButton() {
        if (this.element.config.uiStyles.attributes.postButton) {

            // fetch the post button alias from the content server, if applicable.
            let alias = this.element.config.uiStyles.attributes.postButtonAlias;
            if (alias) {
                this.wcs.getContent(alias);
            }
        }
    }
}
