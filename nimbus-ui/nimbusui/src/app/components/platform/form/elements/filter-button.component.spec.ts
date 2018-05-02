'use strict';
import { TestBed, async } from '@angular/core/testing';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { FilterButton } from './filter-button.component';
import { PageService } from '../../../../services/page.service';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { LoaderService } from '../../../../services/loader.service';
import { ConfigService } from '../../../../services/config.service';
import { LoggerService } from '../../../../services/logger.service';

let fixture, app, pageservice;

describe('FilterButton', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        FilterButton
       ],
       imports: [
           HttpModule,
           HttpClientTestingModule
       ],
       providers: [
        PageService,
        CustomHttpClient,
        LoaderService,
        ConfigService,
        LoggerService
       ]
    }).compileComponents();
    fixture = TestBed.createComponent(FilterButton);
    app = fixture.debugElement.componentInstance;
    pageservice = TestBed.get(PageService);
  }));

  it('should create the app', async(() => {
    expect(app).toBeTruthy();
  }));

  it('ngOnInit() should call loadLabelConfig()', async(() => {
      spyOn(app, 'loadLabelConfig').and.returnValue('');
      app.filterButton = {
        type: {
            model: {
                params: {
                    t: {
                        config: {
                            uiStyles: {
                                attributes: {
                                    alias: 'Button'
                                }
                            }
                        }
                    }
                }
            }
        }
      };
    app.ngOnInit();
    expect(app.loadLabelConfig).toHaveBeenCalled();
  }));

  it('ngOnInit() should update the fText property', async(() => {
    spyOn(app, 'loadLabelConfig').and.returnValue('');
    app.filterButton = {
      type: {
          model: {
              params: {
                  t: {
                      config: {
                          uiStyles: {
                              attributes: {
                                  alias: 'TextBox'
                              }
                          }
                      }
                  }
              }
          }
      }
    };
    const test = {
        config: {
            uiStyles: {
                attributes: {
                    alias: 'TextBox'
                }
            }
        }
    };
  app.ngOnInit();
  expect(app.fText).toEqual(test);
}));

  it('ngOnInit() should subscribe to buttonClickEvent', async(() => {
      spyOn(app.buttonClickEvent, 'subscribe').and.callThrough();
      app.filterButton = {
        type: {
            model: {
                params: ''
            }
        }
      };
      app.ngOnInit();
    expect(app.buttonClickEvent.subscribe).toHaveBeenCalled();
  }));

  it('ngOnInit() should call pageservice.processEvent()', async(() => {
    spyOn(pageservice, 'processEvent').and.returnValue('');
    app.filterButton = {
      type: {
          model: {
              params: ''
          }
      }
    };
    app.ngOnInit();
    const eve = {
        fbutton: {
            path: '',
            config: {
                uiStyles: {
                    attributes: {
                        b: '',
                        method: ''
                    }
                }
            }
        }
    }
    app.buttonClickEvent.emit(eve);
    expect(pageservice.processEvent).toHaveBeenCalled();
  }));

  it('emitEvent() should call buttonClickEvent.emit()', async(() => {
      spyOn(app.buttonClickEvent, 'emit').and.callThrough();
      app.emitEvent('test');
    expect(app.buttonClickEvent.emit).toHaveBeenCalled();
  }));

});
