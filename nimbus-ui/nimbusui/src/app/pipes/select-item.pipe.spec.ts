import { SelectItemPipe } from './select-item.pipe';

describe('SelectItemPipe', () => {
  it('create an SelectItemPipe instance', () => {
    const pipe = new SelectItemPipe();
    expect(pipe).toBeTruthy();
  });

  it('should return empty array', () => {
    const pipe = new SelectItemPipe();
    const result = pipe.transform('');
    expect(result).toEqual([]);
  });

  it('should return valid response', () => {
    const pipe = new SelectItemPipe();
    const test = [{
        label: 'testing',
        code: 123
    }];
    const result = pipe.transform(test);
    expect(result).toEqual([{label: 'testing', value: 123}]);
  });

});
