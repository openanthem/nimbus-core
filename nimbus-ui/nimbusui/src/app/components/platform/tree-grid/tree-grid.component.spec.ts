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

let fixture, app, pageService;

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

describe('TreeGrid', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
          TreeGrid,
          Button,
          TooltipComponent,
          Image,
          SvgComponent
       ],
       imports: [
           DataTableModule, SharedModule, OverlayPanelModule, PickListModule, DragDropModule, CalendarModule, 
           FileUploadModule, ListboxModule, DialogModule, CheckboxModule, DropdownModule, RadioButtonModule, 
           ProgressBarModule, ProgressSpinnerModule, AccordionModule, GrowlModule, InputSwitchModule, TreeTableModule,
           AngularSvgIconModule,
           HttpClientModule,
           StorageServiceModule,
           HttpModule
       ],
       providers: [
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
       ]
    }).compileComponents();
    fixture = TestBed.createComponent(TreeGrid);
    app = fixture.debugElement.componentInstance;
    pageService = TestBed.get(PageService);
  }));

  it('should create the app', async(() => {
    expect(app).toBeTruthy();
  }));

  it('registerOnChange() should update the onChange', async(() => {
      const test = () => { };
      app.registerOnChange(test);
    expect(app.onChange).toEqual(test);
  }));

  it('registerOnTouched() should update the onTouched', async(() => {
    const test = () => { };
    app.registerOnTouched(test);
  expect(app.onTouched).toEqual(test);
}));

it('isDisplayValueColumn() should return false if col.uiStyles is not available', async(() => {
    const col = {
        type: {
            collection: false
        }
    };
    expect(app.isDisplayValueColumn(col)).toBeFalsy();
}));

it('isDisplayValueColumn() should return false', async(() => {
    const col = {
        type: {
            collection: []
        },
        uiStyles: 't'
    };
    expect(app.isDisplayValueColumn(col)).toBeFalsy();
}));

it('ngOnInit() should update collectionAlias, treeData and firstColumn', async(() => {
    app.element = {
        elemLabels : {
            get: () => {
                return '';
            }
        },
        path: '',
        config: {
            type: {
                elementConfig: {
                    type: {
                        model: {
                            paramConfigs: [{
                                code: 'test',
                                uiStyles: {
                                    attributes: {
                                        alias: 'TreeGridChild',
                                    }
                                }
                            }]
                        }
                    }
                }
            }
        }
    };
    app.params = [{
        uiStyles: {
            attributes: {
                hidden: false
            }
        }
    }];
    const event = {
        path: '',
        gridList: 'testgridlist'
    };
    app.getTreeStructure = (a) => {
        return a;
    };
    app.ngOnInit();
    pageService.logError(event);
    expect(app.collectionAlias).toEqual('test');
    expect(app.firstColumn).not.toEqual(undefined);
    expect(app.treeData).toEqual('testgridlist');
  }));


  it('isRenderableComponent() should return true', async(() => {
      const param = {
          uiStyles: {
              attributes: {
                  alias: 'Button'
              }
          }
      };      
    expect(app.isRenderableComponent(param)).toBeTruthy();
  }));

  it('buildNestedCollectionPath() should return rowNode.node.data.elemId', async(() => {
      const rowNode = {
          level: 0,
          node: {
              data: {
                  elemId: 123
              }
          }
      };
    expect(app.buildNestedCollectionPath(rowNode)).toEqual(123);
  }));

//   it('4should create the app', async(() => {
//     const rowNode = {
//         level: 1,
//         node: {
//             data: {
//                 elemId: 123
//             }
//         }
//     };
//     spyOn(RowNodeUtils, 'createParentRowNode').and.returnValue(rowNode);
//     app.buildNestedCollectionPath(rowNode);
//     // expect(app).toBeTruthy();
//   }));

//   it('should create the app', async(() => {
//     expect(app).toBeTruthy();
//   }));

});