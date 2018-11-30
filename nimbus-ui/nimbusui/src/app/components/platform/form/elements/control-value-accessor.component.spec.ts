'use strict';
import { TestBed, async } from '@angular/core/testing';

import { BaseControlValueAccessor } from './control-value-accessor.component';

let sut;

describe('BaseControlValueAccessor', () => {
  beforeEach(() => {
    sut = new BaseControlValueAccessor();
  });

  it('should create the app', async(() => {
    expect(sut).toBeTruthy();
  }));

  it('writeValue() should update the innerValue', async(() => {
    sut.writeValue('test');
    expect(sut.innerValue).toEqual('test');
  }));

  it('get value() should return the innerValue', async(() => {
    sut.writeValue('test');
    expect(sut.value).toEqual('test');
  }));

  it('set value() should return the innerValue', async(() => {
    sut.value = 'testing';
    sut.value = 'testing';
    expect(sut.innerValue).toEqual('testing');
  }));

  it('registerOnChange() should update changed array', async(() => {
    const test = () => {};
    sut.registerOnChange(test);
    expect(sut.changed.length).not.toEqual(0);
  }));

  it('registerOnTouched() should update touched array', async(() => {
    const test = () => {};
    sut.registerOnTouched(test);
    expect(sut.touched.length).not.toEqual(0);
  }));

});
