import { KeysPipe } from './app.pipe';
import { TestBed, async } from '@angular/core/testing';
import { Param } from '../shared/param-state';
import { ConfigService } from './../services/config.service';

describe('KeysPipe', () => {

beforeEach(
  async(() => {
    TestBed.configureTestingModule({
      providers: [ConfigService]
    }).compileComponents();
  })
);


  it('create an KeysPipe instance', () => {
    const pipe = new KeysPipe();
    expect(pipe).toBeTruthy();
  });

  it('KeysPipe should not return anything on passing string', () => {
    const pipe = new KeysPipe();
    const result = pipe.transform('abcd');
    expect(result).toBeFalsy();
  });

  it('KeysPipe should return result', () => {
    const pipe = new KeysPipe();
    const data = new Map<string,  Param[]>();
    const service = TestBed.get(ConfigService);
    const p = new Param(service);
    data.set('hello', [p]);
    const result = pipe.transform(data);
    expect(result).toBeTruthy();
  });
});
