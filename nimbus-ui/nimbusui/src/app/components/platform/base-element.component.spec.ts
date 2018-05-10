'use strict';
import { TestBed, async } from '@angular/core/testing';

import { BaseElement } from './base-element.component';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Param } from '../../shared/param-state';
import { ConfigService } from '../../services/config.service';

let fixture, app, configService;

describe('BaseElement', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
          BaseElement
       ],
       imports: [
           HttpModule,
           HttpClientTestingModule
       ],
       providers: [
           ConfigService
       ]
    }).compileComponents();
    fixture = TestBed.createComponent(BaseElement);
    app = fixture.debugElement.componentInstance;
    configService = TestBed.get(ConfigService);
  }));

  it('should create the app', async(() => {
    expect(app).toBeTruthy();
  }));

//   it('1should create the app', async(() => {
//       app.element = new Param(configService);
//     spyOn(app, 'loadLabelConfig').and.callThrough();
//     app.ngOnInit();
//     expect(app.loadLabelConfig).toHaveBeenCalled();
//   }));

it('nestedParams should update from element.type.model.params', async(() => {
    app.element = {
        type: {
            model: {
                params: 'test'
            }
        }
    };
    expect(app.nestedParams).toEqual('test');
  }));

  it('nestedParams should return undefined', async(() => {
    app.element = {
        type: {
            model: null
        }
    };
    expect(app.nestedParams).toEqual(undefined);
  }));

  it('imgSrc should update from element.config.uiStyles.attributes.imgSrc', async(() => {
    app.element = {
        config: {
            uiStyles: {
                attributes: {
                    imgSrc: 'src1'
                }
            }
        }
    };
    expect(app.imgSrc).toEqual('src1');
  }));

  it('code should update from element.config.code', async(() => {
    app.element = {
        config: {
            code: 'code1'
        }
    };
    expect(app.code).toEqual('code1');
  }));

  it('visible should update from element.visible', async(() => {
    app.element = {
            visible: 'visible1'
    };
    expect(app.visible).toEqual('visible1');
  }));

  it('enabled should update from element.enabled', async(() => {
    app.element = {
        enabled: 'enabled1'
    };
    expect(app.enabled).toEqual('enabled1');
  }));

  it('_visible should update from visible property', async(() => {
      app.visible = true;
      expect(app._visible).toEqual(true);
  }));

  it('_enabled should update from enabled property', async(() => {
    app.enabled = true;
    expect(app._enabled).toEqual(true);
}));

it('cssClass should update from element.config.uiStyles.attributes.cssClass', async(() => {
    app.element = {
        config: {
            uiStyles: {
                attributes: {
                    cssClass: 'testClass'
                }
            }
        }
    };
    expect(app.cssClass).toEqual('testClass');
  }));

  it('type should update from element.config.uiStyles.attributes.type', async(() => {
    app.element = {
        config: {
            uiStyles: {
                attributes: {
                    type: 'testType'
                }
            }
        }
    };
    expect(app.type).toEqual('testType');
  }));

  it('elementStyle should update based on element.config.validation.constraints[0].name', async(() => {
    app.element = {
        config: {
            validation: {
                constraints: [{
                    name: 'NotNull'
                }]
            }
        }
    };
    expect(app.elementStyle).toEqual('required');
  }));

  it('elementStyle should be empty string based on element.config.validation.constraints[0].name', async(() => {
    app.element = {
        config: {
            validation: {
                constraints: [{
                    name: 'test'
                }]
            }
        }
    };
    expect(app.elementStyle).toEqual('');
  }));

  it('elementStyle should be empty string if element.config.validation.constraints[0].name is not avialable', async(() => {
    app.element = {
        config: {
            validation: null
        }
    };
    expect(app.elementStyle).toEqual('');
  }));

  it('app.isDate() should return true', async(() => {
    expect(app.isDate('Date')).toBeTruthy();
  }));

  it('placeholder should update from element.config.uiStyles.attributes.placeholder', async(() => {
      app.element = {
        config: {
            uiStyles: {
                attributes: {
                    placeholder: 'test'
                }
            }
        }
      };
    expect(app.placeholder).toEqual('test');;
  }));

  it('placeholder should be undefined if element.config.uiStyles.attributes is null', async(() => {
    app.element = {
      config: {
          uiStyles: {
              attributes: null
          }
      }
    };
  expect(app.placeholder).toEqual(undefined);;
}));

});