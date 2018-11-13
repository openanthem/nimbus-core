'use strict';
import { TestBed, async } from '@angular/core/testing';
import { DataTableModule, SharedModule, OverlayPanelModule, PickListModule, DragDropModule, CalendarModule, 
    FileUploadModule, ListboxModule, DialogModule, CheckboxModule, DropdownModule, RadioButtonModule, 
    ProgressBarModule, ProgressSpinnerModule, AccordionModule, GrowlModule, InputSwitchModule, TreeTableModule } from 'primeng/primeng';
import { HttpClientModule } from '@angular/common/http';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { HttpModule } from '@angular/http';
import { Location, LocationStrategy, HashLocationStrategy } from '@angular/common';
import { Subject } from 'rxjs';

import { TreeGrid, RowNodeUtils } from './tree-grid.component';
import { Button } from '../form/elements/button.component';
import { TooltipComponent } from '../tooltip/tooltip.component';
import { Image } from '../image.component';
import { SvgComponent } from '../svg/svg.component';
import { AngularSvgIconModule } from 'angular-svg-icon';
import { PageService } from '../../../services/page.service';
import { CustomHttpClient } from '../../../services/httpclient.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../services/session.store';
import { LoaderService } from '../../../services/loader.service';
import { ConfigService } from '../../../services/config.service';
import { LoggerService } from '../../../services/logger.service';
import { AppInitService } from '../../../services/app.init.service';
import { GridUtils } from '../../../shared/grid-utils';
import { DateTimeFormatPipe } from '../../../pipes/date.pipe';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../setup.spec';
import * as data from '../../../payload.json';

let pageService, param;

class MockPageService {
    public config$: Subject<any>;
    public subdomainconfig$: Subject<any>;
    public gridValueUpdate$: Subject<any>;

  
    constructor() {
      this.config$ = new Subject();
      this.subdomainconfig$ = new Subject();
      this.gridValueUpdate$ = new Subject();
    }
  
    logError(res) {
      this.gridValueUpdate$.next(res);
    }

    processEvent(a, b, c, d, e) {    }
  
  }

const declarations = [
    TreeGrid,
    Button,
    TooltipComponent,
    Image,
    SvgComponent
 ];
const imports = [
     DataTableModule, SharedModule, OverlayPanelModule, PickListModule, DragDropModule, CalendarModule, 
     FileUploadModule, ListboxModule, DialogModule, CheckboxModule, DropdownModule, RadioButtonModule, 
     ProgressBarModule, ProgressSpinnerModule, AccordionModule, GrowlModule, InputSwitchModule, TreeTableModule,
     AngularSvgIconModule,
     HttpClientModule,
     StorageServiceModule,
     HttpModule
 ];
const providers = [
     { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
     { provide: 'JSNLOG', useValue: JL },
     { provide: LocationStrategy, useClass: HashLocationStrategy },
     {provide: PageService, useClass: MockPageService},
     Location,
     CustomHttpClient,
     SessionStoreService,
     LoaderService,
     ConfigService,
     LoggerService,
     AppInitService,
     GridUtils,
     DateTimeFormatPipe
 ];

describe('TreeGrid', () => {

    configureTestSuite();
    setup(TreeGrid, declarations, imports, providers);
    param = (<any>data).payload;
  
    beforeEach(async function(this: TestContext<TreeGrid>){
      this.hostComponent.element = param;
      pageService = TestBed.get(PageService);
    });
    
    it('should create the TreeGrid', async function (this: TestContext<TreeGrid>) {
        expect(this.hostComponent).toBeTruthy();
    });

    it('registerOnChange() should update the onChange', async function (this: TestContext<TreeGrid>) {
        const test = () => { };
        this.hostComponent.registerOnChange(test);
        expect(this.hostComponent.onChange).toEqual(test);
    });

    it('registerOnTouched() should update the onTouched', async function (this: TestContext<TreeGrid>) {
        const test = () => { };
        this.hostComponent.registerOnTouched(test);
        expect(this.hostComponent.onTouched).toEqual(test);
    });

    it('isDisplayValueColumn() should return false if col.uiStyles is not available', async function (this: TestContext<TreeGrid>) {
        const col: any = {
            type: {
                collection: false
            }
        };
        expect(this.hostComponent.isDisplayValueColumn(col)).toBeFalsy();
    });

    it('isDisplayValueColumn() should return false', async function (this: TestContext<TreeGrid>) {
        const col: any = {
            type: {
                collection: []
            },
            uiStyles: 't'
        };
        expect(this.hostComponent.isDisplayValueColumn(col)).toBeFalsy();
    });

    it('ngOnInit() should update collectionAlias, treeData and firstColumn', async function (this: TestContext<TreeGrid>) {
        this.fixture.whenStable().then(() => {
            const elemLabels: any = [];
            this.hostComponent.element.elemLabels = elemLabels;
            this.hostComponent.element.path = '';
            const model: any = {
                paramConfigs: [{
                    code: 'test',
                    uiStyles: {
                        attributes: {
                            alias: 'TreeGridChild',
                        }
                    }
                }]
            };
            this.hostComponent.element.config.type.elementConfig.type.model = model;
            const params: any = [{
                uiStyles: {
                    attributes: {
                        hidden: false
                    }
                }
            }];
            this.hostComponent.params = params;
            const event = {
                path: '',
                gridData: {
                    leafState: 'testgridlist'
                }
            };
            this.hostComponent.getTreeStructure = (a) => {
                return a;
            };
            this.hostComponent.ngOnInit();
            pageService.logError(event);
            expect((this.hostComponent as any).collectionAlias).toEqual('test');
            expect(this.hostComponent.firstColumn).not.toEqual(undefined);
            expect(this.hostComponent.treeData).toEqual('testgridlist');
        });
    });

    it('isRenderableComponent() should return true', async function (this: TestContext<TreeGrid>) {
        const param1: any = {
            uiStyles: {
                attributes: {
                    alias: 'Button'
                }
            }
        };
        expect(this.hostComponent.isRenderableComponent(param1)).toBeTruthy();
    });

    it('buildNestedCollectionPath() should return rowNode.node.data.elemId', async function (this: TestContext<TreeGrid>) {
        const rowNode = {
            level: 0,
            node: {
                data: {
                    elemId: 123
                }
            }
        };
        const res: any = 123;
        expect(this.hostComponent.buildNestedCollectionPath(rowNode)).toEqual(res);
    });

});